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
          <el-breadcrumb-item @click="refresh">查询课程</el-breadcrumb-item>
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
        <!--              <el-main class="content">-->
        <el-header class="fentitle">查询课程</el-header>
        <el-container><div class="ta">
          <el-row class="row1">
            <el-col :span="2">&nbsp;</el-col>
            <el-col :span="2" class="stitle">搜索课程</el-col>
            <el-col :span="6">
              <el-input v-model="inputCourse" placeholder="请输入课程名字"></el-input>
            </el-col>
            <el-col :span="3">&nbsp;</el-col>
            <el-col :span="2" class="stitle">搜索教师</el-col>
            <el-col :span="6">
              <el-input v-model="inputName" placeholder="请输入教师姓名"></el-input>
            </el-col>
          </el-row>
          <el-table stripe
                    :data="studentsNewList"
                    class="eltable1">
            <el-table-column
                prop="info"
                label="课程名字"
                width="450">
            </el-table-column>
            <el-table-column
                prop="name"
                label="教师"
                width="300">
            </el-table-column>
            <el-table-column
                prop="type"
                label="状态"
                width="300">
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="100">
              <!--              管理员可见-->
              <template v-slot="scope">
                <div v-if = "seen1">
                  <el-popconfirm
                                 confirmButtonText="确定"
                                 cancelButtonText="取消"
                                 icon="el-icon-info"
                                 iconColor="red"
                                 title="确认移除此课程？"
                                 @confirm="deleteRow(scope.$index, studentsNewList)"
                  >
                    <template #reference>
                      <el-button type="primary">移除</el-button>
                    </template>
                  </el-popconfirm>
                </div>

                <!--              学生可见-->
                <div v-if="seen2">
                  <el-popconfirm v-if="scope.row.attend"
                                 confirmButtonText="确定"
                                 cancelButtonText="取消"
                                 icon="el-icon-info"
                                 iconColor="red"
                                 title="确认加入此课程？"
                                 @confirm="open(scope.$index, studentsNewList)"
                  >
                    <template #reference>
                      <el-button type="primary">加入</el-button>
                    </template>
                  </el-popconfirm>
                  <el-popconfirm v-if="!scope.row.attend">
                    <template #reference>
                      <el-button type="primary" disabled>已加入</el-button>
                    </template>
                  </el-popconfirm>
                </div>

              </template>
            </el-table-column>
          </el-table>
        </div></el-container>

      </el-container>
    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">

import CourseService from "../services/CourseService";
import {onBeforeMount} from "vue";
import LoginService from "@/services/LoginService";

export default {
  name: "Search",
  data() {
    return {
      name: '',
      isCollapse: true,
      isAdmin: false, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
      seen1: false, //管理看的
      seen2: false, //学生看的

      studentsList:[],
      studentsNewList:[],
      inputName:'',
      inputCourse:'',
    }

  },
  mounted() {
    this.name=localStorage.name
    if(localStorage.type=='3')
      this.isAdmin=true
    this.getStudentsNum()
    this.see()
  },
  methods: {
    open(index, row) {
      this.$prompt('请输入选课密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        //下面两行是对密码格式进行校验，可不要
        // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
        // inputErrorMessage: '选课密码格式不正确'
      }).then(async ({value}) => {
        let res;
        res = await CourseService.attend(row[index].info, row[index].name,value);
        if (res.data.code == '200') {//密码和输入的value一致
          this.$message({
            type: 'success',
            message: '选课成功'//可以在后面加“+value”来显示选课密码（测试是不是这个格式），正式就别加了 message: '你的密码:'+value
          });
          row[index].attend = false;
        } else {
          this.$message({
            type: 'error',
            message: '选课失败'+res.data.msg
          });
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
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
    see(){
      var ltype = localStorage.type;
      console.log(ltype);
      if(ltype == '1')
        this.seen2 = true;
      if(ltype == '3')
        this.seen1 = true;
    },
    deleteRow(index, rows) {
      CourseService.Qdelete(rows[index].info,rows[index].name).then(result =>{
        if(result.data.code == '200')
        {
          this.$message({
            type: 'success',
            message: result.data.data
          })
          rows.splice(index, 1);
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
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    getStudentsNum() {
      CourseService.getInfos().then(result => {
        this.studentsNewList = this.studentsList = result.data.data.record
        for(var i=0;i<this.studentsNewList.length;i++) {
          if (this.studentsNewList[i].status == '0')
            this.studentsNewList[i].type = '开课中'
          else if (this.studentsNewList[i].status == '1')
            this.studentsNewList[i].type = '不可选'
          else if (this.studentsNewList[i].status == '2')
            this.studentsNewList[i].type = '申请添加中'
          else if (this.studentsNewList[i].status == '3')
            this.studentsNewList[i].type = '申请删除中'

          if (localStorage.type == '1')
            if(this.studentsNewList[i].attend == '1')
              this.studentsNewList[i].attend = false;
            else this.studentsNewList[i].attend = true;
        }
        console.log(this.studentsNewList);
      }).catch(err => {
        console.log(err)
      })
    },
    searchId(keywords) {
      return this.studentsList.filter(item =>{
        if(item.info.includes(keywords)&&item.name.includes(this.inputName)) {
          return item
        }
      })
    },
    searchName(keywords) {
      return this.studentsList.filter(item =>{
        if(item.name.includes(keywords)&&item.info.includes(this.inputCourse)){
          return item
        }
      })
    },
    refresh(){
      location.reload()
    },
  },
  watch:{
    inputCourse: function(val) {
      this.studentsNewList = this.searchId(val)
    },
    inputName: function(val) {
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
.logo_img {
  height: 60px;
  width: auto;
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

.container {
  display: flex;
  width: 100%;
  padding: 1% 2%;
  box-sizing: border-box;
}
el-breadcrumb-item >span{
  font-size: 5px;
}

.main{
  margin-top: 4px;
}

.menu{
  margin:10px;
}
.sma{
  font-size: 5px;
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
.ta{
  background-color: whitesmoke ;
  width:100%;
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