package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.ContactMessage;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    //JpaRepository ile bu kodun icini doldurmaya gerek yok calısır.
    boolean existsByEmailEqualsAndDateEquals(String email, LocalDate now);

    Page<ContactMessage> findByEmailEquals(String email, Pageable pageable);

    Page<ContactMessage> findBySubjectEquals(String subject, Pageable pageable);
}
