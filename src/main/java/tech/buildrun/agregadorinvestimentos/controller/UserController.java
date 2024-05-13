package tech.buildrun.agregadorinvestimentos.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @Transactional
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userId)).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        try {
            var findUSer = userService.getUserById(userId);
            return findUSer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        var userList = userService.listUsers();
        return ResponseEntity.ok(userList);
    }

    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<Void> deleteByID(@PathVariable("userId") String userId) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}")
    @Transactional
    public ResponseEntity<Void> updateUserByID(
            @PathVariable("userId") String userId,
            @RequestBody UpdateUserDto updateUserDto) {

        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

}
