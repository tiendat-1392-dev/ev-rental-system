package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.phone = :phone")
    User findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(Role role);
}
