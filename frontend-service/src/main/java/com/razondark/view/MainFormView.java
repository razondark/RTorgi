package com.razondark.view;

import com.razondark.dto.*;
import com.razondark.dto.response.*;
import com.razondark.service.ConfigService;
import com.razondark.service.DataService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@PageTitle("RTorgi")
@Route("")
public class MainFormView extends VerticalLayout {
    private final ConfigService configService;
    private final DataService dataService;

    private final AttributesResponse attributes;
    private final CategoriesResponse categories;
    private final BiddTypeResponse biddTypes;
    private final SpecificationsResponse specifications;

    private final FilterPanelItemsValues filterPanelItemsString;

    private final Grid<LotDto> grid;

    public MainFormView(ConfigService configService, DataService dataService) {
        this.configService = configService;
        this.dataService = dataService;

        this.attributes = this.configService.getAttributes();
        this.categories = this.configService.getCategories();
        this.biddTypes = this.configService.getBiddTypes();
        this.specifications = this.configService.getSpecifications();

        this.filterPanelItemsString = new FilterPanelItemsValues();

        var scroller = new Scroller();
        var verticalLayout = new VerticalLayout();

        add(createHeader());
        //verticalLayout.add(createHeader());
        verticalLayout.add(createFilters());

        grid = createGrid();
        verticalLayout.add(grid);

        scroller.setContent(verticalLayout);
        add(scroller);
    }

    private H1 createHeader() {
        var header = new H1("RTorgi");

        header.getStyle().set("text-align", "center");
        setHorizontalComponentAlignment(Alignment.CENTER, header);

        return header;
    }

