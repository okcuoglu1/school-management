package com.schoolmanagement.payload.response;

import com.schoolmanagement.entity.enums.Term;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true) //true yazilmazsa default da disable
public class EducationTermResponse {

    private Long id; //Normalde responselarda id olmaz. Ama bize gelen requirements böyle.
    private Term term;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastRegistrationDate;






}
