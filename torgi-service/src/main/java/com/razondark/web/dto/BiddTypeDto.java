package com.razondark.web.dto;

/*
    Сущность информации о типе предложения
*/

import lombok.Data;

@Data
public class BiddTypeDto {
    private String code; // код вида "ZK"
    private String shortName; // краткое название
}
