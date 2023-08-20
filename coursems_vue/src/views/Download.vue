<template>
  <el-container>
    <el-header class="head">
      <el-container class="leftH">
        <img src="../assets/img.png" alt class="logo_img" />
        <h1>课程管理网</h1>
      </el-container>
      <div class="rightH">
        <i class="el-icon-user-solid"></i>
        <span>{{name}}</span>
        <el-dropdown>
          <i class="el-icon-setting" style="color: white"></i>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goInformation">个人主页</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

    </el-header>
  </el-container>

  <el-container class="final">
    <el-header style=" text-align: left;font-size: 10px" class="mh" height="50px">
      <el-container>
        <el-radio-group v-model="isCollapse"  size="small" style="margin-top:10px;">
          <el-radio-button :label="false" type="info">展开</el-radio-button>
          <el-radio-button :label="true" type="info">收起</el-radio-button>
        </el-radio-group>
        <el-breadcrumb separator-class="el-icon-arrow-right" class="bre">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/my' }">我的课程</el-breadcrumb-item>
          <el-breadcrumb-item :to="{path: ''}" @click="back(courseInfo,teacherName)">{{ courseInfo }}</el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh">课件</el-breadcrumb-item>
          <!--        <el-breadcrumb-item>活动管理</el-breadcrumb-item>-->
        </el-breadcrumb></el-container>
    </el-header>
    <el-container class="main">
      <el-aside width=auto @mouseenter="collapseOpen"  @mouseleave="collapseClose" style="background-color: rgb(238, 241, 246)">
        <div class="menu">
          <el-menu router :default-active="$route.path" background-color="white" class="el-menu-vertical-demo" active-text-color="lightsteelblue " @open="handleOpen" @close="handleClose" :collapse="isCollapse" default-openeds="['3']"  ><!--        默认展开哪几项内容-->
            <el-menu-item index="/home"><i class="el-icon-s-home"></i><span>首页</span></el-menu-item>
            <el-menu-item index="/discuss"><i class="el-icon-s-promotion"></i><span>论坛</span></el-menu-item>
            <el-sub-menu index="/">
              <template v-slot:title ><i class="el-icon-s-management"></i><span >课程</span></template>
              <el-menu-item index="/search" ><i class="el-icon-top-left"></i><span class="sma">查询课程</span></el-menu-item>
              <el-menu-item index="/my" ><i class="el-icon-arrow-right"></i><span class="sma">我的课程</span></el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/message" v-if="isAdmin"><i class="el-icon-message-solid"></i><span>消息</span></el-menu-item>
            <el-menu-item index="/manage" v-if="isAdmin"><i class="el-icon-s-operation"></i><span>管理</span></el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      <el-container class="container">
        <el-header class="emm">
          <div class="leftH"> <h3>{{'课程: ' + courseInfo }}</h3>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h3>{{'讲师: ' + teacherName }}</h3>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h4>第{{ week }}周</h4>
            &nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;<h5>课件</h5>
          </div>
        </el-header>
        <div class="up" v-if="type=='2'">
          <el-upload
              style="margin-top: 20px"
              class="upload-demo"
              drag
              ref="upload"
              :headers="headers"
              :on-success="handleSuccess"
              :on-change="handleChange"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              :action="uploadUrl"
              multiple>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          </el-upload>
          <div v-if="fileList.length==0" style="font-size: 24px;position: relative;top: 75px" >你还没有上传课件</div>
        </div>
        <div v-else>
          <el-upload
              disabled
              style="margin-top: 20px"
              class="upload-demo"
              ref="upload"
              :on-change="handleChange"
              :on-preview="handlePreview"
              :file-list="fileList"
              multiple>
          </el-upload>
          <div v-if="fileList.length==0" style="font-size: 24px;position: relative;top: 75px" >教师还未上传课件</div>
        </div>

      </el-container>
    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import FileService from "@/services/FileService";
import axios from "axios";
import CourseService from "@/services/CourseService";

