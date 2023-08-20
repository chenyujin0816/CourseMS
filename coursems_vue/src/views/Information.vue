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
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

    </el-header>
  </el-container>

  <el-container class="final">
    <el-header style=" text-align: left;font-size: 10px"  class="mh" height="50px">
      <el-container>
        <el-radio-group v-model="isCollapse"  size="small" style="margin-top:10px;">
          <el-radio-button :label="false" type="info">展开</el-radio-button>
          <el-radio-button :label="true" type="info">收起</el-radio-button>
        </el-radio-group>
        <el-breadcrumb separator-class="el-icon-arrow-right" class="bre">
          <el-breadcrumb-item :to="{path:''}" >设置</el-breadcrumb-item>
          <el-breadcrumb-item >个人中心</el-breadcrumb-item>
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
      <div style="display: flex;flex-direction: row">
      <el-container class="container">
        <div style="display: flex;flex-direction: column">
        <el-descriptions
            class="margin-top"
            title="个人信息"
            :column="3"
            border
            style="margin-bottom: 20vh;left: 40px;position: relative"
        >
          <template #extra>
            <el-button type="primary" size="small" @click="open">修改密码</el-button>
          </template>
          <el-descriptions-item width="8vw">
            <template #label>
              <i class="el-icon-user"></i>
              用户名
            </template>
            {{ name }}
          </el-descriptions-item>
          <el-descriptions-item width="8vw">
            <template #label>
              <i class="el-icon-mobile-phone"></i>
              学号
            </template>
            {{ username }}
          </el-descriptions-item>

          <el-descriptions-item width="8vw">
            <template #label>
              <i class="el-icon-tickets"></i>
              身份
            </template>
            <el-tag size="small">{{ type }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <i class="el-icon-box"></i>
              密码
            </template>
            <div @click="showPassword">{{ password }}</div>
          </el-descriptions-item>
        </el-descriptions>
          <div style="width: 100%;left: 40px;position: relative;text-align: left"  v-show="showCourse">
            <div style="font-weight: bold;position: relative;left: 4px"><p>已参加课程</p></div>
          <el-table :data="myCourseData" style="width: 100%" border stripe>
            <el-table-column prop="courseinfo" label="课程" > </el-table-column>
            <el-table-column prop="teachername" label="老师" > </el-table-column>
          </el-table>
          </div>
        </div>
      </el-container>
      <div style="width: 650px;height: 100%;background: rgba(19,63,250,0.4);padding: 5px;margin: 0">
        <el-calendar v-model="value" > </el-calendar>
      </div>
      </div>
    </el-container>

    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import NewsService from "@/services/NewsService";
import CourseService from "@/services/CourseService";

export default {
  name: "information",
  data() {
    return {
      isCollapse: true,
      type: null,
      isAdmin: false,
      name: null,
      id:null,
      username:null,
      password:"",
      ifShow:false,
      value:new Date(),
      showCourse:true,
      myCourseData:[]
    }
  },
  mounted() {
    this.getmylist()
    this.setType()
    this.setName()
    this.setShowCourse()
    this.username = localStorage.username
    this.hidePassword()

    if(localStorage.type=='3')
      this.isAdmin=true
  },
  methods: {
    setShowCourse(){
      if(localStorage.type=='3'||localStorage.type=='2')
      {
        this.showCourse = false;
      }
    },
    async getmylist(){
      if(this.showCourse)
      {
      const res = await CourseService.getmyindex();
      this.courselist = res.data.data.record;
      this.myCourseData=this.courselist
      }
    },
    async open() {
      this.$prompt('请输入新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      })
          .then(async (value) => {
            const res = await LoginService.changePassword(
                localStorage.id,
                value.value
            );
            if (res.data.code == "200") {
              this.$message.success("修改成功");
              localStorage.password = value.value

            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '取消输入',
            })
          })
    },
    hidePassword(){
      this.password=''
      for(let i =0;i<localStorage.username.length;i++)
      {
        this.password+="*"
      }
    },
    showPassword(){
      if(this.ifShow)
      {
        this.hidePassword()
        this.ifShow=false
      }
      else {
        this.password = localStorage.password
        this.ifShow=true
      }
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
    setType(){
      if(localStorage.type=="1")
        this.type="学生";
      else if(localStorage.type=="2")
        this.type="教师"
      else this.type="管理员"
    },
    setName(){
      this.name=localStorage.name;
    },

    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    refresh(){
      location.reload()
    },
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

  margin-right:15px;
  margin-left: 10px;
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
  color: grey;
  font-weight: bold;
}
.logo_img {
  height: 60px;
  width: auto;
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

ul {
  list-style-type: none;
  padding: 0;
}

a {
  color: #42b983;
}
.container {
  display: flex;
  /*background-color: #e0e0e0;*/
  width: 100%;
  padding: 2% 2%;
  box-sizing: border-box;
  height: 72.5vh;
  border-radius: 15px;

}

.tem{
  margin-top:18px;
  position: absolute;
  right: 5vw;
  /*background-color: yellow;*/
}
</style>