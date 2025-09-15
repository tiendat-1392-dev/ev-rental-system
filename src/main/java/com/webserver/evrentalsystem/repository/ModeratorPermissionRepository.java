package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.ModeratorPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorPermissionRepository extends JpaRepository<ModeratorPermission, String> {
    @Query("SELECT COUNT(p) > 0 FROM ModeratorPermission p WHERE p.userName = :userName AND p.permission = :permissionId")
    boolean isHasPermission(String userName, String permissionId);

    @Modifying
    @Query("DELETE FROM ModeratorPermission p WHERE p.userName = :userName AND p.permission = :permissionId")
    void revokePermission(String userName, String permissionId);
}
