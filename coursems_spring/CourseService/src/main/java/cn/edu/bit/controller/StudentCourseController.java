package cn.edu.bit.controller;

import cn.edu.bit.common.lang.Result;
import cn.edu.bit.entity.AttendCourse;
import cn.edu.bit.entity.Course;
import cn.edu.bit.entity.StudentCourse;
import cn.edu.bit.entity.User;
import cn.edu.bit.feign.FeignService;
import cn.edu.bit.service.AttendCourseService;
import cn.edu.bit.service.CourseService;
import cn.edu.bit.service.StudentCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/studentcourse")
public class StudentCourseController {
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    CourseService courseService;
    @Autowired
    AttendCourseService attendCourseService;
    @Autowired
    FeignService feignService;

    @GetMapping("/getworklist") //查询该周作业情况
    public Result getworklist(@RequestParam(value = "courseinfo") String courseinfo,
                              @RequestParam(value = "teachername") String teachername,
                              @RequestParam(value = "week") Integer week){
        if(this.getCurUserType() == '1')
            return Result.fail("用户权限不符");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        if(curCourse.getTeacherId() != this.getCurUserId() && this.isTeacher())
            return Result.fail("该课程不属于您任教");

        class StudentRes{
            String name; //姓名
            String number; //学号
            String filename; //文件名
            Integer grade; //评分
            char posted; //是否提交作业

            public StudentRes(String name, String number) {
                this.name = name;
                this.number = number;
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

            public String getFilename() {
                return filename;
            }

            public StudentRes setFilename(String filename) {
                this.filename = filename;
                return this;
            }

            public Integer getGrade() {
                return grade;
            }

            public StudentRes setGrade(Integer grade) {
                this.grade = grade;
                return this;
            }

            public char getPosted() {return posted;}

            public StudentRes setPosted(char posted){
                this.posted = posted;
                return this;
            }
        };

        ArrayList<StudentRes> res = new ArrayList<>();
        List<AttendCourse> attendCourseList = attendCourseService.list(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()));

        for (AttendCourse attendCourse : attendCourseList) {
            User curStudent = this.getUserById(attendCourse.getStudentId());

            StudentRes studentRes = new StudentRes(
                    curStudent.getName(),
                    curStudent.getUsername()
            );

            StudentCourse curStudentcourse = studentCourseService.getOne(new QueryWrapper<StudentCourse>().
                    eq("course_id", curCourse.getId()).eq("student_id", curStudent.getId()).
                    eq("course_week", week));

            if (curStudentcourse == null) {
                studentRes.setPosted('0');
            } else {
                studentRes.setPosted('1'); //有提交物
                studentRes.setFilename(curStudentcourse.getUrl());
                studentRes.setGrade(curStudentcourse.getScore());
            }

            res.add(studentRes);
        }
        Map map = new LinkedHashMap();
        map.put("record", res);

        return Result.succ(map);
    }

    @PostMapping("/judgework") //作业评分
    public Result judgework(@RequestParam(value = "courseinfo") String courseinfo,
                            @RequestParam(value = "teachername") String teachername,
                            @RequestParam(value = "grade") String grade,
                            @RequestParam(value = "number") String number,
                            @RequestParam(value = "week") String week){
        if(!this.isTeacher())
            return Result.fail("该用户不是老师");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        if(curCourse.getTeacherId() != this.getCurUserId())
            return Result.fail("该课程不属于您任教");

        User student =this.getUserByUsername(number);

        int a = Integer.parseInt(grade);
        if (a < 1 )
            return Result.fail("评分不能低于1分");
        if (a > 100)
            return Result.fail("评分不能高于100分");

        int curWeek = Integer.parseInt(week);

        StudentCourse studentCourse = studentCourseService.getOne(new QueryWrapper<StudentCourse>()
                .eq("course_id",curCourse.getId()).eq("student_id",student.getId())
                .eq("course_week",curWeek));

        studentCourse.setScore(a);

        studentCourseService.update(studentCourse,new QueryWrapper<StudentCourse>()
                .eq("course_id",curCourse.getId()).eq("student_id",student.getId())
                .eq("course_week",curWeek));

        List<StudentCourse> studentCourseList = studentCourseService.list(new QueryWrapper<StudentCourse>()
                .eq("course_id",curCourse.getId()).eq("student_id",student.getId()));

        int sum = 0;
        for (StudentCourse course : studentCourseList)
            sum += course.getScore();
        sum /= studentCourseList.size();

        AttendCourse curAttendcourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>()
                .eq("course_id",curCourse.getId()).eq("student_id",student.getId()));
        curAttendcourse.setGrade(sum);
        attendCourseService.update(curAttendcourse,new QueryWrapper<AttendCourse>()
                .eq("course_id",curCourse.getId()).eq("student_id",student.getId()));

        return Result.succ("评分成功");
    }

    @GetMapping("/lookgrade") //查询该周作业情况
    public Result lookgrade(@RequestParam(value = "courseinfo") String courseinfo,
                            @RequestParam(value = "teachername") String teachername,
                            @RequestParam(value = "week") String  week){
        if(this.getCurUserType() != '1')
            return Result.fail("该用户不是学生");

        Integer curWeek = Integer.parseInt(week);

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        StudentCourse studentCourse = studentCourseService.getOne(new QueryWrapper<StudentCourse>()
                .eq("student_id",this.getCurUserId()).eq("course_id",curCourse.getId())
                .eq("course_week",curWeek));

        return Result.succ(studentCourse.getScore());
    }

    public boolean isTeacher(){
        return feignService.isTeacher();
    }

    public int getCurUserId(){
        return feignService.getCurUserId();
    }

    public char getCurUserType(){
        return feignService.getCurUserType();
    }

    public User getUserByUsername(String username){return feignService.getUserByUsername(username);}

    public User getUserById(Long id){return feignService.getUserById(id);}
}
