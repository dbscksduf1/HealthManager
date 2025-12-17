package com.example.health.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 발생하는 예외를 공통으로 처리하는 클래스
 * 컨트롤러에서 발생한 예외를 HTTP 응답 형태로 변환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청(IllegalArgumentException) 처리
     * @return 400 Bad Request + 오류 메시지
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * 보안 관련 예외(SecurityException) 처리
     * - 인증/인가 실패, 토큰 오류 등에 사용
     * @param ex 발생한 SecurityException
     * @return 401 Unauthorized + 오류 메시지
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurity(SecurityException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * 그 외 모든 예외 처리
     * - 예상하지 못한 서버 오류에 대한 공통 처리
     * @param ex 발생한 Exception
     * @return 500 Internal Server Error + 공통 오류 메시지
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "서버 내부 오류가 발생했습니다."));
    }
}
