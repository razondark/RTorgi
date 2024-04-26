package com.razondark.dto;

import lombok.Data;

/*
    сущность характеристик лота
*/

@Data
public class CharacteristicsDto {
    private Object characteristicValue;
    private String name;
    private String code;
}
