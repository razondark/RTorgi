package com.razondark.domain.dynamicAttributes;

import lombok.Data;

import java.util.List;

@Data
public class DisplayCondition {
    private TypeBidd typeBidd;
    private List<FormBidd> formBidd;
}
