package cn.edu.bit.controller;

import cn.edu.bit.common.dto.AttendCourseDto;
import cn.edu.bit.common.dto.StudentNumberDto;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.AttendCourse;
import cn.edu.bit.entity.Course;
import cn.edu.bit.entity.StudentCourse;
import cn.edu.bit.entity.User;
import cn.edu.bit.feign.FeignService;
import cn.edu.bit.service.AttendCourseService;
import cn.edu.bit.service.CourseService;
import cn.edu.bit.service.StudentCourseService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/attendCourse")
public class AttendCourseController {
    @Autowired
    AttendCourseService attendCourseService;
    @Autowired
    CourseService courseService;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    FeignService feignService;

    @PostMapping("/attend") //学生加入课程
    public Result attend(@RequestParam(value = "courseinfo") String courseinfo,
                         @RequestParam(value = "teachername") String teachername,
                         @Validated @RequestBody AttendCourseDto attendCourseDto){

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info", courseinfo).eq("teacher_id",this.getUserIdByName(teachername)));
        AttendCourse attendCourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>().eq("course_id", curCourse.getId()).eq("student_id", this.getCurUserId()));

        if(this.getCurUserType() != '1' )
            return Result.fail("此用户不是学生");
        if(curCourse.getCourseStatus() == '2')
            return Result.fail("该课程不可选");
        if(curCourse.getCourseStatus() == '3')
            return Result.fail("该课程审核中");
        if(attendCourse!=null)
            return Result.fail("学生已报此课");
        if(!Objects.equals(attendCourseDto.getCoursePwd(), curCourse.getCoursePwd()))
            return Result.fail("密码错误");

        AttendCourse newattendcourse = new AttendCourse();
        newattendcourse.setCourseId(curCourse.getId());
        newattendcourse.setStudentId((long) this.getCurUserId());
        newattendcourse.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        attendCourseService.save(newattendcourse);

