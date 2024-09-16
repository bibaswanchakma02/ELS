package project7.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project7.userservice.model.UserRoles;

import java.util.Optional;
import java.util.UUID;

public interface UserRolesRepository extends MongoRepository<UserRoles, UUID> {

    Optional<UserRoles> findByUserId(UUID userId);
}
