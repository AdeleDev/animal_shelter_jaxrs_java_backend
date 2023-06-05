package org.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "dogs")
@Table
public class DogEntity extends PetEntity {

    @Column(length = 1)
    @NotNull
    private Boolean tailDocked;

    public Boolean getTailDocked() {
        return tailDocked;
    }

    public void setTailDocked(Boolean tailDocked) {
        this.tailDocked = tailDocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DogEntity)) {
            return false;
        }

        DogEntity dogEntity = (DogEntity) o;

        return getTailDocked().equals(dogEntity.getTailDocked());
    }

    @Override
    public int hashCode() {
        return getTailDocked().hashCode();
    }
}