        return  Result.succ("选课成功");
    }

    @PostMapping("/exit") //学生退出课程
    public Result exit(@RequestParam(value = "courseinfo") String courseinfo){
        int curUserId =  this.getCurUserId();
        String curUsername =  this.getCurUsername();
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        AttendCourse attendCourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>().eq("course_id", curCourse.getId()).eq("student_id", curUserId));

        if(this.getCurUserType() != '1' )
            return Result.fail("此用户不是学生");
        if(attendCourse!=null) {
            attendCourseService.remove(new QueryWrapper<AttendCourse>().eq("course_id", curCourse.getId()).eq("student_id", curUserId));;

            List<StudentCourse> studentCourseList = studentCourseService.list(new QueryWrapper<StudentCourse>().eq("course_id",curCourse.getId()).eq("student_id",curUserId));
            for(int i = 0 ; i < studentCourseList.size(); i++){
                studentCourseService.remove(new QueryWrapper<StudentCourse>().eq("id",studentCourseList.get(i).getId()));
            }

            for(int i = 1 ; i <= curCourse.getCourseWeek(); i++)
            {
                String assignmentPath = new File("files" + File.separator +
                        "assignment" + File.separator +
                        curCourse.getCourseInfo() + File.separator +
                        i + File.separator + curUsername).getAbsolutePath();

                String handoutPath = new File("files" + File.separator +
                        "handout" + File.separator +
                        curCourse.getCourseInfo() + File.separator +
                        i + File.separator + curUsername).getAbsolutePath();

                FileSystemUtils.deleteRecursively(new File(assignmentPath));
                FileSystemUtils.deleteRecursively(new File(handoutPath));
            }

            return Result.succ("成功退课");
        }
        else return Result.fail("学生未报名该课程");
    }

    @PostMapping("/addstudent") //老师添加学生进入课程
    public Result addstudent(@RequestParam(value = "courseinfo") String courseinfo,
                             @Validated @RequestBody StudentNumberDto studentNumberDto){
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        if(this.getCurUserType() != '2')
            return Result.fail("用户不是老师");
        if(curCourse.getTeacherId() != this.getCurUserId())
            return Result.fail("不是此任课老师");

        User student = this.getUserByUsername(studentNumberDto.getUserName());
        Assert.notNull(student, "查无此学生");

        if(student.getType() != '1')
            return Result.fail("要添加的用户不是学生");

        AttendCourse attendCourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()).eq("student_id",student.getId()));
        if(attendCourse != null)
            return Result.fail("学生已在此课程中");

        AttendCourse newattendcourse = new AttendCourse();
        newattendcourse.setCourseId(curCourse.getId());
        newattendcourse.setStudentId(student.getId());
        newattendcourse.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        attendCourseService.save(newattendcourse);

        return Result.succ("成功添加学生");
    }

    @PostMapping("/deletestudent") //老师将学生移出课程
    public Result deletestudent(@RequestParam(value = "courseinfo") String courseinfo,
                                @Validated @RequestBody StudentNumberDto studentNumberDto){
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        if(this.getCurUserType() != '2')
            return Result.fail("用户不是老师");
        if(curCourse.getTeacherId() != this.getCurUserId())
            return Result.fail("不是此任课老师");

        User student = this.getUserByUsername(studentNumberDto.getUserName());
        if(student == null)
            return Result.fail("查无此学生");

        AttendCourse attendCourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()).eq("student_id",student.getId()));
        if(attendCourse == null)
            return Result.fail("学生不在此课程中");

        attendCourseService.remove(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()).eq("student_id",student.getId()));

        List<StudentCourse> studentCourseList = studentCourseService.list(new QueryWrapper<StudentCourse>().eq("course_id",curCourse.getId()).eq("student_id",student.getId()));
        for (StudentCourse studentCourse : studentCourseList) {
            studentCourseService.remove(new QueryWrapper<StudentCourse>().eq("id", studentCourse.getId()));
        }

        for(int i = 1 ; i <= curCourse.getCourseWeek(); i++)
        {
            String assignmentPath = new File("files" + File.separator +
                    "assignment" + File.separator +
                    curCourse.getCourseInfo() + File.separator +
                    i + File.separator + student.getUsername()).getAbsolutePath();

            String handoutPath = new File("files" + File.separator +
                    "handout" + File.separator +
                    curCourse.getCourseInfo() + File.separator +
                    i + File.separator + student.getUsername()).getAbsolutePath();

            FileSystemUtils.deleteRecursively(new File(assignmentPath));
            FileSystemUtils.deleteRecursively(new File(handoutPath));
        }

        return Result.succ("成功将学生从课程中移除");
    }

    @GetMapping("/studentlist") //对该课程的学生列表
    public Result studentlist(@RequestParam(value = "courseinfo") String courseinfo){
        if(this.getCurUserType() != '2')
            return Result.fail("该用户不是老师");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        if(curCourse.getTeacherId() != this.getCurUserId())
            return Result.fail("该课程不属于您任教");

        class StudentRes{
            String number; //学号
            String name; //姓名
            char attend; //是否已报课

            public StudentRes(String number, String name) {
                this.number = number;
                this.name = name;
            }

            public String getNumber() {
                return number;
            }

            public StudentRes setNumber(String number) {
                this.number = number;
                return this;
            }

            public String getName() {
                return name;
            }

            public StudentRes setName(String name) {
                this.name = name;
                return this;
            }

            public char getAttend() {return attend;}

            public StudentRes setAttend(char attend){
                this.attend = attend;
                return this;
            }
        };

        ArrayList<StudentRes> res = new ArrayList<StudentRes>();
        List<User> userlist = this.getAllStudent();

        for (User user : userlist) {
            StudentRes studentRes = new StudentRes(
                    user.getUsername(),
                    user.getName()
            );

            AttendCourse curAttendcourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>().
                    eq("student_id", user.getId()).eq("course_id", curCourse.getId()));

            if (curAttendcourse != null) studentRes.setAttend('1');
            else studentRes.setAttend('0');

            res.add(studentRes);
        }
        Map map = new LinkedHashMap();
        map.put("record", res);

        return Result.succ(map);
    }

    @GetMapping("/check") //学生查询自己的成绩
    public Result check(@RequestParam(value = "courseinfo") String courseinfo){
        if(this.getCurUserType() != '1')
            return Result.fail("该用户不是学生");
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));

        AttendCourse attendCourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>()
                .eq("student_id",this.getCurUserId()).eq("course_id",curCourse.getId()));

        return Result.succ(attendCourse.getGrade());
    }

    @GetMapping("/chart1") //图表1：返回报名数和学生总数
    public Result chart1(@RequestParam(value = "courseinfo") String courseinfo){
        if(this.getCurUserType() == '1')
            return Result.fail("没有权限");
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));

        List<User> userlist = this.getAllStudent();
        int attended = 0;
        for (User user : userlist) {
            AttendCourse attendCourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>()
                    .eq("course_id", curCourse.getId()).eq("student_id", user.getId()));
            if (attendCourse != null)
                attended++;
        }

        return Result.succ(MapUtil.builder()
                .put("attended", attended)
                .put("sum", userlist.size())
                .map()
        );
    }

    @GetMapping("/chart2") //图表2：返回报名学生的成绩
    public Result chart2(@RequestParam(value = "courseinfo") String courseinfo){
        class StudentRes{
            String name; //姓名
            String number; //学号
            Integer grade; //评分

            public StudentRes(String name, String number, Integer grade) {
                this.name = name;
                this.number = number;
                this.grade = grade;
            }

            public String getNumber() {
                return number;
            }

            public StudentRes setNumber(String number) {
                this.number = number;
                return this;
            }

            public String getName() {
                return name;
            }

            public StudentRes setName(String name) {
                this.name = name;
                return this;
            }

            public Integer getGrade() {
                return grade;
            }

            public StudentRes setGrade(Integer grade) {
                this.grade = grade;
                return this;
            }
        };

        if(this.getCurUserType() == '1')
            return Result.fail("该学生用户权限不足");
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));

        ArrayList<StudentRes> res = new ArrayList<StudentRes>();
        List<AttendCourse> attendCourseList = attendCourseService.list(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()));

        for (AttendCourse attendCourse : attendCourseList) {
            User curStudent = this.getUserById(attendCourse.getStudentId());

            StudentRes studentRes = new StudentRes(
                    curStudent.getName(),
                    curStudent.getUsername(),
                    attendCourse.getGrade()
            );

            res.add(studentRes);
        }
        Map map = new LinkedHashMap();
        map.put("record", res);

        return Result.succ(map);
    }

    @GetMapping("/chart3") //图表3：返回各分段人数
    public Result chart3(@RequestParam(value = "courseinfo") String courseinfo){

        if(this.getCurUserType() == '1')
            return Result.fail("该学生用户权限不足");
        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));

        List<AttendCourse> attendCourseList = attendCourseService.list(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()));
        int A85=0,B75=0,C60=0,DFail=0,unknown=0;
        for (AttendCourse temp : attendCourseList) {
            if (temp.getGrade() >= 85)
                A85++;
            else if (temp.getGrade() >= 75)
                B75++;
            else if (temp.getGrade() >= 60)
                C60++;
            else if (temp.getGrade() > 0)
                DFail++;
            else
                unknown++;
        }

        return Result.succ(MapUtil.builder()
                .put("A85", A85)
                .put("B75", B75)
                .put("C60", C60)
                .put("DFail", DFail)
                .put("unknown", unknown)
                .map());
    }

    public int getCurUserId(){
        return feignService.getCurUserId();
    }

    public char getCurUserType(){
        return feignService.getCurUserType();
    }

    public String getCurUsername(){return feignService.getCurUsername();}

    public Long getUserIdByName(String name){return feignService.getUserIdByName(name);}

    public User getUserByUsername(String username){return feignService.getUserByUsername(username);}

    public User getUserById(Long id){return feignService.getUserById(id);}

    public List<User> getAllStudent(){return feignService.getAllStudent();}
}
