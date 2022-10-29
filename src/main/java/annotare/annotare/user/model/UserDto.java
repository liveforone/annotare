package annotare.annotare.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private Role auth;

    public Users toEntity() {
        return Users.builder()
                .id(id)
                .email(email)
                .password(password)
                .auth(auth)
                .build();
    }
}
