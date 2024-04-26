package com.razondark.dto;

import com.razondark.dto.BaseAttrValue;
import com.razondark.dto.DynAttrValue;
import lombok.Data;

/*
    dto информации
*/

@Data
public class MappingTableDto {
    private String code;
    private BaseAttrValue baseAttrValue; // сущность статической информации о значении атрибута
    private DynAttrValue dynAttrValue; // сущность динамической информации о значении атрибута
}
