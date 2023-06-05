package org.demo.repository;

import org.demo.entity.CatEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CatRepository extends CrudRepository<CatEntity, Long> {

    List<CatEntity> findByGoesToTray(Boolean goesToTray);

}
