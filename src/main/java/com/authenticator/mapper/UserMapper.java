package com.authenticator.mapper;


import com.authenticator.model.dao.User;
import com.authenticator.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User user);

}
