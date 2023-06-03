package com.schoolmanagement.repository;

import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    // "?1" - Alttaki parametre icerisindeki değeri al querye getir. 2-3 parametre olsaydı yine ?2 ?3
    @Query("select r from UserRole r where r.roleType = ?1")
    Optional<UserRole> findByERoleEqueals(RoleType roleType);

    @Query("select (count(r)>0 from UserRole r where r.roleType = ?1")
    boolean existsByERoleEqueals(RoleType roleType);
}
