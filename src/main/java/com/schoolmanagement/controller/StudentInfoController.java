package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.StudentInfoRequestWithoutTeacherId;
import com.schoolmanagement.payload.request.UpdateStudentInfoRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentInfoResponse;
import com.schoolmanagement.service.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

    //Not: Neden ayrı ayrı getAll yapıyoruz da bir tane getAll methodu yazıp yetkilendirmeyi student teacher admin vb. yapmıyoruz?
    //not Cevap: Gelen requestten anlık olarak login olan kullanıcının yapabileceği methodlar olduğu için. Unique bir datayla kim olduğunu belirleyip
    //not Ona bağlı olan fieldlar ile işlem yapıyoruz. Best-Practice böyledir!!!

    private final StudentInfoService studentInfoService;


    // Not: save()****************************************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save")
    public ResponseMessage<StudentInfoResponse> save(HttpServletRequest httpServletRequest,
                                                     @RequestBody @Valid StudentInfoRequestWithoutTeacherId studentInfoRequestWithoutTeacherId) {

        //String username = (String) httpServletRequest.getAttribute("username");
        String username = httpServletRequest.getHeader("username");
        return studentInfoService.save(username, studentInfoRequestWithoutTeacherId);

    }

    // Not: delete()****************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @DeleteMapping("/delete/{studentInfoId}")
    public ResponseMessage<?> delete(@PathVariable Long studentInfoId) {
        return studentInfoService.deleteStudentInfo(studentInfoId);
    }

    // Not: update()****************************************************************

    //not putMapping de tüm fieldları setlememiz lazım. Eğer setlemezsek null atar veriler uçar gider.
    //not patchMapping de ise sadece istediğimiz fieldları değiştirebiliriz. Değiştirmediklerimiz aynı kalır.
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PutMapping("/update/{studentInfoId}")
    public ResponseMessage<StudentInfoResponse> update(@RequestBody @Valid UpdateStudentInfoRequest studentInfoRequest,
                                                       @PathVariable Long studentInfoId) {
        return studentInfoService.update(studentInfoRequest, studentInfoId);
    }


    // Not: getAllForAdmin()*********************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/getAllForAdmin")
    public ResponseEntity<Page<StudentInfoResponse>> getAll(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        // Pageable obje olusturma islemini Service katinda yazilmasi best-practice
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<StudentInfoResponse> studentInfoResponse = studentInfoService.getAllForAdmin(pageable);

        return new ResponseEntity<>(studentInfoResponse, HttpStatus.OK);
    }


    // Not: getAllForTeacher()*********************************************************

    // --> Bir ogretmen kendi ogrencilerinin bilgilerini almak istedigi zaman bu method calisacak
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllForTeacher")
    public ResponseEntity<Page<StudentInfoResponse>> getAllForTeacher(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        // Pageable obje olusturma islemini Service katinda yazilmasi best-practice
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String username = (String) httpServletRequest.getAttribute("username");

        Page<StudentInfoResponse> studentInfoResponse = studentInfoService.getAllTeacher(pageable, username);

        return new ResponseEntity<>(studentInfoResponse, HttpStatus.OK); // ResponseEntity.ok(studentInfoResponse);

    }

    // Not: getAllForStudent()*********************************************************
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllByStudent")
    public ResponseEntity<Page<StudentInfoResponse>> getAllByStudent(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size

    ) {
        // Pageable obje olusturma islemini Service katinda yazilmasi best-practice
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String username = (String) httpServletRequest.getAttribute("username");
        Page<StudentInfoResponse> studentInfoResponse = studentInfoService.getAllStudentInfoByStudent(username, pageable);
        return ResponseEntity.ok(studentInfoResponse);
    }

    // Not: getStudentInfoByStudentId()*************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    @GetMapping("/getByStudentId/{studentId}")
    public ResponseEntity<List<StudentInfoResponse>> getStudentId(@PathVariable Long studentId) {

        List<StudentInfoResponse> studentInfoResponse = studentInfoService.getStudentInfoByStudentId(studentId);
        return ResponseEntity.ok(studentInfoResponse);

    }


    // Not: getStudentInfoById()*******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<StudentInfoResponse> get(@PathVariable Long id) {

        StudentInfoResponse studentInfoResponse = studentInfoService.findStudentInfoById(id);
        return ResponseEntity.ok(studentInfoResponse);
    }


    // Not: getAllWithPage()******************************************************

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/search")
    public Page<StudentInfoResponse> search(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {
        return  studentInfoService.search(page,size,sort,type);
    }




}