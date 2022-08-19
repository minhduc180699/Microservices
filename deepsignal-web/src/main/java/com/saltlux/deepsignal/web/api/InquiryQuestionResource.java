package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.exception.*;
import com.saltlux.deepsignal.web.service.IFileStorageService;
import com.saltlux.deepsignal.web.service.IInquiryQuestionService;
import com.saltlux.deepsignal.web.service.dto.InquiryAnswerDTO;
import com.saltlux.deepsignal.web.service.dto.InquiryQuestionDTO;
import com.saltlux.deepsignal.web.service.dto.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry-question")
@Tag(name = "Question Inquiry Management", description = "Question Inquiry management API")
public class InquiryQuestionResource {

    @Autowired
    private IInquiryQuestionService iInquiryQuestionService;

    private final String FOLDER_NAME = "question";

    @Autowired
    private IFileStorageService iFileStorageService;

    private ApplicationProperties applicationProperties;

    public InquiryQuestionResource(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     *
     * @param inquiryQuetionDTO: infomation Question Inquiry
     * @return
     */
    @PostMapping(value = "/save", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Create Question Inquiry", tags = { "Question Inquiry Management" })
    public ResponseEntity<?> save(@Valid @RequestBody InquiryQuestionDTO inquiryQuetionDTO) {
        try {
            if (inquiryQuetionDTO.getId() != null) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Constants.ErrorCode.DEEPSINAL_INQUIRY_QUESTION_ID_NOT_NULL.description);
            }
            return ResponseEntity.status(HttpStatus.OK).body(iInquiryQuestionService.save(inquiryQuetionDTO));
        } catch (UploadFileMaxSizeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_MAX_SIZE.description);
        } catch (UploadFileFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_INCORRECT_FORMAT.description);
        } catch (UploadFileExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.ErrorCode.DEEPSINAL_FILE_EXISTS.description);
        } catch (UploadException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Constants.ErrorCode.DEEPSINAL_FILE_UPLOAD_ERROR.description);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     *
     * @param id: Id Question Inquiry
     * @return
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "Get Question Inquiry by Id", tags = { "Question Inquiry Management" })
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            InquiryQuestionDTO inquiryQuestionDTO = iInquiryQuestionService.findByInquiryQuestionId(id);

            inquiryQuestionDTO.setFile(
                !StringHelper.isNullOrEmptyString(inquiryQuestionDTO.getFile())
                    ? applicationProperties.getFilesUpload().getUrlFile() + inquiryQuestionDTO.getFile()
                    : inquiryQuestionDTO.getFile()
            );

            InquiryAnswerDTO inquiryAnswerDTO = inquiryQuestionDTO.getInquiryAnswer();

            if (inquiryAnswerDTO != null) {
                inquiryAnswerDTO.setFile(
                    !StringHelper.isNullOrEmptyString(inquiryAnswerDTO.getFile())
                        ? applicationProperties.getFilesUpload().getUrlFile() + inquiryAnswerDTO.getFile()
                        : inquiryAnswerDTO.getFile()
                );
            }

            return ResponseEntity.status(HttpStatus.OK).body(inquiryQuestionDTO);
        } catch (ResourceNotFoundException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     *
     * @param userId: User Id
     * @param page
     * @param size
     * @param orderBy
     * @param sortDirection
     * @return
     */
    @GetMapping("/getByUser")
    @Operation(summary = "Get Question Inquiry By User_Id", tags = { "Question Inquiry Management" })
    public ResponseEntity<?> getByUser(
        @RequestParam("userId") String userId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "createdDate") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            PagedResponse<InquiryQuestionDTO> inquiryQuetionDTOPagedResponse = iInquiryQuestionService.findByUser(
                page,
                size,
                orderBy,
                sortDirection,
                userId
            );

            if (inquiryQuetionDTOPagedResponse.getContent() != null) {
                for (InquiryQuestionDTO inquiryQuestionDTO : inquiryQuetionDTOPagedResponse.getContent()) {
                    inquiryQuestionDTO.setFile(
                        !StringHelper.isNullOrEmptyString(inquiryQuestionDTO.getFile())
                            ? applicationProperties.getFilesUpload().getUrlFile() + inquiryQuestionDTO.getFile()
                            : inquiryQuestionDTO.getFile()
                    );
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(inquiryQuetionDTOPagedResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
