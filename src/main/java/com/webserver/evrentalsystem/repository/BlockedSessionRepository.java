package com.webserver.evrentalsystem.repository;

import com.webserver.evrentalsystem.entity.BlockedSession;
import com.webserver.evrentalsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedSessionRepository extends JpaRepository<BlockedSession, Long> {

    @Query("SELECT bs FROM BlockedSession bs WHERE bs.blockedUser = :user AND bs.isUnblocked = false")
    BlockedSession findAndCheckIfUserIsBlocked(User user);

    @Query("SELECT CASE WHEN COUNT(bs) > 0 THEN true ELSE false END FROM BlockedSession bs WHERE bs.blockedUser.userName = :userName AND bs.isUnblocked = false")
    boolean isBlocked(String userName);
}
