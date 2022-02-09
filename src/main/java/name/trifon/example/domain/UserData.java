package name.trifon.example.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

import name.trifon.example.domain.util.AttributeEncryptor; //@Trifon

/**
 * A UserData.
 */
@Entity
@Table(name = "user_data", uniqueConstraints = {@UniqueConstraint(columnNames={})})
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

//	private RecordSource recordSource; //@Trifon

	private String code;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@PrePersist
	public void prePersist() {
		if (code == null || code.isEmpty()) {
			code = java.util.UUID.randomUUID().toString();
		}
	}


    @Column(name = "monetary_balance", precision = 21, scale = 2)
    private BigDecimal monetaryBalance; //@Trifon


	@PreRemove
	private void preRemove() {
	}

    @OneToOne(optional = false)
    @NotNull

    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonetaryBalance() {
        return monetaryBalance;
    }

    public UserData monetaryBalance(BigDecimal monetaryBalance) {
        this.monetaryBalance = monetaryBalance;
        return this;
    }

    public void setMonetaryBalance(BigDecimal monetaryBalance) {
        this.monetaryBalance = monetaryBalance;
    }

    public User getUser() {
        return user;
    }

    public UserData user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserData)) {
            return false;
        }
        return id != null && id.equals(((UserData) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserData{" +
            "id=" + getId() +
            ", monetaryBalance=" + getMonetaryBalance() +
            "}";
    }
}
