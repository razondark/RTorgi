package com.razondark.service.cache;

import com.razondark.web.dto.LotInfoDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CacheService {
//    @Cacheable("lots-cache")
//    public String getLots(RestTemplate restTemplate, String uri) {
//        return restTemplate.getForObject(uri, String.class);
//    }
//
//    @Cacheable("lots-info-cache")
//    public LotInfoDto getLotsInfo(RestTemplate restTemplate, String uri) {
//        return restTemplate.getForObject(uri, LotInfoDto.class);
//    }
}
