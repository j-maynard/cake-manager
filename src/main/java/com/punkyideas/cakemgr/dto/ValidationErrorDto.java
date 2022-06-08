package com.punkyideas.cakemgr.dto;

import java.util.Objects;

public class ValidationErrorDto {
    private final String fieldName;
    private final String message;

    public ValidationErrorDto(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "fieldName = " + fieldName + ", message = " + message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ValidationErrorDto)) return false;
        ValidationErrorDto msg = (ValidationErrorDto) obj;
        return Objects.equals(fieldName, msg.fieldName)
                && Objects.equals(message, msg.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, message);
    }
}
