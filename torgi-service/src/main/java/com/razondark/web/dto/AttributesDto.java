package com.razondark.web.dto;

import com.razondark.domain.dynamicAttributes.DisplayCondition;
import com.razondark.domain.dynamicAttributes.MappingTable;
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
