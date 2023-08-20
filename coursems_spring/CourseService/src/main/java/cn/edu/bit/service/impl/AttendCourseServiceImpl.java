package cn.edu.bit.service.impl;

import cn.edu.bit.entity.AttendCourse;
import cn.edu.bit.mapper.AttendCourseMapper;
import cn.edu.bit.service.AttendCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AttendCourseServiceImpl extends ServiceImpl<AttendCourseMapper, AttendCourse> implements AttendCourseService {
}
