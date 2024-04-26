package com.razondark.service.impl;

import com.razondark.dto.response.AttributesResponse;
import com.razondark.dto.response.BiddTypeResponse;
import com.razondark.dto.response.CategoriesResponse;
import com.razondark.dto.response.SpecificationsResponse;
import com.razondark.props.ConfigLinkProperties;
import com.razondark.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final RestTemplate restTemplate;
    private final ConfigLinkProperties configLinkProperties;

    @Override
    @SneakyThrows
    public CategoriesResponse getCategories() {
        var response = restTemplate.exchange(configLinkProperties.getCategoriesLink(),
                HttpMethod.GET,
                null,
                CategoriesResponse.class);

        return response.getBody();
    }

    @Override
    @SneakyThrows
    public BiddTypeResponse getBiddTypes() {
        var response = restTemplate.exchange(configLinkProperties.getBiddTypeLink(),
                HttpMethod.GET,
                null,
                BiddTypeResponse.class);

        return response.getBody();
    }

    @Override
    @SneakyThrows
    public AttributesResponse getAttributes() {
        var response = restTemplate.exchange(configLinkProperties.getAttributesLink(),
                HttpMethod.GET,
                null,
                AttributesResponse.class);

        return response.getBody();
    }

    @Override
    @SneakyThrows
    public SpecificationsResponse getSpecifications() {
        var response = restTemplate.exchange(configLinkProperties.getSpecificationsLink(),
                HttpMethod.GET,
                null,
                SpecificationsResponse.class);

        return response.getBody();
    }
}
