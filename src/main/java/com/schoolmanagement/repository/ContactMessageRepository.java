package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    //JpaRepository ile bu kodun icini doldurmaya gerek yok calısır.
    boolean existsByEmailEqualsAndDateEquals(String email, LocalDate now);
}
