package com.razondark.service;

import com.razondark.web.dto.LotDto;

import java.util.List;

public interface TorgiService {
    List<LotDto> getLotsAll(Integer page, Integer size);
    List<LotDto> getLothBySubject(Integer subject, Integer page, Integer size);

}
