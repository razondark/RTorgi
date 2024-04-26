package com.razondark.dto.response;

import com.razondark.dto.AttributesDto;
import lombok.Data;

import java.util.List;

@Data
public class AttributesResponse {
    private List<AttributesDto> attributes;
}
