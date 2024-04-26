package com.razondark.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razondark.props.TorgiProperties;
import com.razondark.service.TorgiService;
import com.razondark.service.cache.CacheService;
import com.razondark.web.dto.CharacteristicsDto;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.LotInfoDto;
import com.razondark.web.mapper.LotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TorgiServiceImpl implements TorgiService {
    //private final CacheService cacheService;
    private final RestTemplate restTemplate;
    private final TorgiProperties torgiProperties;
    private final LotMapper lotMapper;

    private String uriBuilder(String uri, MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(params)
                .toUriString();
    }

    @Override
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
    public List<LotDto> getLothBySubject(Integer subject, Integer page, Integer size) {
        var uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("dynSubjRF", String.valueOf(subject));
        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        var uri = uriBuilder(torgiProperties.getSearchLotsLink(), uriParams);

        var response = restTemplate.getForObject(uri, String.class);
        var dtos = lotMapper.jsonToDtoList(response);

        dtos.forEach(this::enrichLotDtoWithInfo);

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