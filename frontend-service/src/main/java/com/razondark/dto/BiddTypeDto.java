package com.razondark.dto;

/*
    dto информации о типе предложения
*/

import lombok.Data;

@Data
public class BiddTypeDto {
    private String code; // код вида "ZK"
    private String shortName; // краткое название
}
