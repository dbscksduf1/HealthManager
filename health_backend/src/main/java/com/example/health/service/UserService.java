package com.example.health.service;

import com.example.health.domain.User;
import com.example.health.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }


    public User save(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("아이디는 필수 입력 항목입니다.");
        }

        if (repo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력 항목입니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }


    public User findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }


    public List<User> findAll() {
        return repo.findAll();
    }


    public User findByUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }

        User user = repo.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return user;
    }


    public boolean matches(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }


    public User update(Long id, User newData) {

        User user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (newData.getUsername() != null && !newData.getUsername().isBlank()) {
            user.setUsername(newData.getUsername());
        }

        user.setAge(newData.getAge());


        if (newData.getPassword() != null && !newData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(newData.getPassword()));
        }

        return repo.save(user);
    }


    public void delete(Long id) {

        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("삭제할 사용자를 찾을 수 없습니다.");
        }

        repo.deleteById(id);
    }
}
