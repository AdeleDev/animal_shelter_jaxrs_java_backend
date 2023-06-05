package org.demo.repository;

import org.demo.entity.PetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends CrudRepository<PetEntity, Long>, QueryByExampleExecutor<PetEntity> {

    List<PetEntity> findByName(String name);

    PetEntity findByNameAndKind(String name, String kind);

    Boolean existsByNameAndKind(String name, String kind);

    List<PetEntity> findByType(PetEntity.PetEnum type);

    List<PetEntity> findByBooked(Boolean isBooked);

}
