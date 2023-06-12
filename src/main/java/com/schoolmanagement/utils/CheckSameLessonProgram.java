package com.schoolmanagement.utils;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.exception.BadRequestException;

import java.util.HashSet;
import java.util.Set;

public class CheckSameLessonProgram {

    // not bu method parametre olarak bir mevcut programı bir de requestten gelen programı alıcak.
    //  Mevcut program ile yeni girilen ders programında aynı saatlerde ders programı var mı diye kontrol edicek.
    //  Hem de yeni eklenilecek ders programlarında saatlerde ve tarihlerde cakısma var mı diye bakıcak ?
    //  Saatlerde,günlerde Çakışma var mı diye kontrol edicek

        public static void  checkLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest){

            //lessonProgramRequest.size()>1 -> 1 den fazla olmasının sebebi en az 2 olsunki karşılaştıralım.

            if(existLessonProgram.isEmpty() && lessonProgramRequest.size()>1) {
                checkDuplicateLessonPrograms(lessonProgramRequest);
            } else {
                checkDuplicateLessonPrograms(lessonProgramRequest);
                checkDuplicateLessonPrograms(existLessonProgram,lessonProgramRequest);
            }

        }


        private static void checkDuplicateLessonPrograms(Set<LessonProgram> lessonPrograms) {

            //Date + start time concat yapıp bir keyword olusturuyoruz.Sonrasında bunu set bir yapıya atıp bunun icine gelen yeni keywordleri karsılastırıyoruz.

            Set<String> uniqueLessonProgramKeys = new HashSet<>();

            for (LessonProgram lessonProgram : lessonPrograms ) {
                String lessonProgramKey = lessonProgram.getDay().name() + lessonProgram.getStartTime();
                if(uniqueLessonProgramKeys.contains(lessonProgramKey)){
                    throw new BadRequestException(Messages.LESSON_PROGRAM_EXIST_MESSAGE);
                }
                //not baska bir logic tarzı olarak;  Set yapılarda add() methodu eğer data eklendiyse true eklenmediyse false döndürür. Bu sayede eğer eklenirse duplicate yok eğer eklenmezse duplicate var!.
                uniqueLessonProgramKeys.add(lessonProgramKey);
            }
        }

        //not aynı methodu şimdi de; mevcut ile yeni eklenmek istenen lessonprogramlar çakışıyor mu diye kontrol!!
        public static void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest ){

            for (LessonProgram requestLessonProgram : lessonProgramRequest) {

                if(existLessonProgram.stream().anyMatch(lessonProgram ->
                        lessonProgram.getStartTime().equals(requestLessonProgram.getStartTime()) &&
                                lessonProgram.getDay().name().equals(requestLessonProgram.getDay().name()))) {
                    throw  new BadRequestException(Messages.LESSON_PROGRAM_EXIST_MESSAGE);
                }

            }

        }

        // TODO : startTime baska bir lessonProgramin startTime ve endTime arasinda mi kontrolu eklenecek örneğin aynı günde bir dersin 11.00 da baslması diğerinin 11.30da baslaması gibi hatalar.
    }




