package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.service.IInteractionUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interaction-user")
@Tag(name = "Interaction User Management", description = "The Interaction by User management API")
public class InteractionUserResource {

    private final IInteractionUserService interactionUserService;

    public InteractionUserResource(IInteractionUserService interactionUserService) {
        this.interactionUserService = interactionUserService;
    }

    @PostMapping("/like")
    @Operation(summary = "Save Log Like Feed By User", tags = { "Interaction User Management" })
    public ResponseEntity<?> saveLike(
        @RequestParam("feedId") String feedId,
        @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete
    ) throws Exception {
        return buildBaseResourceInteractionUser(feedId, Constants.TypeInteraction.LIKE.type, isDelete);
    }

    @PostMapping("/dislike")
    @Operation(summary = "Save Log Dislike Feed By User", tags = { "Interaction User Management" })
    public ResponseEntity<?> saveDislike(
        @RequestParam("feedId") String feedId,
        @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete
    ) throws Exception {
        return buildBaseResourceInteractionUser(feedId, Constants.TypeInteraction.DISLIKE.type, isDelete);
    }

    @PostMapping("/share")
    @Operation(summary = "Save Log Share Feed By User", tags = { "Interaction User Management" })
    public ResponseEntity<?> saveShare(
        @RequestParam("feedId") String feedId,
        @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete
    ) throws Exception {
        return buildBaseResourceInteractionUser(feedId, Constants.TypeInteraction.SHARE.type, isDelete);
    }

    @PostMapping("/bookmark")
    @Operation(summary = "Save Log Bookmark Feed By User", tags = { "Interaction User Management" })
    public ResponseEntity<?> saveBookmark(
        @RequestParam("feedId") String feedId,
        @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete
    ) throws Exception {
        return buildBaseResourceInteractionUser(feedId, Constants.TypeInteraction.BOOKMARK.type, isDelete);
    }

    @PostMapping("/comment")
    @Operation(summary = "Save Log Comment Feed By User", tags = { "Interaction User Management" })
    public ResponseEntity<?> saveComment(
        @RequestParam("feedId") String feedId,
        @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete
    ) throws Exception {
        return buildBaseResourceInteractionUser(feedId, Constants.TypeInteraction.COMMENT.type, isDelete);
    }

    @PostMapping("/delete")
    @Operation(summary = "Save Log Delete Feed By User", tags = { "Interaction User Management" })
    public ResponseEntity<?> saveDelete(
        @RequestParam("feedId") String feedId,
        @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete
    ) throws Exception {
        return buildBaseResourceInteractionUser(feedId, Constants.TypeInteraction.DELETE.type, isDelete);
    }

    @GetMapping("/statistic")
    @Operation(summary = "Statistic Interaction By Feed", tags = { "Interaction User Management" })
    public ResponseEntity<?> statisticInteraction(@RequestParam("feedId") String feedId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(interactionUserService.statisticInteraction(feedId));
    }

    private ResponseEntity<?> buildBaseResourceInteractionUser(String feedId, int typeInteraction, boolean isDelete) throws Exception {
        if (isDelete) {
            return ResponseEntity.status(HttpStatus.OK).body(interactionUserService.delete(feedId, typeInteraction));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(interactionUserService.save(feedId, typeInteraction));
        }
    }
}
