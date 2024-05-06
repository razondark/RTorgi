package com.razondark.service;

import com.razondark.dto.LotDto;
import com.razondark.dto.response.LotResponse;

import java.util.List;

public interface DataService {
    List<LotDto> getAllLots(Integer page, Integer size);
    List<LotDto> getLotsBySubjects(String subjects, Integer page, Integer size);
}
