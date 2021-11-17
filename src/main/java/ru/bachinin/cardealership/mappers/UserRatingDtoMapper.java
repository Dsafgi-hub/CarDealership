package ru.bachinin.cardealership.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.bachinin.cardealership.dto.UserRatingDTO;
import ru.bachinin.cardealership.entities.User;

@Component
@Mapper(componentModel = "spring")
public interface UserRatingDtoMapper {
    UserRatingDTO userToUserRatingDto(User user);
}
