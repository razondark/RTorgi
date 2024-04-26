package com.razondark.web.dto.response;

import com.razondark.domain.specification.Specification;
import lombok.Data;

import java.util.List;

@Data
public class SpecificationsResponse {
    private List<Specification> specifications;
}
