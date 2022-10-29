package annotare.annotare.board.repository;

import annotare.annotare.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "select b from Board b join b.users")
    Page<Board> findBoardAll(Pageable pageable);
}
