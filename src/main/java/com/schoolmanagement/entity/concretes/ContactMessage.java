package com.schoolmanagement.entity.concretes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true) //Yeni bir nesne oluşturmak yerine varolan nesnenin kopyasını alarak ilgili dataları setlememizi sağlar.
public class ContactMessage implements Serializable {
    //Siteden bilgi almak isteyen bir kullanıcı olabilir. Bu kullanıcıyla iletişime geçmek için önce bilgilerini alıyoruz.


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name ; //isim bilgisi
    @NotNull
    private String email ; //email bilgisi
    @NotNull
    private String subject ; //Gönderilecek olan mesajın başlığı
    @NotNull
    private String message ; //mesajın kendisi

    //Json benim belirttiğim formatta gönderilsin.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date; //mesajın bana gönderildiği tarih

}
