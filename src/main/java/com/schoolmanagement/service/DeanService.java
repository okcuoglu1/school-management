package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.DeanRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private Dean createDtoForDean(DeanRequest deanRequest) { //createDtoToPOJO

        return deanDto.dtoDean(deanRequest);

    }

    //Pojo-DTO donusumu
    private DeanResponse createDeanResponse(Dean dean) {
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


    //Not: UpdateById()********************************
    public ResponseMessage<DeanResponse> update(DeanRequest newDean, Long deanId) {

        //Eğer dbde verilen idli kullanıcı yoksa null bir obje döndürücek. Var ise dean in kendisi gelicek.
        Optional<Dean> dean = deanRepository.findById(deanId);

        //Null olma ihtimaline karşı kontrol ediyoruz. orElseThrow ile yapılabilir.Ama farklı yapıları görmek icin böyle yapıyoruz.

        if (!dean.isPresent()) { //isEmpty() de kullanılabilir.

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, deanId));

        } else if (!CheckParameterUpdateMethod.checkParameter(dean.get(), newDean)) {

            adminService.checkDuplicate(newDean.getUsername(), newDean.getSsn(), newDean.getPhoneNumber());

        }

        //!!! Guncellenen yeni bilgilerle ile Dean objesini kaydediyoruz.DTO-POJO
        Dean updatedDean = createUpdatedDean(newDean, deanId);
        //!!! password encode -> DB ye kaydedilmeye hazır hale getiriyoruz.
        updatedDean.setPassword(passwordEncoder.encode(newDean.getPassword()));

        deanRepository.save(updatedDean);

        return ResponseMessage.<DeanResponse>builder().
                message("Dean updated successfully").
                httpStatus(HttpStatus.OK).
                object(createDeanResponse(updatedDean)).
                build();


    }

    //!!! yardimci metod
    //DTO-POJO
    private Dean createUpdatedDean(DeanRequest deanRequest, Long managerId) {

        return Dean.builder()
                .id(managerId)
                .username(deanRequest.getUsername())
                .ssn(deanRequest.getSsn())
                .name(deanRequest.getName())
                .surname(deanRequest.getSurname())
                .birthPlace(deanRequest.getBirthPlace())
                .birthDay(deanRequest.getBirthDay())
                .phoneNumber(deanRequest.getPhoneNumber())
                .gender(deanRequest.getGender())
                .userRole(userRoleService.getUserRole(RoleType.MANAGER))
                .build();
    }

    //Not: Delete()***********************************
    public ResponseMessage<?> deleteDean(Long deanId) {

        Optional<Dean> dean = findByeId(deanId);

        deanRepository.deleteById(deanId);

        //Objeyi sildiğimiz için Objecti setlemiyoruz.
        return ResponseMessage.builder().
                message("Dean deleted").
                httpStatus(HttpStatus.OK).
                build();

    }


    // Not :  getById() *******************************************************
    public ResponseMessage<DeanResponse> getDeanById(Long userId) {

        Optional<Dean> dean = findByeId(userId);


        return ResponseMessage.<DeanResponse>builder()
                .message("Dean Successfully found")
                .httpStatus(HttpStatus.OK)
                .object(createDeanResponse(dean.get()))
                .build();


    }

    // Not :  getAll() *************************************************************************
    public List<DeanResponse> getAllDean() {

        return deanRepository.findAll() //Deanler geliyor bunu deanResponse a çevirmem gerek.
                .stream() //dean akışı
                .map(this::createDeanResponse) //akışı değiştiriyoruz deanleri deanresponse a ceviriyoruz.
                .collect(Collectors.toList());
    }

    // Not :  Search() *************************************************************************
    public Page<DeanResponse> search(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return deanRepository.findAll(pageable).map(this::createDeanResponse); //Pojoları DTO ya cevirmemiz gerek.

    }


    //yardımcı method
    private Optional<Dean> findByeId(Long userId) {

        Optional<Dean> dean = deanRepository.findById(userId);

        if (!dean.isPresent()) { //isEmpty() de kullanılabilir.

            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, userId));

        }

        return dean;

    }
}
