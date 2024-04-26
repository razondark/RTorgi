package com.razondark.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LotInfoDto {
    private Feature feature;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        private Attrs attrs;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attrs {
        @JsonProperty("cad_cost")
        private BigDecimal cadCost;
        @JsonProperty("area_value")
        private BigDecimal areaValue;
    }
}
