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
          <el-breadcrumb-item @click="refresh">我的课程</el-breadcrumb-item>
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


      <!--      清除下面el-container内的代码，写新的内容-->
      <el-container class="container">
        <el-header class="fentitle">我的课程</el-header>
        <!--        <el-header class="fentitle">我创建的课程</el-header>-->
        <div >
          <p class="ctitle">课程概览
            <el-tooltip v-if="seen2" content="新建课程" placement="top" effect="light">
              <el-button icon="el-icon-plus" @click="gonew" circle></el-button>
            </el-tooltip>
          </p>
        </div>

        <div style="margin-left:2%;margin-right:2%" class="gun">
          <el-row :gutter="16">
            <el-col :span=4.1 v-for="(item) in courselist" :key="item" :offset="1">
              <div style="margin-top:30px;width:250px">
                <el-card :body-style="{ padding: '5px'}" shadow="hover" @click="tocour(item.courseinfo, item.teachername,item.weeks,item.courseId)">
                  <img src="https://img.51miz.com/Element/00/84/62/62/44cd2b18_E846262_21063da1.png" class="image">
                  <div>
                    <span class="text-l">{{item.info}}</span><br>
                    <span class="text-b">{{item.courseinfo}}</span><br>
                    <span class="text-r">{{item.teachername}}</span><br>
                    <span class="text-k">创建时间:{{item.date}}</span><br>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-container>
    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import CourseService from "../services/CourseService";

export default {
  name: "emm",
  data() {
    return {
      name:null,
      isCollapse: true,
      isAdmin: false,
      seen: true, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
      seen1: false, //学生看的
      seen2: false, //教师看的
      seen3: false, //管理看的

      //该页面独有
      courselist:[]
    }
  },
  mounted() {
    this.setName()
    this.see()
    this.getmylist()
    if(localStorage.type=='3')
      this.isAdmin=true
  },
  methods: {
    see(){
      var ltype = localStorage.type;
      console.log(ltype);
      if(ltype == '1')
        this.seen1 = true;
      if(ltype == '2')
        this.seen2 = true;
      if(ltype == '3')
        this.seen3 = true;
    },
    async getmylist(){
      const res = await CourseService.getmyindex();
      this.courselist = res.data.data.record;

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

      for (var i=0;i < this.courselist.length; i++){
        var date = new Date(this.courselist[i].date);
        this.courselist[i].date = date.format("yyyy-MM-dd hh:mm:ss");
      }

      console.log(this.courselist);
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
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
    setName(){
      this.name = localStorage.name;
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    //该页面独有
    tocour(courseinfo, teachername,weeks,courseId){
      this.$router.push({
        path: '/my/course',
        query: {
          courseinfo: courseinfo,
          teachername: teachername,
          weeks: weeks,
          courseId: courseId
        }
      })
      // this.$router.push('/course')
    },
    gonew(){
      this.$router.push('/my/newcourse')
    },
    refresh(){
      location.reload()
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
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.fentitle{
  font-size: 24px;font-weight: bold;
}

/*页面独有CSS*/
.gun{
  background-color: whitesmoke;
  padding-bottom: 25px;
}
.ctitle{
  background-color: #E9EEF3;
  margin: 0 2% 0 2%;
  text-align: left;
  font-size: 19px;
  font-weight: bold;
  padding: 8px 10px 8px ;
  font-family: "FangSong";
}
.text-l{
  font-size:14px;
  color:lightslategray ;
}
.text-b{
  font-size:17px;
  color: black;
  font-weight: bolder;
  font-family: "MingLiU";
}
.text-r{
  color: gray;
  font-size:15px;
  font-family: "KaiTi";
}
.text-k{
  color: lightslategray;
  font-size:14px;
}
</style>