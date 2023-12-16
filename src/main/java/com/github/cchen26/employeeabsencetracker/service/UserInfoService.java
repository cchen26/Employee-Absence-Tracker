package com.github.cchen26.employeeabsencetracker.service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import com.github.cchen26.employeeabsencetracker.repository.UserInfoRepository;

@Service(value = "userInfoService")
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserInfoService(UserInfoRepository userInfoRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<UserInfo> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return this.findUserByEmail(authentication.getName());
    }

    public Optional<UserInfo> findUserByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    public void saveUser(UserInfo userInfo) {
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        userInfo.setActive(false);
        userInfoRepository.save(userInfo);
    }

    public List<UserInfo> getUsers() {
        return userInfoRepository.findAllByOrderById();
    }

    public Optional<UserInfo> getUserById(Long id) {
        return userInfoRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userInfoRepository.deleteById(id);
    }

    public void blockUser(Long id) {
        userInfoRepository.blockUser(id);
    }

    public void unBlockUser(Long id) {
        userInfoRepository.unBlockUser(id);
    }
}
