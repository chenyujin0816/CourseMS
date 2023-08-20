package cn.edu.bit.service.impl;

import cn.edu.bit.entity.News;
import cn.edu.bit.mapper.NewsMapper;
import cn.edu.bit.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
}
