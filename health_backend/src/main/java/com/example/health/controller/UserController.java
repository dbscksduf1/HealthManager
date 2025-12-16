package com.example.health.controller;

import com.example.health.domain.User;
import com.example.health.dto.LoginRequest;
import com.example.health.dto.LoginResponse;
import com.example.health.security.JwtUtil;
import com.example.health.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("prod")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JwtUtil jwtUtil;  // 🔥 JwtUtil DI 추가


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            User saved = service.save(user);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            User user = service.findById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<User> users = service.findAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String token) {

        try {
            if (token == null || token.isBlank()) {
                throw new SecurityException("토큰이 제공되지 않았습니다.");
            }

            String realToken = token.replace("Bearer ", "");
            String username = jwtUtil.getUsername(realToken);  // 🔥 static 제거됨

            User user = service.findByUsername(username);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User userData) {
        try {
            User updated = service.update(id, userData);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().body("삭제 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            User user = service.findByUsername(request.getUsername());

            if (!service.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("비밀번호가 틀렸습니다.");
            }

            String token = jwtUtil.generateToken(user.getUsername());  // 🔥 generateToken 사용
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
