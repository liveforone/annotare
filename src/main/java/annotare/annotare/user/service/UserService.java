package annotare.annotare.user.service;

import annotare.annotare.user.model.Role;
import annotare.annotare.user.model.UserDto;
import annotare.annotare.user.model.UserResponseDto;
import annotare.annotare.user.model.Users;
import annotare.annotare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //== 회원 가입 로직 ==//
    @Transactional
    public Long joinUser(UserDto userDto) {
        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setAuth(Role.MEMBER);  //기본 권한 매핑

        return userRepository.save(userDto.toEntity()).getId();
    }

    //== 로그인 - 세션과 컨텍스트홀더 사용 ==//
    @Transactional
    public void login(UserDto userDto, HttpSession httpSession) throws UsernameNotFoundException {

        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Users user = userRepository.findByEmail(email);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(token);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        List<GrantedAuthority> authorities = new ArrayList<>();
        /*
        처음 어드민이 로그인을 하는경우 이메일로 판별해서 권한을 admin으로 변경해주고
        그 다음부터 어드민이 업데이트 할때에는 auth 칼럼으로 판별해서 db 업데이트 하지않고,
        grandtedauthority 만 업데이트 해준다.
         */
        if (user.getAuth() != Role.ADMIN && ("admin@annotare.com").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
            userRepository.updateAuth(Role.ADMIN, userDto.getEmail());
        } else if (user.getAuth() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        new User(user.getEmail(), user.getPassword(), authorities);
    }

    //== spring context 반환 메소드(필수) ==//
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (users.getAuth() == Role.ADMIN) {  //어드민 아이디 지정됨, 비밀번호는 회원가입해야함
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(users.getEmail(), users.getPassword(), authorities);
    }

    //== pw없이 유저 정보 가져오기 ==//
    @Transactional(readOnly = true)
    public UserResponseDto getUser(String email) {
        Users users = userRepository.findByEmail(email);

        return UserResponseDto.builder()
                .id(users.getId())
                .email(users.getEmail())
                .auth(users.getAuth())
                .build();
    }

    //== 전체 유저 리턴 for admin ==//
    @Transactional(readOnly = true)
    public List<Users> getAllUsersForAdmin() {
        return userRepository.findAll();
    }
}
