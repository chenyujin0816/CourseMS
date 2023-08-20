package cn.edu.bit.service.impl;

import cn.edu.bit.entity.ForumPost;
import cn.edu.bit.mapper.ForumPostMapper;
import cn.edu.bit.service.ForumPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumPostService {
}
