package com.razondark.view;

import com.razondark.dto.AttributesDto;
import com.razondark.dto.DynAttrValue;
import com.razondark.dto.MappingTableDto;
import com.razondark.dto.response.AttributesResponse;
import com.razondark.service.ConfigService;
import com.razondark.service.impl.ConfigServiceImpl;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("RTorgi")
@Route("")
public class MainFormView extends VerticalLayout {
    private final ConfigService configService;

    public MainFormView(ConfigService configService) {
        this.configService = configService;

        createHeader();
        createFilters();
    }

    private void createHeader() {
        var header = new H1("RTorgi");

        header.getStyle().set("text-align", "center");
        add(header);
        setHorizontalComponentAlignment(Alignment.CENTER, header);
    }

    private void createFilters() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        try {
            //var categories = configService.getCategories();
            //var biddTypes = configService.getBiddTypes();
            var attributes = configService.getAttributes();
            //var specifications = configService.getSpecifications();

            var horizontalLayout = new HorizontalLayout();
            var startDatePicker = new DatePicker("Дата начала подачи заявок");
            startDatePicker.setValue(LocalDate.now());
            //startDatePicker.setWidth("225px");
            // all listener

            var endDatePicker = new DatePicker("Дата окончания подачи заявок");
            //endDatePicker.setWidth("225px");
            // all listener

            var locationComboBox = new MultiSelectComboBox<>("Регионы");
            locationComboBox.setItems(attributes.getAttributes().stream()
                    .filter(item -> item.getCode().equals("resourceLocation"))
                    .flatMap(item -> item.getMappingTable().stream())
                    .map(mappingTableDto -> mappingTableDto.getDynAttrValue().getName())
                    .sorted()
                    .collect(Collectors.toList()));
            locationComboBox.setAllowCustomValue(false); // Не разрешать ввод произвольных значений
            //locationComboBox.setWidth("125px");

            horizontalLayout.add(startDatePicker);
            horizontalLayout.add(endDatePicker);
            horizontalLayout.add(locationComboBox);
            horizontalLayout.setWidthFull();

            add(horizontalLayout);
        }
        catch (Exception e) {
            e.printStackTrace();
        }











    }
}
