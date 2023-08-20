package cn.edu.bit.controller;

import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.feign.CourseFeign;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
//@RequestMapping("/client")
public class CourseController {
    @Autowired
    CourseFeign feignService;

    @GetMapping("/assignment/assign")
    public Result assignDetail(@RequestParam("courseId") Long courseId,@RequestParam("week") Long week){
        return feignService.assignDetail(courseId,week);
    }

    @PostMapping("/assignment/createAssignment")
    public Result createAssignment(@RequestBody CreateAssignDto createAssignDto){
        return feignService.createAssignment(createAssignDto);
    }

    @PostMapping("/attendCourse/attend")
    public Result attend(@RequestParam("courseinfo") String courseinfo,
                         @RequestParam("teachername") String teachername,
                         @RequestBody AttendCourseDto attendCourseDto){
        return feignService.attend(courseinfo,teachername,attendCourseDto);
    }

    @PostMapping("/attendCourse/exit")
    public Result exit(@RequestParam("courseinfo") String courseinfo){
        return feignService.exit(courseinfo);
    }

    @PostMapping("/attendCourse/addstudent")
    public Result addstudent(@RequestParam("courseinfo") String courseinfo,
                             @RequestBody StudentNumberDto studentNumberDto){
        return feignService.addstudent(courseinfo,studentNumberDto);
    }

    @PostMapping("/attendCourse/deletestudent")
    public Result deletestudent(@RequestParam("courseinfo") String courseinfo,
                                @RequestBody StudentNumberDto studentNumberDto){
        return feignService.deletestudent(courseinfo,studentNumberDto);
    }

    @GetMapping("/attendCourse/studentlist")
    public Result studentlist(@RequestParam("courseinfo") String courseinfo){
        return feignService.studentlist(courseinfo);
    }

    @GetMapping("/attendCourse/check")
    public Result check(@RequestParam("courseinfo") String courseinfo){
        return feignService.check(courseinfo);
    }

    @GetMapping("/attendCourse/chart1")
    public Result chart1(@RequestParam("courseinfo") String courseinfo){
        return feignService.chart1(courseinfo);
    }

    @GetMapping("/attendCourse/chart2")
    public Result chart2(@RequestParam("courseinfo") String courseinfo){
        return feignService.chart2(courseinfo);
    }

    @GetMapping("/attendCourse/chart3")
    public Result chart3(@RequestParam("courseinfo") String courseinfo){
        return feignService.chart3(courseinfo);
    }

    @GetMapping("/course/courses")
    public Result getInfos(){
        return feignService.getInfos();
    }

    @GetMapping("/course/mycourse")
    public Result getInfo(@RequestParam("courseinfo") String courseinfo,
                          @RequestParam("teachername") String teachername){
        return feignService.getInfo(courseinfo,teachername);
    }

    @PostMapping("/course/add")
    public Result addCourse(@RequestBody AddCourseDto addCourseDto){
        return feignService.addCourse(addCourseDto);
    }

    @PostMapping("/course/delete")
    public Result deleteCourse(@RequestParam("courseinfo") String courseinfo){
        return feignService.deleteCourse(courseinfo);
    }

    @PostMapping("/course/fixedadd")
    public Result fixedadd(@RequestParam("courseinfo") String courseinfo,
                           @RequestParam("teachername") String teachername){
        return feignService.fixedadd(courseinfo,teachername);
    }

    @PostMapping("/course/fixeddelete")
    public Result fixeddelete(@RequestParam("courseinfo") String courseinfo,
                              @RequestParam("teachername") String teachername){
        return feignService.fixeddelete(courseinfo,teachername);
    }

    @PostMapping("/course/fixed")
    public Result fixed(@RequestParam("courseinfo") String courseinfo,
                        @RequestParam("teachername") String teachername){
        return feignService.fixed(courseinfo,teachername);
    }

    @PostMapping("/course/Qdelete")
    public Result Qdelete(@RequestParam("courseinfo") String courseinfo,
                          @RequestParam("teachername") String teachername){
        return feignService.Qdelete(courseinfo,teachername);
    }

    @GetMapping("/course/addlist")
    public Result addlist(){
        return feignService.addlist();
    }

    @GetMapping("/course/deletelist")
    public Result deletelist(){
        return feignService.deletelist();
    }

    @GetMapping("/course/myindex")
    public Result myindex(){
        return feignService.myindex();
    }

