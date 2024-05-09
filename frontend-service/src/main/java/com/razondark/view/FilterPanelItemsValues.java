package com.razondark.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterPanelItemsValues {
    private String endDateFrom = null;
    private String endDateTo = null;
    private String regions = null;
    private String category = null;
    private String permittedUse = null;
    private String squareFrom = null;
    private String squareTo = null;
    private String startPriceFrom = null;
    private String startPriceTo = null;
    private String cadCostFrom = null;
    private String cadCostTo = null;
    private String percentPriceCadFrom = null;
    private String percentPriceCadTo = null;
    private String text = null;
}
