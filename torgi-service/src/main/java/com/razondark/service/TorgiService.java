package com.razondark.service;

import com.razondark.web.dto.LotDto;

import java.util.List;

public interface TorgiService {
    List<LotDto> getLotsAll(Integer page, Integer size);
    List<LotDto> getLotsBySubject(String subjects, Integer page, Integer size);
    List<LotDto> getLots(String endDateFrom, String endDateTo, String regions, String cadastralNumber, String lotNumber,
            String categories, String permittedUse, Integer page, Integer size);
}
