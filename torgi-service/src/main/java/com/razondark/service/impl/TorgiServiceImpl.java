package com.razondark.service.impl;

import com.razondark.props.TorgiProperties;
import com.razondark.service.TorgiService;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.LotInfoDto;
import com.razondark.web.dto.response.LotResponse;
import com.razondark.web.mapper.LotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TorgiServiceImpl implements TorgiService {
    private final RestTemplate restTemplate;
    private final TorgiProperties torgiProperties;
    private final LotMapper lotMapper;

    private String uriBuilder(String uri, MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(params)
                .toUriString();
    }

    private void addToUriParamsIfNotNull(MultiValueMap<String, String> uriParams, String paramName, String paramValue) {
        if (paramValue != null) {
            uriParams.add(paramName, paramValue);
        }
    }

    @Override
    @Cacheable(cacheNames = "lots", key = "{#page, #size}")
    public List<LotDto> getLotsAll(Integer page, Integer size) {
        var uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        var uri = uriBuilder(torgiProperties.getSearchLotsLink(), uriParams);

        var response = restTemplate.getForObject(uri, String.class);
        var parsedResponse = lotMapper.jsonToDtoList(response);

        parsedResponse.getContent().parallelStream().forEach(this::enrichLotDtoWithInfo);

        return parsedResponse.getContent();
    }

    // TODO: fix to lorResponse
    @Override
    public List<LotDto> getLotsBySubject(String subjects, Integer page, Integer size) {
        var uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("dynSubjRF", String.valueOf(subjects));
        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        var uri = uriBuilder(torgiProperties.getSearchLotsLink(), uriParams);

        var response = restTemplate.getForObject(uri, String.class);
        var parsedResponse = lotMapper.jsonToDtoList(response);

        parsedResponse.getContent().parallelStream().forEach(this::enrichLotDtoWithInfo);

        return parsedResponse.getContent();
    }

    @Override
    @Cacheable(value = "lots", key = "{#endDateFrom, #endDateTo, #regions, #category, #permittedUse, #squareFrom, " +
            "#squareTo, #startPriceFrom, #startPriceTo, #cadCostFrom, #cadCostTo, #percentPriceCadFrom, #percentPriceCadTo, " +
            "#page, #size, #text}")
    public LotResponse getLots(String endDateFrom, String endDateTo, String regions, String category, String permittedUse,
                               String squareFrom, String squareTo,
                               String startPriceFrom, String startPriceTo,
                               String cadCostFrom, String cadCostTo,
                               String percentPriceCadFrom, String percentPriceCadTo,
                               Integer page, Integer size, String text) {
        var uriParams = new LinkedMultiValueMap<String, String>();

        uriParams.add("lotStatus", "PUBLISHED,APPLICATIONS_SUBMISSION,DETERMINING_WINNER,SUCCEED");
        uriParams.add("sort", "firstVersionPublicationDate,desc");
        uriParams.add("biddType", "ZK"); // TODO: change hardcode

        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        // base params
        addToUriParamsIfNotNull(uriParams, "biddEndFrom", endDateFrom);
        addToUriParamsIfNotNull(uriParams, "biddEndTo", endDateTo);
        addToUriParamsIfNotNull(uriParams, "dynSubjRF", regions);
        addToUriParamsIfNotNull(uriParams, "catCode", category);

        addToUriParamsIfNotNull(uriParams, "priceMinFrom", startPriceFrom);
        addToUriParamsIfNotNull(uriParams, "priceMinTo", startPriceTo);

        if (permittedUse != null) {
            uriParams.add("chars", "msl-PermittedUse:" + permittedUse);
        }

        if (squareFrom != null || squareTo != null) {
            if (squareFrom != null && squareTo != null) {
                uriParams.add("chars", "dec-SquareZU:" + squareFrom + "~" + squareTo);
            } else if (squareFrom != null) {
                uriParams.add("chars", "dec-SquareZU:" + squareFrom + "~");
            } else {
                uriParams.add("chars", "dec-SquareZU:~" + squareTo);
            }
        }

        var uri = uriBuilder(torgiProperties.getSearchLotsLink(), uriParams);
        if (text != null) {
            uri += "&text=" + text;
        }

        var response = restTemplate.getForObject(uri, String.class);
        var parsedResponse = lotMapper.jsonToDtoList(response);

        //dtos.forEach(this::enrichLotDtoWithInfo);
        parsedResponse.getContent().parallelStream().forEach(this::enrichLotDtoWithInfo);

        // values params
        if (cadCostFrom != null || cadCostTo != null || percentPriceCadFrom != null || percentPriceCadTo != null) {
            parsedResponse.getContent().removeIf(lotDto -> {
                if (lotDto.getCadCost() != null) {
                    // Условие для cadCostFrom и cadCostTo
                    if (cadCostFrom != null && lotDto.getCadCost().compareTo(new BigDecimal(cadCostFrom)) <= 0) {
                        return true;
                    }
                    if (cadCostTo != null && lotDto.getCadCost().compareTo(new BigDecimal(cadCostTo)) >= 0) {
                        return true;
                    }
                }

                if (lotDto.getPercentPriceCad() != null) {
                    // Условие для percentPriceCadFrom и percentPriceCadTo
                    if (percentPriceCadFrom != null && lotDto.getPercentPriceCad()
                            .compareTo(Double.parseDouble(percentPriceCadFrom)) <= 0) {
                        return true;
                    }
                    return percentPriceCadTo != null && lotDto.getPercentPriceCad()
                            .compareTo(Double.parseDouble(percentPriceCadTo)) >= 0;
                }

                return false;
            });
        }

        return parsedResponse;
    }

    private void enrichLotDtoWithInfo(LotDto lotDto) {
        if (lotDto.getCadNumber() == null) {
            return;
        }

        var infoResponse = getLotInfo(lotDto.getCadNumber());
        if (infoResponse != null) {
            if (infoResponse.getFeature() != null && infoResponse.getFeature().getAttrs() != null) {
                lotDto.setCadCost(infoResponse.getFeature().getAttrs().getCadCost());
            }
        }

        calculatePercentPrice(lotDto);
    }

    private LotInfoDto getLotInfo(String cadNumber) {
        var uri = torgiProperties.getLotInfoLink() + cadNumber.replaceAll(":0+", ":");
        return restTemplate.getForObject(uri, LotInfoDto.class);
    }

    private void calculatePercentPrice(LotDto lotDto) {
        if (lotDto.getPriceMin() != null && lotDto.getCadCost() != null) {
            var percent = lotDto.getPriceMin()
                    .divide(lotDto.getCadCost(), 2, RoundingMode.DOWN)
                    .doubleValue();
            lotDto.setPercentPriceCad(percent);
        }
    }
}
