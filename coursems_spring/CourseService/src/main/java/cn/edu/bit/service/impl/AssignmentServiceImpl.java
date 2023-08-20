package cn.edu.bit.service.impl;

import cn.edu.bit.entity.Assignment;
import cn.edu.bit.mapper.AssignmentMapper;
import cn.edu.bit.service.AssignmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements AssignmentService {
}
