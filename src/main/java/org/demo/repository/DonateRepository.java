package org.demo.repository;

import org.demo.entity.DonateEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DonateRepository extends CrudRepository<DonateEntity, Long> {

    List<DonateEntity> findByUserId(long userId);

    List<DonateEntity> findByPetId(long petId);
}
