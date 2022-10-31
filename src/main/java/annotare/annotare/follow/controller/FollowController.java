package annotare.annotare.follow.controller;

import annotare.annotare.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;
}
