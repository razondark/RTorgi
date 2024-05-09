package com.razondark.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razondark.props.TorgiProperties;
import com.razondark.service.TorgiService;
import com.razondark.web.dto.CharacteristicValue;
import com.razondark.web.dto.CharacteristicsDto;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.LotInfoDto;
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
import java.util.*;

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
        var dtos = lotMapper.jsonToDtoList(response);

        dtos.forEach(this::enrichLotDtoWithInfo);

        return dtos;
    }

    @Override
    public List<LotDto> getLotsBySubject(String subjects, Integer page, Integer size) {
        var uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("dynSubjRF", String.valueOf(subjects));
        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        var uri = uriBuilder(torgiProperties.getSearchLotsLink(), uriParams);

        var response = restTemplate.getForObject(uri, String.class);
        var dtos = lotMapper.jsonToDtoList(response);

        dtos.forEach(this::enrichLotDtoWithInfo);

        return dtos;
    }

    @Override
    public List<LotDto> getLots(String endDateFrom, String endDateTo, String regions, String category, String permittedUse,
                                String squareFrom, String squareTo,
                                String startPriceFrom, String startPriceTo,
                                String cadCostFrom, String cadCostTo,
                                String percentPriceCadFrom, String percentPriceCadTo,
                                Integer page, Integer size, String text) {
        var uriParams = new LinkedMultiValueMap<String, String>();

        uriParams.add("lotStatus", "PUBLISHED,APPLICATIONS_SUBMISSION,DETERMINING_WINNER,SUCCEED");
        //uriParams.add("lotStatus", "APPLICATIONS_SUBMISSION");
        uriParams.add("sort", "firstVersionPublicationDate,desc");

        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));
        addToUriParamsIfNotNull(uriParams, "text", text);

        // base params
        addToUriParamsIfNotNull(uriParams, "biddEndFrom", endDateFrom);
        addToUriParamsIfNotNull(uriParams, "biddEndTo", endDateTo);
        addToUriParamsIfNotNull(uriParams, "dynSubjRF", regions);
        addToUriParamsIfNotNull(uriParams, "catCode", category);

        if (permittedUse != null) {
            uriParams.add("chars", "msl-PermittedUse:" + permittedUse);
        }

        if (squareFrom != null || squareTo != null) {
            if (squareFrom != null && squareTo != null) {
                uriParams.add("chars", "dec-SquareZU:" + squareFrom + "~" + squareTo);
            }
            else if (squareFrom != null) {
                uriParams.add("chars", "dec-SquareZU:" + squareFrom + "~");
            }
            else {
                uriParams.add("chars", "dec-SquareZU:~" + squareTo);
            }
        }

        var uri = uriBuilder(torgiProperties.getSearchLotsLink(), uriParams);

        var response = restTemplate.getForObject(uri, String.class);
        var dtos = lotMapper.jsonToDtoList(response);

        dtos.forEach(this::enrichLotDtoWithInfo);

        // values params
        if (startPriceFrom != null || startPriceTo != null || cadCostFrom != null || cadCostTo != null ||
                percentPriceCadFrom != null || percentPriceCadTo != null) {
            dtos.removeIf(lotDto -> {
                if (lotDto.getPriceMin() != null) {
                    // Условие для startPriceFrom и startPriceTo
                    if (startPriceFrom != null && lotDto.getPriceMin().compareTo(new BigDecimal(startPriceFrom)) <= 0) {
                        return true;
                    }
                    if (startPriceTo != null && lotDto.getPriceMin().compareTo(new BigDecimal(startPriceTo)) >= 0) {
                        return true;
                    }
                }

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

        return dtos;
    }

    private void enrichLotDtoWithInfo(LotDto lotDto) {
        var cadNumber = getCadastralNumber(lotDto);
        if (cadNumber == null) {
            return;
        }

        var infoResponse = getLotInfo(cadNumber);
        if (infoResponse != null) {
            lotDto.setCadCost(infoResponse.getFeature().getAttrs().getCadCost());
            lotDto.setAreaValue(infoResponse.getFeature().getAttrs().getAreaValue());
        }

        // add permitted use to dto
        var permUseObject = lotDto.getCharacteristics()
                        .stream()
                        .filter(i -> i.getCode().equalsIgnoreCase("PermittedUse"))
                        .map(CharacteristicsDto::getCharacteristicValue)
                .findFirst();

        if (permUseObject.isPresent()) {
            if (permUseObject.get() instanceof ArrayList<?>) {
                var permittedUseList = new ArrayList<String>();

                for (var item : (ArrayList<?>) permUseObject.get()) {
                    var objectMapper = new ObjectMapper();

                    if (item instanceof LinkedHashMap) {
                        var value = objectMapper.convertValue(item, CharacteristicValue.class);
                        permittedUseList.add(value.getName());
                    }
                }

                lotDto.setPermittedUse(String.join(", ", permittedUseList));
            }
        }

        calculatePercentPrice(lotDto);
    }

    private String getCadastralNumber(LotDto lotDto) {
        var cadastralNumber = lotDto.getCharacteristics()
                .stream()
                .filter(d -> d.getCode().equalsIgnoreCase("CadastralNumber"))
                .map(CharacteristicsDto::getCharacteristicValue)
                .findAny()
                .orElse(null);

        return cadastralNumber != null ? cadastralNumber.toString() : null;
    }

    private LotInfoDto getLotInfo(String cadNumber) {
        var uri = torgiProperties.getLotInfoLink() + cadNumber.replaceAll(":0+", ":");
        return restTemplate.getForObject(uri, LotInfoDto.class);
    }

    private void calculatePercentPrice(LotDto lotDto) {
        if (lotDto.getPriceMin() != null && lotDto.getCadCost() != null) {
            var percent = lotDto.getCadCost()
                    .divide(lotDto.getPriceMin(), 2, RoundingMode.DOWN)
                    .doubleValue();
            lotDto.setPercentPriceCad(percent);
        }
    }
}
