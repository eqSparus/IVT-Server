package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.DynamicProxyable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.User;

import java.util.Optional;
import java.util.UUID;

@Collection("user-records")
@Repository
public interface UserRepository extends CouchbaseRepository<User, UUID>, DynamicProxyable<UserRepository> {

    Optional<User> findByEmail(@NonNull String email);

}
