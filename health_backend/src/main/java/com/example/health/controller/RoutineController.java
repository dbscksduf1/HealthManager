package com.example.health.controller;

import com.example.health.domain.Routine;
import com.example.health.domain.User;
import com.example.health.security.JwtUtil;
import com.example.health.service.RoutineService;
import com.example.health.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routine")
public class RoutineController {

    private final RoutineService routineService;
    private final UserService userService;

    public RoutineController(RoutineService routineService, UserService userService) {
        this.routineService = routineService;
        this.userService = userService;
    }


    @PostMapping("/add")
    public Routine addRoutine(
            @RequestHeader("Authorization") String token,
            @RequestBody Routine routine
    ) {
        String realToken = token.replace("Bearer ", "");
        String username = JwtUtil.getUsername(realToken);

        User user = userService.findByUsername(username);
        routine.setUserId(user.getId());

        return routineService.save(routine);
    }


    @GetMapping("/my")
    public List<Routine> myRoutine(
            @RequestHeader("Authorization") String token
    ) {
        String realToken = token.replace("Bearer ", "");
        String username = JwtUtil.getUsername(realToken);

        User user = userService.findByUsername(username);
        return routineService.findByUserId(user.getId());
    }


    @PutMapping("/update/{id}")
    public Routine updateRoutine(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody Routine routine
    ) {
        return routineService.update(id, routine);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteRoutine(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        routineService.delete(id);
    }
}
