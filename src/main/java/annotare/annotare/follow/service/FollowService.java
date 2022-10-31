package annotare.annotare.follow.service;

import annotare.annotare.follow.model.Follow;
import annotare.annotare.follow.repository.FollowRepository;
import annotare.annotare.user.model.Users;
import annotare.annotare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    //== 내가 팔로우 한 사람들 ==//
    public List<String> getMyFollowList(String email) {
        List<String> followList = new ArrayList<>();
        List<Follow> myFollowList = followRepository.findFollowList(email);

        for (Follow follow : myFollowList) {
            followList.add(follow.getFollowing());
        }

        return followList;
    }

    //== 나를 팔로우 하는 사람들 ==//
    public List<String> getMyFollowerList(String email) {
        List<String> followerList = new ArrayList<>();
        List<Follow> myFollowerList = followRepository.findFollowerList(email);

        for (Follow follow : myFollowerList) {
            followerList.add(follow.getUsers().getEmail());
        }

        return followerList;
    }

    //== 작가가 팔로잉 하는 사람들 ==//
    public List<String> getWriterFollowList(String writer) {
        List<String> followingList = new ArrayList<>();
        List<Follow> myFollowList = followRepository.findFollowList(writer);

        for (Follow follow : myFollowList) {
            followingList.add(follow.getFollowing());
        }

        return followingList;
    }

    //== 작가를 팔로우 하는 사람들 ==//
    public List<String> getWriterFollowerList(String writer) {
        List<String> followerList = new ArrayList<>();
        List<Follow> writerFollowerList = followRepository.findFollowerList(writer);

        for (Follow follow : writerFollowerList) {
            followerList.add(follow.getUsers().getEmail());
        }

        return followerList;
    }

    @Transactional
    public void saveFollow(String email, String me) {
        Users users = userRepository.findByEmail(me);

        Follow follow = Follow.builder()
                .following(email)
                .users(users)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(String email) {
        followRepository.deleteByFollowing(email);
    }
}
