package com.users.adapters.input.resquests.edit;

import com.users.adapters.input.validations.ValidAreaCode;
import com.users.adapters.input.validations.ValidCountryCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    private String number;

    @NotBlank(message = "Area code is required")
    @ValidAreaCode
    private String areaCode;

    @NotBlank(message = "Country code is required")
    @ValidCountryCode
    private String countryCode;
}
