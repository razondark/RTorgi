package com.razondark.web.dto.response;

import com.razondark.domain.category.Categories;
import lombok.Data;

import java.util.List;

@Data
public class CategoriesResponse {
    private List<Categories> categories;
}
