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
          <el-breadcrumb-item @click="refresh"><span >发布新闻</span></el-breadcrumb-item>
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
        <div class ="content1"><el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="ruleForm.title"></el-input>
          </el-form-item>
          <el-form-item label="课程编号" prop="courseNumber" v-if="type">
            <el-input v-model="ruleForm.courseNumber"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="description">
            <el-input type="textarea" rows="12" v-model="ruleForm.description"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">发布</el-button>
            <el-button @click="back">返回</el-button>
          </el-form-item>
        </el-form></div>
      </el-container>
    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import NewsService from "@/services/NewsService";

export default {
  name: "emm",
  data() {
    return {
      name: '',
      type: true,
      ruleForm: {
        title: '',
        description: '',
        courseNumber: '',
      },
      rules: {
        title: [
          { required: true, message: '请输入标题！', trigger: 'blur' },
          { min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur' }
        ],
        courseNumber: [
          { required: true, message: '请输入课程编号！', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入内容！', trigger: 'change' }
        ]
      },
      // collapseBtnClick: false,
      isCollapse: true,
      isAdmin: false, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
    }
  },
  mounted() {
    this.setName()
    this.setType()
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
    submitForm(formName){
      this.$refs[formName].validate(async valid =>{
        if(valid){
          if(this.type==true) {
            const res = await NewsService.releaseNews(this.ruleForm.title, this.ruleForm.courseNumber, this.ruleForm.description);
            if (res.data.code == "200") {
              this.$message.success("发布成功！");
              this.$router.push({path: "/home"});
            } else {
              this.$message.error(res.data.msg);
            }
          }
          else {
            const res = await NewsService.releasePublicNews(this.ruleForm.title, this.ruleForm.description);
            if (res.data.code == "200") {
              this.$message.success("发布成功！");
              this.$router.push({path: "/home"});
            } else {
              this.$message.error(res.data.msg);
            }
          }
        }
        else return;
      });
    },
    setName(){
      this.name=localStorage.name;
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    setType(){
      if(localStorage.type=="3") {
        this.type = false;
        this.rules.courseNumber.required=false;
      }
      else if(localStorage.type=="2") {
        this.type = true;
        this.rules.courseNumber.required=true;
      }
    },
    resetForm(formName){
      this.$refs[formName].resetFields();
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
    back(){
        this.$router.push('/home')
    }
  }
};
</script>

<style scoped>
.content1{
  margin: 0 auto;
  width: 100%;
}
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
  margin: 0 auto;
  display: flex;
  width: 100%;
  padding: 4% 3% 1% 2%;
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
</style>