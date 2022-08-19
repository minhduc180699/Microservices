package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.domain.RecentSearch;
import com.saltlux.deepsignal.web.exception.BadRequestException;
import com.saltlux.deepsignal.web.service.IRecentSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recent-search")
@Tag(name = "Recent Search Management", description = "The recent search management API")
public class RecentSearchResource {

    private final IRecentSearchService recentSearchService;

    public RecentSearchResource(IRecentSearchService recentSearchService) {
        this.recentSearchService = recentSearchService;
    }

    /**
     * {@code POST  /save : save recent search.
     *
     * @param recentSearch model recent search
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "Save recent search", tags = { "Recent Search Management" })
    public ResponseEntity<?> saveRecentSearch(@RequestBody RecentSearch recentSearch) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recentSearchService.save(recentSearch));
        } catch (BadRequestException be) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(be.getErrorKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@code GET  /getAll : get all recent search.
     *
     * @param userId: userId of user login
     * @return
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get recent search", tags = { "Recent Search Management" })
    public ResponseEntity<?> getAll(@RequestParam(value = "userId") String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recentSearchService.getAllByContentAndUserId(userId));
        } catch (BadRequestException be) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(be.getErrorKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@code DELETE  /{id} : delete recent search by id.
     *
     * @param
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recent search by id", tags = { "Recent Search Management" })
    public ResponseEntity<?> deleteRecentSearch(@PathVariable Long id) {
        try {
            recentSearchService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE SUCCESS");
        } catch (BadRequestException be) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(be.getErrorKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@code DELETE  /{id} : delete all recent search by userId.
     *
     * @param
     * @return
     */
    @DeleteMapping("/deleteAll/{userId}")
    @Operation(summary = "Delete all recent search by userId", tags = { "Recent Search Management" })
    public ResponseEntity<?> deleteAllRecentSearchByUserId(@PathVariable String userId) {
        try {
            recentSearchService.deleteAllByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE SUCCESS");
        } catch (BadRequestException be) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(be.getErrorKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
