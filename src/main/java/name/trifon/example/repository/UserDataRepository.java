package name.trifon.example.repository;

import name.trifon.example.domain.UserData;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param; //@Trifon - childSequence
import org.springframework.stereotype.Repository;
import java.util.Optional; //@Trifon

/**
 * Spring Data  repository for the UserData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long>, JpaSpecificationExecutor<UserData> {
// @Trifon -}
