package test.googleoauth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {

    private final ObjectMapper objectMapper;

    public String decodeIdToken(String idToken) {
        // idToken payload 부분 디코딩
        String[] parts = idToken.split("\\.");
        String payload = parts[1];
        String decodedPayload = new String(Base64.getUrlDecoder().decode(payload));

        // string -> json
        // payload 에서 email 값 가져옴
        try {
            JsonNode jsonNode = objectMapper.readTree(decodedPayload);
            return jsonNode.get("email").asText();
        } catch (JsonProcessingException e) {
            log.error("Failed to decode idToken", e);
            return null;
        }
    }
}
