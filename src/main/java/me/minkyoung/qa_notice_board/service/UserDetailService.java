package me.minkyoung.qa_notice_board.service;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("해당 사용자는 없습니다."+email));
    }
}
