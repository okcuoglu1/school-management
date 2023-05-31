package com.schoolmanagement.entity.concretes;

import com.schoolmanagement.entity.enums.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absentee;

    private Double midtermExam;
    private Double finalExam;
    private Double examAverage;

    private String infoNote;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne //Bir öğrencinin birden fazla studentInfo classı olsun.
    private Student student;

    @Enumerated(EnumType.STRING)
    private Note letterGrade;

    @ManyToOne
    private Lesson lesson;

    @OneToOne
    private EducationTerm educationTerm;




}
