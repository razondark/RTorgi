package com.razondark.web.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.response.LotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LotMapper {
    private final ObjectMapper objectMapper;

    public LotDto jsonToDto(String json) {
        try {
            return objectMapper.readValue(json, LotDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize LotDto from JSON", e);
        }
    }

    public List<LotDto> jsonToDtoList(String json) {
        try {
            var content = objectMapper.readValue(json, LotResponse.class);
            return content.getContent();
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize LotDto from JSON", e);
        }
    }
}
