package name.trifon.example.service.mapper;


import name.trifon.example.domain.*;
import name.trifon.example.service.dto.UserDataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserData} and its DTO {@link UserDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserDataMapper extends EntityMapper<UserDataDTO, UserData> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserDataDTO toDto(UserData userData);

    @Mapping(source = "userId", target = "user")
    UserData toEntity(UserDataDTO userDataDTO);

    default UserData fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserData userData = new UserData();
        userData.setId(id);
        return userData;
    }
}
