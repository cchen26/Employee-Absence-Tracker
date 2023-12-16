package com.github.cchen26.employeeabsencetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmail(String email);

    List<UserInfo> findAllByOrderById();

    Optional<UserInfo> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserInfo u SET u.active = false WHERE u.id = :id")
    void blockUser(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserInfo u SET u.active = true WHERE u.id = :id")
    void unBlockUser(Long id);
}
