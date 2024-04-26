package com.razondark.service.impl;

import com.razondark.props.TorgiProperties;
import com.razondark.service.TorgiService;
import com.razondark.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class TorgiServiceImpl implements TorgiService {
    private final CacheService cacheService;
    private final RestTemplate restTemplate;
    private final TorgiProperties torgiProperties;

    public String getLots(int page, int size) {
        var uri = UriComponentsBuilder.fromHttpUrl(torgiProperties.getSearchLotsLink())
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .toUriString();

        return restTemplate.getForObject(uri, String.class);
    }
}
