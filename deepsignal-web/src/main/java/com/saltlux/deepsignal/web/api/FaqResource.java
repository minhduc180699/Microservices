package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.api.errors.ErrorConstants;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Faqs;
import com.saltlux.deepsignal.web.service.IFaqService;
import com.saltlux.deepsignal.web.service.dto.FaqDTO;
import com.saltlux.deepsignal.web.service.dto.PagedResponse;
import com.saltlux.deepsignal.web.util.ObjectMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faqs")
@Tag(name = "Faqs Management", description = "The Faqs management API")
public class FaqResource {

    private final Logger log = LoggerFactory.getLogger(FaqResource.class);

    @Autowired
    private IFaqService iFaqService;

    /**
     *
     * @param page
     * @param size
     * @param orderBy
     * @param sortDirection
     * @param keyWord
     * @param code: category code
     * @return
     */
    @PostMapping("/search")
    @Operation(summary = "Search Faqs", tags = { "Faqs Management" })
    public ResponseEntity<PagedResponse<FaqDTO>> searchKeyWord(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "view_count") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "keyWord") String keyWord,
        @RequestParam(value = "code") String code
    ) {
        try {
            PagedResponse<FaqDTO> faqDTOPagedResponse = iFaqService.searchKeyWord(page, size, orderBy, sortDirection, keyWord, code);
            return ResponseEntity.status(HttpStatus.OK).body(faqDTOPagedResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     *
     * @param page
     * @param size
     * @param orderBy
     * @param sortDirection
     * @param code: category code
     * @return
     */

    @GetMapping("/findByCategory")
    @Operation(summary = "Find Faqs By Category code", tags = { "Faqs Management" })
    public ResponseEntity<PagedResponse<FaqDTO>> findByCategory(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "viewCount") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
        @RequestParam(value = "code") String code
    ) {
        try {
            PagedResponse<FaqDTO> faqDTOPagedResponse = iFaqService.findByCategoryId(code, page, size, orderBy, sortDirection);
            return ResponseEntity.status(HttpStatus.OK).body(faqDTOPagedResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     *
     * @param page
     * @param size
     * @param orderBy
     * @param sortDirection
     * @return
     */
    @GetMapping("/findAll")
    @Operation(summary = "Get All Faqs  ", tags = { "Faqs Management" })
    public ResponseEntity<PagedResponse<FaqDTO>> findAll(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "orderBy", defaultValue = "viewCount") String orderBy,
        @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
    ) {
        try {
            PagedResponse<FaqDTO> faqDTOPagedResponse = iFaqService.findAll(page, size, orderBy, sortDirection);
            return ResponseEntity.status(HttpStatus.OK).body(faqDTOPagedResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     *
     * @param faqDTO: infomation faqs
     * @return
     */
    @PostMapping("/create")
    @Operation(summary = "Create Faqs", tags = { "Faqs Management" })
    public ResponseEntity<?> save(
        @Parameter(
            description = "Faq to create. Cannot null or empty.",
            required = true,
            schema = @Schema(implementation = FaqDTO.class)
        ) @RequestBody FaqDTO faqDTO
    ) {
        log.debug("REST request to save faq : {} ", faqDTO);

        if (faqDTO.getFaqId() != null) {
            throw new BadRequestException(ErrorConstants.DEFAULT_TYPE, Constants.ErrorCode.DEEPSINAL_FAQ_ID_NOT_NULL.description);
        }

        //        if (StringHelper.isNullOrEmptyString(faqDTO.getUser().getId())) {
        //            throw new BadRequestException(ErrorConstants.DEFAULT_TYPE, Constants.ErrorCode.DEEPSINAL_USER_ID_NULL.description);
        //        }

        try {
            iFaqService.save(ObjectMapperUtils.map(faqDTO, Faqs.class));
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
