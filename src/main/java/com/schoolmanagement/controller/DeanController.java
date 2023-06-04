package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("dean")
public class DeanController {

    private final DeanService deanService;

    //Not: Save()**********************************
    @PostMapping("/save") //http://localhost:8080/dean/save
    @PreAuthorize("hasAuthority('ADMIN')") //sadece bu role sahip olanlar
    public ResponseMessage<DeanResponse> save(@RequestBody @Valid DeanRequest deanRequest){

        return deanService.save(deanRequest);

    }

    //Not: UpdateById()********************************
    //Sadece benim verdiğim datalar değişsin diğerleri aynı kalsın istiyorsak Patch Mapping. Ama put mapping de tüm dataları setlememiz gerek.
    //Yoksa setlenmeyen datalar null gözükür.
    @PutMapping("/update/{userId}") //http://localhost:8080/dean/update/1
    @PreAuthorize("hasAuthority('ADMIN')") //TODO: DEAN EKLENMELİ
    public ResponseMessage<DeanResponse> update(@RequestBody @Valid DeanRequest deanRequest, @PathVariable Long userId){

        return deanService.update(deanRequest, userId);

    }


    //Not: Delete()***********************************
    @DeleteMapping("/delete/{userId}") //http://localhost:8080/dean/delete/1
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<?> delete(@PathVariable Long userId){ //ResponseMessage<?> -> DeanResponse da yazılabilir "?" da yazılabilir. Service katından göndereceğim data türü dönecek demek..

        return deanService.deleteDean(userId);

    }

    // Not :  getById() ************************************************************************
    @GetMapping("/getManagerById/{userId}") // http://localhost:8080/dean/getManagerById/1
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<DeanResponse> getDeanById(@PathVariable Long userId){

        return deanService.getDeanById(userId);

    }

    // Not :  getAll() *************************************************************************

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getAll")  // http://localhost:8080/dean/getAll
    public List<DeanResponse> getAll() {
        return deanService.getAllDean();
    }

    // Not :  Search() *************************************************************************
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search") // hht://localhost:8080/dean/search
    public Page<DeanResponse> search( //TODO: getAllWithPage
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {

        return deanService.search(page,size,sort,type);
    }










}