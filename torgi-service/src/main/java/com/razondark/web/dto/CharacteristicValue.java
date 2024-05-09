package com.razondark.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacteristicValue {
    private String value;
    private String selectNsi;
    private String code;
    private String name;
    private String actual;
}
