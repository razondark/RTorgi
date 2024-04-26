package com.razondark.web.controller;

import com.razondark.domain.category.Categories;
import com.razondark.service.CommonDataService;
import com.razondark.service.TorgiService;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.response.AttributesResponse;
import com.razondark.web.dto.response.BiddTypeResponse;
import com.razondark.web.dto.response.CategoriesResponse;
import com.razondark.web.dto.response.SpecificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/common-data")
@RequiredArgsConstructor
public class CommonDataController {
    private final CommonDataService commonDataService;

    @GetMapping("categories")
    public CategoriesResponse getCategories() {
        return commonDataService.getCategories();
    }

    @GetMapping("bidd-types")
    public BiddTypeResponse getBiddTypes() {
        return commonDataService.getBiddTypes();
    }

    @GetMapping("attributes")
    public AttributesResponse getAttributes() {
        return commonDataService.getAttributes();
    }

    @GetMapping("specifications")
    public SpecificationsResponse getSpecifications() {
        return commonDataService.getSpecifications();
    }
}
