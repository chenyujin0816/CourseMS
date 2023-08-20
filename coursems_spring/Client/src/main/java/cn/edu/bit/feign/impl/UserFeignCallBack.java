package cn.edu.bit.feign.impl;

import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.feign.CourseFeign;
import cn.edu.bit.feign.UserFeign;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFeignCallBack implements UserFeign {

    @Override
    public String index() {
        return "服务维护中，请稍后。。。";
    }

    @Override
    public Response login(LoginDto loginDto) {
        return null;
    }

    @Override
    public Result checkLogin(LoginDto loginDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result changePassword(ChangePasswordDto changePasswordDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result register(RegisterDto registerDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result logout() {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result confirmStatus(ConfirmDto confirmDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result confirmLoginTime(LoginTimeDto loginTimeDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public void setLoginTime(LoginTimeDto loginTimeDto) {

    }

    @Override
    public void setLoginStatus(LoginStatusDto loginStatusDto) {

    }

    @Override
    public Result getStatus(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getName(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getType(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result agreeReg(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result denyReg(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result freeze(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result unfreeze(Long userId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result regApplies(Integer currentPage) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result userList(Integer currentPage) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result onlineData(Integer currentPage) {
        return Result.fail("服务维护中，请稍后。。。");
    }
}
