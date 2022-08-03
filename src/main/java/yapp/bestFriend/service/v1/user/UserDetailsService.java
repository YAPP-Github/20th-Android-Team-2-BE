package yapp.bestFriend.service.v1.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yapp.bestFriend.model.entity.User;
import yapp.bestFriend.repository.UserRepository;

import java.util.Optional;

/**
 * Spring security에서 로그인 할 때 전달된 정보를 기반으로 DB에서 유저를 가져오는 책임을 가지는 인터페이스
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(Long.parseLong(username));

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("404 - User Not Found");
        }

        return new yapp.bestFriend.service.v1.user.UserDetails(user.get());
    }

}
