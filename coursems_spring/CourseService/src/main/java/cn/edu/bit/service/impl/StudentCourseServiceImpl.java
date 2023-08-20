package cn.edu.bit.service.impl;

import cn.edu.bit.entity.StudentCourse;
import cn.edu.bit.mapper.StudentCourseMapper;
import cn.edu.bit.service.StudentCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements StudentCourseService {
}
