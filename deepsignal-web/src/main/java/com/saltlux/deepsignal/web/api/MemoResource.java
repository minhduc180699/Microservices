package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.config.ApplicationProperties;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Memo;
import com.saltlux.deepsignal.web.service.IMemoService;
import com.saltlux.deepsignal.web.service.dto.MemoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/memo/{connectomeId}")
@Tag(name = "Memo Management", description = "Memo Management API")
public class MemoResource {

    private final ModelMapper modelMapper;

    private final IMemoService iMemoService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public MemoResource(ModelMapper modelMapper, IMemoService iMemoService) {
        this.modelMapper = modelMapper;
        this.iMemoService = iMemoService;
    }

    /**
     * {@code POST  /save : save memo
     *
     * @param memo
     * @return
     */
    @PostMapping("{feedId}/save")
    @Operation(summary = "Save memo", tags = { "Memo Management" })
    @UserActivity(activityName = Constants.UserActivities.MEMO)
    public ResponseEntity<?> saveMemo(
        @PathVariable("connectomeId") String connectomeId,
        @PathVariable("feedId") String feedId,
        @Valid @RequestBody MemoDTO memoDTO
    ) {
        try {
            String url = applicationProperties.getExternalApi().getDeepsignalAdapter() + "/connectome-feed/memo";
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
            uriComponentsBuilder.queryParam("feedId", memoDTO.getFeedId());
            uriComponentsBuilder.queryParam("status", 1);
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.POST,
                new HttpEntity<>(memoDTO),
                String.class
            );
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Memo memo = iMemoService.save(modelMapper.map(memoDTO, Memo.class));
                return ResponseEntity.status(HttpStatus.OK).body(memo);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * {@code GET : get memos
     *
     * @return
     */
    @GetMapping("{userId}")
    @Operation(summary = "Get memos", tags = { "Memo Management" })
    public ResponseEntity<?> getMemosByUserIdAndFeedId(
        @PathVariable(value = "userId") String userId,
        @RequestParam(value = "feedId") String feedId,
        Pageable pageable
    ) {
        try {
            Page<Memo> memoPage = iMemoService.findMemosByUserIdAndFeedId(userId, feedId, pageable);
            HttpHeaders headers = null;
            if (Objects.nonNull(memoPage)) {
                headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), memoPage);
            }
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(memoPage.getContent());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("")
    @Operation(summary = "Delete memos", tags = { "Memo Management" })
    public ResponseEntity<?> deleteMemo(@RequestParam("memoId") Long memoId) {
        try {
            iMemoService.remove(memoId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
