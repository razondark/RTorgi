package com.razondark.service;

import com.razondark.dto.LotDto;
import com.razondark.dto.response.LotResponse;

import java.util.List;

public interface DataService {
    // links
    String getLotPageLink();
    String getMapEGRPLink();

    //List<LotDto> getAllLots(Integer page, Integer size);
    List<LotDto> getLotsBySubjects(String subjects, Integer page, Integer size);

    LotResponse getLots(String endDateFrom, String endDateTo, String regions, String category, String permittedUse,
                         String squareFrom, String squareTo,
                         String startPriceFrom, String startPriceTo,
                         String cadCostFrom, String cadCostTo,
                         String percentPriceCadFrom, String percentPriceCadTo,
                         Integer page, Integer size, String text);
}
