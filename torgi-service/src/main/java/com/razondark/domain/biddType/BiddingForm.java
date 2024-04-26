package com.razondark.domain.biddType;

import lombok.Data;

/*
    Сущность формы для участия в торгах
*/

@Data
public class BiddingForm {
    private String code; // код вида "EA"
    private String name; // название
}
