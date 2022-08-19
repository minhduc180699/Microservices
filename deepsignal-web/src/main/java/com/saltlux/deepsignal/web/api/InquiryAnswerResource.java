package com.saltlux.deepsignal.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.InquiryAnswer;
import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;
import com.saltlux.deepsignal.web.exception.*;
import com.saltlux.deepsignal.web.service.IFileStorageService;
import com.saltlux.deepsignal.web.service.IInquiryAnswerEmailService;
import com.saltlux.deepsignal.web.service.IInquiryAnswerService;
import com.saltlux.deepsignal.web.service.MailService;
import com.saltlux.deepsignal.web.service.dto.InquiryAnswerDTO;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/inquiry-answer")
@Tag(name = "Answer Inquiry Management", description = "Answer Inquiry management API")
public class InquiryAnswerResource {

    @Autowired
    private IInquiryAnswerService iInquiryAnswerService;

    @Autowired
    private IInquiryAnswerEmailService iInquiryAnswerEmailService;

    private final String FOLDER_NAME = "answer";

    @Autowired
    private MailService mailService;

    @Autowired
    private IFileStorageService iFileStorageService;

    /**
     *
     * @param file
     * @param inquiryAnswer: infomation inquiry answer
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "Create Answer Inquiry", tags = { "Answer Inquiry Management" })
    public ResponseEntity<?> save(@RequestPart("file") MultipartFile file, @RequestPart("inquiryAnswer") String inquiryAnswer) {
        try {
            InquiryAnswerDTO inquiryAnswerDTO = new ObjectMapper().readValue(inquiryAnswer, InquiryAnswerDTO.class);
            if (inquiryAnswerDTO.getId() != null) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Constants.ErrorCode.DEEPSINAL_INQUIRY_ANSWER_ID_NOT_NULL.description);
            }
            String pathName = iFileStorageService.saveInquiry(file, inquiryAnswerDTO.getUser().getLogin(), FOLDER_NAME);
            inquiryAnswerDTO.setFile(pathName);
            InquiryAnswer outputAnswer = iInquiryAnswerService.save(inquiryAnswerDTO);
            InquiryAnswerDTO output = ObjectMapperUtils.map(outputAnswer, InquiryAnswerDTO.class);

            InquiryAnswerEmail inquiryAnswerEmail = iInquiryAnswerEmailService.findByInquiryQuestionId(
                inquiryAnswerDTO.getInquiryQuestion().getId()
            );
            if (inquiryAnswerEmail != null) {
                mailService.sendMail(inquiryAnswerEmail);
            }
            return ResponseEntity.status(HttpStatus.OK).body(output);
        } catch (ResourceNotFoundException rn) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_INQUIRY_QUESTION_ID_NULL.description);
        } catch (UploadFileMaxSizeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_MAX_SIZE.description);
        } catch (UploadFileFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.ErrorCode.DEEPSINAL_FILE_INCORRECT_FORMAT.description);
        } catch (UploadFileExistedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Constants.ErrorCode.DEEPSINAL_FILE_EXISTS.description);
        } catch (UploadException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Constants.ErrorCode.DEEPSINAL_FILE_UPLOAD_ERROR.description);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     *
     * @param questionId: id of question
     * @return
     */

    @GetMapping("/getByQuestionId")
    @Operation(summary = "Get Answer Inquiry By QuestionId", tags = { "Answer Inquiry Management" })
    public InquiryAnswerDTO getByQuestionId(@RequestParam("questionId") Long questionId) {
        return ObjectMapperUtils.map(iInquiryAnswerService.findByInquiryQuestionId(questionId), InquiryAnswerDTO.class);
    }
}
