package annotare.annotare.user.repository;

import annotare.annotare.user.model.Role;
import annotare.annotare.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);

    //== 권한 업데이트 ==//
    @Modifying
    @Query("update Users u set u.auth = :auth where u.email = :email")
    void updateAuth(@Param("auth") Role auth, @Param("email") String email);
}
