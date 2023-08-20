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
          <el-breadcrumb-item :to="{path: ''}" @click="back">{{ courseInfo }}</el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh">作业</el-breadcrumb-item>
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

      <el-container id="card" >
        <el-card id="sethomework" style="width: 80%; alignment: center">
          <el-form ref="form" :model="form" label-width="80px" :rules="formRules">
            <el-form-item label="作业名称" prop="title">
              <el-input v-model="form.title"></el-input>
            </el-form-item>
            <el-form-item label="所属周次" style="text-align: left" prop="week">
              <el-select v-model="form.week" placeholder="请选择周次" >
                <el-option v-for="count in week" :label="'第'+count+'周'" :value="count">
                </el-option>
              </el-select>
            </el-form-item>
            <el-row>
              <el-form-item label="截止时间" prop="date">
                <el-date-picker type="date" placeholder="选择日期" v-model="form.date" style="width: 100%;"></el-date-picker>
              </el-form-item>
              <el-form-item prop="time">
                <el-time-picker placeholder="选择时间" v-model="form.time" style="width: 100%;"></el-time-picker>
              </el-form-item>
            </el-row>
            <el-form-item label="作业要求" prop="description">
              <el-input type="textarea" v-model="form.description" :autosize="{ minRows: 10}"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit('form')">立即布置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-container>

    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import AssignmentService from "@/services/AssignmentService";

export default {
  name: "releasehomework",
  data() {
    return {
      week:0,
      name:null,
      form: {
        title: '',
        week: '',
        date: '',
        time: '',
        description: ''
      },
      formRules: {
        title: [
          { required: true, message: "请输入作业题目", trigger: "blur" },
          { min: 3, max: 20, message: "长度在 3 到 20 个字符", trigger: "blur" }
        ],
        week: [
          { required: true, message: "请选择周次", trigger: "blur" }
        ],
        date: [
          { required: true, message: "请选择截止日期", trigger: "blur" }
        ],
        time: [
          { required: true, message: "请选择截止时间", trigger: "blur" }
        ],
        description: [
          { required: true, message: "请输入作业描述", trigger: "blur" },
          { min: 3, max: 500, message: "长度在 3 到 500 个字符", trigger: "blur" }
        ],
      },
      // collapseBtnClick: false,
      isCollapse: true,
      isAdmin: false,
      teacherId: '',
      courseId: '',
      courseInfo: '',
      taecherName: ''
    }
  },
  mounted() {
    this.name = localStorage.name
    if(localStorage.type=='3')
      this.isAdmin=true
    this.week=Number(this.$route.query.week)
    this.courseId=this.$route.query.courseId
    this.courseInfo=this.$route.query.courseInfo
    this.teacherName=this.$route.query.teacherName
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
    onSubmit(formName) {
      this.$refs[formName].validate(async valid=>{
        if(!valid) return;
        const deadline = new Date()
        deadline.setUTCFullYear(this.form.date.getUTCFullYear())
        deadline.setUTCMonth(this.form.date.getUTCMonth())
        deadline.setUTCDate(this.form.date.getDate())
        deadline.setUTCHours(this.form.time.getUTCHours())
        deadline.setUTCMinutes(this.form.time.getUTCMinutes())
        deadline.setUTCSeconds(this.form.time.getUTCSeconds())
        Date.prototype.format = function (format) {
          var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(), //day
            "h+": this.getHours(), //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
            "S": this.getMilliseconds() //millisecond
          }
          if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
              (this.getFullYear() + "").substr(4 - RegExp.$1.length));
          for (var k in o) if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                    ("00" + o[k]).substr(("" + o[k]).length));
          return format;
        }

        const ddl = deadline.format("yyyy-MM-dd hh:mm:ss")
        const res = await AssignmentService.release(
            this.form.title,
            this.form.description,
            this.courseId,
            ddl,
            this.form.week)
        if (res.data.code == '200'){
          this.$message.success(res.data.data);
          this.$router.push({
            path: "/my/course",
            query: {
              courseinfo: this.courseInfo,
              teachername: this.teacherName,
              weeks: this.week,
              courseId: this.courseId
            }
          });
        }
        else
          this.$message.error(res.data.msg);
      })
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    back(){
      this.$router.push({
        path: '/my/course',
        query: {
          courseinfo: this.courseInfo,
          teachername: this.teacherName,
          weeks: this.week,
          courseId: this.courseId
        }
      })
    },
    refresh(){
      location.reload()
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
  padding: 4% 2%;
  box-sizing: border-box;
  height: 100vh;
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

.wu{
  font-size: 3px;
}
.menu{
  margin:10px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 200px;
}
.box {
  flex: 1;
  overflow: hidden;
  transition: .5s;
  margin: 0 2%;
  box-shadow: 0 20px 30px rgba(0,0,0,.1);
  line-height: 0;
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

.box:hover { flex: 1 1 50%; }
.box:hover > img {
  width: 100%;
  height: 100%;
}
#card{
  position: relative;
  width: 80%;
  height:100%;
  display:flex;
  justify-content: center;
  align-items: center;
}
</style>
