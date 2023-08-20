package cn.edu.bit.controller;


import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.User;
import cn.edu.bit.service.UserService;
import cn.edu.bit.utils.CheckPwdStr;
import cn.edu.bit.utils.JwtUtils;
import cn.edu.bit.utils.Md5Util;
import cn.edu.bit.utils.ShiroUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Value("${server.port}")
    String port;

    @GetMapping("/test")
    public String test(){
        return "Hello world!";
    }

    @GetMapping("/index")
    public String index(){
        return this.port;
    }

    @PostMapping("/checkLogin")
    public Result checkLogin(@Validated @RequestBody LoginDto loginDto){
        User user = this.getUserByUsername(loginDto.getUsername());
        if (user == null)
            return Result.fail("用户不存在");
        if (!user.getPassword().equals(Md5Util.getMD5String(loginDto.getPassword())))
            return Result.fail("密码不正确");
        if(user.getStatus()=='0')
            return Result.fail("用户被冻结");
        if(user.getStatus()=='2')
            return Result.fail("用户待审核");
        return Result.succ("allowed.");
    }

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = this.getUserByUsername(loginDto.getUsername());

        if (user == null){
            return Result.fail("用户不存在");
        }
        if (!user.getPassword().equals(Md5Util.getMD5String(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        if(user.getStatus()=='0'){
            return Result.fail("用户被冻结");
        }
        if(user.getStatus()=='2'){
            return Result.fail("用户待审核");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("name", user.getName())
                .put("type", user.getType())
                .map()
        );
    }

    @RequiresAuthentication
    @PostMapping("/changePassword")
    public Result changePassword(@Validated @RequestBody ChangePasswordDto changePasswordDto) {

        User user = userService.getOne(new QueryWrapper<User>().eq("id", changePasswordDto.getId()));
        int pwdStr = CheckPwdStr.getPwdStrength(changePasswordDto.getPassword());
        if (pwdStr < 0)
            return Result.fail("密码长度不合适");
        else if (pwdStr < 1)
            return Result.fail("密码强度弱");

        user.setPassword(Md5Util.getMD5String(changePasswordDto.getPassword()));
        userService.update(user,new QueryWrapper<User>().eq("id", user.getId()));
        return Result.succ(MapUtil.builder()
                .put("password", changePasswordDto.getPassword())
                .map()
        );
    }

    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto registerDto) {

        User user = userService.getOne(new QueryWrapper<User>().eq("username", registerDto.getUsername()));
        if(user!=null)
            return Result.fail("用户已存在");

        if (!registerDto.getPassword().equals(registerDto.getConfirmPwd()))
            return Result.fail("两次密码不一致");
        //密码强度校验
        int pwdStr = CheckPwdStr.getPwdStrength(registerDto.getPassword());
        if (pwdStr < 0)
            return Result.fail("密码长度不合适");
        else if (pwdStr < 1)
            return Result.fail("密码强度弱");

        User newUser = new User();
        newUser.setUsername(registerDto.getUsername());
        newUser.setPassword(Md5Util.getMD5String(registerDto.getPassword()));
        newUser.setName(registerDto.getName());

        newUser.setType(registerDto.getType());
//        if(registerDto.getType()=='1')
//            newUser.setClassNumber(registerDto.getClassNumber());
        newUser.setStatus('2');
        newUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userService.save(newUser);

        return Result.succ(MapUtil.builder()
                .put("username", registerDto.getUsername())
                .put("password", registerDto.getPassword())
                .put("type", registerDto.getType())
                .map()
        );
    }

    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ("logged out.");
    }

    //根据id返回状态
    @PostMapping("/confirmStatus")
    public Result confirmStatus(@Validated @RequestBody ConfirmDto confirmDto){
        User user = userService.getOne(new QueryWrapper<User>().eq("id",confirmDto.getId()));
        return Result.succ(user.getStatus());

    }

    //根据id返回最后登录时间
    @PostMapping("/confirmLoginTime")
    public Result confirmLoginTime(@Validated @RequestBody LoginTimeDto loginTimeDto){
        User user = userService.getOne(new QueryWrapper<User>().eq("id",loginTimeDto.getId()));

        return Result.succ(user.getTime());
    }

    @PostMapping("/setLoginTime")
    public void setLoginTime(@Validated @RequestBody LoginTimeDto loginTimeDto) {
        User user = userService.getOne(new QueryWrapper<User>().eq("id", loginTimeDto.getId()));
        user.setTime(loginTimeDto.getTime());
        userService.update(user,new QueryWrapper<User>().eq("id", user.getId()));
    }

    @PostMapping("/setLoginStatus")
    public void setLoginStatus(@Validated @RequestBody LoginStatusDto loginStatusDto) throws ParseException {

        User user = userService.getOne(new QueryWrapper<User>().eq("id", loginStatusDto.getId()));
        user.setLoginStatus(loginStatusDto.getLoginStatus());
        userService.update(user,new QueryWrapper<User>().eq("id", user.getId()));

    }

    //根据id返回状态
    @GetMapping("/getStatus")
    public Result getStatus(@RequestParam Long userId) {
        User user = userService.getOne(new QueryWrapper<User>().eq("id",userId));
        return Result.succ(user.getStatus());
    }

    //根据id返回姓名
    @GetMapping("/getName")
    public Result getName(@RequestParam Long userId) {
        User user = userService.getOne(new QueryWrapper<User>().eq("id",userId));
        return Result.succ(user.getName());
    }

    //根据id返回用户类型
    @GetMapping("/getType")
    public Result getType(@RequestParam Long userId) {
        User user = userService.getOne(new QueryWrapper<User>().eq("id",userId));
        return Result.succ(user.getType());
    }

    //通过注册
    @RequiresAuthentication
    @GetMapping("/agreeReg")
    public Result agreeReg(@RequestParam Long userId) {
        if(!this.isAdmin())
            return Result.fail("没有权限");
        User targetUser=userService.getOne(new QueryWrapper<User>().eq("id",userId));
        targetUser.setStatus('1');
        userService.update(targetUser,new QueryWrapper<User>().eq("id",userId));
        return Result.succ(null);
    }

    //拒绝注册
    @RequiresAuthentication
    @GetMapping("/denyReg")
    public Result denyReg(@RequestParam Long userId) {
        if(!this.isAdmin())
            return Result.fail("没有权限");
        userService.remove(new QueryWrapper<User>().eq("id",userId));
        return Result.succ(null);
    }

    //封禁用户
    @RequiresAuthentication
    @GetMapping("/freeze")
    public Result freeze(@RequestParam Long userId) {
        if(!this.isAdmin())
            return Result.fail("没有权限");
        User targetUser = userService.getOne(new QueryWrapper<User>().eq("id",userId));
        targetUser.setStatus('0');
        userService.update(targetUser,new QueryWrapper<User>().eq("id",userId));
        return Result.succ(null);
    }

    //解封用户
    @RequiresAuthentication
    @GetMapping("/unfreeze")
    public Result unfreeze(@RequestParam Long userId) {
        if(!this.isAdmin())
            return Result.fail("没有权限");
        User targetUser = userService.getOne(new QueryWrapper<User>().eq("id",userId));
        targetUser.setStatus('1');
        userService.update(targetUser,new QueryWrapper<User>().eq("id",userId));
        return Result.succ(null);
    }

    //获取全部注册申请（返回Page）
    @RequiresAuthentication
    @GetMapping("/regApplies")
    public Result regApplies(@RequestParam(defaultValue = "1") Integer currentPage) {
        if(!this.isAdmin())
            return Result.fail("没有权限");
        Page page = new Page(currentPage,30);
        IPage pageData = userService.page(page,new QueryWrapper<User>().eq("status",'2').orderByDesc("create_time"));
        return Result.succ(pageData);
    }

    //获取用户列表
    @RequiresAuthentication
    @GetMapping("/userList")
    public Result userList(@RequestParam(defaultValue = "1") Integer currentPage) {
        if(!this.isAdmin())
            return Result.fail("没有权限");
        Page page = new Page(currentPage,10);
        IPage pageData = userService.page(page,new QueryWrapper<User>().ne("status",'2').ne("type",'3').orderByDesc("create_time"));
        return Result.succ(pageData);
    }

    @GetMapping("/onlineData")
    public Result onlineData(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage,7);
        IPage pageData = userService.page(page,new QueryWrapper<User>().ne("login_status",'0').ne("status", "2").orderByDesc("time"));
        return Result.succ(pageData);
    }

    @RequiresAuthentication
    @GetMapping("/isAdmin")
    public boolean isAdmin(){
        char curUserType = ShiroUtil.getProfile().getType();
        return curUserType == '3';
    }

    @RequiresAuthentication
    @GetMapping("/isTeacher")
    public boolean isTeacher(){
        char curUserType = ShiroUtil.getProfile().getType();
        return curUserType == '2';
    }

    @RequiresAuthentication
    @GetMapping("/getCurUsername")
    public String getCurUsername(){
        return ShiroUtil.getProfile().getUsername();
    }

    @RequiresAuthentication
    @GetMapping("/getCurName")
    public String getCurName(){
        return ShiroUtil.getProfile().getName();
    }

    @RequiresAuthentication
    @GetMapping("/getCurUserId")
    public int getCurUserId(){
        return ShiroUtil.getProfile().getId();
    }

    @RequiresAuthentication
    @GetMapping("/getCurUserType")
    public char getCurUserType(){
        return ShiroUtil.getProfile().getType();
    }

    @RequiresAuthentication
    @GetMapping("/getUserIdByName")
    public Long getUserIdByName(@RequestParam String name){
        User user = userService.getOne(new QueryWrapper<User>().eq("name",name));
        if(user!=null)
            return user.getId();
        return null;
    }

    @RequiresAuthentication
    @GetMapping("/getUserById")
    public User getUserById(@RequestParam Long id){
        return userService.getById(id);
    }

    @RequiresAuthentication
    @GetMapping("/getUserByUsername")
    public User getUserByUsername(@RequestParam String username){
        return userService.getOne(new QueryWrapper<User>().eq("username",username));
    }

    @RequiresAuthentication
    @GetMapping("/getAllStudent")
    public List<User> getAllStudent(){
        return userService.list(new QueryWrapper<User>().eq("type",'1'));
    }

}
