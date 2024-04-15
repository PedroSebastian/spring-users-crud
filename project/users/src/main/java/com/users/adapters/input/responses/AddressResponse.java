package com.users.adapters.input.responses;

import com.users.domain.model.Address;
import lombok.Value;

@Value
public class AddressResponse {
    String street;
    String number;
    String city;
    String state;
    String country;
    String zipCode;

    public static AddressResponse fromModel(Address address) {
        return new AddressResponse(
                address.getStreet(),
                address.getNumber(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getZipCode()
        );
    }
}
