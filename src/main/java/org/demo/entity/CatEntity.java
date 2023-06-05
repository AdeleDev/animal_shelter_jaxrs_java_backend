package org.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "cats")
@Table
public class CatEntity extends PetEntity {

    @Column(length = 1)
    @NotNull
    private Boolean goesToTray;

    public Boolean getGoesToTray() {
        return goesToTray;
    }

    public void setGoesToTray(Boolean goesToTray) {
        this.goesToTray = goesToTray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatEntity)) {
            return false;
        }

        CatEntity catEntity = (CatEntity) o;

        return getGoesToTray().equals(catEntity.getGoesToTray());
    }

    @Override
    public int hashCode() {
        return getGoesToTray().hashCode();
    }
}
