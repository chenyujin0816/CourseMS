package cn.edu.bit.service.impl;

import cn.edu.bit.entity.Replies;
import cn.edu.bit.mapper.RepliesMapper;
import cn.edu.bit.service.RepliesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RepliesServiceImpl  extends ServiceImpl<RepliesMapper, Replies> implements RepliesService {
}
