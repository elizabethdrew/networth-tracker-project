package com.drew.commonlibrary.types;

public enum TaxYear {
    YEAR_2020_2021("2020-2021"),
    YEAR_2021_2022("2021-2022"),
    YEAR_2022_2023("2022-2023"),
    YEAR_2023_2024("2023-2024"),
    YEAR_2024_2025("2024-2025");

    private final String yearString;

    TaxYear(String yearString) {
        this.yearString = yearString;
    }

    @Override
    public String toString() {
        return this.yearString;
    }
}
