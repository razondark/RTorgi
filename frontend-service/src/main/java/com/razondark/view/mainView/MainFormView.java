package com.razondark.view.mainView;

import com.razondark.dto.*;
import com.razondark.dto.response.*;
import com.razondark.service.ConfigService;
import com.razondark.service.DataService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@PageTitle("RTorgi")
@Route("")
public class MainFormView extends VerticalLayout {
    private final ConfigService configService;
    private final DataService dataService;

    private final AttributesResponse attributes;
    private final CategoriesResponse categories;
    //private final BiddTypeResponse biddTypes; // TODO: add later
    private final SpecificationsResponse specifications;

    private final FilterPanelItemsValues filterPanelItemsString;
    private PageableDto pageableDto;

    private final Grid<LotDto> grid;
    //private final com.vaadin.flow.component.checkbox.Checkbox moreInfoCheckBox;

    public MainFormView(ConfigService configService, DataService dataService) {
        this.configService = configService;
        this.dataService = dataService;

        this.attributes = this.configService.getAttributes();
        this.categories = this.configService.getCategories();
        //this.biddTypes = this.configService.getBiddTypes();
        this.specifications = this.configService.getSpecifications();

        this.filterPanelItemsString = new FilterPanelItemsValues();

        setSizeFull();
        setMargin(false);
        setSpacing(false);

        var scroller = new Scroller();
        scroller.setWidth("100%");

        var verticalLayout = new VerticalLayout();

        add(createHeader());
        //verticalLayout.add(createHeader());
        verticalLayout.add(createFilters());

        //this.moreInfoCheckBox = new Checkbox("Отображать кадастровую стоимость и %");
        //verticalLayout.add(moreInfoCheckBox);

        grid = createGrid();
        verticalLayout.add(grid);

        verticalLayout.add(createGridNavigationField());

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
        try {
            var searchLayout = new HorizontalLayout();
            searchLayout.setWidth("100%");

            // search field
            var searchTextField = createTextField("Поиск", "100%");
            searchTextField.addValueChangeListener(value ->
            {
                var text = value.getValue();
                if (text.isEmpty()) {
                    filterPanelItemsString.setText(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                    return;
                }

                filterPanelItemsString.setText(text);
                filterPanelItemsString.setCurrentPage(0);
                updateGridData();
            });
            searchTextField.setValueChangeMode(ValueChangeMode.LAZY);
            searchLayout.add(searchTextField);

            var horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidth("100%");

            // start date picker
            var endDateFrom = createDatePicker("Дата окончания подачи заявок от", LocalDate.now(), "25%");
            filterPanelItemsString.setEndDateFrom(endDateFrom.getValue().toString());
            endDateFrom.addValueChangeListener(value ->
            {
                var selectedDate = value.getValue();
                filterPanelItemsString.setEndDateFrom(selectedDate.toString());
                filterPanelItemsString.setCurrentPage(0);
                updateGridData();
            });

            horizontalLayout.add(endDateFrom);

            // end date picker
            var endDateTo = createDatePicker("Дата окончания подачи заявок до", null, "25%");
            endDateTo.addValueChangeListener(value ->
            {
                var selectedDate = value.getValue();
                filterPanelItemsString.setEndDateTo(selectedDate.toString());
                filterPanelItemsString.setCurrentPage(0);
                updateGridData();
            });

            horizontalLayout.add(endDateTo);

            // regions multi select combo box
            var regionItems = attributes.getAttributes().stream()
                    .filter(item -> item.getCode().equals("resourceLocation"))
                    .flatMap(item -> item.getMappingTable().stream())
                    .map(mappingTableDto -> mappingTableDto.getDynAttrValue().getName())
                    .sorted()
                    .toList();

            var regionMultiSelectComboBox = createMultiSelectComboBox("Регионы", regionItems, "25%");
            regionMultiSelectComboBox.addValueChangeListener(values ->
            {
                var selectedLocations = values.getValue().stream()
                        .map(Object::toString)
                        .toList();

                if (selectedLocations.isEmpty()) {
                    filterPanelItemsString.setRegions(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
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
                filterPanelItemsString.setCurrentPage(0);

                updateGridData();
            });

            horizontalLayout.add(regionMultiSelectComboBox);

//            // cadastral Number TextField
//            var cadastralNumberTextField = createTextField("Кадастровый номер", "225px");
//            cadastralNumberTextField.addValueChangeListener(value ->
//            {
//
//            });
//            cadastralNumberTextField.setValueChangeMode(ValueChangeMode.LAZY);
//            horizontalLayout.add(cadastralNumberTextField);
//
//            // notification Number TextField
//            var notificationNumberTextField = createTextField("Извещение", "225px");
//            notificationNumberTextField.addValueChangeListener(value ->
//            {
//
//            });
//            notificationNumberTextField.setValueChangeMode(ValueChangeMode.LAZY);
//            horizontalLayout.add(createTextField("Извещение", "225px"));

            // categories combo box
            var categoriesItems = categories.getCategories().stream()
                    .filter(item -> "2".equalsIgnoreCase(item.getCode()) || "2".equalsIgnoreCase(item.getParentCode()))
                    .map(Categories::getName)
                    .sorted()
                    .toList();

            var categoriesComboBox = createComboBox("Категории", categoriesItems, "25%");
            categoriesComboBox.setValue(categoriesItems.get(0));
            filterPanelItemsString.setCategory("2"); // TODO: change this hardcode ???
            categoriesComboBox.addValueChangeListener(values ->
            {
                var selectedCategory = values.getValue();

                if (selectedCategory.isEmpty()) {
                    filterPanelItemsString.setCategory(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                }

                var selectedCategoryCodeObject = categories.getCategories().stream()
                        .filter(i -> i.getName().equalsIgnoreCase(selectedCategory))
                        .map(Categories::getCode)
                        .findFirst();

                if (selectedCategoryCodeObject.isPresent()) {
                    filterPanelItemsString.setCategory(selectedCategoryCodeObject.get());
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                }
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
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
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
                filterPanelItemsString.setCurrentPage(0);
                updateGridData();
            });

            horizontalLayoutPermittedUse.add(permittedUseMultiSelectComboBox);


            var horizontalLayoutFilter = new HorizontalLayout();
            horizontalLayoutFilter.setWidth("100%");

            var searchMinPriceMoreTextField = createTextField("Начальная цена (более)", "25%");
            searchMinPriceMoreTextField.addValueChangeListener(value ->
            {
                if (value.getValue().isEmpty()) {
                    filterPanelItemsString.setStartPriceFrom(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                    return;
                }

                try {
                    new BigDecimal(value.getValue());

                    filterPanelItemsString.setStartPriceFrom(value.getValue());
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                }
                catch (Exception e) {
                    showExceptionDialog(e.getMessage());
                }
            });
            searchMinPriceMoreTextField.setValueChangeMode(ValueChangeMode.LAZY);
            horizontalLayoutFilter.add(searchMinPriceMoreTextField);

            var searchMinPriceLessTextField = createTextField("Начальная цена (менее)", "25%");
            searchMinPriceLessTextField.addValueChangeListener(value ->
            {
                if (value.getValue().isEmpty()) {
                    filterPanelItemsString.setStartPriceTo(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                    return;
                }

                try {
                    new BigDecimal(value.getValue());

                    filterPanelItemsString.setStartPriceTo(value.getValue());
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                }
                catch (Exception e) {
                    showExceptionDialog(e.getMessage());
                }
            });
            searchMinPriceLessTextField.setValueChangeMode(ValueChangeMode.LAZY);
            horizontalLayoutFilter.add(searchMinPriceLessTextField);

            var squareMoreTextField = createTextField("Площадь, м2 (более)", "25%");
            squareMoreTextField.addValueChangeListener(value ->
            {
                if (value.getValue().isEmpty()) {
                    filterPanelItemsString.setSquareFrom(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                    return;
                }

                try {
                    new BigDecimal(value.getValue());

                    filterPanelItemsString.setSquareFrom(value.getValue());
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                }
                catch (Exception e) {
                    showExceptionDialog(e.getMessage());
                }
            });
            squareMoreTextField.setValueChangeMode(ValueChangeMode.LAZY);
            horizontalLayoutFilter.add(squareMoreTextField);

            var squareLessTextField = createTextField("Площадь, м2 (менее)", "25%");
            squareLessTextField.addValueChangeListener(value ->
            {
                if (value.getValue().isEmpty()) {
                    filterPanelItemsString.setSquareTo(null);
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                    return;
                }

                try {
                    new BigDecimal(value.getValue());

                    filterPanelItemsString.setSquareTo(value.getValue());
                    filterPanelItemsString.setCurrentPage(0);
                    updateGridData();
                }
                catch (Exception e) {
                    showExceptionDialog(e.getMessage());
                }
            });
            squareLessTextField.setValueChangeMode(ValueChangeMode.LAZY);
            horizontalLayoutFilter.add(squareLessTextField);

            var verticalLayout = new VerticalLayout();
            verticalLayout.setMargin(false);
            verticalLayout.setSpacing(false);

            verticalLayout.add(searchLayout);
            verticalLayout.add(horizontalLayout);
            verticalLayout.add(horizontalLayoutPermittedUse);
            verticalLayout.add(horizontalLayoutFilter);

            return verticalLayout;
        }
        catch (Exception e) {
            showExceptionDialog(e.getMessage());
        }



        return null;
    }

    private HorizontalLayout createGridNavigationField() {
        var horizontalLayout = new HorizontalLayout();

        var prevButton = new Button(VaadinIcon.CHEVRON_LEFT.create());
        prevButton.setEnabled(false);

        var currentPage = new TextField();
        currentPage.setValue(filterPanelItemsString.getCurrentPage().toString());

        var nextButton = new Button(VaadinIcon.CHEVRON_RIGHT.create());

        prevButton.addClickListener(i -> {
            var newPage = filterPanelItemsString.getCurrentPage() - 1;
            filterPanelItemsString.setCurrentPage(newPage);
            currentPage.setValue(filterPanelItemsString.getCurrentPage().toString());
            updateGridData();

            nextButton.setEnabled(!filterPanelItemsString.isLastPage());
            prevButton.setEnabled(!filterPanelItemsString.isFirstPage());
        });

        currentPage.addValueChangeListener(item -> {
            try {
                var value = Integer.parseInt(item.getValue());

                filterPanelItemsString.setCurrentPage(value);
                updateGridData();
            }
            catch (Exception e) {
                showExceptionDialog(e.getMessage());
            }
        });

        nextButton.addClickListener(i -> {
            var newPage = filterPanelItemsString.getCurrentPage() + 1;
            filterPanelItemsString.setCurrentPage(newPage);
            currentPage.setValue(filterPanelItemsString.getCurrentPage().toString());
            updateGridData();

            prevButton.setEnabled(!filterPanelItemsString.isFirstPage());
            nextButton.setEnabled(!filterPanelItemsString.isLastPage());
        });

        horizontalLayout.add(prevButton, currentPage, nextButton);

        return horizontalLayout;
    }

    private Grid<LotDto> createGrid() {
        try {
            var grid = new Grid<LotDto>();

            var data = getLotsByParams().getContent();
            grid.setItems(data);

            grid.setWidth("100%");
            grid.setHeight("440px");

            grid.addComponentColumn(lot -> {
                Anchor anchor = new Anchor(dataService.getLotPageLink() + lot.getId(), lot.getId());
                anchor.setTarget("_blank"); // new page
                return anchor;
            })
                    .setHeader("URL");
            grid.addColumn(LotDto::getCadNumber)
                            .setHeader("Кадастровый номер");
            grid.addColumn(LotDto::getLotStatus)
                    .setHeader("Статус")
                    .setRenderer(new ComponentRenderer<>(item -> {
                        var pending = new Span(createIcon(LotStatus.valueOf(item.getLotStatus()).getIcon()),
                                new Span(LotStatus.valueOf(item.getLotStatus()).getValue()));

                        pending.getElement().getThemeList().add(LotStatus.valueOf(item.getLotStatus()).getTheme());

                        return pending;
                    }));
            grid.addColumn(LotDto::getAreaValue)
                    .setHeader("Площадь, м2")
                    .setSortable(true)
                    .setRenderer(new TextRenderer<>(item -> {
                        if (item.getAreaValue() == null) {
                            return "";
                        }

                        var symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator(' ');
                        symbols.setDecimalSeparator('.');
                        var formatter = new DecimalFormat("#,##0.00", symbols);
                        return formatter.format(item.getAreaValue());
                    }));
            grid.addColumn(LotDto::getPriceMin)
                    .setHeader("Начальная цена")
                    .setSortable(true)
                    .setRenderer(new TextRenderer<>(item -> {
                        if (item.getPriceMin() == null) {
                            return "";
                        }

                        var symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator(' ');
                        symbols.setDecimalSeparator('.');
                        var formatter = new DecimalFormat("#,##0.00", symbols);
                        var value = formatter.format(item.getPriceMin());

                        return value + " ₽";
                    }));
            grid.addColumn(LotDto::getCadCost)
                    .setHeader("Кадастровая стоимость")
                    .setSortable(true)
                    .setRenderer(new TextRenderer<>(item -> {
                        if (item.getCadCost() == null) {
                            return "";
                        }

                        var symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator(' ');
                        symbols.setDecimalSeparator('.');
                        var formatter = new DecimalFormat("#,##0.00", symbols);
                        var value = formatter.format(item.getCadCost());

                        return value + " ₽";
                    }));;
            grid.addColumn(LotDto::getPercentPriceCad)
                    .setHeader("% нач. цены от кад. стоим.")
                    .setSortable(true)
                    .setRenderer(new TextRenderer<>(i -> {
                        if (i.getPercentPriceCad() != null) {
                            var value = i.getPercentPriceCad() * 100;
                            return value + " %";
                        }

                        return "";
                    }));
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
            showExceptionDialog(e.getMessage());
        }


        return null;
    }

    private void updateGridData() {
        try {
            var data = getLotsByParams();

            filterPanelItemsString.setPageInfo(data.getPagesInfo());
            filterPanelItemsString.setCurrentPage(data.getPagesInfo().getPageNumber());
            filterPanelItemsString.setLastPage(data.isLast());
            filterPanelItemsString.setFirstPage(data.isFirst());

            grid.setItems(data.getContent());
            grid.getColumns().forEach(column -> column.setAutoWidth(true));
        }
        catch (Exception e) {
            showExceptionDialog(e.getMessage());
        }
    }

    private void showExceptionDialog(String message) {
        var dialog = new Dialog("Ошибка");
        dialog.setModal(true);

        dialog.getFooter().add(new Text(message));
        dialog.getFooter().add(new Button("Закрыть", e -> dialog.close()));

        dialog.open();
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs)");
        return icon;
    }

    private LotResponse getLotsByParams() {
        return dataService.getLots(
                filterPanelItemsString.getEndDateFrom(),
                filterPanelItemsString.getEndDateTo(),
                filterPanelItemsString.getRegions(),
                filterPanelItemsString.getCategory(),
                filterPanelItemsString.getPermittedUse(),
                filterPanelItemsString.getSquareFrom(),
                filterPanelItemsString.getSquareTo(),
                filterPanelItemsString.getStartPriceFrom(),
                filterPanelItemsString.getStartPriceTo(),
                null,
                null,
                null,
                null,
                filterPanelItemsString.getCurrentPage(),
                100,
                filterPanelItemsString.getText());
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
