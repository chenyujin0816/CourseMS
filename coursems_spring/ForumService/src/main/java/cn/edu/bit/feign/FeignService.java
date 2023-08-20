package cn.edu.bit.feign;

import cn.edu.bit.feign.impl.FeignServiceCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = "user-service",fallback = FeignServiceCallBack.class)
public interface FeignService {
    @GetMapping("/user/isAdmin")
    boolean isAdmin();

    @GetMapping("/user/getCurUserId")
    int getCurUserId();

    @GetMapping("/user/getCurUserType")
    char getCurUserType();
}
