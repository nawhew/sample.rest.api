package com.example.sample.rest.api.auth;

import com.example.sample.rest.api.domain.Role;
import com.example.sample.rest.api.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private static final String REGID_GOOGLE = "google";
    private static final String REGID_NAVER = "naver";

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        return OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
    }

    /**
     * OAuth2UserRequest 정보를 O-Auth 속성으로 변환
     * @param registrationId 서비스 구분 코드 : google, naver 등을 구분 할 때 사용
     * @param userNameAttributeName OAuth2 로그인 진행시 키가 되는 필드 값 (PK 같은 의미)
     * @param attributes 속성값들(Map)
     * @return OAuth 속성들
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(REGID_NAVER.equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    /**
     * OAuth 속성을 구글 인증에 맞추어 값을 세팅
     * @param userNameAttributeName 
     * @param attributes
     * @return
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * OAuth 속성을 네이버 인증에 맞추어 값을 세팅.
     * 네아로(네이버 아이디 로그인)의 경우 response 아래에 정보가 있어 response에서 값을 꺼내와서 세팅
     * @param userNameAttributeName
     * @param attributes
     * @return
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        response.put("response", response.get("name"));
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * User 엔티티를 생성합니다.
     * 가입시 기본 권한으로 Role.GUEST 권한을 줍니다.
     * @return
     */
    public User toEntity() {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .picture(this.picture)
                .role(Role.GUEST)
                .build();
    }
}
