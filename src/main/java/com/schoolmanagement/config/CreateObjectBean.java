package com.schoolmanagement.config;

import com.schoolmanagement.payload.dto.DeanDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateObjectBean { //Bu class olusturdugumuz beanleri toplu bi sekilde görmemizi, revize etmemizi, takip etmeyi kolaylastırır.

    @Bean
    public DeanDto deanDto(){
        return new DeanDto();
    }


}
