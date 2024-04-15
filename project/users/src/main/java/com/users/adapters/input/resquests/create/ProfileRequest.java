package com.users.adapters.input.resquests.create;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequest {
    @Size(max = 20, message = "Language preference must not exceed 20 characters")
    private String languagePreference;

    @Size(max = 20, message = "Theme preference must not exceed 20 characters")
    private String themePreference;

    private boolean receiveNewsletter;

    @Size(max = 10, message = "Time zone must not exceed 10 characters")
    private String timeZone;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}
