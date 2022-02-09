package name.trifon.example.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDataMapperTest {

    private UserDataMapper userDataMapper;

    @BeforeEach
    public void setUp() {
        userDataMapper = new UserDataMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(userDataMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userDataMapper.fromId(null)).isNull();
    }
}
