package com.digital.library.data;

public enum ResourceType {
    BOOK("Book"),
    USER("User"),
    LOAN("Loan");

    public final String label;

    private ResourceType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
