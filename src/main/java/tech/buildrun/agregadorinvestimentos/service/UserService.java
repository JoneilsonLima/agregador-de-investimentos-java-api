package tech.buildrun.agregadorinvestimentos.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.buildrun.agregadorinvestimentos.controller.CreateUserDto;
import tech.buildrun.agregadorinvestimentos.controller.UpdateUserDto;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.mapper.UserMapper;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        User userSaved = new User();
        userSaved.setPassword(createUserDto.password());
        userSaved.setEmail(createUserDto.email());
        userSaved.setUsername(createUserDto.username());
        return userRepository.save(userSaved).getUserId();
    }

    public Optional<User> getUserById(String userId) {
        var findUser = userRepository.findById(UUID.fromString(userId));
        if (findUser.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }

        return findUser;
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }

            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);
        }

    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (!userExists) {
            new EntityNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }
}
