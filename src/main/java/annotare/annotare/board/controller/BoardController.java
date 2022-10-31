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
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /*
    * 게시글 홈에는 최신순으로 정렬.
     */
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

    /*
    * 검색 페이지에는 조회수를 기준으로 정렬.
     */
    @GetMapping("/board/search")
    public ResponseEntity<Page<Board>> boardSearch(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "view", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @RequestParam("keyword") String keyword
    ) {
        Page<Board> board = boardService.searchBoard(keyword, pageable);

        return ResponseEntity.ok(board);
    }

    /*
    * 카테고리 페이지는 좋아요를 기준으로 정렬함.
     */
    @GetMapping("/board/category/{category}")
    public ResponseEntity<Page<Board>> boardCategoryList(
            @PathVariable("category") String category,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable
    ) {
        Page<Board> categoryList = boardService.getCategoryList(category, pageable);

        return ResponseEntity.ok(categoryList);
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
    수정과 삭제는 게시글의 작성자만 가능하고,
     이를 판별하기 위해 현재 유저를 같이 map으로 보내준다.
     */
    @GetMapping("/board/{id}")
    public ResponseEntity<Map<String, Object>> boardDetail(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Map<String, Object> map = new HashMap<>();
        Board board = boardService.getDetail(id);

        boardService.updateView(id);
        log.info("게시글 id=" + id + " 조회수 업데이트 성공!!");

        map.put("body", board);
        map.put("writer", board.getUsers().getEmail());
        map.put("user", principal.getName());

        return ResponseEntity.ok(map);
    }

    @PostMapping("/board/good/{id}")
    public ResponseEntity<?> boardClickGood(@PathVariable("id") Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/board/" + id));

        boardService.updateGood(id);
        log.info("게시글 id=" + id + " 좋아요 업데이트 !!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/board/edit/{id}")
    public ResponseEntity<Board> boardEditPage(@PathVariable("id") Long id) {
        Board board = boardService.getDetail(id);

        return ResponseEntity.ok(board);
    }

    /*
    뷰에서 한 번 작성자와 현재 유저를 판별해주었지만
    서버단에서 민감한 수정/삭제는 한 번더 판별해준다.
     */
    @PostMapping("/board/edit/{id}")
    public ResponseEntity<?> boardEdit(
            @PathVariable("id") Long id,
            @RequestBody BoardDto boardDto,
            Principal principal
    ) {
        Board board = boardService.getDetail(id);

        if (Objects.equals(board.getUsers().getEmail(), principal.getName())) {
            Long boardId = boardService.editBoard(id, boardDto);
            log.info("게시글 id=" + id + " 업데이트 완료!!");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("/board/" + boardId));

            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .headers(httpHeaders)
                    .build();
        } else {
            log.info("작성자와 현재 유저가 일치하지 않습니다.");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    /*
    수정과 마찬가지로 한 번 더 서버단에서 작성자와 현재 유저를 판별해준다.
     */
    @PostMapping("/board/delete/{id}")
    public ResponseEntity<?> boardDelete(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Board board = boardService.getDetail(id);

        if (Objects.equals(board.getUsers().getEmail(), principal.getName())) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("/board"));

            boardService.deleteBoard(id);
            log.info("게시글 id=" + id + " 삭제 완료!!");

            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .headers(httpHeaders)
                    .build();
        } else {
            log.info("작성자와 현재 유저가 일치하지 않습니다.");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }
}
