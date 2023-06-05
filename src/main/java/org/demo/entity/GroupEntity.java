package org.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "groups")
@Table
public class GroupEntity {

    @Id
    private Long id;

    @Column(length = 5)
    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupEnum type;

    public GroupEntity() {

    }

    public GroupEntity(GroupEnum groupEnum) {
        this.type = groupEnum;

    }

    public GroupEnum getType() {
        return type;
    }

    public void setType(GroupEnum type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupEntity)) {
            return false;
        }

        GroupEntity that = (GroupEntity) o;

        if (!getId().equals(that.getId())) {
            return false;
        }
        return getType() == that.getType();
    }


    public enum GroupEnum {
        ADMIN, GUEST;

        private String value;

        GroupEnum(String value) {
            this.value = value;
        }

        GroupEnum() {

        }

        public String getValue() {
            return value;
        }
    }
}
