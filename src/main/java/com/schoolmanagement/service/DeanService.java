package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.DeanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeanService {

    private final DeanRepository deanRepository;
    private final AdminService adminService;
    private final DeanDto deanDto;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;


    public ResponseMessage<DeanResponse> save(DeanRequest deanRequest) {

    //!!! Duplicate kontrolü -> Unique olması gereken datalar var. Müdür'ün ssn num ve built_in olarak adminin ssni ile aynı olabilir bunu engellemek gerek.
    adminService.checkDuplicate(deanRequest.getUsername(), deanRequest.getSsn(), deanRequest.getPhoneNumber());

    //!!! DT0- POJO DONUSUMU - Save islemi olacagı icin pojoya ceviriyoruz.
   Dean dean = createDtoForDean(deanRequest);
   //Role bilgisi setleniyor.
   dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
   //dean requestten gelen password plain text onu encode etmemiz gerekiyor.
    dean.setPassword(passwordEncoder.encode(dean.getPassword()));

    //DBye kayıt -> Frontende biz yapılan islem sonrası objeyi de yolluyoruz. Bunu yapmak zorunda değiliz. Ama bu projede senaryo böyle
    //Bu pojo classı direk frontende yollayamam içinde gitmesini istemediğimiz datalar var. O yüzden  POJO-DTO dönüsümü yapıcaz.
    Dean savedDean = deanRepository.save(dean);

    return ResponseMessage.<DeanResponse>builder().
            message("Dean saved").
            httpStatus(HttpStatus.CREATED).
            object(createDeanResponse(savedDean)).
            build();




    }
    //DTO-POJO donusumu
    private Dean createDtoForDean(DeanRequest deanRequest){ //createDtoToPOJO

        return deanDto.dtoDean(deanRequest);

    }

    //Pojo-DTO donusumu
    private DeanResponse createDeanResponse(Dean dean){
        return DeanResponse.builder()
                .userId(dean.getId())
                .username(dean.getUsername())
                .name(dean.getName())
                .surname(dean.getSurname())
                .birthDay(dean.getBirthDay())
                .birthPlace(dean.getBirthPlace())
                .phoneNumber(dean.getPhoneNumber())
                .gender(dean.getGender())
                .ssn(dean.getSsn())
                .build();
    }


}
