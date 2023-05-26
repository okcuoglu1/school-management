package com.schoolmanagement.service;

import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

        if(isSameMessageWithSameEmailForToday) throw new ConflictException("")


    }
}
