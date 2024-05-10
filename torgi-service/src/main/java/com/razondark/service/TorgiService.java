package com.razondark.service;

import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.response.LotResponse;

import java.util.List;

public interface TorgiService {
    List<LotDto> getLotsAll(Integer page, Integer size);
    List<LotDto> getLotsBySubject(String subjects, Integer page, Integer size);
    LotResponse getLots(String endDateFrom, String endDateTo, String regions, String category, String permittedUse,
                        String squareFrom, String squareTo,
                        String startPriceFrom, String startPriceTo,
                        String cadCostFrom, String cadCostTo,
                        String percentPriceCadFrom, String percentPriceCadTo,
                        Integer page, Integer size, String text);
}
