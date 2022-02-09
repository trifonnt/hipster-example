package name.trifon.example.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import name.trifon.example.web.rest.TestUtil;

public class UserDataDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDataDTO.class);
        UserDataDTO userDataDTO1 = new UserDataDTO();
        userDataDTO1.setId(1L);
        UserDataDTO userDataDTO2 = new UserDataDTO();
        assertThat(userDataDTO1).isNotEqualTo(userDataDTO2);
        userDataDTO2.setId(userDataDTO1.getId());
        assertThat(userDataDTO1).isEqualTo(userDataDTO2);
        userDataDTO2.setId(2L);
        assertThat(userDataDTO1).isNotEqualTo(userDataDTO2);
        userDataDTO1.setId(null);
        assertThat(userDataDTO1).isNotEqualTo(userDataDTO2);
    }
}