export default {
  name: "emm",
  data() {

    return {
      type: '0',
      name:null,
      isCollapse: true,
      isAdmin: false,
      courseInfo: '',
      teacherName: '',
      week: '',
      headers: {},
      uploadUrl: 'http://192.168.163.128:8888/client/file/upload?type=handout&',
      activeNames: ['1'],
      fileList: []
    }
  },
  mounted() {
    this.headers={
      'Authorization': localStorage.token
    }
    this.type=localStorage.type
    this.courseInfo=this.$route.query.courseinfo
    this.teacherName=this.$route.query.teachername
    this.week=this.$route.query.week
    this.name = localStorage.name
    this.getFiles("handout",this.courseInfo,this.week)
    //console.log(this.fileList)
    this.uploadUrl+="courseName="+this.courseInfo+"&week="+this.week
    console.log(this.uploadUrl)
    if(localStorage.type=='3')
      this.isAdmin=true
  },
  methods: {
    async goInformation(){
      await this.$router.push({path: "/information"});
    },
    async logout(){
      let id = localStorage.id
      await LoginService.setLoginStatus(id,'0')
      const loading = this.$loading({
        lock: true,
        text: '正在退出登录',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      setTimeout(() => {
        loading.close()
      }, 1000)

      const res=await LoginService.logout()
      if (res.data.data) {
        localStorage.clear()
        this.$message.success(res.data.msg);
        await this.$router.push({path: "/"});
      }else{
        this.$message.error(res.data.msg);
      }
    },
    handleSuccess(response, file, fileList) {
      console.log("fileList", fileList)
      console.log(file)
      if (response.code == '400') {
        this.$message.error(response.msg);
        this.getFiles("handout", this.courseInfo, this.week)
      } else{
        this.$message.success(response.msg);
        this.getFiles("handout", this.courseInfo, this.week)
      }
    },
    async getFiles(type,courseName,week) {
      const res=await FileService.getFileList(type,courseName,week)
      this.fileList.length=0;
      if(res.data.data){
        for(var i=0;i<res.data.data.length;i++) {
          var file = {
            'name': res.data.data[i],
            'url': ''
          }
          this.fileList.push(file)
        }
      }
      console.log(this.fileList)
    },
    async handleRemove(file, fileList) {
      const res=await FileService.deleteFile("handout",this.courseInfo,this.week,file.name)
      this.getFiles("handout",this.courseInfo,this.week)
    },
    async handlePreview(file) {

      const url = "http://192.168.163.128:8888/client/file/download?type=handout&courseName="+this.courseInfo+"&week="+this.week+"&name="+file.name
      document.location.href=url

    },
    handleChange(file,fileList) {
      console.log(fileList)
    },
    refresh(){
      location.reload()
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    async back(courseinfo,teachername){
      const res = await CourseService.mycourse(courseinfo,teachername)
      console.log(res.data)
      this.$router.push({
        path: '/my/course',
        query: {
          courseinfo: res.data.data.courseinfo,
          teachername: res.data.data.teachername,
          weeks: res.data.data.weeks,
          courseId: res.data.data.courseid
        }
      })
    }
  },
};
</script>

<style scoped>
.head{
  height:60px;
  background-color: #154a96;
  /*opacity: 0.9;*/
  display: flex;
  padding: 0;
  margin:0;
  justify-content: space-between;
  color: #E9EEF3;
}
.leftH{
  display: flex;
  padding: 0;
  margin:0;
}
.rightH{
  right: 20px;
  position: relative;
  margin-top: 20px;
  padding-right: 10px;
}
.leftH >h1{
  margin-left:10px;
}
.rightH >span{

  margin-left: 10px;
  margin-right:15px;
}
.mh{
  background-color:  lightgray  ;
  opacity: 0.7;
  margin-top: 2px;
}
.final{
  background-color: snow;
  opacity: 0.9;
}
.foot{
  height:80px;
  background-color: gainsboro ;
}
.foot > h6{
  color:silver ;
}
.bre{
  margin-top:18px;
  margin-left:90px;
  color:gray;
  font-weight: bold;
}
.logo_img {
  height: 60px;
  width: auto;
}
.container {
  display: flex;
  width: 100%;
  padding: 1% 2%;
  box-sizing: border-box;
  min-height: 74vh;
}
el-breadcrumb-item >span{
  font-size: 5px;
}
.sma{
  font-size: 5px;
}
.main{
  margin-top: 4px;
}
.menu{
  margin:10px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 200px;
}
.box > img {
  width: 200%;
  height: calc(100% - 10vh);
  object-fit: cover;
  transition: .5s;
}

.box > span {
  font-size: 3.8vh;
  display: block;
  text-align: center;
  height: 10vh;
  line-height: 2.6;
}

.box:hover > img {
  width: 100%;
  height: 100%;
}


/*此页独有*/
.emm{
  background-color:  #E9EEF3;
  height:60px;
  display: flex;
  padding-left: 2%;
  margin:0;
  justify-content: space-between;

}
strong{
  margin-top: 23px;
}

.up{
  margin-top:10px;
}
</style>