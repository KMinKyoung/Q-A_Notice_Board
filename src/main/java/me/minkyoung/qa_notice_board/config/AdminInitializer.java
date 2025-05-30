package me.minkyoung.qa_notice_board.config;

import lombok.RequiredArgsConstructor;
import me.minkyoung.qa_notice_board.domain.Role;
import me.minkyoung.qa_notice_board.entity.User;
import me.minkyoung.qa_notice_board.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void run(String... args){
        String adminEmail = "admin@example.com";

        if(userRepository.findByEmail(adminEmail).isEmpty()){
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setNickname("관리자");
            admin.setRole(Role.ROLE_ADMIN);
            admin.setCreatedAt(LocalDateTime.now());

            userRepository.save(admin);
            System.out.println("관리자 계쩡 생성 완료"+adminEmail);
        }
    }
}
