package com.project.evm.models.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.evm.models.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    @Query(value = "SELECT COUNT(*) FROM users",nativeQuery = true)
    public Long getCount();

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE name = :username) AS user_exists",nativeQuery = true)
    public boolean existsByUsername(@Param("username")String username);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)AS user_exists",nativeQuery = true)
    public boolean existsByEmail(@Param("email")String email);

    @Query(value = "SELECT password FROM users WHERE name = :username",nativeQuery = true)
    public String getPasswordByUsername(@Param("username")String username);
    
    @Query(value = "SELECT id,password FROM users WHERE name = :username",nativeQuery = true)
    public User getPasswordByUsernameWithID(@Param("username")String username);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE name = :username OR email = :email)",nativeQuery = true)
    public boolean userExists(@Param("username")String username,@Param("email")String email);

    @Query(value = "SELECT name,email,description FROM users u WHERE u.id = :id",nativeQuery = true)
    public Optional<User> getUserById(@Param("id")Long id);
} 
