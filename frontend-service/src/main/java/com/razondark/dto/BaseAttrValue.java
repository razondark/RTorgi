package com.razondark.dto;

import lombok.Data;

/*
    Сущность статической информации о значении атрибута
*/

@Data
public class BaseAttrValue {
    private String code; // код вида "int"
    private String name; // название
}
