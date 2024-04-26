package com.razondark.domain.dynamicAttributes;

import lombok.Data;

import java.util.List;

/*
    Сущность информации об атрибутах
*/

@Data
public class Attributes {
    private String code; // код вида "resourceLocation"
    private String name; // полное название
    private boolean published; // статус
    private String format; // формат вида "select", "checker"
    private List<MappingTable> mappingTable; // сущность информации
    private List<DisplayCondition> displayConditions; // сущность информации
}
