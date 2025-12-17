package com.example.health.controller;

import com.example.health.domain.Routine;
import com.example.health.domain.User;
import com.example.health.security.JwtUtil;
import com.example.health.service.RoutineService;
import com.example.health.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RoutineController
 * - 로그인한 사용자의 운동 루틴을 관리하는 컨트롤러
 * - JWT 토큰을 통해 사용자 인증 후
 *   루틴 추가 / 조회 / 수정 / 삭제 기능 제공
 */
@RestController
@RequestMapping("/routine")
public class RoutineController {

    /**
     * 운동 루틴 관련 비즈니스 로직 처리 서비스
     */
    private final RoutineService routineService;

    /**
     * 사용자 정보 조회를 위한 서비스
     */
    private final UserService userService;

    /**
     * JWT 토큰에서 사용자 정보를 추출하기 위한 유틸 클래스
     */
    private final JwtUtil jwtUtil;   // 🔥 추가됨

    /**
     * 생성자 주입
     * @param routineService 운동 루틴 서비스
     * @param userService 사용자 서비스
     * @param jwtUtil JWT 유틸
     */
    public RoutineController(RoutineService routineService,
                             UserService userService,
                             JwtUtil jwtUtil) {    // 🔥 생성자 주입
        this.routineService = routineService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 운동 루틴 추가 API
     * @param token Authorization 헤더의 JWT 토큰
     * @param routine 추가할 운동 루틴 데이터
     * @return 저장된 운동 루틴
     */
    @PostMapping("/add")
    public Routine addRoutine(
            @RequestHeader("Authorization") String token,
            @RequestBody Routine routine
    ) {
        // Bearer 접두어 제거
        String realToken = token.replace("Bearer ", "");

        // JWT 토큰에서 사용자명 추출
        String username = jwtUtil.getUsername(realToken);   // 🔥 수정됨

        // 사용자 조회
        User user = userService.findByUsername(username);

        // 루틴에 사용자 ID 설정
        routine.setUserId(user.getId());

        // 루틴 저장
        return routineService.save(routine);
    }

    /**
     * 로그인한 사용자의 운동 루틴 조회 API
     * @param token Authorization 헤더의 JWT 토큰
     * @return 사용자의 운동 루틴 목록
     */
    @GetMapping("/my")
    public List<Routine> myRoutine(
            @RequestHeader("Authorization") String token
    ) {
        // Bearer 접두어 제거
        String realToken = token.replace("Bearer ", "");

        // JWT 토큰에서 사용자명 추출
        String username = jwtUtil.getUsername(realToken);   // 🔥 수정됨

        // 사용자 조회
        User user = userService.findByUsername(username);

        // 사용자 ID 기준으로 루틴 조회
        return routineService.findByUserId(user.getId());
    }

    /**
     * 운동 루틴 수정 API
     * @param id 수정할 루틴 ID
     * @param token Authorization 헤더의 JWT 토큰
     * @param routine 수정할 루틴 데이터
     * @return 수정된 운동 루틴
     */
    @PutMapping("/update/{id}")
    public Routine updateRoutine(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody Routine routine
    ) {
        // 루틴 ID 기준으로 수정
        return routineService.update(id, routine);
    }

    /**
     * 운동 루틴 삭제 API
     * @param id 삭제할 루틴 ID
     * @param token Authorization 헤더의 JWT 토큰
     */
    @DeleteMapping("/delete/{id}")
    public void deleteRoutine(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        // 루틴 ID 기준으로 삭제
        routineService.delete(id);
    }
}
