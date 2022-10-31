package annotare.annotare.comment.repository;

import annotare.annotare.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join c.board b where b.id = :id")
    Page<Comment> findCommentList(@Param("id") Long id, Pageable pageable);

    @Query("select c from Comment c join fetch c.board where c.id = :id")
    Comment findOneById(@Param("id") Long id);
}
