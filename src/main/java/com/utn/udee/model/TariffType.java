package com.utn.udee.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum TariffType {
    RESIDENTIAL("Residential"),
    COMMERCIAL("Commercial"),
    SOCIAL("social");

    private String type;

    TariffType(String type) { this.type = type; }

    public static TariffType find(final String value)
    {
        for (TariffType tariff : values())
        {
            if (value.toString().equalsIgnoreCase(value))
            {
                return tariff;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid Type for Tariff: %s",value));
    }

    public String getType() {
        return type;
    }
}
