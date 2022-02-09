package name.trifon.example.service;

import name.trifon.example.service.dto.UserDataDTO;
import name.trifon.example.domain.UserData; //@Trifon

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map; //@Trifon
import java.util.Optional;

/**
 * Service Interface for managing {@link name.trifon.example.domain.UserData}.
 */
public interface UserDataService {

    /**
     * Save a userData.
     *
     * @param userDataDTO the entity to save.
     * @return the persisted entity.
     */
    UserDataDTO save(UserDataDTO userDataDTO);

    /**
     * Save a userData.
     *
     * @param userData the entity to save.
     * @return the persisted entity.
     */
     // @Trifon
    UserData save(UserData userData);

    /**
     * Get all the userData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserDataDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserDataDTO> findOne(Long id);

    /**
     * Delete the "id" userData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

	//@Trifon
	public Map<String, String> columnsToNameValueMap(String[] columns);

}
