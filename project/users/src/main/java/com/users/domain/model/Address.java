package com.users.domain.model;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public Address(String street, String number, String city, String state, String country, String zipCode) {
        if ((street == null || street.isBlank()) ||
                (number == null || number.isBlank()) ||
                (city == null || city.isBlank()) ||
                (state == null || state.isBlank()) ||
                (country == null || country.isBlank()) ||
                (zipCode == null || zipCode.isBlank())) {
            throw new IllegalArgumentException("Street, number, city, state, country, and zip code are required.");
        }
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }
}
