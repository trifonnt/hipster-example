package name.trifon.example.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import name.trifon.example.domain.UserData;
import name.trifon.example.domain.*; // for static metamodels
import name.trifon.example.repository.UserDataRepository;
import name.trifon.example.service.dto.UserDataCriteria;
import name.trifon.example.service.dto.UserDataDTO;
import name.trifon.example.service.mapper.UserDataMapper;

/**
 * Service for executing complex queries for {@link UserData} entities in the database.
 * The main input is a {@link UserDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserDataDTO} or a {@link Page} of {@link UserDataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserDataQueryService extends QueryService<UserData> {

    private final Logger log = LoggerFactory.getLogger(UserDataQueryService.class);

    private final UserDataRepository userDataRepository;

    private final UserDataMapper userDataMapper;

    public UserDataQueryService(UserDataRepository userDataRepository, UserDataMapper userDataMapper) {
        this.userDataRepository = userDataRepository;
        this.userDataMapper = userDataMapper;
    }

    /**
     * Return a {@link List} of {@link UserDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDataDTO> findByCriteria(UserDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserData> specification = createSpecification(criteria);
        return userDataMapper.toDto(userDataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserDataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDataDTO> findByCriteria(UserDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserData> specification = createSpecification(criteria);
        return userDataRepository.findAll(specification, page)
            .map(userDataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserData> specification = createSpecification(criteria);
        return userDataRepository.count(specification);
    }

    /**
     * Function to convert {@link UserDataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserData> createSpecification(UserDataCriteria criteria) {
        Specification<UserData> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserData_.id));
            }
            if (criteria.getMonetaryBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonetaryBalance(), UserData_.monetaryBalance));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserData_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