    private VerticalLayout createFilters() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        try {
            var horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidth("100%");

            // start date picker
            var endDateFrom = createDatePicker("Дата окончания подачи заявок от", LocalDate.now(), "225px");
            filterPanelItemsString.setEndDateFrom(endDateFrom.getValue().toString());
            endDateFrom.addValueChangeListener(value ->
            {
                var selectedDate = value.getValue();
                filterPanelItemsString.setEndDateFrom(selectedDate.toString());
                grid.setItems(getLotsByParams());
            });

            horizontalLayout.add(endDateFrom);

            // end date picker
            var endDateTo = createDatePicker("Дата окончания подачи заявок до", null, "225px");
            endDateTo.addValueChangeListener(value ->
            {
                var selectedDate = value.getValue();
                filterPanelItemsString.setEndDateTo(selectedDate.toString());
                grid.setItems(getLotsByParams());
            });

            horizontalLayout.add(endDateTo);

            // regions multi select combo box
            var regionItems = attributes.getAttributes().stream()
                    .filter(item -> item.getCode().equals("resourceLocation"))
                    .flatMap(item -> item.getMappingTable().stream())
                    .map(mappingTableDto -> mappingTableDto.getDynAttrValue().getName())
                    .sorted()
                    .toList();

            var regionMultiSelectComboBox = createMultiSelectComboBox("Регионы", regionItems, "225px");
            regionMultiSelectComboBox.addValueChangeListener(values ->
            {
                var selectedLocations = values.getValue().stream()
                        .map(Object::toString)
                        .toList();

                if (selectedLocations.isEmpty()) {
                    filterPanelItemsString.setRegions(null);
                    grid.setItems(getLotsByParams());
                }

                var locationsWithCodes = attributes.getAttributes().stream()
                        .filter(item -> item.getCode().equals("resourceLocation"))
                        .flatMap(item -> item.getMappingTable().stream())
                        .map(MappingTableDto::getDynAttrValue)
                        .toList();

                var selectedCodes = new ArrayList<String>();
                for (var i : selectedLocations) {
                    for (var j : locationsWithCodes) {
                        if (i.equals(j.getName())) {
                            selectedCodes.add(j.getCode().substring(1));
                            break;
                        }
                    }
                }

                var codes = String.join(",", selectedCodes);
                filterPanelItemsString.setRegions(codes);

                grid.setItems(getLotsByParams());
            });

            horizontalLayout.add(regionMultiSelectComboBox);

            // cadastral Number TextField
            var cadastralNumberTextField = createTextField("Кадастровый номер", "225px");
            cadastralNumberTextField.addValueChangeListener(value ->
            {

            });
            cadastralNumberTextField.setValueChangeMode(ValueChangeMode.LAZY);
            horizontalLayout.add(cadastralNumberTextField);

            // notification Number TextField
            var notificationNumberTextField = createTextField("Извещение", "225px");
            notificationNumberTextField.addValueChangeListener(value ->
            {

            });
            notificationNumberTextField.setValueChangeMode(ValueChangeMode.LAZY);
            horizontalLayout.add(createTextField("Извещение", "225px"));

            // categories combo box
            var categoriesItems = categories.getCategories().stream()
                    .filter(item -> "2".equalsIgnoreCase(item.getCode()) || "2".equalsIgnoreCase(item.getParentCode()))
                    .map(Categories::getName)
                    .sorted()
                    .toList();

            var categoriesComboBox = createComboBox("Категории", categoriesItems, "225px");
            categoriesComboBox.setValue(categoriesItems.get(0));
            filterPanelItemsString.setCategory("2"); // TODO: change this hardcode ???
            categoriesComboBox.addValueChangeListener(values ->
            {
                var selectedCategory = values.getValue();

                if (selectedCategory.isEmpty()) {
                    filterPanelItemsString.setCategory(null);
                    grid.setItems(getLotsByParams());
                }

                var selectedCategoryCode = categories.getCategories().stream()
                        .filter(i -> i.getName().equalsIgnoreCase(selectedCategory))
                        .map(Categories::getCode)
                        .findFirst()
                        .get();


                filterPanelItemsString.setCategory(selectedCategoryCode);
                grid.setItems(getLotsByParams());
            });

            horizontalLayout.add(categoriesComboBox);


            var horizontalLayoutPermittedUse = new HorizontalLayout();
            horizontalLayoutPermittedUse.setWidth("100%");

            // permitted use multi select combo box
            var permittedUseItems = specifications.getSpecifications().stream()
                    .filter(item -> item.getCode().equalsIgnoreCase("PermittedUse"))
                    .flatMap(item -> item.getSelectNsi().stream())
                    .map(item -> item.name)
                    .sorted()
                    .toList();

            var permittedUseMultiSelectComboBox = createMultiSelectComboBox("ВРИ", permittedUseItems, "100%");
            permittedUseMultiSelectComboBox.addValueChangeListener(values ->
            {
                var selectedPermittedUse = values.getValue().stream()
                        .map(Object::toString)
                        .toList();

                if (selectedPermittedUse.isEmpty()) {
                    filterPanelItemsString.setPermittedUse(null);
                    grid.setItems(getLotsByParams());
                }

                var permittedUseCodes = specifications.getSpecifications().stream()
                                .filter(item -> item.getCode().equalsIgnoreCase("PermittedUse"))
                                .flatMap(item -> item.getSelectNsi().stream())
                                .toList();

                var selectedCodes = new ArrayList<String>();
                for (var i : selectedPermittedUse) {
                    for (var j : permittedUseCodes) {
                        if (i.equalsIgnoreCase(j.name)) {
                            selectedCodes.add(j.code);
                            break;
                        }
                    }
                }

                var codes = String.join(";", selectedCodes);

                filterPanelItemsString.setPermittedUse(codes);
                var i = getLotsByParams();

                grid.setItems(getLotsByParams());
            });

            horizontalLayoutPermittedUse.add(permittedUseMultiSelectComboBox);




            var verticalLayout = new VerticalLayout();

            verticalLayout.add(horizontalLayout);
            verticalLayout.add(horizontalLayoutPermittedUse);

            return verticalLayout;
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }

