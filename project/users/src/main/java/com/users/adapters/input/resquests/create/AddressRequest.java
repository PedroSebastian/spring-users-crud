package com.users.adapters.input.resquests.create;

import com.users.adapters.input.validations.ValidName;
import com.users.adapters.input.validations.ValidZipCode;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class AddressRequest {
    @NotBlank(message = "Street is required")
    @ValidName
    private String street;

    @NotBlank(message = "Number is required")
    @ValidName
    private String number;

    @NotBlank(message = "City is required")
    @ValidName
    private String city;

    @NotBlank(message = "State is required")
    @ValidName
    private String state;

    @NotBlank(message = "Country is required")
    @ValidName
    private String country;

    @NotBlank(message = "Zip code is required")
    @ValidZipCode
    private String zipCode;
}
