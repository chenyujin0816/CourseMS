package cn.edu.bit.controller;

import cn.edu.bit.common.dto.AddCourseDto;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    AttendCourseService attendCourseService;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    FeignService feignService;

    @GetMapping("/courses") //查询所有课程
    public Result getInfos(){

        ArrayList<HashMap<String,String>> res = new ArrayList<>();
        List<Course> courselist = courseService.list(new QueryWrapper<>());

        for (Course course : courselist) {
            User curteacher = this.getUserById(course.getTeacherId());

            HashMap<String, String> resMap = new LinkedHashMap<>();
            resMap.put("info", course.getCourseInfo());
            resMap.put("name", curteacher.getName());
            resMap.put("status", String.valueOf(course.getCourseStatus()));

            if (this.getCurUserType() == '1') {
                AttendCourse curAttendcourse = attendCourseService.getOne(new QueryWrapper<AttendCourse>().
                        eq("student_id", this.getCurUserId()).eq("course_id", course.getId()));
                if (curAttendcourse != null)
                    resMap.put("attend", "1");
                else
                    resMap.put("attend", "0");
            }

            res.add(resMap);
        }
        Map map = new LinkedHashMap();
        map.put("record", res);
        return Result.succ(map);
    }

    @GetMapping("/mycourse") //查询单个课程
    public Result getInfo(@RequestParam(value = "courseinfo") String courseinfo,
                          @RequestParam(value = "teachername") String teachername) {

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info", courseinfo));
        if(curCourse == null)
            return Result.fail("该课程不存在");

        Map map=new LinkedHashMap();
        map.put("courseinfo", curCourse.getCourseInfo());
        map.put("teachername", teachername);
        map.put("weeks", curCourse.getCourseWeek());
        map.put("courseid",curCourse.getId());
        return Result.succ(map);
    }

    @PostMapping("/add") //老师申请添加课程
    public Result addCourse(@Validated @RequestBody AddCourseDto addCourseDto){
        if(!this.isTeacher())
            return Result.fail("该用户不是老师");

        Course course1 = courseService.getOne(new QueryWrapper<Course>().eq("course_number",addCourseDto.getCourseNumber()));
        if(course1!=null)
            return Result.fail("该课程编号已被占用");
        Course course2 = courseService.getOne(new QueryWrapper<Course>().eq("course_name",addCourseDto.getCourseName()));
        if(course2!=null)
            return Result.fail("该课程已有申报");
        Course course3 = courseService.getOne(new QueryWrapper<Course>().eq("course_info",addCourseDto.getCourseInfo()));
        if(course3!=null)
            return Result.fail("该课程名已被占用");

        Course newcourse = new Course();
        newcourse.setCourseNumber(addCourseDto.getCourseNumber());
        newcourse.setCourseName(addCourseDto.getCourseName());
        newcourse.setCourseInfo(addCourseDto.getCourseInfo());
        newcourse.setCourseScore(addCourseDto.getCourseScore());
        newcourse.setCoursePwd(addCourseDto.getCoursePwd());
        newcourse.setTeacherId((long) this.getCurUserId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(addCourseDto.getCourseStart());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(addCourseDto.getCourseWeek() > 18)
            return Result.fail("开课周数超过18周");

        if(addCourseDto.getCourseWeek() < 3)
            return Result.fail("开课周低于3周");

        Timestamp timestamp = new Timestamp(date.getTime());
        newcourse.setCourseStart(timestamp);
        newcourse.setCourseWeek(addCourseDto.getCourseWeek());
        newcourse.setCourseStatus('2');
        newcourse.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        courseService.save(newcourse);

        return Result.succ("申请增加课程成功，等待管理员通过");
    }

    @PostMapping("/delete") //老师申请删除课程
    public Result deleteCourse(@RequestParam(value = "courseinfo") String courseinfo){
        if(!this.isTeacher())
            return Result.fail("该用户不是老师");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo));
        Assert.notNull(curCourse, "该课程不存在");

        if(curCourse.getTeacherId() != this.getCurUserId())
            return Result.fail("该课程不属于此任课老师");

        curCourse.setCourseStatus('3');
        courseService.update(curCourse,new QueryWrapper<Course>().eq("course_info",courseinfo));

        return Result.succ("申请删除课程成功，等待管理员通过");
    }

    @PostMapping("/fixedadd") //管理员通过申请添加的课程
    public Result fixedadd(@RequestParam(value = "courseinfo") String courseinfo,
                           @RequestParam(value = "teachername") String teachername){
        if(!this.isAdmin())
            return Result.fail("该用户不是管理员");

        Long curTecherId = this.getUserIdByName(teachername);
        if (curTecherId == null)  return Result.fail("该教师不存在");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTecherId));
        if (curCourse == null)  return  Result.fail("该课程不存在");

        if(curCourse.getCourseStatus() != '2')
            return Result.fail("该课程并不在申请添加队列中");

        curCourse.setCourseStatus('0');
        courseService.update(curCourse,new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTecherId));
        return Result.succ("课程成功添加");
    }

    @PostMapping("/fixeddelete") //管理员通过申请删除的课程
    public Result fixeddelete(@RequestParam(value = "courseinfo") String courseinfo,
                              @RequestParam(value = "teachername") String teachername){
        Long curTeacherId = this.getUserIdByName(teachername);
        if (curTeacherId == null)  return Result.fail("该教师不存在");

        if(!this.isAdmin())
            return Result.fail("该用户不是管理员");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTeacherId));
        if (curCourse == null)  return  Result.fail("该课程不存在");

        if(curCourse.getCourseStatus() != '3')
            return Result.fail("该课程并不在申请删除队列中");

        courseService.remove(new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTeacherId));;

        List<AttendCourse> attendCourseList = attendCourseService.list(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()));
        for (AttendCourse attendCourse : attendCourseList) {
            attendCourseService.remove(new QueryWrapper<AttendCourse>().eq("id", attendCourse.getId()));
        }

        List<StudentCourse> studentCourseList = studentCourseService.list(new QueryWrapper<StudentCourse>().eq("course_id",curCourse.getId()));
        for (StudentCourse studentCourse : studentCourseList) {
            studentCourseService.remove(new QueryWrapper<StudentCourse>().eq("id", studentCourse.getId()));
        }

        String assignmentPath = new File("files" + File.separator +
                "assignment" + File.separator +
                curCourse.getCourseInfo()).getAbsolutePath();

        String handoutPath = new File("files" + File.separator +
                "handout" + File.separator +
                curCourse.getCourseInfo()).getAbsolutePath();

        FileSystemUtils.deleteRecursively(new File(assignmentPath));
        FileSystemUtils.deleteRecursively(new File(handoutPath));

        return Result.succ("课程成功删除");
    }

    @PostMapping("/fixed") //管理员拒绝申请删除的课程，让其恢复
    public Result fixed(@RequestParam(value = "courseinfo") String courseinfo,
                        @RequestParam(value = "teachername") String teachername){
        if(!this.isAdmin())
            return Result.fail("该用户不是管理员");

        Long curTeacherId = this.getUserIdByName(teachername);
        if (curTeacherId == null)  return Result.fail("该教师不存在");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTeacherId));
        if (curCourse == null)  return  Result.fail("该课程不存在");

        if(curCourse.getCourseStatus() != '3')
            return Result.fail("该课程并不在申请删除队列中");

        curCourse.setCourseStatus('0');
        courseService.update(curCourse,new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTeacherId));
        return Result.succ("课程状态恢复");
    }

    @PostMapping("/Qdelete") //管理员主动删除课程
    public Result Qdelete(@RequestParam(value = "courseinfo") String courseinfo,
                          @RequestParam(value = "teachername") String teachername){
        if(!this.isAdmin())
            return Result.fail("该用户不是管理员");

        Long curTeacherId = this.getUserIdByName(teachername);
        if (curTeacherId == null)  return Result.fail("该教师不存在");

        Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTeacherId));
        if (curCourse == null)  return  Result.fail("该课程不存在");

        courseService.remove(new QueryWrapper<Course>().eq("course_info",courseinfo).eq("teacher_id",curTeacherId));;

        List<AttendCourse> attendCourseList = attendCourseService.list(new QueryWrapper<AttendCourse>().eq("course_id",curCourse.getId()));
        for (AttendCourse attendCourse : attendCourseList)
            attendCourseService.remove(new QueryWrapper<AttendCourse>().eq("id", attendCourse.getId()));

        List<StudentCourse> studentCourseList = studentCourseService.list(new QueryWrapper<StudentCourse>().eq("course_id",curCourse.getId()));
        for (StudentCourse studentCourse : studentCourseList)
            studentCourseService.remove(new QueryWrapper<StudentCourse>().eq("id", studentCourse.getId()));

        String assignmentPath = new File("files" + File.separator +
                "assignment" + File.separator +
                curCourse.getCourseInfo()).getAbsolutePath();

        String handoutPath = new File("files" + File.separator +
                "handout" + File.separator +
                curCourse.getCourseInfo()).getAbsolutePath();

        FileSystemUtils.deleteRecursively(new File(assignmentPath));
        FileSystemUtils.deleteRecursively(new File(handoutPath));

        return Result.succ("课程成功删除");
    }

    @GetMapping("/addlist") //管理员查询正在等待添加的课程
    public Result addlist(){
        class CourseRes{
            Date date;
            String courseinfo; //课程信息
            String teachername; //老师姓名

            public CourseRes(Date date, String courseinfo, String teachername) {
                this.date = date;
                this.courseinfo = courseinfo;
                this.teachername = teachername;
            }

            public Date getDate() {
                return date;
            }

            public CourseRes SetDate(Date date){
                this.date = date;
                return this;
            }

            public String getCourseinfo() {
                return courseinfo;
            }

            public CourseRes setCourseinfo(String courseinfo) {
                this.courseinfo = courseinfo;
                return this;
            }

            public String getTeachername() {
                return teachername;
            }

            public CourseRes setTeachername(String teachername) {
                this.teachername = teachername;
                return this;
            }
        };

        if(!this.isAdmin())
            return Result.fail("该用户不是管理员");

        ArrayList<CourseRes> res = new ArrayList<CourseRes>();
        List<Course> courselist = courseService.list(new QueryWrapper<Course>().eq("course_status",'2'));

        for (Course course : courselist) {
            User curteacher = this.getUserById(course.getTeacherId());
            Date d = new Date(course.getCreateTime().getTime());
            CourseRes courseRes = new CourseRes(
                    d,
                    course.getCourseInfo(),
                    curteacher.getName()
            );
            res.add(courseRes);
        }
        Map map = new LinkedHashMap();
        map.put("record", res);

        return Result.succ(map);
    }

    @GetMapping("/deletelist") //管理员查询正在等待删除的课程
    public Result deletelist(){
        class CourseRes{
            Date date;
            String courseinfo; //课程信息
            String teachername; //老师姓名

            public CourseRes(Date date, String courseinfo, String teachername) {
                this.date = date;
                this.courseinfo = courseinfo;
                this.teachername = teachername;
            }

            public Date getDate() {
                return date;
            }

            public CourseRes SetDate(Date date){
                this.date = date;
                return this;
            }

            public String getCourseinfo() {
                return courseinfo;
            }

            public CourseRes setCourseinfo(String courseinfo) {
                this.courseinfo = courseinfo;
                return this;
            }

            public String getTeachername() {
                return teachername;
            }

            public CourseRes setTeachername(String teachername) {
                this.teachername = teachername;
                return this;
            }
        };
        if(!this.isAdmin())
            return Result.fail("该用户不是管理员");

        ArrayList<CourseRes> res = new ArrayList<>();
        List<Course> courselist = courseService.list(new QueryWrapper<Course>().eq("course_status",'3'));

        for (Course course : courselist) {
            User curteacher = this.getUserById(course.getTeacherId());
            Date d = new Date(course.getCreateTime().getTime());
            CourseRes courseRes = new CourseRes(
                    d,
                    course.getCourseInfo(),
                    curteacher.getName()
            );
            res.add(courseRes);
        }
        Map map = new LinkedHashMap();
        map.put("record", res);

        return Result.succ(map);
    }

    @GetMapping("/myindex") //查询主页的课程表
    public Result myindex(){
        class CourseRes{
            Date date;
            String courseinfo; //课程信息
            String teachername; //老师姓名
            String weeks;
            Long courseId;

            public Long getCourseId() {
                return courseId;
            }

            public CourseRes(Date date, String courseinfo, String teachername, String weeks, Long courseId) {
                this.date = date;
                this.courseinfo = courseinfo;
                this.teachername = teachername;
                this.weeks = weeks;
                this.courseId = courseId;
            }

            public CourseRes setCourseId(Long courseId) {
                this.courseId = courseId;
                return this;
            }

            public String getWeeks() {
                return weeks;
            }

            public CourseRes setWeeks(String weeks) {
                this.weeks = weeks;
                return this;
            }

            public Date getDate() {
                return date;
            }

            public CourseRes SetDate(Date date){
                this.date = date;
                return this;
            }

            public String getCourseinfo() {
                return courseinfo;
            }

            public CourseRes setCourseinfo(String courseinfo) {
                this.courseinfo = courseinfo;
                return this;
            }

            public String getTeachername() {
                return teachername;
            }

            public CourseRes setTeachername(String teachername) {
                this.teachername = teachername;
                return this;
            }
        };

        if(this.getCurUserType()=='1') //学生的返回
        {
            ArrayList<CourseRes> res = new ArrayList<CourseRes>();
            List<AttendCourse> attendCourseList = attendCourseService.list(new QueryWrapper<AttendCourse>().eq("student_id",this.getCurUserId()));

            for (AttendCourse attendCourse : attendCourseList) {
                Course curCourse = courseService.getOne(new QueryWrapper<Course>().eq("id", attendCourse.getCourseId()));
                User curteacher = this.getUserById(curCourse.getTeacherId());
                Date d = new Date(curCourse.getCreateTime().getTime());
                CourseRes courseRes = new CourseRes(
                        d,
                        curCourse.getCourseInfo(),
                        curteacher.getName(),
                        String.valueOf(curCourse.getCourseWeek()),
                        curCourse.getId()
                );
                res.add(courseRes);
            }
            Map map = new LinkedHashMap();
            map.put("record", res);

            return Result.succ(map);
        }
        else if(this.isTeacher()) //教师的返回
        {
            ArrayList<CourseRes> res = new ArrayList<CourseRes>();
            List<Course> courselist = courseService.list(new QueryWrapper<Course>().eq("teacher_id",this.getCurUserId()));

            for(int i = 0 ; i < courselist.size(); i++){
                Date d = new Date(courselist.get(i).getCreateTime().getTime());
                CourseRes courseRes = new CourseRes(
                        d,
                        courselist.get(i).getCourseInfo(),
                        this.getCurName(),
                        String.valueOf(courselist.get(i).getCourseWeek()),
                        courselist.get(i).getId()
                );
                res.add(courseRes);
            }
            Map map = new LinkedHashMap();
            map.put("record", res);

            return Result.succ(map);
        }
        else if(this.isAdmin())
        {
            ArrayList<CourseRes> res = new ArrayList<>();
            List<Course> courselist = courseService.list(new QueryWrapper<>());

            for (Course course : courselist) {
                User curteacher = this.getUserById(course.getTeacherId());
                Date d = new Date(course.getCreateTime().getTime());
                CourseRes courseRes = new CourseRes(
                        d,
                        course.getCourseInfo(),
                        curteacher.getName(),
                        String.valueOf(course.getCourseWeek()),
                        course.getId()
                );

                res.add(courseRes);
            }
            Map map = new LinkedHashMap();
            map.put("record", res);

            return Result.succ(map);
        }
        return Result.fail("错误的用户类别");
    }

    public boolean isAdmin(){
        return feignService.isAdmin();
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

    public String getCurName(){return feignService.getCurName();}

    public Long getUserIdByName(String name){return feignService.getUserIdByName(name);}

    public User getUserById(Long id){return feignService.getUserById(id);}

}
