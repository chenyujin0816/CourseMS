package cn.edu.bit.feign.impl;

import cn.edu.bit.entity.User;
import cn.edu.bit.feign.FeignService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeignServiceCallBack implements FeignService {
    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isTeacher() {
        return false;
    }

    @Override
    public int getCurUserId() {
        return 0;
    }

    @Override
    public char getCurUserType() {
        return '0';
    }

    @Override
    public String getCurName() {
        return null;
    }

    @Override
    public String getCurUsername() {
        return null;
    }

    @Override
    public Long getUserIdByName(String name) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public List<User> getAllStudent() {
        return null;
    }
}
