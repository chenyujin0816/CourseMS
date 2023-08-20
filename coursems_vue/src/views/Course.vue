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
              <el-dropdown-item @click="toMyPage">个人主页</el-dropdown-item>
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
          <el-breadcrumb-item @click="refresh">{{ courseinfo }}</el-breadcrumb-item>
        </el-breadcrumb>
      </el-container>
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


      <!--      清除下面el-container内的代码，写新的内容-->
      <el-container class="container">
        <el-header class="emm">
          <div class="leftH"> <h3>{{'课程: ' + courseinfo }}</h3>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h3>{{'讲师: ' + teachername }}</h3>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h4></h4></div>
          <div class="rightH">
            <el-dropdown>
              <i class="el-icon-thumb"></i>
              <template #dropdown>
                <el-dropdown-menu v-if="seen2">
                  <el-dropdown-item @click="del">删除课程</el-dropdown-item>
                  <el-dropdown-item @click="mana(this.courseinfo, this.teachername,this.$route.query.weeks)">管理学生</el-dropdown-item>
                  <el-dropdown-item @click="setHomework(this.week,this.courseId)">布置作业</el-dropdown-item>
                  <el-dropdown-item @click="goChart">查看图表</el-dropdown-item>
                </el-dropdown-menu>
                <el-dropdown-menu v-if="seen1">
                  <el-dropdown-item @click="check">查询成绩</el-dropdown-item>
                  <el-dropdown-item @click="exi">撤销选课</el-dropdown-item>
                </el-dropdown-menu>
                <el-dropdown-menu v-if="seen3">
                  <el-dropdown-item @click="goChart">查看图表</el-dropdown-item>
                </el-dropdown-menu>

              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-collapse v-model="activeNames" @change="handleChange" class="gun">
          <div >
            <el-collapse-item v-for="(i,index) in weeklist" :name="index">
              <template v-slot:title>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<p class="title" >第{{ i }}周</p>&nbsp;&nbsp;&nbsp;&nbsp;<i class="el-icon-d-arrow-right"></i>
              </template>
              <el-divider></el-divider>
              <div class="ztitle">
                <el-link type="info"  @click="godld(i)" :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="el-icon-download"></i>&nbsp;&nbsp;课件</el-link>
                <el-divider></el-divider>
                <el-link type="info"  @click="gozuo(i)" :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="el-icon-notebook-2"></i>&nbsp;&nbsp;作业</el-link>
              </div>
            </el-collapse-item>
          </div>
        </el-collapse>
      </el-container>
    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import CourseService from "../services/CourseService";
import axios from "axios";

export default {
  name: "emm",
  data() {
    return {
      name:null,
      isCollapse: true,
      isAdmin: false,
      activeNames: ['1'],
      courseinfo: "",
      teachername: "",
      grade: '',
      seen1: false, //学生看的
      seen2: false, //教师看的
      seen3: false, //管理看的
      week: 0,
      courseId: 0,
      //该页面独有
      weeklist:[]
    }
  },
  mounted() {
    this.see()
    this.name = localStorage.name
    for(var i=0;i<this.$route.query.weeks;i++)
      this.weeklist[i]=i+1
    if(localStorage.type=='3')
      this.isAdmin=true
    this.week=this.$route.query.weeks
    this.courseId=this.$route.query.courseId
  },
  methods: {
    async check(){
      const res = await CourseService.checkgrade(this.courseinfo,this.teachername);
      console.log(res);
      if(res.data.code=='200')
      {
        this.grade = res.data.data
        if(this.grade == null || this.grade == '0')
          this.$message({
            type: 'fail',
            message: '暂无成绩'
          });
        else this.$message({
          type: 'success',
          message: '成绩：' + this.grade
        });
      }
      else{
        this.$message.error(res.data.msg);
      }
    },
    async see(){
      this.courseinfo = await this.$route.query.courseinfo
      this.teachername = await this.$route.query.teachername
      var ltype = localStorage.type;
      console.log(ltype);
      if(ltype == '1')
        this.seen1 = true;
      if(ltype == '2')
        this.seen2 = true;
      if(ltype == '3')
        this.seen3 = true;
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
    setHomework(week,courseId){
      this.$router.push({
        path: '/my/course/set',
        query: {
          week: week,
          courseId: courseId,
          courseInfo: this.courseinfo,
          teacherName: this.teachername
        }
      })
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    mana(courseinfo, teachername,weeks){
      this.$router.push({
        path: '/my/course/tms',
        query: {
          courseinfo: courseinfo,
          teachername: teachername,
          weeks: weeks
        }
      })
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    godld(week){
      this.$router.push({
        path:'/my/course/dld',
        query:{
          courseinfo: this.$route.query.courseinfo,
          teachername: this.$route.query.teachername,
          week: week
        }
      })
    },
    goChart(){
      this.$router.push({
        path:'/my/course/chart',
        query:{
          courseInfo: this.$route.query.courseinfo,
          week: this.week,
          courseId: this.courseId,
          teacherName: this.teachername
        }
      })
    },
    gozuo(week){
      this.$router.push({
        path:'/my/course/homework',
        query:{
          courseinfo: this.$route.query.courseinfo,
          teachername: this.$route.query.teachername,
          courseId: this.courseId,
          week: week
        }
      })
    },
    del(){
      this.$confirm('是否删除该授课?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning'
      }).then(async () => {
        console.log(this.courseinfo);
        let res;
        res = await CourseService.delete(this.courseinfo);
        if (res.data.code == '200') {
          this.$message({
            type: 'success',
            message: '申请删课成功!'
          });
          await this.$router.push('/my')
        } else {
          this.$message({
            type: 'fail',
            message: '申请失败!' + res.data.msg
          });
        }
      })
    },
    refresh(){
      location.reload()
    },
    exi() {
      this.$confirm('是否继续退课?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        type: 'warning'
      }).then(async () => {
        let res;
        res = await CourseService.exit(this.courseinfo);
        if (res.data.code == '200') {
          this.$message({
            type: 'success',
            message: '退课成功!'
          });
          await this.$router.push('/my')
        } else {
          this.$message({
            type: 'fail',
            message: '退课失败!' + res.data.msg
          });
        }
      })
    }
  }
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
.title{
  font-weight: bold; font-size: 17px;
}
.ztitle{
  font-weight: bold; font-size: 15px;
  text-align: left;
}

</style>