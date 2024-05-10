package com.razondark.web.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razondark.web.dto.CharacteristicValue;
import com.razondark.web.dto.CharacteristicsDto;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.response.LotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LotMapper {
    private final ObjectMapper objectMapper;

    public LotDto jsonToDto(String json) {
        try {
            var dto = objectMapper.readValue(json, LotDto.class);

            parseCadastralNumber(dto);
            parseAreaValue(dto);
            parsePermittedUse(dto);

            return dto;
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize LotDto from JSON", e);
        }
    }

    public LotResponse jsonToDtoList(String json) {
        try {
            var response = objectMapper.readValue(json, LotResponse.class);

            for (var dto : response.getContent()) {
                parseCadastralNumber(dto);
                parseAreaValue(dto);
                parsePermittedUse(dto);
            }

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize LotDto from JSON", e);
        }
    }

    private void parseAreaValue(LotDto lotDto) {
        var areaObject = lotDto.getCharacteristics()
                .stream()
                .filter(i -> i.getCode().equalsIgnoreCase("SquareZU"))
                .map(CharacteristicsDto::getCharacteristicValue)
                .findFirst();

        if (areaObject.isPresent()) {
            if (areaObject.get() instanceof Double value) {
                lotDto.setAreaValue(BigDecimal.valueOf(value));
            }
        }
    }

    private void parseCadastralNumber(LotDto lotDto) {
        var cadNumberObject = lotDto.getCharacteristics().stream()
                .filter(i -> i.getCode().equalsIgnoreCase("CadastralNumber"))
                .map(CharacteristicsDto::getCharacteristicValue)
                .findFirst();

        if (cadNumberObject.isPresent()) {
            if (cadNumberObject.get() instanceof String value) {
                if (!value.isEmpty()) {
                    lotDto.setCadNumber(value);
                }
            }
        }
    }

    private void parsePermittedUse(LotDto lotDto) {
        var permUseObject = lotDto.getCharacteristics()
                .stream()
                .filter(i -> i.getCode().equalsIgnoreCase("PermittedUse"))
                .map(CharacteristicsDto::getCharacteristicValue)
                .findFirst();

        if (permUseObject.isPresent()) {
            if (permUseObject.get() instanceof ArrayList<?>) {
                var permittedUseList = new ArrayList<String>();

                for (var item : (ArrayList<?>) permUseObject.get()) {
                    var objectMapper = new ObjectMapper();

                    if (item instanceof LinkedHashMap) {
                        var value = objectMapper.convertValue(item, CharacteristicValue.class);
                        permittedUseList.add(value.getName());
                    }
                }

                lotDto.setPermittedUse(String.join(", ", permittedUseList));
            }
        }
    }
}
