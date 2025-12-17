package com.example.health.service;

import com.example.health.domain.User;
import com.example.health.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService
 * - 사용자 관련 핵심 비즈니스 로직을 담당하는 서비스
 * - 회원가입, 조회, 수정, 삭제
 * - 비밀번호 암호화 및 검증 처리
 */
@Service
public class UserService {

    /**
     * 사용자 정보를 DB와 주고받는 Repository
     */
    private final UserRepository repo;

    /**
     * 비밀번호 암호화를 위한 BCrypt 인코더
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 생성자 주입
     * @param repo 사용자 Repository
     */
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * 회원가입 처리
     * - 아이디 중복 검사
     * - 비밀번호 암호화 후 저장
     */
    public User save(User user) {

        // 아이디 유효성 검사
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("아이디는 필수 입력 항목입니다.");
        }

        // 아이디 중복 검사
        if (repo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 유효성 검사
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력 항목입니다.");
        }

        // 비밀번호 암호화 후 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    /**
     * 사용자 단건 조회 (ID 기준)
     */
    public User findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    /**
     * 전체 사용자 목록 조회
     */
    public List<User> findAll() {
        return repo.findAll();
    }

    /**
     * 사용자명(username)으로 사용자 조회
     * - 로그인, 인증 과정에서 사용
     */
    public User findByUsername(String username) {

        // 아이디 유효성 검사
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }

        User user = repo.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return user;
    }

    /**
     * 비밀번호 검증
     * - 로그인 시 입력한 비밀번호와
     *   DB에 저장된 암호화 비밀번호 비교
     */
    public boolean matches(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    /**
     * 사용자 정보 수정
     * - 아이디, 나이, 비밀번호 변경 가능
     * - 비밀번호는 변경 시에만 암호화
     */
    public User update(Long id, User newData) {

        // 수정 대상 사용자 조회
        User user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 아이디 변경 (값이 있을 때만)
        if (newData.getUsername() != null && !newData.getUsername().isBlank()) {
            user.setUsername(newData.getUsername());
        }

        // 나이 수정
        user.setAge(newData.getAge());

        // 비밀번호 변경 (값이 있을 때만)
        if (newData.getPassword() != null && !newData.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(newData.getPassword()));
        }

        return repo.save(user);
    }

    /**
     * 사용자 삭제
     * - 존재 여부 확인 후 삭제
     */
    public void delete(Long id) {

        // 사용자 존재 여부 확인
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("삭제할 사용자를 찾을 수 없습니다.");
        }

        repo.deleteById(id);
    }
}
