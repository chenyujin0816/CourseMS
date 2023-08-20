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
          <el-breadcrumb-item>消息</el-breadcrumb-item>
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
            <el-menu-item index="/message"><i class="el-icon-message-solid"></i><span>消息</span></el-menu-item>
            <el-menu-item index="/manage"><i class="el-icon-s-operation"></i><span>管理</span></el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      <el-container class="table">
        <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick" style="width: 100%">
          <el-tab-pane label="注册申请" name="first">
            <el-table empty-text="暂无申请" :data="regApplies" style="text-align: center;width: 100%" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
              <el-table-column prop="username" label="学号/工号" style="width: max-content">
              </el-table-column>
              <el-table-column prop="name" label="姓名" style="width: max-content">
              </el-table-column>
              <el-table-column prop="type" label="身份" style="width: max-content">
              </el-table-column>
              <el-table-column prop="action" label="处理" style="width: max-content">
                <template v-slot="scope">
                  <div v-if="scope.row.show">
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认通过吗？"
                        @confirm="confirmEvent1(scope.row.id,scope.$index)"
                    >
                      <template #reference>
                        <el-button type="primary">通过</el-button>
                      </template>
                    </el-popconfirm>
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认拒绝吗？"
                        @confirm="confirmEvent1_(scope.row.id,scope.$index)"
                    >
                      <template #reference>
                        <el-button type="danger">拒绝</el-button>
                      </template>
                    </el-popconfirm>
                  </div>
                  <div v-else>已处理</div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="创建课程申请" name="second" >
            <el-table :data="addApplies" style="text-align: center;width: 100%" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
              <el-table-column prop="date" label="时间" style="width: max-content">
              </el-table-column>
              <el-table-column prop="teachername" label="教师姓名" style="width: max-content">
              </el-table-column>
              <el-table-column prop="courseinfo" label="创建课程名" style="width: max-content">
              </el-table-column>
              <el-table-column prop="action" label="处理" style="width: max-content">
                <template v-slot="scope">
                  <div v-if = "scope.row.show">
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认通过吗？"
                        @confirm="confirmEvent2(scope.row.courseinfo, scope.row.teachername,scope.$index)"
                    >
                      <template #reference>
                        <el-button type="primary">通过</el-button>
                      </template>
                    </el-popconfirm>
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认拒绝吗？"
                        @confirm="confirmEvent2_(scope.row.courseinfo, scope.row.teachername,scope.$index)"
                    >
                      <template #reference>
                        <el-button type="danger">拒绝</el-button>
                      </template>
                    </el-popconfirm>
                  </div>
                  <div v-else>已处理</div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="删除课程申请" name="third">
            <el-table :data="delApplies" style="text-align: center;width: 100%" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
              <el-table-column prop="date" label="时间" style="width: max-content">
              </el-table-column>
              <el-table-column prop="teachername" label="教师姓名" style="width: max-content">
              </el-table-column>
              <el-table-column prop="courseinfo" label="删除课程名" style="width: max-content">
              </el-table-column>
              <el-table-column prop="action" label="处理" style="width: max-content">
                <template v-slot="scope">
                  <div v-if = "scope.row.show">
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认通过吗？"
                        @confirm="confirmEvent3(scope.row.courseinfo, scope.row.teachername,scope.$index)"
                    >
                      <template #reference>
                        <el-button type="primary">通过</el-button>
                      </template>
                    </el-popconfirm>
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认拒绝吗？"
                        @confirm="confirmEvent3_(scope.row.courseinfo, scope.row.teachername,scope.$index)"
                    >
                      <template #reference>
                        <el-button type="danger">拒绝</el-button>
                      </template>
                    </el-popconfirm>
                  </div>
                  <div v-else>已处理</div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-container>

    </el-container>
    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import LoginService from "@/services/LoginService";
import ManageService from "@/services/ManageService";
import CourseService from "../services/CourseService";

