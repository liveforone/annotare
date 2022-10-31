package annotare.annotare.follow.repository;

import annotare.annotare.follow.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
