package ru.bachinin.cardealership.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.bachinin.cardealership.dto.RegisterUserDTO;
import ru.bachinin.cardealership.dto.UserRatingDTO;
import ru.bachinin.cardealership.entities.User;

@Mapper
public interface RegisterUserDtoMapper {
    RegisterUserDtoMapper REGISTER_USER_DTO_MAPPER = Mappers.getMapper(RegisterUserDtoMapper.class);

    User registerUserDtoToUser(RegisterUserDTO registerUserDto);

    UserRatingDTO userToUserRatingDto(User user);
}
