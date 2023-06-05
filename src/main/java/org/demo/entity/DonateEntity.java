package org.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "donate")
@Table
public class DonateEntity extends BaseEntity {

    @NotNull
    private long userId;


    @NotNull
    private long petId;

    @NotNull
    private long sum;

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonateEntity)) {
            return false;
        }

        DonateEntity that = (DonateEntity) o;

        if (getUserId() != that.getUserId()) {
            return false;
        }
        if (getPetId() != that.getPetId()) {
            return false;
        }
        return getSum() == that.getSum();
    }

    @Override
    public int hashCode() {
        int result = (int) (getUserId() ^ (getUserId() >>> 32));
        result = 31 * result + (int) (getPetId() ^ (getPetId() >>> 32));
        result = 31 * result + (int) (getSum() ^ (getSum() >>> 32));
        return result;
    }

}
