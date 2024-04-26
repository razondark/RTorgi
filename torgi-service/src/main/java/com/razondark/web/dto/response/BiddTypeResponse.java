package com.razondark.web.dto.response;

import com.razondark.domain.category.Categories;
import com.razondark.web.dto.BiddTypeDto;
import lombok.Data;

import java.util.List;

@Data
public class BiddTypeResponse {
    private List<BiddTypeDto> categories;
}
