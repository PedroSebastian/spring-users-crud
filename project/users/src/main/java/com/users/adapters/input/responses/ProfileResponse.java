package com.users.adapters.input.responses;

import com.users.domain.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private final String languagePreference;
    private final String themePreference;
    private final boolean receiveNewsletter;
    private final String timeZone;
    private final String bio;

    public static ProfileResponse fromModel(Profile profile) {
        return new ProfileResponse(
                profile.getLanguagePreference(),
                profile.getThemePreference(),
                profile.isReceiveNewsletter(),
                profile.getTimeZone(),
                profile.getBio()
        );
    }
}
