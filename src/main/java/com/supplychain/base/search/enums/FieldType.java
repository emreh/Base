package com.supplychain.base.search.enums;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public enum FieldType {

    BOOLEAN {
        public Object parse(String value) { return Boolean.valueOf(value); }
    },
    CHAR {
        public Object parse(String value) { return value.charAt(0); }
    },
    DATE {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        public Object parse(String value) {
            try {
                return LocalDateTime.parse(value, FORMATTER);
            } catch (Exception e) {
                log.warn("Failed to parse DATE value: {}", value, e);
                return null;
            }
        }
    },
    DOUBLE {
        public Object parse(String value) { return Double.valueOf(value); }
    },
    INTEGER {
        public Object parse(String value) { return Integer.valueOf(value); }
    },
    LONG {
        public Object parse(String value) { return Long.valueOf(value); }
    },
    STRING {
        public Object parse(String value) { return value; }
    };

    public abstract Object parse(String value);

}
