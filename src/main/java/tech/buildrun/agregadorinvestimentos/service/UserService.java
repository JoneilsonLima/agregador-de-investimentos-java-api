package tech.buildrun.agregadorinvestimentos.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.agregadorinvestimentos.controller.dto.AccountResponseDto;
import tech.buildrun.agregadorinvestimentos.controller.dto.CreateAccountDto;
import tech.buildrun.agregadorinvestimentos.controller.dto.CreateUserDto;
import tech.buildrun.agregadorinvestimentos.controller.dto.UpdateUserDto;
import tech.buildrun.agregadorinvestimentos.entity.Account;
import tech.buildrun.agregadorinvestimentos.entity.BillingAddress;
import tech.buildrun.agregadorinvestimentos.entity.User;
import tech.buildrun.agregadorinvestimentos.mapper.UserMapper;
import tech.buildrun.agregadorinvestimentos.repository.AccountRepository;
import tech.buildrun.agregadorinvestimentos.repository.BillingAddressRepository;
import tech.buildrun.agregadorinvestimentos.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    private final UserMapper userMapper;

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

        if (userExists) {
            userRepository.deleteById(id);
        }

    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setUser(user);
        account.setBillingAddress(null);
        account.setAccountStocks(new ArrayList<>());
        account.setDescription(createAccountDto.description());

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress();
        billingAddress.setId(account.getAccountId());
        billingAddress.setAccount(accountCreated);
        billingAddress.setStreet(createAccountDto.street());
        billingAddress.setNumber(createAccountDto.number());

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var response = user.getAccounts()
                .stream()
                .map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();

        return response;
    }
}
