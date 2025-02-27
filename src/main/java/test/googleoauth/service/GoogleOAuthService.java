package test.googleoauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import test.googleoauth.dto.OAuthResponseDTO;
import test.googleoauth.util.TokenUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {

    private final WebClient googleWebClient;
    private final WebClient googleApiWebClient;
    private final TokenUtil tokenUtil;

    @Value("${spring.security.oauth2.client.google.grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.google.client-secret}")
    private String clientSecret;

    /**
     * 인가 코드로 액세스 토큰을 요청합니다.
     * @param code
     */
    public OAuthResponseDTO.GoogleAccessTokenDTO requestGoogleAccessToken(String code){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);
        requestBody.add("client_secret", clientSecret);

        return googleWebClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Content-Type 헤더
                .bodyValue(requestBody) // 요청 본문에 추가
                .retrieve() // 서버 응답 가져오기
                .bodyToMono(OAuthResponseDTO.GoogleAccessTokenDTO.class) // 응답 본문 -> Mono
                .block();
    }

    /**
     * 액세스 토큰으로 사용자 이메일 얻어옵니다.
     * @param accessToken
     */
    public OAuthResponseDTO.GoogleEmailDTO requestGoogleEmail(String accessToken){
        return googleApiWebClient.get()
                .uri("/gmail/v1/users/me/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve() // 서버 응답 가져오기
                .bodyToMono(OAuthResponseDTO.GoogleEmailDTO.class) // 응답 본문 -> Mono
                .block();
    }

    /**
     * 1. OAuth 인가 코드를 사용하여 액세스 토큰을 요청합니다.
     * 2. 액세스 토큰을 이용해 사용자 이메일 정보를 가져옵니다.
     * @param code
     */
    public void performGoogleEmail(String code){
        OAuthResponseDTO.GoogleAccessTokenDTO googleAccessTokenDTO = requestGoogleAccessToken(code);
        String accessToken = googleAccessTokenDTO.getAccess_token();
        log.info("google accessToken : {}", accessToken);
        String email = requestGoogleEmail(accessToken).getEmailAddress();
        log.info("google email : {}", email);
    }

    /**
     * 1. OAuth 인가 코드를 사용하여 idToken 을 얻어옵니다.
     * 2. idToken 을 디코딩하여 사용자 이메일 정보를 가져옵니다.
     * @param code
     */
    public void getGoogleEmail(String code){
        OAuthResponseDTO.GoogleAccessTokenDTO googleAccessTokenDTO = requestGoogleAccessToken(code);
        String idToken = googleAccessTokenDTO.getId_token();
        log.info("google idToken : {}", idToken);
        String email =  tokenUtil.decodeIdToken(idToken);
        log.info("google email : {}" + email);
    }
}
