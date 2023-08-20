package cn.edu.bit.feign;

import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.feign.impl.CourseFeignCallBack;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "course-service",fallback = CourseFeignCallBack.class)
public interface CourseFeign {
    @GetMapping("/assignment/assignList")
    Result assignList(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage, @RequestParam("courseId") Long courseId);

    @GetMapping("/assignment/assign")
    Result assignDetail(@RequestParam("courseId") Long courseId,@RequestParam("week") Long week);

    @GetMapping("/assignment/deleteAssignment")
    Result deleteAssignment(@RequestParam("assignId") Integer assignId);

    @PostMapping("/assignment/createAssignment")
    Result createAssignment(@RequestBody CreateAssignDto createAssignDto);

    @PostMapping("/attendCourse/attend")
    Result attend(@RequestParam("courseinfo") String courseinfo,
                  @RequestParam("teachername") String teachername,
                  @RequestBody AttendCourseDto attendCourseDto);

    @PostMapping("/attendCourse/exit")
    Result exit(@RequestParam("courseinfo") String courseinfo);

    @PostMapping("/attendCourse/addstudent")
    Result addstudent(@RequestParam("courseinfo") String courseinfo,
                      @RequestBody StudentNumberDto studentNumberDto);

    @PostMapping("/attendCourse/deletestudent")
    Result deletestudent(@RequestParam("courseinfo") String courseinfo,
                         @RequestBody StudentNumberDto studentNumberDto);

    @GetMapping("/attendCourse/studentlist")
    Result studentlist(@RequestParam("courseinfo") String courseinfo);

    @GetMapping("/attendCourse/check")
    Result check(@RequestParam("courseinfo") String courseinfo);

    @GetMapping("/attendCourse/chart1")
    Result chart1(@RequestParam("courseinfo") String courseinfo);

    @GetMapping("/attendCourse/chart2")
    Result chart2(@RequestParam("courseinfo") String courseinfo);

    @GetMapping("/attendCourse/chart3")
    Result chart3(@RequestParam("courseinfo") String courseinfo);

    @GetMapping("/course/courses")
    Result getInfos();

    @GetMapping("/course/mycourse")
    Result getInfo(@RequestParam("courseinfo") String courseinfo,
                   @RequestParam("teachername") String teachername);

    @PostMapping("/course/add")
    Result addCourse(@RequestBody AddCourseDto addCourseDto);

    @PostMapping("/course/delete")
    Result deleteCourse(@RequestParam("courseinfo") String courseinfo);

    @PostMapping("/course/fixedadd")
    Result fixedadd(@RequestParam("courseinfo") String courseinfo,
                    @RequestParam("teachername") String teachername);

    @PostMapping("/course/fixeddelete")
    Result fixeddelete(@RequestParam("courseinfo") String courseinfo,
                       @RequestParam("teachername") String teachername);

    @PostMapping("/course/fixed")
    Result fixed(@RequestParam("courseinfo") String courseinfo,
                 @RequestParam("teachername") String teachername);

    @PostMapping("/course/Qdelete")
    Result Qdelete(@RequestParam("courseinfo") String courseinfo,
                   @RequestParam("teachername") String teachername);

    @GetMapping("/course/addlist")
    Result addlist();

    @GetMapping("/course/deletelist")
    Result deletelist();

    @GetMapping("/course/myindex")
    Result myindex();

    @GetMapping("/file/download")
    Response fileDownload(@RequestParam("type") String type,
                          @RequestParam("courseName") String courseName,
                          @RequestParam("week") String week,
                          @RequestParam("name") String name);

    @GetMapping("/file/downloadAssign")
    Response assignDownload(@RequestParam("url") String url,
                            @RequestParam("name") String name);

    @GetMapping("/file/delete")
    Result fileDelete(@RequestParam("type") String type,
                      @RequestParam("courseName") String courseName,
                      @RequestParam("week") String week,
                      @RequestParam("name") String name);

    @GetMapping("/file/deleteAssign")
    Result assignDelete(@RequestParam("type") String type,
                        @RequestParam("courseName") String courseName,
                        @RequestParam("week") String week,
                        @RequestParam("name") String name);

    @GetMapping("/file/getFiles")
    Result getFiles(@RequestParam("type") String type,
                    @RequestParam("courseName") String courseName,
                    @RequestParam("week") String week);

    @GetMapping("/file/getAssign")
    Result getAssign(@RequestParam("type") String type,
                     @RequestParam("courseName") String courseName,
                     @RequestParam("week") String week);

    @GetMapping("/file/getAllAssign")
    Result getAllAssign(@RequestParam("type") String type,
                        @RequestParam("courseName") String courseName,
                        @RequestParam("week") String week);

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result fileUpload(@RequestPart("file") MultipartFile file,
                      @RequestParam("type") String type,
                      @RequestParam("courseName") String courseName,
                      @RequestParam("week") String week);

    @GetMapping("/file/downloadAllAssign")
    Response downloadAllAssign(@RequestParam("type") String type,
                               @RequestParam("courseName") String courseName,
                               @RequestParam("week") String week);

    @GetMapping("/news/getAllNews")
    Result getNews();

    @PostMapping("/news/releaseNews")
    Result releaseNews(@RequestBody ReleaseNewsDto releaseNewsDto);

    @PostMapping("/news/releasePublicNews")
    Result releasePublicNews(@RequestBody ReleaseNewsDto releaseNewsDto);

    @GetMapping("/studentcourse/getworklist")
    Result getworklist(@RequestParam("courseinfo") String courseinfo,
                       @RequestParam("teachername") String teachername,
                       @RequestParam("week") Integer week);

    @PostMapping("/studentcourse/judgework")
    Result judgework(@RequestParam("courseinfo") String courseinfo,
                     @RequestParam("teachername") String teachername,
                     @RequestParam("grade") String grade,
                     @RequestParam("number") String number,
                     @RequestParam("week") String week);

    @GetMapping("/studentcourse/lookgrade")
    Result lookgrade(@RequestParam("courseinfo") String courseinfo,
                     @RequestParam("teachername") String teachername,
                     @RequestParam("week") String  week);
}
