package com.schoolmanagement.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL) //null olmayan veriler json da gözüksün istersek. Null olanlar json da gözükmüyücek.
public class AuthResponse {

    private String username;
    private String ssn;
    private String role;
    private String token;
    private String name;
    private String isAdvisor;
}