package name.trifon.example.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link name.trifon.example.domain.UserData} entity.
 */
public class UserDataDTO implements Serializable {

	//@Trifon
	private static final long serialVersionUID = 1L;

//	private RecordSource recordSource;

//	private String code;

//	public String getCode() {
//		return code;
//	}
//	public void setCode(String code) {
//		this.code = code;
//	}



    
    private Long id;

    private BigDecimal monetaryBalance; //@Trifon


    private Long userId;

    private String userLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonetaryBalance() {
        return monetaryBalance;
    }

    public void setMonetaryBalance(BigDecimal monetaryBalance) {
        this.monetaryBalance = monetaryBalance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDataDTO)) {
            return false;
        }

        return id != null && id.equals(((UserDataDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDataDTO{" +
            "id=" + getId() +
            ", monetaryBalance=" + getMonetaryBalance() +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
