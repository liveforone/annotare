package annotare.annotare.follow.controller;

import annotare.annotare.follow.model.Follow;
import annotare.annotare.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/{email}")
    public ResponseEntity<?> following(
            @PathVariable("email") String email,
            Principal principal
    ) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/user/writer/" + email));

        followService.saveFollow(email, principal.getName());
        log.info("팔로잉 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    //== 나의 팔로우 리스트 ==//
    @GetMapping("/follow/myfollow")
    public ResponseEntity<List<String>> myFollowList(Principal principal) {
        List<String> myFollowList = followService.getMyFollowList(principal.getName());

        return ResponseEntity.ok(myFollowList);
    }

    /*
    나의 팔로우 리스트에서 팔로우 끊기가 가능하다.
     */
    @PostMapping("/unfollow/{email}")
    public ResponseEntity<?> unfollow(@PathVariable("email") String email) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/user/mypage"));

        followService.unfollow(email);
        log.info("언팔로우 성공 !!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    //== 나를 팔로우 하는 사람들 리스트 ==//
    @GetMapping("/follow/myfollower")
    public ResponseEntity<List<String>> myFollowerList(Principal principal) {
        List<String> myFollowerList = followService.getMyFollowerList(principal.getName());

        return ResponseEntity.ok(myFollowerList);
    }

    //== 작가페이지 - 작가가 팔로우 하는 사람 리스트 ==//
    @GetMapping("/follow/writerfollow/{writer}")
    public ResponseEntity<List<String>> writerFollowList(@PathVariable("writer") String writer) {
        List<String> writerFollowList = followService.getWriterFollowList(writer);

        return ResponseEntity.ok(writerFollowList);
    }

    //== 작가페이지 - 작가를 팔로우 하는 사람 리스트 ==//
    @GetMapping("/follow/writerfollower/{writer}")
    public ResponseEntity<List<String>> writerFollowerList(@PathVariable("writer") String writer) {
        List<String> writerFollowerList = followService.getWriterFollowerList(writer);

        return ResponseEntity.ok(writerFollowerList);
    }
}
