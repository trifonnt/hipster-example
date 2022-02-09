package name.trifon.example.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import name.trifon.example.domain.UserData;
import name.trifon.example.repository.UserDataRepository;

@Service
public class UserDataServiceHelper {

	private final Logger log = LoggerFactory.getLogger(UserDataServiceHelper.class);

	@SuppressWarnings("unused")
	@Autowired
	private UserDataRepository userDataRepository;


	public void beforeSave(UserData userData, boolean isNew) {
		log.debug("BEFORE save UserData.id: {}; isNew: {}", userData.getId(), isNew);
	}

	public void afterSave(UserData userData, boolean isNew) {
		log.debug("AFTER save UserData.id: {}; isNew: {}", userData.getId(), isNew);
	}

	public void beforeDelete(Long id) {
		log.debug("BEFORE delete UserData.id: {}", id);
	}

	public void afterDelete(Long id) {
		log.debug("AFTER delete UserData.id: {}", id);
	}

}
