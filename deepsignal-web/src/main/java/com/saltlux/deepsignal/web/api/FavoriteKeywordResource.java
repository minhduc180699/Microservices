package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.api.errors.BadRequestException;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.FavoriteKeyword;
import com.saltlux.deepsignal.web.service.IFavoriteKeywordService;
import com.saltlux.deepsignal.web.service.dto.FavoriteKeywordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite-keyword/{connectomeId}")
@Tag(name = "Favorite Keyword Management", description = "Favorite Keyword Management API")
public class FavoriteKeywordResource {

    private final ModelMapper modelMapper;

    public final IFavoriteKeywordService favoriteKeywordService;

    public FavoriteKeywordResource(IFavoriteKeywordService favoriteKeywordService, ModelMapper modelMapper) {
        this.favoriteKeywordService = favoriteKeywordService;
        this.modelMapper = modelMapper;
    }

    /**
     * {@code POST  /save : save favorite keyword.
     *
     * @param favorite keyword
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "Save favorite keyword", tags = { "Favorite Keyword Management" })
    @UserActivity(activityName = Constants.UserActivities.FAVORITES)
    public ResponseEntity<?> saveFavoriteKeyword(@RequestBody FavoriteKeywordDTO favoriteKeywordDTO, @PathVariable String connectomeId) {
        if (favoriteKeywordDTO.getContent() == null || favoriteKeywordDTO.getUserId() == null) {
            throw new BadRequestException("Content or User Id cannot be null!");
        } else {
            try {
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(favoriteKeywordService.save(modelMapper.map(favoriteKeywordDTO, FavoriteKeyword.class)));
            } catch (BadRequestException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorKey());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    /**
     * {@code GET : get all favorite keyword by user id.
     *
     * @param userId
     * @return
     */
    @GetMapping("{userId}")
    @Operation(summary = "Get all favorite keyword", tags = { "Favorite Keyword Management" })
    public ResponseEntity<?> getAllFavoriteKeyword(@PathVariable(value = "userId") String userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User Id cannot be null!");
        } else {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(favoriteKeywordService.findAllByUserId(userId));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    /**
     * {@code DELETE  /{id} : delete favorite keyword by id.
     *
     * @param
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete favorite keyword by id", tags = { "Favorite Keyword Management" })
    public ResponseEntity<?> deleteFavoriteKeyword(@PathVariable Long id) {
        try {
            favoriteKeywordService.remove(id);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * {@code DELETE  /deleteAll : delete ALL favorite keyword by userId.
     */
    @DeleteMapping("/deleteAll/{userId}")
    @Operation(summary = "Delete all favorite keyword by userId", tags = { "Favorite Keyword Management" })
    public ResponseEntity<?> deleteAllFavoriteKeyword(@PathVariable String userId) {
        try {
            favoriteKeywordService.deleteByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
