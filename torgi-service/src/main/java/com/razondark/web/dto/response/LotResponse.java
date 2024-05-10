package com.razondark.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.razondark.web.dto.LotDto;
import com.razondark.web.dto.PageableDto;
import lombok.Data;

import java.util.List;

@Data
public class LotResponse {
    @JsonProperty(value = "pageable")
    private PageableDto pagesInfo;

    private Integer totalPages;
    private Integer totalElements;
    private boolean first;
    private boolean last;
    private List<LotDto> content;
}
