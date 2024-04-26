package com.razondark.domain.dynamicAttributes;

import lombok.Data;

/*
    Сущность динамической информации о значении атрибута
*/

@Data
public class DynAttrValue {
    private String code; // код вида "0{int}"
    private String name; // название
}
