package com.razondark.dto;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.Getter;

@Getter
public enum LotStatus {
    PUBLISHED("Опубликован", VaadinIcon.INFO, "badge pill"),
    APPLICATIONS_SUBMISSION("Прием заявок", VaadinIcon.FILE_TEXT, "badge error pill"),
    DETERMINING_WINNER("Определение победителя", VaadinIcon.CLOCK, "badge contrast pill"),
    SUCCEED("Состоялся", VaadinIcon.CHECK, "badge success pill");

    private final String value;
    private final VaadinIcon icon;
    private final String theme;

    LotStatus(String value, VaadinIcon icon, String theme) {
        this.value = value;
        this.icon = icon;
        this.theme = theme;
    }
}
