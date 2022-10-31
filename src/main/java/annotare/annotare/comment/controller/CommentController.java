package annotare.annotare.comment.controller;

import annotare.annotare.comment.model.Comment;
import annotare.annotare.comment.model.CommentDto;
import annotare.annotare.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comment/{boardId}")
    public ResponseEntity<?> commentList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<Comment> commentList = commentService.getCommentList(boardId, pageable);

        return ResponseEntity.ok(commentList);
    }

    @PostMapping("/comment/post/{boardId}")
    public ResponseEntity<?> commentPost(
            @PathVariable("boardId") Long boardId,
            @RequestBody CommentDto commentDto,
            Principal principal
    ) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/comment/" + boardId));

        commentService.saveComment(boardId, commentDto, principal.getName());
        log.info("댓글 등록 완료!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    /*
    서버 내에서 작성자와 해당 url을 요청한 현재 객체를 판별한다.
    다르다면 잘못된 요청으로 넘긴다.
     */
    @GetMapping("/comment/edit/{id}")
    public ResponseEntity<Comment> commentEditPage(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Comment comment = commentService.getComment(id);

        //== 정상 ==//
        if (Objects.equals(principal.getName(), comment.getWriter())) {
            log.info("작성자와 현재 유저가 일치합니다. 접근 성공.");
            return ResponseEntity.ok(comment);
        } else {
            log.info("작성자와 현재 유저가 일치하지 않습니다.");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    @PostMapping("/comment/edit/{id}")
    public ResponseEntity<?> commentEdit(
            @PathVariable("id") Long id,
            @RequestBody CommentDto commentDto
    ) {
        Long boardId = commentService.editComment(id, commentDto);
        log.info("댓글 id=" + id + " 수정 성공!!");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/comment/" + boardId));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
