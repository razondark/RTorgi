package com.razondark.web.dto;

import com.razondark.domain.category.Categories;
import com.razondark.domain.dynamicAttributes.ValueAttribute;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
    dto лота
*/

@Data
public class LotDto {
    private String id;
    private String lotStatus;
    private BiddTypeDto biddType;
    private String lotName;
    private String lotDescription;
    private BigDecimal priceMin;
    private BigDecimal cadCost;
    private List<CharacteristicsDto> characteristics;
    private String permittedUse;
    private String subjectRFCode;
    private Date biddEndTime;
    private BigDecimal areaValue;
    private Double percentPriceCad;
    private ValueAttribute category; // same fields "code", "name"
}
