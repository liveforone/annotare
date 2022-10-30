package annotare.annotare.board.controller;

import annotare.annotare.board.model.Board;
import annotare.annotare.board.model.BoardDto;
import annotare.annotare.board.service.BoardService;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<?> boardHome(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<Board> boardList = boardService.getBoardList(pageable);

        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/board/post")
    public ResponseEntity<?> boardPostPage() {
        return ResponseEntity.ok("게시글 작성 페이지");
    }

    @PostMapping("/board/post")
    public ResponseEntity<?> boardPost(
            @RequestBody BoardDto boardDto,
            Principal principal
    ) {
        Long boardId = boardService.saveBoard(principal.getName(), boardDto);
        log.info("게시글 저장 완료 !!");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/board/" + boardId));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    /*
    detail에서는 수정과 삭제가 가능하다.
    수정과 삭제는 게시글의 작성자만 가능하며 이를 판별하기 위해 현재 유저를 같이 map으로 보내준다.
     */
    @GetMapping("/board/{id}")
    public ResponseEntity<Map<String, Object>> boardDetail(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Map<String, Object> map = new HashMap<>();
        Board board = boardService.getDetail(id);

        boardService.updateView(id);
        log.info("좋아요 업데이트!!");

        map.put("body", board);
        map.put("writer", board.getUsers().getEmail());
        map.put("user", principal.getName());

        return ResponseEntity.ok(map);
    }

    // 좋아요 업데이트, 수정, 삭제, 검색, 카테고리, 작가/마이페이지
}
