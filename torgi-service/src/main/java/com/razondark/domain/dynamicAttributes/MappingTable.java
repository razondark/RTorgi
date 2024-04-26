package com.razondark.domain.dynamicAttributes;

import lombok.Data;

import java.util.List;

/*
    Сущность информации атрибута
*/

@Data
public class MappingTable {
    private String code; // код
    private BaseAttrValue baseAttrValue; // сущность статической информации о значении атрибута
    private DynAttrValue dynAttrValue; // сущность динамической информации о значении атрибута
    private List<DynamicAttribute> dynamicAttribute; // сущности динамической информации об атрибуте
    private ValueAttribute valueAttribute; // сущности значения атрибута
}
