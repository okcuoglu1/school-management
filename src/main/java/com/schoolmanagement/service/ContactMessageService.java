package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.ContactMessage;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static com.schoolmanagement.utils.Messages.ALREADY_SEND_A_MESSAGE_TODAY;

@Service
@RequiredArgsConstructor //final olarak setlediğimiz fieldlardan constructor olusturuyor.Kod kalabalıklığını önler.
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    // Not: save() **********************************************
    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        // !!! Ayni kisi ayni gün icinde sadece 1 defa mesaj gönderebilsin.
        //aynı gün icerisinde Aynı maille mesaj atılmış mı kontrol edicez.
        boolean isSameMessageWithSameEmailForToday = contactMessageRepository.
        existsByEmailEqualsAndDateEquals(contactMessageRequest.getEmail(), LocalDate.now());

        if(isSameMessageWithSameEmailForToday) throw new ConflictException(String.format(ALREADY_SEND_A_MESSAGE_TODAY));

        ContactMessage contactMessage = createObject(contactMessageRequest);
        ContactMessage savedData = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder() //Dönecek olan data type ı belirttik
                .message("Contaxt Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(createResponse(savedData))
                .build();




    }

    //DTO-POJO dönüşümü
    //Builder design pattern ile istediğimiz dataları setleyebiliriz.
    //builder dp olmasaydı istediğimiz dataları setlerken onlarla ilgili farklı farklı constructorlar olusması gerekiyordu.
    private ContactMessage createObject(ContactMessageRequest contactMessageRequest){

        //Argümandan gelen yeni bilgilerle setlenmiş yeni bir contact message olusturur.
        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .subject(contactMessageRequest.getSubject())
                .message(contactMessageRequest.getMessage())
                .email(contactMessageRequest.getEmail())
                .date(LocalDate.now())
                .build();

    }

    //Pojo-dto dönüsümü icin yardımcı method.Save ettikten sonra geriye obje olarak dto döndürmemiz gerek.Güvenlik acısından.
    private ContactMessageResponse createResponse(ContactMessage contactMessage){

        return ContactMessageResponse.builder()
                .name(contactMessage.getName())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .email(contactMessage.getEmail())
                .date(contactMessage.getDate())
                .build();


    }



    // Not: getAll() **********************************************
    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if(Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page,size,Sort.by(sort).descending());
        }

        return contactMessageRepository.findAll(pageable).map(this::createResponse);
        // return contactMessageRepository.findAll(pageable).map(r->createResponse(r));
    }

    // Not: searchByEmail() *************************************
    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if(Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page,size,Sort.by(sort).descending());
        }

        return contactMessageRepository.findByEmailEquals(email, pageable).map(this::createResponse);


    }
    // Not: searchBySubject()************************************
    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());

        if(Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page,size,Sort.by(sort).descending());
        }

        return contactMessageRepository.findBySubjectEquals(subject, pageable).map(this::createResponse);



    }
}
