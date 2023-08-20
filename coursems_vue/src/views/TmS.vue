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
          <el-breadcrumb-item :to="{path: ''}" @click="back(courseinfo,teachername)">{{ courseinfo }}</el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh"><span >管理学生</span></el-breadcrumb-item>
          <!--        <el-breadcrumb-item>活动管理</el-breadcrumb-item>-->
        </el-breadcrumb></el-container>
    </el-header>
    <el-container class="main">
      <el-aside width=auto @mouseenter="collapseOpen"  @mouseleave="collapseClose" style="background-color: rgb(238, 241, 246)">
        <div class="menu">
          <el-menu router :default-active="$route.path" background-color="white" class="el-menu-vertical-demo" active-text-color="lightsteelblue " @open="handleOpen" @close="handleClose" :collapse="isCollapse" default-openeds="['3']"  ><!--        默认展开哪几项内容-->
            <el-menu-item index="/home"><i class="el-icon-s-home"></i>
              <span>首页</span></el-menu-item>
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
        <!--              <el-main class="content">-->
        <el-header class="fentitle">添加/删除学生</el-header>
        <div class="ta">
          <el-row class="row1">
            <el-col :span="2">&nbsp;</el-col>
            <el-col :span="2" class="stitle">搜索姓名</el-col>
            <el-col :span="6">
              <el-input v-model="inputName" placeholder="请输入学生名字"></el-input>
            </el-col>
            <el-col :span="3">&nbsp;</el-col>
            <el-col :span="2" class="stitle">搜索学号</el-col>
            <el-col :span="6">
              <el-input v-model="inputNumber" placeholder="请输入学生学号"></el-input>
            </el-col>
          </el-row>
          <el-table stripe
                    :data="studentsNewList"  height="550"
                    class="eltable1">
            <el-table-column
                prop="name"
                label="姓名"
                width="580">
            </el-table-column>
            <el-table-column
                prop="number"
                label="学号"
                width="600">
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="100">
              <template v-slot="scope">
                <!--              学生可见-->
                <div>
                  <el-popconfirm v-if="!scope.row.attend"
                                 confirmButtonText="确定"
                                 cancelButtonText="取消"
                                 icon="el-icon-info"
                                 iconColor="red"
                                 title="确认加入此学生？"
                                 @confirm="addstudent(scope.$index, studentsNewList)"
                  >
                    <template #reference>
                      <el-button type="primary">加入</el-button>
                    </template>
                  </el-popconfirm>
                  <el-popconfirm v-if="scope.row.attend"
                                 confirmButtonText="确定"
                                 cancelButtonText="取消"
                                 icon="el-icon-info"
                                 iconColor="red"
                                 title="确认移出此学生？"
                                 @confirm="deletestudent(scope.$index, studentsNewList)"
                  >
                    <template #reference>
                      <el-button type="danger">移出</el-button>
                    </template>
                  </el-popconfirm>
                </div>

              </template>
            </el-table-column>
          </el-table>
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

      courseinfo: "",
      teachername: "",
      weeks: "",
      studentsList:[],
      studentsNewList:[],

      inputName:'',
      inputNumber:'',
    }
  },
  mounted() {
    this.getStudentList()
    this.name = localStorage.name
    if(localStorage.type=='3')
      this.isAdmin=true
  },
  methods: {
    async getStudentList() {
      this.courseinfo = await this.$route.query.courseinfo
      this.teachername = await this.$route.query.teachername
      this.weeks = await this.$route.query.weeks
      const res = await CourseService.studentlist(this.courseinfo);
      this.studentsNewList = this.studentsList =res.data.data.record;
      for (var i = 0; i < this.studentsNewList.length; i++) {
        if (this.studentsNewList[i].attend == '0')
          this.studentsNewList[i].attend = false;
        else if (this.studentsNewList[i].attend == '1')
          this.studentsNewList[i].attend = true;
      }
      console.log(this.studentsNewList);
    },
    addstudent(index,row){
      CourseService.addstudent(this.courseinfo,row[index].number).then(result =>{
        if(result.data.code == '200')
        {
          this.$message({
            type: 'success',
            message: '已添加该学生'
          })
          row[index].attend = true;
        }
        else{
          this.$message({
            type: 'fail',
            message: result.data.msg
          })
        }
      }).catch(err => {
        console.log(err)
      })
    },
    deletestudent(index,row){
      CourseService.deletestudent(this.courseinfo,row[index].number).then(result =>{
        if(result.data.code == '200')
        {
          this.$message({
            type: 'success',
            message: '已移除该学生'
          })
          row[index].attend = false;
        }
        else{
          this.$message({
            type: 'fail',
            message: result.data.msg
          })
        }
      }).catch(err => {
        console.log(err)
      })
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
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    searchId(keywords) {
      return this.studentsList.filter(item =>{
        if(item.name.includes(keywords)&&item.number.includes(this.inputNumber)) {
          return item
        }
      })
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    searchName(keywords) {
      return this.studentsList.filter(item =>{
        if(item.number.includes(keywords)&&item.name.includes(this.inputName)){
          return item
        }
      })
    },
    refresh(){
      location.reload()
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
  watch:{
    inputName: function(val) {
      this.studentsNewList = this.searchId(val)
    },
    inputNumber: function(val) {
      this.studentsNewList = this.searchName(val)
    },
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

/*!*查询课程样式代码*!*/
.row1{
  height:60px;line-height:60px;background-color: #E9EEF3;
}
.eltable1{
  overflow: scroll;

  margin: 20px 20px;
}
.eltable1::-webkit-scrollbar{
  display: none;
}
.ta{
  overflow: scroll;
  background-color: whitesmoke ;width:1380px;
}
.ta::-webkit-scrollbar{
  display: none;
}
.stitle
{
  color:darkgray ;
  font-size: 18px;

}
.fentitle{
  font-size: 24px;font-weight: bold;
}
</style>