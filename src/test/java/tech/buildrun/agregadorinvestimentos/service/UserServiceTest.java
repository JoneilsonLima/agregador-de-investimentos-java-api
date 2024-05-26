package tech.buildrun.agregadorinvestimentos.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.buildrun.agregadorinvestimentos.controller.CreateUserDto;
import tech.buildrun.agregadorinvestimentos.entity.Account;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {
        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUserWithSuccess() {
            // Arrange
            List<Account> accounts = new ArrayList<>();
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    accounts,
                    Instant.now(),
                    null
            );
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "password"
            );

            // Act
            var output = userService.createUser(input);
            // Assert
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());

            assertEquals(output, user.getUserId());
        }

        @Test
        @DisplayName("Should exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {
            // Arrange
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "username",
                    "email@email.com",
                    "password"
            );

            // Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by id with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            // Arrange
            List<Account> accounts = new ArrayList<>();
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    accounts,
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Asert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            // Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            // Act
            EntityNotFoundException thrown = assertThrows(
                    EntityNotFoundException.class,
                    () -> userService.getUserById(userId.toString()),
                    "Expected getUserById() to throw, but it didn't"
            );

            // Assert
            assertEquals("User not found with ID: " + userId, thrown.getMessage());
        }

    }

    @Nested
    class listUsers {
        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {
            // Arrange
            List<Account> accounts = new ArrayList<>();
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password",
                    accounts,
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();

            // Act
            var output = userService.listUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteById {
        @Test
        @DisplayName("should delete with success")
        void shouldDeleteWithSuccess() {
            // Arrange
            var userId = UUID.randomUUID();
            doReturn(true)
                    .when(userRepository).existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userRepository).deleteById(uuidArgumentCaptor.capture());

            // Act
            userService.deleteById(userId.toString());

            // Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }
    }

}
