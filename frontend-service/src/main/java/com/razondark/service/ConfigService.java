package com.razondark.service;

import com.razondark.dto.response.AttributesResponse;
import com.razondark.dto.response.BiddTypeResponse;
import com.razondark.dto.response.CategoriesResponse;
import com.razondark.dto.response.SpecificationsResponse;

public interface ConfigService {
    CategoriesResponse getCategories();
    BiddTypeResponse getBiddTypes();
    AttributesResponse getAttributes();
    SpecificationsResponse getSpecifications();
}
