package com.oceanprotocol.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.oceanprotocol.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	User findByEmailAndPassword(String email,String password);

	User findByEmail(String email);
}
