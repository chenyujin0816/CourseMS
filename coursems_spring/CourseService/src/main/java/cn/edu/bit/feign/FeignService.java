package cn.edu.bit.feign;

import cn.edu.bit.entity.User;
import cn.edu.bit.feign.impl.FeignServiceCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "user-service",fallback = FeignServiceCallBack.class)
public interface FeignService {
    @GetMapping("/user/isAdmin")
    boolean isAdmin();

    @GetMapping("/user/isTeacher")
    boolean isTeacher();

    @GetMapping("/user/getCurUserId")
    int getCurUserId();

    @GetMapping("/user/getCurUserType")
    char getCurUserType();

    @GetMapping("/user/getCurName")
    String getCurName();

    @GetMapping("/user/getCurUsername")
    String getCurUsername();

    @GetMapping("/user/getUserIdByName")
    Long getUserIdByName(@RequestParam("name") String name);

    @GetMapping("/user/getUserByUsername")
    User getUserByUsername(@RequestParam("username") String username);

    @GetMapping("/user/getUserById")
    User getUserById(@RequestParam("id") Long id);

    @GetMapping("/user/getAllStudent")
    List<User> getAllStudent();


}
