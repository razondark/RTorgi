package com.razondark.web.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/*
    сущность характеристик лота
*/

@Data
public class CharacteristicsDto {
    private Object characteristicValue;
    private String name;
    private String code;
    private String type;
    private Object unit;
}
