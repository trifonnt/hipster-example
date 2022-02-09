package name.trifon.example.domain;

import java.math.BigDecimal;

//@Trifon
/**
 * A Builder for the UserData entity.
 */
public class UserDataBuilder {

    private BigDecimal monetaryBalance;

    private User user;

    public BigDecimal getMonetaryBalance() {
        return monetaryBalance;
    }

    public UserDataBuilder monetaryBalance(BigDecimal monetaryBalance) {
        this.monetaryBalance = monetaryBalance;
        return this;
    }

    public void setMonetaryBalance(BigDecimal monetaryBalance) {
        this.monetaryBalance = monetaryBalance;
    }


    public User getUser() {
        return user;
    }

    public UserDataBuilder user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public UserData build() {
		return new UserData()
			.monetaryBalance(this.getMonetaryBalance())
			.user(this.getUser())
		;
	}

    @Override
    public String toString() {
        return "UserDataBuilder{" +
            "  monetaryBalance=" + getMonetaryBalance() +
            ", user='" + getUser() + "'" +
            "}";
    }
}
