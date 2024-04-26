package com.razondark.web.dto.response;

import com.razondark.web.dto.LotDto;
import lombok.Data;

import java.util.List;

@Data
public class LotResponse {
    private List<LotDto> content;
}
