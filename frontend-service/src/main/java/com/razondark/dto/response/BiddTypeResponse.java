package com.razondark.dto.response;

import com.razondark.dto.BiddTypeDto;
import lombok.Data;

import java.util.List;

@Data
public class BiddTypeResponse {
    private List<BiddTypeDto> categories;
}
