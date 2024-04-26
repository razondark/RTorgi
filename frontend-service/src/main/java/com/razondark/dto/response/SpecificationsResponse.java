package com.razondark.dto.response;

import com.razondark.dto.Specification;
import lombok.Data;

import java.util.List;

@Data
public class SpecificationsResponse {
    private List<Specification> specifications;
}
