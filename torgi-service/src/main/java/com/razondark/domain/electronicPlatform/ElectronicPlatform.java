package com.razondark.domain.electronicPlatform;

import lombok.Data;

/*
    Сущность информации об электронной платформе
*/

@Data
public class ElectronicPlatform {
    private String code; // код вида "ETP_EETP"
    private String name; // полное название
    private boolean published; // статус
    private String site; // url торговой площадки вида "roseltorg.ru"
    private String shortName; // краткое название
    private Icon icon; // сущность информации об изображении
    private String keyETP; // код ETP
}
