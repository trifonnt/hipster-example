package name.trifon.example.service.impl;

import name.trifon.example.service.UserDataService;
//import name.trifon.example.service.IdentifierService; //@Trifon-sequence
//import name.trifon.example.domain.Identifier; //@Trifon-sequence
import name.trifon.example.domain.UserData;
import name.trifon.example.domain.UserDataBuilder; //@Trifon
import name.trifon.example.repository.UserDataRepository;
import name.trifon.example.repository.UserRepository;
import name.trifon.example.service.dto.UserDataDTO;
//import name.trifon.example.service.dto.IdentifierDTO; //@Trifon-sequence
//import name.trifon.example.service.mapper.IdentifierMapper; //@Trifon-sequence
import name.trifon.example.service.mapper.UserDataMapper;
import name.trifon.example.service.util.SequenceUtil; //@Trifon-sequence
import name.trifon.example.service.util.ParseUtil; //@Trifon-import

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal; //@Trifon
import java.time.Instant; //@Trifon
import java.time.ZoneId; //@Trifon
import java.time.format.DateTimeFormatter; //@Trifon
import java.time.format.DateTimeFormatterBuilder; //@Trifon
import java.time.temporal.ChronoField; //@Trifon

import org.springframework.beans.factory.annotation.Autowired; //@Trifon-sequence
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map; //@Trifon
import java.util.HashMap; //@Trifon
import java.util.Optional;

/**
 * Service Implementation for managing {@link UserData}.
 */
@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {

    private final Logger log = LoggerFactory.getLogger(UserDataServiceImpl.class);

	//@Trifon - sequence
//	@Autowired
//	private IdentifierService identifierService;
//	@Autowired
//	private IdentifierMapper identifierMapper;
	//@Trifon
	@Autowired
	private UserDataServiceHelper userDataServiceHelper;

    private final UserDataRepository userDataRepository;

    private final UserDataMapper userDataMapper;

    private final UserRepository userRepository;

    public UserDataServiceImpl(UserDataRepository userDataRepository, UserDataMapper userDataMapper, UserRepository userRepository) {
        this.userDataRepository = userDataRepository;
        this.userDataMapper = userDataMapper;
        this.userRepository = userRepository;
    }

	//@Trifon - sequence
	public void setSequenceValue(UserData userData) {
		// @Trifon - set SequenceNumber if id is null or field contains <GENERATED>
	}

    /**
     * Save a userData.
     *
     * @param userDataDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserDataDTO save(UserDataDTO userDataDTO) {
        log.debug("Request to save UserDataDTO : {}", userDataDTO); //@Trifon
        UserData userData = userDataMapper.toEntity(userDataDTO);

//        setSequenceValue(userData); //@Trifon - sequence

        Long userId = userDataDTO.getUserId();
        userRepository.findById(userId).ifPresent(userData::user);
//@Trifon
//        userData = userDataRepository.save(userData);
		userData = save(userData); //@Trifon

        return userDataMapper.toDto(userData);
    }

	/**
	 * Save a userData.
	 *
	 * @param userDataDTO the entity to save.
	 * @return the persisted entity.
	 */
	@Override
	// @Trifon
	public UserData save(UserData userData) {
		log.debug("Request to save UserData : {}", userData);

		setSequenceValue(userData); //@Trifon - sequence

		boolean isNew = userData.getId() == null; //@Trifon
		// @Trifon - BEFORE save listeners
		userDataServiceHelper.beforeSave(userData, isNew);

		userData = userDataRepository.save(userData);
		// @Trifon - AFTER save listeners
		userDataServiceHelper.afterSave(userData, isNew);
		return userData;
	}

    /**
     * Get all the userData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserData");
        return userDataRepository.findAll(pageable)
            .map(userDataMapper::toDto);
    }


    /**
     * Get one userData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDataDTO> findOne(Long id) {
        log.debug("Request to get UserData by id: {}", id);
        return userDataRepository.findById(id)
            .map(userDataMapper::toDto);
    }

    /**
     * Delete the userData by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserData by Id: {}", id);
		userDataServiceHelper.beforeDelete(id); // userData - // @Trifon - BEFORE delete listeners

        userDataRepository.deleteById(id);

		userDataServiceHelper.afterDelete(id); // userData - // @Trifon - AFTER delete listeners
    }

	//@Trifon
	public Map<String, String> columnsToNameValueMap(String[] columns) {
		Map<String, String> result = new HashMap<>();

		if (columns.length > 0) {
			result.put("id", columns[0]);
		}
		if (columns.length > 1) {
			result.put("monetaryBalance", columns[1]);
		}

		return result;
	}


	//@Trifon - multi-unique
}
