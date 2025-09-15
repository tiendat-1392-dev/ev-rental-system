package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    User findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.userName LIKE %:searchTerm%")
    Page<User> findAllByRoleIn(Role role, String searchTerm, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(Role role);
}
