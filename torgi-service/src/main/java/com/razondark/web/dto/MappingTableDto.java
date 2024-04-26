package com.razondark.web.dto;

import com.razondark.domain.dynamicAttributes.BaseAttrValue;
import com.razondark.domain.dynamicAttributes.DynAttrValue;
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
