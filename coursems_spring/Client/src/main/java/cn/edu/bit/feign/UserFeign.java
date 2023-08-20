package cn.edu.bit.feign;

import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.feign.impl.UserFeignCallBack;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service",fallback = UserFeignCallBack.class)
public interface UserFeign {

    @GetMapping("/index")
    String index();

    @PostMapping("/user/login")
    Response login(@RequestBody LoginDto loginDto);

    @PostMapping("/user/checkLogin")
    Result checkLogin(@RequestBody LoginDto loginDto);

    @PostMapping("/user/changePassword")
    Result changePassword(@RequestBody ChangePasswordDto changePasswordDto);

    @PostMapping("/user/register")
    Result register(@RequestBody RegisterDto registerDto);

    @GetMapping("/user/logout")
    Result logout();

    @PostMapping("/user/confirmStatus")
    Result confirmStatus(@RequestBody ConfirmDto confirmDto);

    @PostMapping("/user/confirmLoginTime")
    Result confirmLoginTime(@RequestBody LoginTimeDto loginTimeDto);

    @PostMapping("/user/setLoginTime")
    void setLoginTime(@RequestBody LoginTimeDto loginTimeDto);

    @PostMapping("/user/setLoginStatus")
    void setLoginStatus(@RequestBody LoginStatusDto loginStatusDto);

    @GetMapping("/user/getStatus")
    Result getStatus(@RequestParam("userId") Long userId);

    @GetMapping("/user/getName")
    Result getName(@RequestParam("userId") Long userId);

    @GetMapping("/user/getType")
    Result getType(@RequestParam("userId") Long userId);

    @GetMapping("/user/agreeReg")
    Result agreeReg(@RequestParam("userId") Long userId);

    @GetMapping("/user/denyReg")
    Result denyReg(@RequestParam("userId") Long userId);

    @GetMapping("/user/freeze")
    Result freeze(@RequestParam("userId") Long userId);

    @GetMapping("/user/unfreeze")
    Result unfreeze(@RequestParam("userId") Long userId);

    @GetMapping("/user/regApplies")
    Result regApplies(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage);

    @GetMapping("/user/userList")
    Result userList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage);

    @GetMapping("/user/onlineData")
    Result onlineData(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage);
}
