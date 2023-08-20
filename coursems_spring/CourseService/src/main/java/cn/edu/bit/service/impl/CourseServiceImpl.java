package cn.edu.bit.service.impl;

import cn.edu.bit.entity.Course;
import cn.edu.bit.mapper.CourseMapper;
import cn.edu.bit.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
