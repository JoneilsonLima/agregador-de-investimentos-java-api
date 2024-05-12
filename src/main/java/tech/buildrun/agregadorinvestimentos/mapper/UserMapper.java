package tech.buildrun.agregadorinvestimentos.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import tech.buildrun.agregadorinvestimentos.config.ModelConfiguration;
import tech.buildrun.agregadorinvestimentos.controller.CreateUserDto;
import tech.buildrun.agregadorinvestimentos.entity.User;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public User toUser(CreateUserDto createUserDto) {
        return mapper.map(createUserDto, User.class);
    }

}
