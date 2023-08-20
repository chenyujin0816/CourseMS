package cn.edu.bit.service.impl;

import cn.edu.bit.entity.User;
import cn.edu.bit.mapper.UserMapper;
import cn.edu.bit.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
