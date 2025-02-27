package test.googleoauth.dto;

import lombok.Getter;

public class OAuthResponseDTO {
    @Getter
    public static class GoogleAccessTokenDTO{
        String access_token;
        Integer expires_in;
        String id_token;
        String refresh_token;
        String scope;
        String token_type;
    }

    @Getter
    public static class GoogleEmailDTO{
        String emailAddress;
    }
}
