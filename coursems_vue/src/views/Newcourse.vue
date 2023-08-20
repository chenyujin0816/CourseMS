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
          <el-breadcrumb-item @click="refresh">新建课程</el-breadcrumb-item>
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
        <el-header class="fentitle">新建课程</el-header>
        <div class="form">
          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="课程编号" prop="num">
              <el-input v-model="ruleForm.num" ></el-input>
            </el-form-item>
            <el-form-item label="课程概要" prop="name">
              <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="课程名称" prop="info">
              <el-input v-model="ruleForm.info"></el-input>
            </el-form-item>
            <el-form-item label="开课时间">
              <el-col :span="11">
                <el-form-item prop="start">
                  <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.start" style="width: 100%;"></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col class="line" :span="2"></el-col>
            </el-form-item>
            <el-form-item label="课程学分" prop="score">
              <el-input v-model="ruleForm.score"></el-input>
            </el-form-item>
            <el-form-item label="开课周数" prop="week" >
              <el-input type="age" v-model.number="ruleForm.week" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="课程密码" prop="pwd">
              <el-input v-model="ruleForm.pwd"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="submitForm('ruleForm')">申请创建</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-container>
    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import axios from "axios";
import CourseService from "@/services/CourseService";
import LoginService from "@/services/LoginService";
export default {
  name: "emm",
  data() {
    return {
      name:null,
      isCollapse: true,
      isAdmin: false, //管理部分可隐藏，只为管理员显示，当值为false时，不显示

      // 该页独有
      ruleForm: {
        num: '',
        name: '',
        info: '',
        score: '',
        start:'',
        week: '',
        pwd: '',
      },
      rules: {
        num: [
          { required: true, message: '请输入课程编号', trigger: 'blur' },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入课程名称', trigger: 'blur' }
        ],
        info: [
          { required: true, message: '请输入课程描述', trigger: 'blur'  }
        ],
        score: [
          { required: true, message: '请输入课程学分', trigger: 'blur'  }
        ],
        start: [
          { type: 'date', trigger: 'change' }
        ],
        week:[
          { type: 'number', message: '课程周数必须为数字值'}
        ],
        pwd: [
          { required: true, message: '请输入选课密码', trigger: 'blur'  }
        ],
      }
    }

  },
  mounted() {
    this.name = localStorage.name;
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
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    gettime(data) {
      let value = data.getFullYear() + '-' +
          this.checkTime(data.getMonth() + 1) + '-' +
          this.checkTime(data.getDate()) ;
      return value
    },
    checkTime(i) {
      if (i < 10) {
        i = "0" + i
      }
      return i;
    },
    // 该页独有
    submitForm(formName)
    {
      this.$refs[formName].validate(async valid =>
      {
        if (valid) {
          const date = this.gettime(this.ruleForm.start);

          const res = await CourseService.add(this.ruleForm.num, this.ruleForm.name, this.ruleForm.info,
              this.ruleForm.score, date, this.ruleForm.week, this.ruleForm.pwd);
          console.log(res)
          console.log(res.data)
          if(res.data.code=='200')
          {
            this.$message({
              type: 'success',
              message: '成功申请添加新课' + this.ruleForm.info
            })
          }
          else{
            this.$message({
              type: 'fail',
              message: res.data.msg
            })
          }
        } else {
          console.log('error submit!!');
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    refresh(){
      location.reload()
    },
  },
  setup(){
    axios.defaults.headers.common['Authorization'] = localStorage.token;
    console.log(localStorage);
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
.fentitle{
  font-size: 24px;font-weight: bold;
}

/*此页独有*/
.form{
  width: 70%;
  margin-left:180px;
  /*background-color: yellow;*/
  padding:2% 3% 4px 3%;
  margin-top:30px;
}
</style>