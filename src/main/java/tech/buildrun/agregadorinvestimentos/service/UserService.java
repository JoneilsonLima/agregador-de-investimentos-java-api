package tech.buildrun.agregadorinvestimentos.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.buildrun.agregadorinvestimentos.controller.CreateUserDto;
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
        userRepository.save(userSaved);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        var findUser = userRepository.findById(UUID.fromString(userId));
        if (findUser.isEmpty()) {
            new EntityNotFoundException();
        }

        return findUser;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (!userExists) {
            new EntityNotFoundException();
        }

        userRepository.deleteById(id);
    }
}
