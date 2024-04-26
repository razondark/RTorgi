package com.razondark.web.dto.response;

import com.razondark.web.dto.AttributesDto;
import lombok.Data;

import java.util.List;

@Data
public class AttributesResponse {
    private List<AttributesDto> attributes;
}
