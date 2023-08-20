package cn.edu.bit.controller;

import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.feign.UserFeign;
import cn.edu.bit.utils.ConvertUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@RestController
//@RequestMapping("/client")
public class UserController {

    @Autowired
    UserFeign feignService;

    @PostMapping("/user/login")
    public Result login(@RequestBody LoginDto loginDto, HttpServletResponse response) throws IOException {
        Result feignResult = feignService.checkLogin(loginDto);
        if (feignResult.getCode()==200) {
            Response feignResponse = feignService.login(loginDto);
            ConvertUtil.feignResp2ServletResp(feignResponse, response);
            return null;
        }
        else return feignResult;
    }

    @PostMapping("/user/changePassword")
    public Result changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        return feignService.changePassword(changePasswordDto);
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody RegisterDto registerDto){
        return feignService.register(registerDto);
    }

    @GetMapping("/user/logout")
    public Result logout(){
        return feignService.logout();
    }

    @PostMapping("/user/confirmStatus")
    public Result confirmStatus(@RequestBody ConfirmDto confirmDto){
        return feignService.confirmStatus(confirmDto);
    }

    @PostMapping("/user/confirmLoginTime")
    public Result confirmLoginTime(@RequestBody LoginTimeDto loginTimeDto){
        return feignService.confirmLoginTime(loginTimeDto);
    }

    @PostMapping("/user/setLoginTime")
    public void setLoginTime(@RequestBody LoginTimeDto loginTimeDto){
        feignService.setLoginTime(loginTimeDto);
    }

    @PostMapping("/user/setLoginStatus")
    public void setLoginStatus(@RequestBody LoginStatusDto loginStatusDto){
        feignService.setLoginStatus(loginStatusDto);
    }

    @GetMapping("/user/getStatus")
    public Result getStatus(@RequestParam("userId") Long userId){
        return feignService.getStatus(userId);
    }

    @GetMapping("/user/getName")
    public Result getName(@RequestParam("userId") Long userId){
        return feignService.getName(userId);
    }

    @GetMapping("/user/getType")
    public Result getType(@RequestParam("userId") Long userId){
        return feignService.getType(userId);
    }

    @GetMapping("/user/agreeReg")
    public Result agreeReg(@RequestParam("userId") Long userId){
        return feignService.agreeReg(userId);
    }

    @GetMapping("/user/denyReg")
    public Result denyReg(@RequestParam("userId") Long userId){
        return feignService.denyReg(userId);
    }

    @GetMapping("/user/freeze")
    public Result freeze(@RequestParam("userId") Long userId){
        return feignService.freeze(userId);
    }

    @GetMapping("/user/unfreeze")
    public Result unfreeze(@RequestParam("userId") Long userId){
        return feignService.unfreeze(userId);
    }

    @GetMapping("/user/regApplies")
    public Result regApplies(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage){
        return feignService.regApplies(currentPage);
    }

    @GetMapping("/user/userList")
    public Result userList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage){
        return feignService.userList(currentPage);
    }

    @GetMapping("/user/onlineData")
    public Result onlineData(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage){
        return feignService.onlineData(currentPage);
    }
}
