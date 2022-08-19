package com.saltlux.deepsignal.web.api;

import com.saltlux.deepsignal.web.aop.userActivities.UserActivity;
import com.saltlux.deepsignal.web.config.Constants;
import com.saltlux.deepsignal.web.domain.Comment;
import com.saltlux.deepsignal.web.service.ICommentService;
import com.saltlux.deepsignal.web.service.impl.Pagging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment/{connectomeId}")
@Tag(name = "Comment Management", description = "The Comment management API")
public class CommentResource {

    private final ICommentService commentService;

    public CommentResource(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save")
    @Operation(summary = "Save Comment By User", tags = { "Comment Management" })
    @UserActivity(activityName = Constants.UserActivities.COMMENT)
    public ResponseEntity<?> saveComment(@RequestBody Comment comment, @PathVariable String connectomeId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.save(comment));
    }

    @GetMapping("/findByFeed")
    @Operation(summary = "Find Comment By Feed", tags = { "Comment Management" })
    public ResponseEntity<?> findByFeed(Pageable pageable, @RequestParam String feedId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findByFeed(pageable, feedId));
    }

    @GetMapping("/countByFeed")
    @Operation(summary = "Count All Comment By Feed", tags = { "Comment Management" })
    public ResponseEntity<?> countByFeed(@RequestParam String feedId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.countCommentByFeed(feedId));
    }
}
