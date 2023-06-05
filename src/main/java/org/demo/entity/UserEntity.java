package org.demo.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity(name = "users")
@Table
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)

public class UserEntity extends BaseEntity {

    @Column(length = 30)
    @NotNull
    @Size(min = 1, max = 30)
    private String firstName;

    @Column(length = 30)
    @NotNull
    @Size(min = 1, max = 30)
    private String lastName;

    @Column(length = 40)
    @NotNull
    @Size(min = 1, max = 40)
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address in format txt@post.ru")
    private String email;

    @Column(length = 15)
    @Size(max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "groups_id")
    @Type(type = "pgsql_enum")
    private GroupEntity groups;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GroupEntity getGroups() {
        return groups;
    }

    public void setGroups(GroupEntity groups) {
        this.groups = groups;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }

        UserEntity that = (UserEntity) o;

        if (!getFirstName().equals(that.getFirstName())) {
            return false;
        }
        if (!getLastName().equals(that.getLastName())) {
            return false;
        }
        if (!getEmail().equals(that.getEmail())) {
            return false;
        }
        if (getPhoneNumber() != null
                ? !getPhoneNumber().equals(that.getPhoneNumber())
                : that.getPhoneNumber() != null) {
            return false;
        }
        return getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phoneNumber, password);
    }
}
