package com.razondark.view;

import com.razondark.dto.Categories;
import com.razondark.dto.LotDto;
import com.razondark.dto.response.AttributesResponse;
import com.razondark.service.ConfigService;
import com.razondark.service.DataService;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;

@PageTitle("RTorgi")
@Route("")
public class MainFormView extends VerticalLayout {
    private final ConfigService configService;
    private final DataService dataService;

    private final AttributesResponse attributes;
    private final Grid<LotDto> grid;

    public MainFormView(ConfigService configService, DataService dataService) {
        this.configService = configService;
        this.dataService = dataService;

        this.attributes = this.configService.getAttributes();

        var verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        add(createHeader());
        //verticalLayout.add(createHeader());
        verticalLayout.add(createFilters());

        grid = createGrid();
        verticalLayout.add(grid);

        add(verticalLayout);
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
            var categories = configService.getCategories();
            var biddTypes = configService.getBiddTypes();
            //var attributes = configService.getAttributes();
            var specifications = configService.getSpecifications();

            var horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidth("100%");

            // start date picker
            horizontalLayout.add(createDatePicker("Дата окончания подачи заявок от", LocalDate.now(), "225px"));

            // end date picker
            horizontalLayout.add(createDatePicker("Дата окончания подачи заявок до", null, "225px"));

            // locations multi select combo box
            var locationItems = attributes.getAttributes().stream()
                    .filter(item -> item.getCode().equals("resourceLocation"))
                    .flatMap(item -> item.getMappingTable().stream())
                    .map(mappingTableDto -> mappingTableDto.getDynAttrValue().getName())
                    .sorted()
                    .toList();

            var locationMultiSelectComboBox = createMultiSelectComboBox("Регионы", locationItems, "225px");
            locationMultiSelectComboBox.addValueChangeListener(event ->
            {
                List<String> selectedLocations = event.getValue().stream()
                        .map(Object::toString)
                        .toList();

                if (selectedLocations.isEmpty()) {
                    var data = dataService.getAllLots(1, 5); // TODO: change
                    grid.setItems(data);
                }

                List<String> selectedCodes = attributes.getAttributes().stream()
                        .filter(item -> item.getCode().equals("resourceLocation"))
                        .flatMap(item -> item.getMappingTable().stream())
                        .filter(j -> selectedLocations.contains(j.getDynAttrValue()))
                        .map(j -> j.getCode().substring(1))
                        .toList();

                String joinedCodes = String.join(",", selectedCodes);
                var data = dataService.getLotsBySubjects(joinedCodes, 1, 5);
                grid.setItems(data);
            });

            horizontalLayout.add(locationMultiSelectComboBox);

            // cadastral Number TextField
            horizontalLayout.add(createTextField("Кадастровый номер", "225px"));

            // notification Number TextField
            horizontalLayout.add(createTextField("Извещение", "225px"));

            // categories multi select combo box
            var categoriesItems = categories.getCategories().stream()
                    .filter(item -> "2".equalsIgnoreCase(item.getCode()) || "2".equalsIgnoreCase(item.getParentCode()))
                    .map(Categories::getName)
                    .sorted()
                    .toList();

            var categoriesMultiSelectComboBox = createMultiSelectComboBox("Категории", locationItems, "225px");
            categoriesMultiSelectComboBox.addValueChangeListener(event ->
            {

            });

            horizontalLayout.add(categoriesMultiSelectComboBox);


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
        //var grid = new Grid<LotDto>();

        try {
            var grid = new Grid<LotDto>();
            var data = dataService.getAllLots(1, 5);

            grid.setItems(data);

            //grid.addColumn(LotDto::getId).setHeader("URL");
            grid.addComponentColumn(lot -> {
                Anchor anchor = new Anchor("https://torgi.gov.ru/new/public/lots/lot/" + lot.getId(), lot.getId());
                anchor.setTarget("_blank"); // Открывать ссылку в новой вкладке
                return anchor;
            }).setHeader("URL");

            grid.addColumn(LotDto::getPriceMin).setHeader("Min price");
            grid.addColumn(LotDto::getCadCost).setHeader("Cad cost");
            grid.addColumn(LotDto::getPercentPriceCad).setHeader("percent");



            return grid;
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return null;
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

    private TextField createTextField(String label, String width) {
        var textField = new TextField(label);

        textField.setWidth(width);

        return textField;
    }
}
