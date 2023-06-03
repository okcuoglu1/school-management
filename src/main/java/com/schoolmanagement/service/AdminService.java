package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Admin;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.request.AdminRequest;
import com.schoolmanagement.payload.response.AdminResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.*;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ViceDeanRepository viceDeanRepository;
    private final DeanRepository deanRepository;
    private final TeacherRepository teacherRepository;
    private final GuestUserRepository guestUserRepository;

   private final UserRoleService userRoleService;
   private final PasswordEncoder passwordEncoder;


    // Not: save() ***********************************
    public ResponseMessage save(AdminRequest request) {

        // Girilen username - ssn - phoneNumber unique mi kontrolü-
        checkDuplicate(request.getUsername(), request.getSsn(), request.getPhoneNumber());
        //Admin nesnesini builder ile olusturalım
      Admin admin = createAdminForSave(request);
      admin.setBuilt_in(false);

      if(Objects.equals(request.getUsername(), "Admin")) admin.setBuilt_in(true);

      // !!! Admin rolü veriliyor
        admin.setUserRole(userRoleService.getUserRole(RoleType.ADMIN)); //dbde role yoksa exception atıcak o yüzden ek katmanda onu kontrol edicez.

      // password suanda plain text -> encode etmeliyiz.
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

      Admin savedData =  adminRepository.save(admin);

      return ResponseMessage.<AdminResponse>builder()
              .message("Admin saved").
              httpStatus(HttpStatus.CREATED).
              object(createResponse(savedData)). //pojo-dto
              build();

    }

    public void checkDuplicate(String username,String ssn, String phone){

        if(adminRepository.existsByUsername(username) ||
                deanRepository.existsByUsername(username) ||
                studentRepository.existsByUsername(username) ||
                teacherRepository.existsByUsername(username) ||
                viceDeanRepository.existsByUsername(username) ||
                guestUserRepository.existsByUsername(username)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        } else if (adminRepository.existsBySsn(ssn) ||
                deanRepository.existsBySsn(ssn) ||
                studentRepository.existsBySsn(ssn) ||
                teacherRepository.existsBySsn(ssn) ||
                viceDeanRepository.existsBySsn(ssn) ||
                guestUserRepository.existsBySsn(ssn)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        } else if (adminRepository.existsByPhoneNumber(phone) ||
                deanRepository.existsByPhoneNumber(phone) ||
                studentRepository.existsByPhoneNumber(phone) ||
                teacherRepository.existsByPhoneNumber(phone) ||
                viceDeanRepository.existsByPhoneNumber(phone) ||
                guestUserRepository.existsByPhoneNumber(phone)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
        }


    }


   // ODEV -- yukardaki duplicate methodunu 4 parametreli hale getirmek istersem ???
     /*    public void checkDuplicate2(String... values) {
        String username = values[0];
        String ssn = values[1];
        String phone = values[2];
        String email = values[3];

        if (adminRepository.existsByUsername(username) || deanRepository.existsByUsername(username) ||
                studentRepository.existsByUsername(username) || teacherRepository.existsByUsername(username) ||
                viceDeanRepository.existsByUsername(username) || guestUserRepository.existsByUsername(username)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        } else if (adminRepository.existsBySsn(ssn) || deanRepository.existsBySsn(ssn) ||
                studentRepository.existsBySsn(ssn) || teacherRepository.existsBySsn(ssn) ||
                viceDeanRepository.existsBySsn(ssn) || guestUserRepository.existsBySsn(ssn)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        } else if (adminRepository.existsByPhoneNumber(phone) || deanRepository.existsByPhoneNumber(phone) ||
                studentRepository.existsByPhoneNumber(phone) || teacherRepository.existsByPhoneNumber(phone) ||
                viceDeanRepository.existsByPhoneNumber(phone) || guestUserRepository.existsByPhoneNumber(phone)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
        } else if (studentRepository.existsByEmail(email) || teacherRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }
    }*/ // checkDuplicate VarArgs cozumu ( Odev olarak Ver )

    protected Admin createAdminForSave(AdminRequest request){

        return Admin.builder().
                username(request.getUsername()).
                name(request.getName())
                .surname(request.getSurname())
                .password(request.getPassword())
                .ssn(request.getSsn())
                .birthDay(request.getBirthDay())
                .birthPlace(request.getBirthPlace())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .build();

    }

    private AdminResponse createResponse(Admin admin){

        return AdminResponse.builder()
                .userId(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .surname(admin.getSurname())
                .phoneNumber(admin.getPhoneNumber())
                .gender(admin.getGender())
                .ssn(admin.getSsn())
                .build();

    }


    public Page<Admin> getAllAdmin(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    public String deleteAdmin(Long id) {

      // Admin admin =  adminRepository.findById(id).orElseThrow(() ->  new ConflictException("Admin not found"));

      Optional<Admin> admin = adminRepository.findById(id);

      //Önce optional olarak gelen adminin null olmadığını kontrol etmem lazım.
      //Built in olan admini silmemem lazım. admin.get().isBuilt_in true gelirse bu admini silme yetkin yok dedik.
      if(admin.isPresent() && admin.get().isBuilt_in()){
          throw new ConflictException(Messages.NOT_PERMITTED_METHOD_MESSAGE);
      }

      if(admin.isPresent()){

          adminRepository.deleteById(id);

          return "Admin is deleted successfully";

      }

        return Messages.NOT_FOUND_USER_MESSAGE;



    }

    //Runner tarafı icin yazildi.
    public long countAllAdmin() {

        return adminRepository.count();
    }
}
