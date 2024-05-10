package com.razondark.service.impl;

import com.razondark.dto.LotDto;
import com.razondark.dto.response.LotResponse;
import com.razondark.props.DataLinkProperties;
import com.razondark.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    private final RestTemplate restTemplate;
    private final DataLinkProperties dataLinkProperties;

    public String getLotPageLink() {
        return dataLinkProperties.getLotPageLink();
    }

    public String getMapEGRPLink() {
        return dataLinkProperties.getMapEGRP();
    }

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

//    @Override
//    @SneakyThrows
//    public List<LotDto> getAllLots(Integer page, Integer size) {
//        var uriParams = new LinkedMultiValueMap<String, String>();
//        uriParams.add("page", String.valueOf(page));
//        uriParams.add("size", String.valueOf(size));
//
//        String uri = uriBuilder(dataLinkProperties.getAllDataLink(), uriParams);
//
//        var response = restTemplate.exchange(uri,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<LotDto>>() {});
//
//        return response.getBody();
//    }

    @Override
    @SneakyThrows
    public List<LotDto> getLotsBySubjects(String subjects, Integer page, Integer size) {
        var uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));
        uriParams.add("subject", subjects);

        String uri = uriBuilder(dataLinkProperties.getSubjectsDataLink(), uriParams);

        var response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LotDto>>() {});

        return response.getBody();
    }

    @Override
    @SneakyThrows
    public LotResponse getLots(String endDateFrom, String endDateTo, String regions, String category, String permittedUse,
                               String squareFrom, String squareTo, String startPriceFrom, String startPriceTo,
                               String cadCostFrom, String cadCostTo, String percentPriceCadFrom, String percentPriceCadTo,
                               Integer page, Integer size, String text) {
        var uriParams = new LinkedMultiValueMap<String, String>();

        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        addToUriParamsIfNotNull(uriParams, "end-date-from", endDateFrom);
        addToUriParamsIfNotNull(uriParams, "end-date-to", endDateTo);
        addToUriParamsIfNotNull(uriParams, "regions", regions);
        addToUriParamsIfNotNull(uriParams, "category", category);
        addToUriParamsIfNotNull(uriParams, "permitted-use", permittedUse);
        addToUriParamsIfNotNull(uriParams, "square-from", squareFrom);
        addToUriParamsIfNotNull(uriParams, "square-to", squareTo);
        addToUriParamsIfNotNull(uriParams, "start-price-from", startPriceFrom);
        addToUriParamsIfNotNull(uriParams, "start-price-to", startPriceTo);
        addToUriParamsIfNotNull(uriParams, "cad-cost-from", cadCostFrom);
        addToUriParamsIfNotNull(uriParams, "cad-cost-to", cadCostTo);
        addToUriParamsIfNotNull(uriParams, "percent-price-cad-from", percentPriceCadFrom);
        addToUriParamsIfNotNull(uriParams, "percent-price-cad-to", percentPriceCadTo);

        String uri = uriBuilder(dataLinkProperties.getAllDataLink(), uriParams);

        if (text != null) {
            uri += "&text=" + text;
        }

        var response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                LotResponse.class);

        return response.getBody();
    }
}
