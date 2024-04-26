package com.razondark.dto;

import lombok.Data;

import java.util.List;

/*
    dto информации о регионе
*/

@Data
public class AttributesDto {
    private String code; // код вида "resourceLocation"
    private String name; // полное название
    private List<MappingTableDto> mappingTable; // сущность информации
}
