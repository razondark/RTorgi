package com.razondark.dto;

import lombok.Data;

/*
    Сущность значения атрибута
*/

@Data
public class ValueAttribute {
    private String code; // код вида {int}
    private String name; // название
}
