package com.users.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String languagePreference;
    private String themePreference;
    private boolean receiveNewsletter;
    private String timeZone;
    private String bio;
}
