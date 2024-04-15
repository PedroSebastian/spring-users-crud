package com.users.domain.model;

import lombok.Data;

@Data
public class Phone {
    private final String number;
    private final String areaCode;
    private final String countryCode;

    public Phone(String number, String areaCode, String countryCode) {
        this.number = number;
        this.areaCode = areaCode;
        this.countryCode = countryCode;
    }
}
