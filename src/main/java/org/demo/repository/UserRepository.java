package org.demo.repository;

import org.demo.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Boolean existsByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);
}
