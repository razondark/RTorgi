package com.razondark.service.impl;

import com.razondark.dto.LotDto;
import com.razondark.dto.response.CategoriesResponse;
import com.razondark.dto.response.LotResponse;
import com.razondark.props.DataLinkProperties;
import com.razondark.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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

    private String uriBuilder(String uri, MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(params)
                .toUriString();
    }

    @Override
    @SneakyThrows
    public List<LotDto> getAllLots(Integer page, Integer size) {
        var uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("page", String.valueOf(page));
        uriParams.add("size", String.valueOf(size));

        String uri = uriBuilder(dataLinkProperties.getAllDataLink(), uriParams);

        var response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LotDto>>() {});

        return response.getBody();
    }

    @Override
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
}
