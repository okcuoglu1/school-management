package com.schoolmanagement.payload.response.abstracts;

import com.schoolmanagement.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@SuperBuilder
//@MappedSuperclass dbye gidilmediği için bu annoya gerek yok.
public class BaseUserResponse {

    private Long userId;
    private String username;
    private String name;
    private String surname;
    private LocalDate birthDay;
    private String ssn;
    private String birthPlace;
    private String phoneNumber;
    private Gender gender;

}
