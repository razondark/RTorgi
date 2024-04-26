package com.razondark.domain.biddForm;

import lombok.Data;

import java.util.Date;
import java.util.List;

/*
    Сущность информации о типе предложения
*/

@Data
public class BiddType {
    private String code; // код вида "ZK"
    private String name; // полное название
    private boolean published; // статус
    private String shortName; // краткое название
    private int order; // номер
    private boolean disableShowTorgi; // выключенный показ торгов
    private String icon; // url изображения вида "61bb79f05925817b31629a61" [https://torgi.gov.ru/new/nsi/v1/file-store/{url}]
    private List<BiddingForm> biddingForms; // сущность форм для участия в торгах
    private Date startDate; // дата начала
    private Date endDate; // дата окончания
    private List<IncludeTypeBidding> includeTypeBidding; // сущность включенных типов ставки
}
