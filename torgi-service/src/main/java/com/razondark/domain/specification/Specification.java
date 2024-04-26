package com.razondark.domain.specification;

import lombok.Data;

import java.util.List;

@Data
public class Specification {
    private String code;
    private String name;
    private boolean published;
    private boolean required;
    private List<CategoryCode> categoryCodes;
    private FieldType fieldType;
    private Units units;
    private List<SelectNsi> selectNsi;

    public static class CategoryCode{
        public String code;
        public String name;
    }

    public static class FieldType{
        public String code;
        public String name;
    }

    public static class GroupValue{
        public String code;
        public String name;
    }

    public static class SelectNsi{
        public String code;
        public String name;
        public List<GroupValue> groupValues;
    }

    public static class Units{
        public String code;
        public String name;
    }
}
