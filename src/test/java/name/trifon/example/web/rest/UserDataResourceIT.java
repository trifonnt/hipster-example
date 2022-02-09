package name.trifon.example.web.rest;

import name.trifon.example.HipsterExampleApp;
import name.trifon.example.domain.UserData;
import name.trifon.example.domain.User;
import name.trifon.example.repository.UserDataRepository;
import name.trifon.example.service.UserDataService;
import name.trifon.example.service.dto.UserDataDTO;
import name.trifon.example.service.mapper.UserDataMapper;
import name.trifon.example.service.dto.UserDataCriteria;
import name.trifon.example.service.UserDataQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserDataResource} REST controller.
 */
@SpringBootTest(classes = HipsterExampleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserDataResourceIT {

    private static final BigDecimal DEFAULT_MONETARY_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONETARY_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONETARY_BALANCE = new BigDecimal(1 - 1);

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private UserDataQueryService userDataQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDataMockMvc;

    private UserData userData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserData createEntity(EntityManager em) {
        UserData userData = new UserData()
            .monetaryBalance(DEFAULT_MONETARY_BALANCE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userData.setUser(user);
        return userData;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserData createUpdatedEntity(EntityManager em) {
        UserData userData = new UserData()
            .monetaryBalance(UPDATED_MONETARY_BALANCE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userData.setUser(user);
        return userData;
    }

    @BeforeEach
    public void initTest() {
        userData = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserData() throws Exception {
        int databaseSizeBeforeCreate = userDataRepository.findAll().size();
        // Create the UserData
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);
        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isCreated());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeCreate + 1);
        UserData testUserData = userDataList.get(userDataList.size() - 1);
        assertThat(testUserData.getMonetaryBalance()).isEqualTo(DEFAULT_MONETARY_BALANCE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testUserData.getId()).isEqualTo(testUserData.getUser().getId());
    }

    @Test
    @Transactional
    public void createUserDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDataRepository.findAll().size();

        // Create the UserData with an existing ID
        userData.setId(1L);
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDataMockMvc.perform(post("/api/user-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updateUserDataMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);
        int databaseSizeBeforeCreate = userDataRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the userData
        UserData updatedUserData = userDataRepository.findById(userData.getId()).get();
        // Disconnect from session so that the updates on updatedUserData are not directly saved in db
        em.detach(updatedUserData);

        // Update the User with new association value
        updatedUserData.setUser(user);
        UserDataDTO updatedUserDataDTO = userDataMapper.toDto(updatedUserData);

        // Update the entity
        restUserDataMockMvc.perform(put("/api/user-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserDataDTO)))
            .andExpect(status().isOk());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeCreate);
        UserData testUserData = userDataList.get(userDataList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testUserData.getId()).isEqualTo(testUserData.getUser().getId());
    }

    @Test
    @Transactional
    public void getAllUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList
        restUserDataMockMvc.perform(get("/api/user-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userData.getId().intValue())))
            .andExpect(jsonPath("$.[*].monetaryBalance").value(hasItem(DEFAULT_MONETARY_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get the userData
        restUserDataMockMvc.perform(get("/api/user-data/{id}", userData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userData.getId().intValue()))
            .andExpect(jsonPath("$.monetaryBalance").value(DEFAULT_MONETARY_BALANCE.intValue()));
    }


    @Test
    @Transactional
    public void getUserDataByIdFiltering() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        Long id = userData.getId();

        defaultUserDataShouldBeFound("id.equals=" + id);
        defaultUserDataShouldNotBeFound("id.notEquals=" + id);

        defaultUserDataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserDataShouldNotBeFound("id.greaterThan=" + id);

        defaultUserDataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserDataShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance equals to DEFAULT_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.equals=" + DEFAULT_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance equals to UPDATED_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.equals=" + UPDATED_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance not equals to DEFAULT_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.notEquals=" + DEFAULT_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance not equals to UPDATED_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.notEquals=" + UPDATED_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance in DEFAULT_MONETARY_BALANCE or UPDATED_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.in=" + DEFAULT_MONETARY_BALANCE + "," + UPDATED_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance equals to UPDATED_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.in=" + UPDATED_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance is not null
        defaultUserDataShouldBeFound("monetaryBalance.specified=true");

        // Get all the userDataList where monetaryBalance is null
        defaultUserDataShouldNotBeFound("monetaryBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance is greater than or equal to DEFAULT_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.greaterThanOrEqual=" + DEFAULT_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance is greater than or equal to UPDATED_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.greaterThanOrEqual=" + UPDATED_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance is less than or equal to DEFAULT_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.lessThanOrEqual=" + DEFAULT_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance is less than or equal to SMALLER_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.lessThanOrEqual=" + SMALLER_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance is less than DEFAULT_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.lessThan=" + DEFAULT_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance is less than UPDATED_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.lessThan=" + UPDATED_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserDataByMonetaryBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        // Get all the userDataList where monetaryBalance is greater than DEFAULT_MONETARY_BALANCE
        defaultUserDataShouldNotBeFound("monetaryBalance.greaterThan=" + DEFAULT_MONETARY_BALANCE);

        // Get all the userDataList where monetaryBalance is greater than SMALLER_MONETARY_BALANCE
        defaultUserDataShouldBeFound("monetaryBalance.greaterThan=" + SMALLER_MONETARY_BALANCE);
    }


    @Test
    @Transactional
    public void getAllUserDataByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userData.getUser();
        userDataRepository.saveAndFlush(userData);
        Long userId = user.getId();

        // Get all the userDataList where user equals to userId
        defaultUserDataShouldBeFound("userId.equals=" + userId);

        // Get all the userDataList where user equals to userId + 1
        defaultUserDataShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserDataShouldBeFound(String filter) throws Exception {
        restUserDataMockMvc.perform(get("/api/user-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userData.getId().intValue())))
            .andExpect(jsonPath("$.[*].monetaryBalance").value(hasItem(DEFAULT_MONETARY_BALANCE.intValue())));

        // Check, that the count call also returns 1
        restUserDataMockMvc.perform(get("/api/user-data/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserDataShouldNotBeFound(String filter) throws Exception {
        restUserDataMockMvc.perform(get("/api/user-data?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserDataMockMvc.perform(get("/api/user-data/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserData() throws Exception {
        // Get the userData
        restUserDataMockMvc.perform(get("/api/user-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        int databaseSizeBeforeUpdate = userDataRepository.findAll().size();

        // Update the userData
        UserData updatedUserData = userDataRepository.findById(userData.getId()).get();
        // Disconnect from session so that the updates on updatedUserData are not directly saved in db
        em.detach(updatedUserData);
        updatedUserData
            .monetaryBalance(UPDATED_MONETARY_BALANCE);
        UserDataDTO userDataDTO = userDataMapper.toDto(updatedUserData);

        restUserDataMockMvc.perform(put("/api/user-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isOk());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeUpdate);
        UserData testUserData = userDataList.get(userDataList.size() - 1);
        assertThat(testUserData.getMonetaryBalance()).isEqualTo(UPDATED_MONETARY_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserData() throws Exception {
        int databaseSizeBeforeUpdate = userDataRepository.findAll().size();

        // Create the UserData
        UserDataDTO userDataDTO = userDataMapper.toDto(userData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDataMockMvc.perform(put("/api/user-data")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserData in the database
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserData() throws Exception {
        // Initialize the database
        userDataRepository.saveAndFlush(userData);

        int databaseSizeBeforeDelete = userDataRepository.findAll().size();

        // Delete the userData
        restUserDataMockMvc.perform(delete("/api/user-data/{id}", userData.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserData> userDataList = userDataRepository.findAll();
        assertThat(userDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
