package com.budgets.modules.security.util;

import com.budgets.modules.security.dto.GenerationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

public class Extration {

    public Long extrationId(Claims claims) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String extrationClaims = objectMapper.writeValueAsString(claims);
        GenerationToken generationToken = objectMapper.readValue(extrationClaims, GenerationToken.class);
        return generationToken.getId();
    }

    public String extrationRole(Claims claims) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String extrationClaims = objectMapper.writeValueAsString(claims);
        GenerationToken generationToken = objectMapper.readValue(extrationClaims, GenerationToken.class);
        return generationToken.getType();
    }
}

