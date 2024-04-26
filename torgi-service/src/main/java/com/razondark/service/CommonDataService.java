package com.razondark.service;

import com.razondark.domain.category.Categories;
import com.razondark.web.dto.response.AttributesResponse;
import com.razondark.web.dto.response.BiddTypeResponse;
import com.razondark.web.dto.response.CategoriesResponse;
import com.razondark.web.dto.response.SpecificationsResponse;

import java.util.List;

public interface CommonDataService {
    CategoriesResponse getCategories();
    BiddTypeResponse getBiddTypes();
    AttributesResponse getAttributes();
    SpecificationsResponse getSpecifications();
}
