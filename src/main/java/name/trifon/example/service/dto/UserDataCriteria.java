package name.trifon.example.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter; //@Trifon

/**
 * Criteria class for the {@link name.trifon.example.domain.UserData} entity. This class is used
 * in {@link name.trifon.example.web.rest.UserDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserDataCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter monetaryBalance;

    private LongFilter userId;

    public UserDataCriteria() {
    }

    public UserDataCriteria(UserDataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.monetaryBalance = other.monetaryBalance == null ? null : other.monetaryBalance.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserDataCriteria copy() {
        return new UserDataCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getMonetaryBalance() {
        return monetaryBalance;
    }

    public void setMonetaryBalance(BigDecimalFilter monetaryBalance) {
        this.monetaryBalance = monetaryBalance;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserDataCriteria that = (UserDataCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(monetaryBalance, that.monetaryBalance) &&
            Objects.equals(userId, that.userId)
          ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        monetaryBalance,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDataCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (monetaryBalance != null ? "monetaryBalance=" + monetaryBalance + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
