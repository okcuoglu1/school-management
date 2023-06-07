package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.EducationTerm;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.request.EducationTermRequest;
import com.schoolmanagement.payload.response.EducationTermResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.EducationTermRepository;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;

    // Not :  Save() *************************************************************************
    public ResponseMessage<EducationTermResponse> save(EducationTermRequest request) {

        checkDate(request);

        // !!! save metoduna dto- pojo donusumu yapip gonderiyoruz
        EducationTerm savedEducationTerm = educationTermRepository.save(createEducationTerm(request));

        // !!! response objesi olusturuluyor
        return ResponseMessage.<EducationTermResponse>builder()
                .message("Education Term created")
                .object(createEducationTermResponse(savedEducationTerm))
                .httpStatus(HttpStatus.CREATED)
                .build();



    }

    private EducationTerm createEducationTerm(EducationTermRequest request) {

        return EducationTerm.builder()
                .term(request.getTerm())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .lastRegistrationDate(request.getLastRegistrationDate())
                .build();
    }

    private EducationTermResponse createEducationTermResponse(EducationTerm request) {

        return EducationTermResponse.builder()
                .id(request.getId())
                .term(request.getTerm())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .lastRegistrationDate(request.getLastRegistrationDate())
                .build();

    }

    // Not :  getById() ************************************************************************
    public EducationTermResponse get(Long id) {

        // !!! ya yoksa kontrolu
        if(!educationTermRepository.existsByIdEquals(id)) { // existsById de calisir
            throw new ResourceNotFoundException(String.format(Messages.EDUCATION_TERM_NOT_FOUND_MESSAGE, id));
        }

        // !!! POJO - DTO donusumu ile response hazirlaniyor
        return createEducationTermResponse(educationTermRepository.findByIdEquals(id)); // findById de calisir
    }

    // Not :  getAll() *************************************************************************
    public List<EducationTermResponse> getAll() {

        return educationTermRepository.findAll()
                .stream()
                .map(this::createEducationTermResponse)
                .collect(Collectors.toList());
    }

    // Not :  getAllWithPage() ******************************************************************
    public Page<EducationTermResponse> getAllWithPage(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());
        if(Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }

        return educationTermRepository.findAll(pageable).map(this::createEducationTermResponse);

    }


    // Not :  Delete() *************************************************************************
    public ResponseMessage<?> deleteTerm(Long id) {

      if(!educationTermRepository.existsById(id)){
          throw new ResourceNotFoundException(String.format(Messages.EDUCATION_TERM_NOT_FOUND_MESSAGE,id));      }

      educationTermRepository.deleteById(id);

      return ResponseMessage.builder().message("Education Term is deleted").httpStatus(HttpStatus.OK).build();

    }




    // Not :  UpdateById() ********************************************************************
    public ResponseMessage<EducationTermResponse> updateTerm(EducationTermRequest educationTermRequest, Long id) {

      EducationTerm term =  educationTermRepository.findById(id).orElseThrow(() ->
              new ResourceNotFoundException(String.format(Messages.EDUCATION_TERM_NOT_FOUND_MESSAGE,id)));

        checkDate(educationTermRequest);

        EducationTerm updatedTerm = createUpdatedEduTerm(educationTermRequest,id);

        educationTermRepository.save(updatedTerm);

        //Not: bu işlem fiedları setlemeden önce bos bir obje olusturur.
//        ResponseMessage.ResponseMessageBuilder<EducationTermResponse> responseMessageBuilder =
//                ResponseMessage.builder();

//        return responseMessageBuilder
//                .object(createEducationTermResponse(updated))
//                .message("Education Term Updated Successfully")
//                .build();



        return ResponseMessage.<EducationTermResponse>builder().
                message("Education Term is updated successfully").
                object(createEducationTermResponse(updatedTerm)).
                httpStatus(HttpStatus.OK).
                build();


    }


    //NOT: TARİHLERİN KONTROLÜ İCİN YARDIMCI METHOD
    private void checkDate(EducationTermRequest request){

        //!!! son kayiot tarihi , ders doneminin baslangic tarihinde nsonra olmamali :

        if(request.getStartDate()!=null && request.getLastRegistrationDate()!=null) {
            if(request.getLastRegistrationDate().isAfter(request.getStartDate())) {
                throw new ResourceNotFoundException(Messages.EDUCATION_START_DATE_IS_EARLIER_THAN_LAST_REGISTRATION_DATE);
            }
        }

        // !!! startDate-endDate kontrolu
        if(request.getStartDate()!= null && request.getEndDate()!=null){
            if(request.getEndDate().isBefore(request.getStartDate())){
                throw new ResourceNotFoundException(Messages.EDUCATION_END_DATE_IS_EARLIER_THAN_START_DATE);
            }
        }

        //not sadece start date veya lastregistrationdate degisse buraya takılır ve kodu patlatır.
//        // !!! ayni term ve baslangic tarihine sahip birden fazla kayit var mi kontrolu
//        if(educationTermRepository.existsByTermAndYear(request.getTerm(), request.getStartDate().getYear())) {
//            throw  new ResourceNotFoundException(Messages.EDUCATION_TERM_IS_ALREADY_EXIST_BY_TERM_AND_YEAR_MESSAGE);
//        }

    }

    //NOT UPDATE EDİLEN EDUCATION TERMI POJO DONUSTUREN METHOD
    private EducationTerm createUpdatedEduTerm(EducationTermRequest request, Long id){

      return  EducationTerm.builder().
                id(id).
                term(request.getTerm()).
                startDate(request.getStartDate()).
                endDate(request.getEndDate()).
                lastRegistrationDate(request.getLastRegistrationDate()).
                build();

    }

}