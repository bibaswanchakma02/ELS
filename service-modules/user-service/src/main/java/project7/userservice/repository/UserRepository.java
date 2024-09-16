package project7.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project7.userservice.model.UserModel;

import java.util.Optional;


public interface UserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByVerificationToken(String token);
}
