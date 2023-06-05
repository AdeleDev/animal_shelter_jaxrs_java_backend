package org.demo.repository;

import org.demo.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    GroupEntity findByType(GroupEntity.GroupEnum type);
}
