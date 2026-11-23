package com.osa.api;
/** Field is a logical POM element name for UI, or a JSON field name for API. */
public class UsersValues {
    private final String field;
    private final String expectedValue;
    public UsersValues(String field, String expectedValue) {
        this.field = field;
        this.expectedValue = expectedValue;
    }
    public String getField() { return field; }
    public String getExpectedValue() { return expectedValue; }
}
