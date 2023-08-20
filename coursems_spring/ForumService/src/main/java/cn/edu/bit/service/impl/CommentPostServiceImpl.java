package cn.edu.bit.service.impl;

import cn.edu.bit.entity.CommentPost;
import cn.edu.bit.mapper.CommentPostMapper;
import cn.edu.bit.service.CommentPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentPostServiceImpl extends ServiceImpl<CommentPostMapper, CommentPost> implements CommentPostService {
}
