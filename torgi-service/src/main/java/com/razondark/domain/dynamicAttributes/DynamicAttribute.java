package com.razondark.domain.dynamicAttributes;

import lombok.Data;

/*
    Сущность динамической информации об атрибуте
*/

@Data
public class DynamicAttribute {
    private String code; // код вида "resourceLocation_EA(N)"
    private String name; // название
}
