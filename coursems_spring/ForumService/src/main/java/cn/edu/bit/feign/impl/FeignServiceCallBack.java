package cn.edu.bit.feign.impl;

import cn.edu.bit.feign.FeignService;
import org.springframework.stereotype.Component;

@Component
public class FeignServiceCallBack implements FeignService {
    @Override
    public boolean isAdmin() {
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
}
