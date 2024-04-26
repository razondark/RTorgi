package com.razondark.dto.response;

import com.razondark.dto.Categories;
import lombok.Data;

import java.util.List;

@Data
public class CategoriesResponse {
    private List<Categories> categories;
}
