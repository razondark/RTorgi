package com.razondark.dto;

import lombok.Data;

@Data
public class Categories {
    private String code;
    private String name;
    private boolean published;
    private boolean requiredPhotoOrImage;
    private boolean flagPrivatizationPlans;
    private int order;
    private String parentCode;
    private String defaultImage;
    private String icon;
}
