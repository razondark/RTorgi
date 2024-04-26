package com.razondark.dto.response;

import com.razondark.dto.LotDto;
import lombok.Data;

import java.util.List;

@Data
public class LotResponse {
    private List<LotDto> content;
}
