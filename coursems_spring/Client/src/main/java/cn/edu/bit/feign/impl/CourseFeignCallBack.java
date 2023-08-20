package cn.edu.bit.feign.impl;

import cn.edu.bit.common.dto.*;
import cn.edu.bit.common.lang.Result;
import cn.edu.bit.feign.CourseFeign;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CourseFeignCallBack implements CourseFeign {

    @Override
    public Result assignList(Integer currentPage, Long courseId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result assignDetail(Long courseId, Long week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deleteAssignment(Integer assignId) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result createAssignment(CreateAssignDto createAssignDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result attend(String courseinfo, String teachername, AttendCourseDto attendCourseDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result exit(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result addstudent(String courseinfo, StudentNumberDto studentNumberDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deletestudent(String courseinfo, StudentNumberDto studentNumberDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result studentlist(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result check(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result chart1(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result chart2(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result chart3(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getInfos() {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getInfo(String courseinfo, String teachername) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result addCourse(AddCourseDto addCourseDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deleteCourse(String courseinfo) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result fixedadd(String courseinfo, String teachername) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result fixeddelete(String courseinfo, String teachername) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result fixed(String courseinfo, String teachername) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result Qdelete(String courseinfo, String teachername) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result addlist() {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result deletelist() {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result myindex() {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override//todo
    public Response fileDownload(String type, String courseName, String week, String name) {
        return null;
    }

    @Override//todo
    public Response assignDownload(String url, String name) {
        return null;
    }

    @Override
    public Result fileDelete(String type, String courseName, String week, String name) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result assignDelete(String type, String courseName, String week, String name) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getFiles(String type, String courseName, String week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getAssign(String type, String courseName, String week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getAllAssign(String type, String courseName, String week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result fileUpload(MultipartFile file, String type, String courseName, String week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override//todo
    public Response downloadAllAssign(String type, String courseName, String week) {
        return null;
    }

    @Override
    public Result getNews() {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result releaseNews(ReleaseNewsDto releaseNewsDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result releasePublicNews(ReleaseNewsDto releaseNewsDto) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result getworklist(String courseinfo, String teachername, Integer week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result judgework(String courseinfo, String teachername, String grade, String number, String week) {
        return Result.fail("服务维护中，请稍后。。。");
    }

    @Override
    public Result lookgrade(String courseinfo, String teachername, String week) {
        return Result.fail("服务维护中，请稍后。。。");
    }
}
