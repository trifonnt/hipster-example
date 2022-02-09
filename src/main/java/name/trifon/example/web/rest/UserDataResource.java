package name.trifon.example.web.rest;

import name.trifon.example.service.UserDataService;
import name.trifon.example.web.rest.errors.BadRequestAlertException;
import name.trifon.example.web.rest.errors.EntityNotFoundException; //@Trifon
import name.trifon.example.web.rest.common.JsonPatcher; //@Trifon
import name.trifon.example.web.rest.common.RestMediaType; //@Trifon
import name.trifon.example.service.dto.IdArrayDTO; //@Trifon
import com.github.fge.jsonpatch.JsonPatchException; //@Trifon
import io.swagger.annotations.Api; //@Trifon
import name.trifon.example.service.dto.*; //@Trifon
import name.trifon.example.service.dto.UserDataDTO;
import name.trifon.example.service.dto.UserDataCriteria;
import name.trifon.example.service.UserDataQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.MediaType; //@Trifon
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired; //@Trifon
import org.springframework.web.multipart.MultipartFile; //@Trifon
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader; //@Trifon
import java.io.InputStreamReader; //@Trifon
import java.io.IOException; //@Trifon

import javax.validation.Valid; //@Trifon
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map; //@Trifon
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link name.trifon.example.domain.UserData}.
 */
@RestController
@RequestMapping("/api")
@Api(tags={"user-data"}) //@Trifon
public class UserDataResource {

    private final Logger log = LoggerFactory.getLogger(UserDataResource.class);

	//@Trifon
	@Autowired
	private JsonPatcher jsonPatcher;

    public static final String ENTITY_NAME = "userData"; //@Trifon

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDataService userDataService;

    private final UserDataQueryService userDataQueryService;

    public UserDataResource(UserDataService userDataService, UserDataQueryService userDataQueryService) {
        this.userDataService = userDataService;
        this.userDataQueryService = userDataQueryService;
    }

    /**
     * {@code POST  /user-data} : Create a new userData.
     *
     * @param userDataDTO the userDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDataDTO, or with status {@code 400 (Bad Request)} if the userData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-data")
    public ResponseEntity<UserDataDTO> createUserData(@Valid @RequestBody UserDataDTO userDataDTO) throws URISyntaxException {
        log.debug("REST request to save UserData : {}", userDataDTO);
        if (userDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new userData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(userDataDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        UserDataDTO result = userDataService.save(userDataDTO);
        return ResponseEntity.created(new URI("/api/user-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-data} : Updates an existing userData.
     *
     * @param userDataDTO the userDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDataDTO,
     * or with status {@code 400 (Bad Request)} if the userDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-data")
    public ResponseEntity<UserDataDTO> updateUserData(@Valid @RequestBody UserDataDTO userDataDTO) throws URISyntaxException {
        log.debug("REST request to update UserData : {}", userDataDTO);
        if (userDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserDataDTO result = userDataService.save(userDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDataDTO.getId().toString()))
            .body(result);
    }

    // @Trifon
    // JSON PATCH - V3
    // echo '[{ "op": "replace", "path": "/description", "value": "Patched Description" }]' | http PATCH :10000/api/user-data/1 Content-Type:application/json-patch+json
    /**
     * PATCH  /user-data/:id : Patches an existing userData.
     *
     * @param userDataDTO the userDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userDataDTO,
     * or with status 400 (Bad Request) if the userDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the userDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/user-data/{id}", 
    	consumes = RestMediaType.APPLICATION_PATCH_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDataDTO> partialUpdateUserData(@PathVariable Long id, @RequestBody String patchCommand) {
        log.debug("REST request to PATCH UserData : {}", id);

		UserDataDTO existingUserDataDTO = userDataService.findOne(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
		UserDataDTO userDataDTO = null;
		try {
			userDataDTO = (UserDataDTO)jsonPatcher.patch(patchCommand, existingUserDataDTO).get();
		} catch (RuntimeException ex) {
			if (JsonPatchException.class.isAssignableFrom(ex.getCause().getClass())) {
				log.error(ex.getMessage());
			}
		}

        UserDataDTO result = userDataService.save(userDataDTO);
		return ResponseEntity.ok()
			.headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
			.body(result);
	}
    /**
     * {@code GET  /user-data} : get all the userData.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userData in body.
     */
    @GetMapping("/user-data")
    public ResponseEntity<List<UserDataDTO>> getAllUserData(UserDataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserData by criteria: {}", criteria);
        Page<UserDataDTO> page = userDataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-data/count} : count all the userData.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-data/count")
    public ResponseEntity<Long> countUserData(UserDataCriteria criteria) {
        log.debug("REST request to count UserData by criteria: {}", criteria);
        return ResponseEntity.ok().body(userDataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-data/:id} : get the "id" userData.
     *
     * @param id the id of the userDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-data/{id}")
    public ResponseEntity<UserDataDTO> getUserData(@PathVariable Long id) {
        log.debug("REST request to get UserData : {}", id);
        Optional<UserDataDTO> userDataDTO = userDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDataDTO);
    }

	/**
	 * GET  /user-data/template : get the Template JSON for userData.
	 *
	 * @return the ResponseEntity with status 200 (OK) and with body the userDataDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/user-data/template")
	public ResponseEntity<UserDataDTO> getTemplateUserData() {
		log.debug("REST request to get Template for UserData");

		UserDataDTO userDataDTO = new UserDataDTO();
		// TODO - Fields which are sequence controlled should be with <GENERATED>
		if (userDataDTO.getValue() == null || userDataDTO.getValue().isEmpty()) {
			userDataDTO.setValue("<GENERATED>");
		}

		return ResponseEntity.ok().body(userDataDTO);
	}

    /**
     * {@code DELETE  /user-data/:id} : delete the "id" userData.
     *
     * @param id the id of the userDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-data/{id}")
    public ResponseEntity<Void> deleteUserData(@PathVariable Long id) {
        log.debug("REST request to delete UserData : {}", id);
        userDataService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

	//@Trifon
    /**
     * DELETE  /user-data/ : delete list of userData.
     *
     * @param IdArrayDTO the list with UserData ids to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    //   Delete multiple records using REST
    // - https://stackoverflow.com/questions/21863326/delete-multiple-records-using-rest
    @DeleteMapping("/user-data")
    public ResponseEntity<Void> deleteMultipleUserData(@Valid @RequestBody IdArrayDTO idArrayDTO) {
        log.debug("REST request to delete MULTIPLE UserData : {}", idArrayDTO);
        for (Long id : idArrayDTO.getIds()) {
            userDataService.delete(id);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, idArrayDTO.getIds().toString())).build();
    }


}
