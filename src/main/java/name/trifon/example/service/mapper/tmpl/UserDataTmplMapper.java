package name.trifon.example.service.mapper.tmpl;

import name.trifon.example.service.dto.UserDataDTO;
import name.trifon.example.service.dto.UserDataTemplateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDataTemplateDTO} and its DTO {@link UserDataDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserDataTmplMapper extends TmplMapper<UserDataDTO, UserDataTemplateDTO> {

//	@Mapping(target = "images", ignore = true)
	UserDataDTO toDto(UserDataTemplateDTO userDataTemplateDTO);

	UserDataTemplateDTO toTemplate(UserDataDTO userDataDTO);
}
