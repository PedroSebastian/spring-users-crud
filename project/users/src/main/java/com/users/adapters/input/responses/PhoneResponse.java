package com.users.adapters.input.responses;

import com.users.domain.model.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneResponse {
    private final String number;
    private final String areaCode;
    private final String countryCode;

    public static PhoneResponse fromModel(Phone phone) {
        return new PhoneResponse(
                phone.getNumber(),
                phone.getAreaCode(),
                phone.getCountryCode()
        );
    }
}
