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
          <el-breadcrumb-item
              :to="{
            path: '/my/course',
            query:{
              courseinfo: courseInfo,
              teachername: teacherName,
              weeks: week,
              courseId: courseId
            }}">
            {{ courseInfo }}
          </el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh">图表</el-breadcrumb-item>
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
      <el-container class="table">

        <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick" style="width: 100%">
          <el-tab-pane label="学生分布" name="first" >
            <div ref="map2" style="width:1000px;height:480px;margin: 0 auto"></div>
          </el-tab-pane>
          <el-tab-pane label="成绩分布-柱" name="second">
            <div ref="map" style="width:1000px;height:480px;margin: 0 auto"></div>
          </el-tab-pane>
          <el-tab-pane label="成绩分布-饼" name="third">
            <div ref="map3" style="width:1000px;height:480px;margin: 0 auto"></div>
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
import * as echarts from 'echarts'

export default {
  name: "chart",
  data() {
    return {
      newVisible: false,
      visible:false,
      name:null,
      courseInfo: '',
      courseId: 0,
      week: 0,
      attended: 0,
      sum: 0,
      xData:[],
      yData:[],
      gradeData:[],
      A85:0,
      B75:0,
      C60:0,
      DFail:0,
      unknown: 0,
      teacherName: '',
      // collapseBtnClick: false,
      isCollapse: true,
      isAdmin: false, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
      activeName: 'first',
    }
  },
  async mounted() {
    this.name = localStorage.name
    if(localStorage.type=='3')
      this.isAdmin=true
    this.courseInfo=this.$route.query.courseInfo
    this.week=this.$route.query.week
    this.courseId=this.$route.query.courseId
    this.teacherName=this.$route.query.teacherName
    await this.getStudents()
    await this.getCount()
    await this.getGrade()
    this.initEcharts()
    this.initEcharts2()
    this.initEcharts3()
  },
  methods: {
    refresh(){
      location.reload()
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
    async getStudents(){
      //console.log("courseInfo",this.courseInfo)
      const res=await CourseService.chart2(this.courseInfo)
      for (var i=0;i<res.data.data.record.length;i++){
        this.xData.push(res.data.data.record[i].name)
        this.gradeData.push(Number(res.data.data.record[i].grade))
      }
    },
    async getCount(){
      const res=await CourseService.chart1(this.courseInfo)
      this.attended=Number(res.data.data.attended)
      this.sum=Number(res.data.data.sum)
    },
    async getGrade(){
      const res=await CourseService.chart3(this.courseInfo)
      this.A85=Number(res.data.data.A85);
      this.B75=Number(res.data.data.B75);
      this.C60=Number(res.data.data.C60);
      this.DFail=Number(res.data.data.DFail);
      this.unknown=Number(res.data.data.unknown);
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    initEcharts() {
      var echarts = require('echarts');
      var myChart = echarts.init(this.$refs.map);
      const option = {
        title: {
          text: this.courseInfo+' 的成绩分布',
          left: 'center'
        },
        tooltip: {},
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [{
          name: '成绩',
          type: 'bar',
          data: this.gradeData
        }]
      };
      myChart.setOption(option)
    },
    initEcharts2() {
      var echarts = require('echarts');
      var myChart = echarts.init(this.$refs.map2);
      const option = {
        title:{
          text: this.courseInfo+' 的选课人数占比',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: this.courseInfo,
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '34',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {value: this.sum-this.attended, name: '未选课人数'},
              {value: this.attended, name: '选课人数'}
            ]
          }
        ]
      };
      myChart.setOption(option)
    },
    initEcharts3() {
      var echarts = require('echarts');
      var myChart = echarts.init(this.$refs.map3);
      const option = {
        title:{
          text: this.courseInfo+' 的成绩分布占比',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: this.courseInfo,
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '34',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {value: this.A85, name: '85分及以上'},
              {value: this.B75, name: '75分~85分'},
              {value: this.C60, name: '60分以上'},
              {value: this.DFail, name: '不及格'},
              {value: this.unknown, name: '未评分'},
            ]
          }
        ]
      };
      myChart.setOption(option)
    },
    handleClick(tab, event) {
      console.log(tab, event);
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
/*::v-deep .el-tabs__nav-scroll{*/
/*  width: 20%!important;*/
/*  margin: 0 auto!important;*/
/*}*/
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