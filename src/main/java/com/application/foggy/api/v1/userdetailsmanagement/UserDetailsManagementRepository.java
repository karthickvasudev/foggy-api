package com.application.foggy.api.v1.userdetailsmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsManagementRepository extends MongoRepository<UserDetailsManagement, String> {
    Optional<UserDetailsManagement> findByEmailAndId(String email, String id);

    Optional<UserDetailsManagement> findByEmail(String email);
}
