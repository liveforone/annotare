package annotare.annotare.follow.repository;

import annotare.annotare.follow.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from Follow f join fetch f.users u where u.email = :email")
    List<Follow> findFollowList(@Param("email") String email);

    @Query("select f from Follow f join fetch f.users where f.following = :following")
    List<Follow> findFollowerList(@Param("following") String following);

    void deleteByFollowing(String following);
}
