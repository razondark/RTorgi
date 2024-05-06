package com.razondark.service;

import com.razondark.dto.response.*;

public interface ConfigService {
    CategoriesResponse getCategories();
    BiddTypeResponse getBiddTypes();
    AttributesResponse getAttributes();
    SpecificationsResponse getSpecifications();
}
