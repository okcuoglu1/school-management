package com.schoolmanagement.entity.concretes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schoolmanagement.entity.abstracts.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ManyToOne(cascade = CascadeType.PERSIST) //Öğrenci kaydolduğunda advisor teacherı da kaydolsun.
    @JsonIgnore
    private AdvisorTeacher advisorTeacher;



    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<StudentInfo> studentInfos;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "student_lessonProgram",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_program_id")
    )
    private Set<LessonProgram> lessonsProgramList;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "meet_student_table",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "meet_id")
    )
    private List<Meet> meetList;

}