    @GetMapping("/file/download")
    public void fileDownload(@RequestParam("type") String type,
                             @RequestParam("courseName") String courseName,
                             @RequestParam("week") String week,
                             @RequestParam("name") String name,
                             HttpServletResponse response) throws IOException {

        Response feignResponse = feignService.fileDownload(type,courseName,week,name);
        Response.Body body = feignResponse.body();
        for (Object key : feignResponse.headers().keySet()) {
            List<String> kList = (List<String>) feignResponse.headers().get(key);
            for (String val : kList) {
                response.setHeader(key.toString(), val);
            }
        }

        String fileName = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = body.asInputStream();
            outputStream = response.getOutputStream();
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            outputStream.write(b);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("失败");
        }finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @GetMapping("/file/downloadAssign")
    public void assignDownload(@RequestParam("url") String url,
                               @RequestParam("name") String name,
                               HttpServletResponse response) throws IOException {
        Response feignResponse = feignService.assignDownload(url,name);
        Response.Body body = feignResponse.body();
        for (Object key : feignResponse.headers().keySet()) {
            List<String> kList = (List<String>) feignResponse.headers().get(key);
            for (String val : kList) {
                response.setHeader(key.toString(), val);
            }
        }
        String fileName = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = body.asInputStream();
            outputStream = response.getOutputStream();
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            outputStream.write(b);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("失败");
        }finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @GetMapping("/file/delete")
    public Result fileDelete(@RequestParam("type") String type,
                             @RequestParam("courseName") String courseName,
                             @RequestParam("week") String week,
                             @RequestParam("name") String name){
        return feignService.fileDelete(type,courseName,week,name);
    }

    @GetMapping("/file/deleteAssign")
    public Result assignDelete(@RequestParam("type") String type,
                               @RequestParam("courseName") String courseName,
                               @RequestParam("week") String week,
                               @RequestParam("name") String name){
        return feignService.assignDelete(type,courseName,week,name);
    }

    @GetMapping("/file/getFiles")
    public Result getFiles(@RequestParam("type") String type,
                           @RequestParam("courseName") String courseName,
                           @RequestParam("week") String week){
        return feignService.getFiles(type,courseName,week);
    }

    @GetMapping("/file/getAssign")
    public Result getAssign(@RequestParam("type") String type,
                            @RequestParam("courseName") String courseName,
                            @RequestParam("week") String week){
        return feignService.getAssign(type,courseName,week);
    }

    @GetMapping("/file/getAllAssign")
    public Result getAllAssign(@RequestParam("type") String type,
                               @RequestParam("courseName") String courseName,
                               @RequestParam("week") String week){
        return feignService.getAllAssign(type,courseName,week);
    }

    @PostMapping("/file/upload")
    public Result fileUpload(@RequestPart("file") MultipartFile file,
                             @RequestParam("type") String type,
                             @RequestParam("courseName") String courseName,
                             @RequestParam("week") String week){
        return feignService.fileUpload(file,type,courseName,week);
    }

    @GetMapping("/file/downloadAllAssign")
    public void downloadAllAssign(@RequestParam("type") String type,
                                  @RequestParam("courseName") String courseName,
                                  @RequestParam("week") String week,
                                  HttpServletResponse response) throws IOException {
        Response feignResponse = feignService.downloadAllAssign(type,courseName,week);
        Response.Body body = feignResponse.body();
        for (Object key : feignResponse.headers().keySet()) {
            List<String> kList = (List<String>) feignResponse.headers().get(key);
            for (String val : kList) {
                response.setHeader(key.toString(), val);
            }
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=download.zip");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = body.asInputStream();
            outputStream = response.getOutputStream();

            ByteArrayOutputStream bos =new ByteArrayOutputStream();
            byte[] buf =new byte[1024*10];
            while (true) {
                int len = inputStream.read(buf);
                if (len < 0) {
                    break;
                }
                bos.write(buf, 0, len);
            }
            byte[] b = bos.toByteArray();

            inputStream.read(b);
            outputStream.write(b);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("失败");
        }finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @GetMapping("/news/getAllNews")
    public Result getNews(){
        return feignService.getNews();
    }

    @PostMapping("/news/releaseNews")
    public Result releaseNews(@RequestBody ReleaseNewsDto releaseNewsDto){
        return feignService.releaseNews(releaseNewsDto);
    }

    @PostMapping("/news/releasePublicNews")
    public Result releasePublicNews(@RequestBody ReleaseNewsDto releaseNewsDto){
        return feignService.releasePublicNews(releaseNewsDto);
    }

    @GetMapping("/studentcourse/getworklist")
    public Result getworklist(@RequestParam("courseinfo") String courseinfo,
                              @RequestParam("teachername") String teachername,
                              @RequestParam("week") Integer week){
        return feignService.getworklist(courseinfo,teachername,week);
    }

    @PostMapping("/studentcourse/judgework")
    public Result judgework(@RequestParam("courseinfo") String courseinfo,
                            @RequestParam("teachername") String teachername,
                            @RequestParam("grade") String grade,
                            @RequestParam("number") String number,
                            @RequestParam("week") String week){
        return feignService.judgework(courseinfo,teachername,grade,number,week);
    }

    @GetMapping("/studentcourse/lookgrade")
    public Result lookgrade(@RequestParam("courseinfo") String courseinfo,
                            @RequestParam("teachername") String teachername,
                            @RequestParam("week") String  week){
        return feignService.lookgrade(courseinfo,teachername,week);
    }
}
