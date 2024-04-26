package com.razondark.domain.biddForm;

import lombok.Data;

/*
    Сущность формы для участия в торгах
*/

@Data
public class BiddingForm {
    private String code; // код вида "EA"
    private String name; // название
}
