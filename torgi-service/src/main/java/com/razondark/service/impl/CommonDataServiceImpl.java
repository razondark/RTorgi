package com.razondark.service.impl;

import com.razondark.domain.category.Categories;
import com.razondark.domain.specification.Specification;
import com.razondark.props.TorgiProperties;
import com.razondark.service.CommonDataService;
import com.razondark.web.dto.AttributesDto;
import com.razondark.web.dto.BiddTypeDto;
import com.razondark.web.dto.response.AttributesResponse;
import com.razondark.web.dto.response.BiddTypeResponse;
import com.razondark.web.dto.response.CategoriesResponse;
import com.razondark.web.dto.response.SpecificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonDataServiceImpl implements CommonDataService {
    //private final CacheService cacheService;
    private final RestTemplate restTemplate;
    private final TorgiProperties torgiProperties;

    @Override
    public CategoriesResponse getCategories() {
        var response = restTemplate.exchange(torgiProperties.getCategoriesLink(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Categories>>() {});

        var categoriesResponse = new CategoriesResponse();
        categoriesResponse.setCategories(response.getBody());

        return categoriesResponse;
    }

    @Override
    public BiddTypeResponse getBiddTypes() {
        var response = restTemplate.exchange(torgiProperties.getBiddTypeLink(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BiddTypeDto>>() {});

        var biddTypeResponse = new BiddTypeResponse();
        biddTypeResponse.setCategories(response.getBody());

        return biddTypeResponse;
    }

    @Override
    public AttributesResponse getAttributes() {
        var response = restTemplate.exchange(torgiProperties.getDynamicAttrSearchOptionLink(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AttributesDto>>() {});

        var attributesResponse = new AttributesResponse();
        attributesResponse.setAttributes(response.getBody());

        return attributesResponse;
    }

    @Override
    public SpecificationsResponse getSpecifications() {
        var response = restTemplate.exchange(torgiProperties.getSpecificationsLink(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Specification>>() {});

        var specificationsResponse = new SpecificationsResponse();
        specificationsResponse.setSpecifications(response.getBody());

        return specificationsResponse;
    }
}
