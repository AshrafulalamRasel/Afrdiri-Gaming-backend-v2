package com.afridi.gamingbackend.dto.response;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;

    public JwtResponse(String accessToken,String id) {
        this.token = accessToken;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }
}
