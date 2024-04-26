package com.razondark.domain.ownershipForm;

import lombok.Data;

import java.util.List;

/*
    Сущность типа собственности
*/

@Data
public class OwnershipForm {
    private String code; // код
    private String name; // название собственности
    private boolean published; // статус

    // в данный момент null или empty
    private List<Object> children;
    private String assemblyAlgorithm;
}
