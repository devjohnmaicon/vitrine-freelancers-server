package com.vitrine_freelancers_server.controllers.users;

import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.exceptions.response.ResponseSuccess;
import com.vitrine_freelancers_server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseSuccess> getAllUsers() {
        List<UserEntity> users = userService.allUsers();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseSuccess(
                        "success",
                        HttpStatus.OK.value(),
                        users
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuccess> userById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(
                new ResponseSuccess(
                        "success",
                        HttpStatus.FOUND.value(),
                        userService.findUserById(id)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest user) {
        // TODO: Implement update logic
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSuccess> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ResponseSuccess(
                        "success",
                        HttpStatus.ACCEPTED.value(),
                        null
                )
        );
    }
}