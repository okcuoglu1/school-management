package com.schoolmanagement.entity.concretes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AdvisorTeacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //DBDEN OKUMA YAPILDIĞINDA ROLE BİLGİSİ GİTMESİN.
     private UserRole userRole;


    //TODO !!! Teacher - Student - Meet


}