export default {
  name: "message",
  data() {
    const item = {
      date1: '2021-08-30',
      type1: '学生',
      name1: '张三',
      username1:'1120180000',
      date2: '2021-09-01',
      name2: '李四',
      class:'计算机基础',
      date3:'2021-09-02',
      name3: '王五',
      class2:'大学物理'
    };
    return {
      visible:false,
      name:null,
      regApplies: [],
      addApplies: [],
      delApplies: [],
      // collapseBtnClick: false,
      isCollapse: true,
      isAdmin: true, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
      messageData3: Array(10).fill(item),
      activeName: 'first'
    }
  },
  mounted() {
    this.name = localStorage.name
    this.getRegList()
    this.getAddList()
    this.getDelList()
  },
  methods: {
    async getDelList() {
      const res = await CourseService.getdeletelist();
      this.delApplies = res.data.data.record;

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

      for (var i=0;i < this.delApplies.length; i++){
        var date = new Date(this.delApplies[i].date);
        this.delApplies[i].date = date.format("yyyy-MM-dd hh:mm:ss");
        this.delApplies[i].show = true;
      }

      console.log(this.delApplies);
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    async getAddList() {
      const res = await CourseService.getaddlist();
      this.addApplies = res.data.data.record;

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

      for (var i=0;i < this.addApplies.length; i++){
        var date = new Date(this.addApplies[i].date);
        this.addApplies[i].date = date.format("yyyy-MM-dd hh:mm:ss");
        this.addApplies[i].show = true;
      }

      console.log(this.addApplies);
    },
    async getRegList() {

      const res = await ManageService.getRegList();
      this.regApplies=res.data.data.records;
      for(var i=0;i<this.regApplies.length;i++) {
        if (this.regApplies[i].type == '1')
          this.regApplies[i].type='学生'
        else this.regApplies[i].type='教师'
        this.regApplies[i].show=true
      }
      console.log(this.regApplies)
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
    handleClick(tab, event) {
      console.log(tab, event);
    },
    async confirmEvent1(id,index) {
      const res=await ManageService.agree(id)
      if(res.data.code == '200')
      {
        this.regApplies[index].show=false
        this.$message({
          type: 'success',
          message: '已通过该申请'
        })
      }
      else{
        this.$message({
          type: 'fail',
          message: res.data.msg
        })
      }
    },
    async confirmEvent1_(id,index) {
      const res=await ManageService.deny(id)
      if(res.data.code == '200')
      {
        this.regApplies[index].show=false
        this.$message({
          type: 'warning',
          message: '已拒绝该申请'
        })
      }
      else{
        this.$message({
          type: 'fail',
          message: res.data.msg
        })
      }
    },
    async confirmEvent2(courseinfo, teachername, index) {
      const res = await CourseService.fixedadd(courseinfo,teachername);
      if(res.data.code =='200')
      {
        this.addApplies[index].show = false
        this.$message({
          type: 'success',
          message: '已通过该申请'
        })
      }
      else{
        this.$message({
          type: 'fail',
          message: res.data.msg
        })
      }
    },
    async confirmEvent2_(courseinfo, teachername, index) {
      const res = await CourseService.Qdelete(courseinfo,teachername);
      if(res.data.code == '200')
      {
        this.addApplies[index].show = false
        this.$message({
          type: 'warning',
          message: '已拒绝该申请'
        })
      }
      else{
        this.$message({
          type: 'fail',
          message: res.data.msg
        })
      }
    },
    async confirmEvent3(courseinfo, teachername, index) {
      const res = await CourseService.fixeddelete(courseinfo,teachername);
      if(res.data.code =='200')
      {
        this.delApplies[index].show = false
        this.$message({
          type: 'success',
          message: '已通过该申请'
        })
      }
      else{
        this.$message({
          type: 'fail',
          message: res.data.msg
        })
      }
    },
    async confirmEvent3_(courseinfo, teachername, index) {
      const res = await CourseService.fixed(courseinfo,teachername);
      if(res.data.code == '200')
      {
        this.delApplies[index].show = false
        this.$message({
          type: 'warning',
          message: '已拒绝该申请'
        })
      }
      else{
        this.$message({
          type: 'fail',
          message: res.data.msg
        })
      }
    },

    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
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
  min-height: 74vh;
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
.fentitle{
  font-size: 24px;font-weight: bold;
}
</style>
