package annotare.annotare.board.repository;

import annotare.annotare.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "select b from Board b join b.users")
    Page<Board> findBoardAll(Pageable pageable);

    @Query("select b from Board b join fetch b.users where b.id = :id")
    Board findOneById(@Param("id") Long id);

    @Query("select b from Board b join b.users where b.title like %:title%")
    Page<Board> searchByTitle(@Param("title") String keyword, Pageable pageable);

    @Query("select b from Board b join b.users where b.category = :category")
    Page<Board> findCategoryList(@Param("category") String category, Pageable pageable);

    @Query("select b from Board b join b.users u where u.email = :email")
    Page<Board> findByUsersEmail(@Param("email") String email, Pageable pageable);

    @Modifying
    @Query("update Board b set b.view = b.view + 1 where b.id = :id")
    void updateView(@Param("id") Long id);

    @Modifying
    @Query("update Board b set b.good = b.good + 1 where b.id = :id")
    void updateGood(@Param("id") Long id);
}
