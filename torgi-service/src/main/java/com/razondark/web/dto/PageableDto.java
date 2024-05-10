package com.razondark.web.dto;

import lombok.Data;

/*

    dto информации о страницах в теле ответа

*/

@Data
public class PageableDto {
    private Object sort; // TODO: change
    private Integer pageNumber;
    private Integer pageSize;
}
