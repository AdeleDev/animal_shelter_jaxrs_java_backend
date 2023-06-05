package org.demo.repository;

import org.demo.entity.DogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DogRepository extends CrudRepository<DogEntity, Long> {

    List<DogEntity> findByTailDocked(Boolean tailDocked);
}