    private Grid<LotDto> createGrid() {
        try {
            var grid = new Grid<LotDto>();

            var data = getLotsByParams();
            grid.setItems(data);

            grid.setHeight("440px");

            //grid.addColumn(LotDto::getId).setHeader("URL");
            grid.addComponentColumn(lot -> {
                Anchor anchor = new Anchor("https://torgi.gov.ru/new/public/lots/lot/" + lot.getId(), lot.getId());
                anchor.setTarget("_blank"); // Открывать ссылку в новой вкладке
                return anchor;
            })
                    .setHeader("URL");

            grid.addColumn(LotDto::getLotStatus)
                    .setHeader("Статус");
            grid.addColumn(LotDto::getAreaValue)
                    .setHeader("Площадь");
            grid.addColumn(LotDto::getPriceMin)
                    .setHeader("Начальная цена")
                    .setSortable(true);
            grid.addColumn(LotDto::getCadCost)
                    .setHeader("Кадастровая стоимость")
                    .setSortable(true);
            grid.addColumn(LotDto::getPercentPriceCad)
                    .setHeader("% нач. цены от кад. стоим.")
                    .setSortable(true);
            grid.addColumn(LotDto::getBiddEndTime)
                    .setHeader("Окончание подачи заявок")
                    .setRenderer(new TextRenderer<>(item -> {
                        var dateTime = item.getBiddEndTime();
                        if (dateTime != null) {
                            var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.ENGLISH);
                            return formatter.format(dateTime.toInstant().atZone(ZoneId.of("Europe/Moscow")));
                        } else {
                            return "";
                        }
                    }));
            grid.addColumn(LotDto::getCharacteristics)
                    .setHeader("Категория")
                    .setRenderer(new TextRenderer<>(item -> {
                        if (item != null && item.getCategory() != null) {
                            return item.getCategory().getName();
                        } else {
                            return "";
                        }
                    }));
            grid.addColumn(LotDto::getCharacteristics)
                    .setHeader("ВРИ")
                    .setRenderer(new TextRenderer<>(item -> {
                        if (item != null && item.getPermittedUse() != null) {
                            return item.getPermittedUse();
                        }
                        else {
                            return "";
                        }
                    }));
            grid.addColumn(LotDto::getSubjectRFCode)
                    .setHeader("Код региона");
            grid.addColumn(LotDto::getSubjectRFCode)
                            .setHeader("Регион")
                            .setRenderer(new TextRenderer<>(item -> {
                                var regionName = attributes.getAttributes().stream()
                                        .filter(i -> i.getCode().equals("resourceLocation"))
                                        .flatMap(i -> i.getMappingTable().stream())
                                        .map(MappingTableDto::getBaseAttrValue)
                                        .filter(baseAttrValue -> baseAttrValue.getCode().equalsIgnoreCase(item.getSubjectRFCode()))
                                        .findFirst();


                                if (regionName.isPresent()) {
                                    return regionName.get().getName();
                                }
                                else {
                                    return "";
                                }
                            }));


            grid.getColumns().forEach(column -> column.setAutoWidth(true));
            return grid;
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private List<LotDto> getLotsByParams() {
        return dataService.getLots(
                filterPanelItemsString.getEndDateFrom(),
                filterPanelItemsString.getEndDateTo(),
                filterPanelItemsString.getRegions(),
                filterPanelItemsString.getCategory(),
                filterPanelItemsString.getPermittedUse(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0, 10);
    }

    private DatePicker createDatePicker(String title, LocalDate date, String width) {
        var datePicker = new DatePicker(title);
        datePicker.setValue(date);
        datePicker.setWidth(width);
        // all listener

        return datePicker;
    }

    private MultiSelectComboBox<String> createMultiSelectComboBox(String title, List<String> items, String width) {
        var multiSelectComboBox = new MultiSelectComboBox<String>(title);

        multiSelectComboBox.setItems(items);
        multiSelectComboBox.setAllowCustomValue(false); // Не разрешать ввод произвольных значений
        multiSelectComboBox.setWidth(width);

        return multiSelectComboBox;
    }

    private ComboBox<String> createComboBox(String title, List<String> items, String width) {
        var comboBox = new ComboBox<String>(title);

        comboBox.setItems(items);
        comboBox.setAllowCustomValue(false); // Не разрешать ввод произвольных значений
        comboBox.setWidth(width);

        return comboBox;
    }

    private TextField createTextField(String label, String width) {
        var textField = new TextField(label);

        textField.setWidth(width);

        return textField;
    }
}
