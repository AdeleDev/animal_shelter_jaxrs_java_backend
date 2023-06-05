package org.demo.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity(name = "pets")
@Inheritance(strategy = InheritanceType.JOINED)
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)

public class PetEntity extends BaseEntity {

    @Column(length = 30)
    @NotNull
    @Size(min = 1, max = 30)
    private String name;

    @Column(length = 3)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private PetEnum type;

    @Column(length = 6)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private GenderEnum gender;

    @NotNull
    private String kind;

    @Column(length = 3)
    @NotNull
    @Positive
    private int age;

    @Column(length = 3)
    @NotNull
    @Positive
    private int weight;

    @Column(length = 20)
    @NotNull
    private String color;

    @Column(length = 1)
    @NotNull
    private Boolean vaccinated;

    @Column(length = 1)
    @NotNull
    private Boolean castrated;

    @Column(length = 1)
    @NotNull
    private Boolean specialTreatment;

    @Column(length = 60)
    @NotNull
    private String story;

    @Column(length = 1)
    @NotNull
    private Boolean booked;

    @Column(length = 10)
    @PositiveOrZero
    @NotNull
    private Long donate;

    public PetEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetEnum getType() {
        return type;
    }

    public void setType(PetEnum type) {
        this.type = type;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(Boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public Boolean getCastrated() {
        return castrated;
    }

    public void setCastrated(Boolean castrated) {
        this.castrated = castrated;
    }

    public Boolean getSpecialTreatment() {
        return specialTreatment;
    }

    public void setSpecialTreatment(Boolean specialTreatment) {
        this.specialTreatment = specialTreatment;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Boolean getBooked() {
        return booked;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }

    public Long getDonate() {
        return donate;
    }

    public void setDonate(Long donate) {
        this.donate = donate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PetEntity)) {
            return false;
        }

        PetEntity petEntity = (PetEntity) o;

        if (getAge() != petEntity.getAge()) {
            return false;
        }
        if (getWeight() != petEntity.getWeight()) {
            return false;
        }
        if (!getName().equals(petEntity.getName())) {
            return false;
        }
        if (getType() != petEntity.getType()) {
            return false;
        }
        if (getGender() != petEntity.getGender()) {
            return false;
        }
        if (!getKind().equals(petEntity.getKind())) {
            return false;
        }
        if (!getColor().equals(petEntity.getColor())) {
            return false;
        }
        if (!getVaccinated().equals(petEntity.getVaccinated())) {
            return false;
        }
        if (!getCastrated().equals(petEntity.getCastrated())) {
            return false;
        }
        if (!getSpecialTreatment().equals(petEntity.getSpecialTreatment())) {
            return false;
        }
        if (!getStory().equals(petEntity.getStory())) {
            return false;
        }
        return getBooked().equals(petEntity.getBooked());
    }


    public enum PetEnum {
        CAT, DOG;

        private String value;

        PetEnum(String value) {
            this.value = value;
        }

        PetEnum() {

        }

        public String getValue() {
            return value;
        }
    }

    public enum GenderEnum {
        MALE, FEMALE;

        private Character value;

        GenderEnum(Character value) {
            this.value = value;
        }

        GenderEnum() {

        }

        public Character getValue() {
            return value;
        }
    }
}


