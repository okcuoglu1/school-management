package com.schoolmanagement.entity.concretes;

import com.schoolmanagement.entity.abstracts.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder //Farklı senaryolar icin parametrelerle constructor üretir. İcinde bulundugum classın parentındaki fieldlarla da obje üretmek istiyorsam kullanılır.
//equals ve hascode methodunu aktif eder. İki tane objeyi kıyaslamak icin kullanılır.
//callSuper = true -> sadece icinde bulundugun classı kontrol etmez, Parenttaki fieldları da karsılastırır.
//onlyExplicitlyIncluded = true -> True ya cekildiği zaman belirttiğimiz fieldlara bakarak karsılastırabilirsin diyoruz. Ek annotationlarla.
@EqualsAndHashCode(callSuper = true , onlyExplicitlyIncluded = true)
@ToString(callSuper = true) //Bu classdan olusacak objedeki fieldları yaz parenttakileri de yaz.ToString in güclendirilmiş hali.
public class Student extends User {

    private String motherName;

    private String fatherName;

    private int studentNumber;

    private boolean isActive ;

    @Column(unique = true)
    private String email;

    // AdvisorTeacher, StudentInfo, LessonProgram, Meet

}