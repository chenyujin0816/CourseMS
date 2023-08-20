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
          <el-breadcrumb-item :to="{path: ''}" @click="back2(courseinfo,teachername)">{{ '第' + week + '周作业' }}</el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh">评分</el-breadcrumb-item>
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
        <el-header class="emm">
          <div class="leftH"> <h3>{{'课程: ' + courseinfo }}</h3>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h3>{{'讲师: ' + teachername }}</h3>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h4>第{{ week }}周</h4>&nbsp;&nbsp;<strong>|</strong>&nbsp;&nbsp;
            <h5>作业</h5>
          </div>
          <div class="rightH">
          </div>
        </el-header>
        <div class="ta">
          <el-table stripe
                    :data="list"
                    class="eltable1">
            <el-table-column
                prop="name"
                label="姓名"
                width="280">
            </el-table-column>
            <el-table-column
                prop="number"
                label="学号"
                width="350">
            </el-table-column>
            <el-table-column
                prop="filename"
                label="作业名"
                width="400">
            </el-table-column>
            <el-table-column
                prop="grade"
                label="评分"
                width="150">
            </el-table-column>
            <el-table-column  label="操作" align="center" min-width="100">
              <template v-slot="scope" v-if="seen2">
                <div>
                  <el-popconfirm v-if="scope.row.posted=='1' && scope.row.grade=='未评分'"
                                 confirmButtonText="确定"
                                 cancelButtonText="取消"
                                 icon="el-icon-info"
                                 iconColor="red"
                                 title="确认对该作业评分?"
                                 @confirm="open(scope.$index, list)"
                  >
                    <template #reference>
                      <el-button type="primary">评分</el-button>
                    </template>
                  </el-popconfirm>
                  <el-popconfirm v-if="scope.row.posted=='1' && scope.row.grade!='未评分'"
                                 confirmButtonText="确定"
                                 cancelButtonText="取消"
                                 icon="el-icon-info"
                                 iconColor="red"
                                 title="重新对该作业评分?"
                                 @confirm="open(scope.$index, list)"
                  >
                    <template #reference>
                      <el-button type="primary">重评</el-button>
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
import CourseService from "@/services/CourseService";

export default {
  name: "emm",
  data() {

    return {
      name:null,
      isCollapse: true,
      isAdmin: false, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
      activeNames: ['1'],
      input: '',
      courseinfo: '',
      teachername: '',
      week: '',
      headers: {},
      seen2 : false,
      //该页面独有
      list:[],
    }
  },
  mounted() {
    this.headers={
      'Authorization': localStorage.token
    }
    this.courseinfo=this.$route.query.courseinfo
    this.teachername=this.$route.query.teachername
    this.week=this.$route.query.week
    this.name = localStorage.name
    if(localStorage.type=='3')
      this.isAdmin=true
    if(localStorage.type=='2')
      this.seen2=true
    this.getlist(this.courseinfo,this.teachername,this.week)
  },
  methods: {
    open(index, row) {
      this.$prompt('请输入评分', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(async ({value}) => {
        let res;
        res = await CourseService.judgework(this.courseinfo, this.teachername,value,row[index].number,this.week);
        if (res.data.code == '200') {//密码和输入的value一致
          this.$message({
            type: 'success',
            message: '成功评分'
          });
          location.reload()
        } else {
          this.$message({
            type: 'error',
            message: '评分失败'+res.data.msg
          });
        }
      }).catch(() => {
      });
    },
    async getlist(courseinfo,teachername,week){
      CourseService.getworklist(courseinfo,teachername,week).then(result => {
        this.list = result.data.data.record
        console.log(this.list)
        for(var i=0;i<this.list.length;i++) {
          if(this.list[i].posted == '0')
          {
            this.list[i].filename = '未提交'
            this.list[i].grade = '/'
          }
          if(this.list[i].posted == '1')
          {
            if(this.list[i].grade == 0)
            {
              this.list[i].grade = '未评分'
            }
          }
        }
      }).catch(err => {
        console.log(err)
      })
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
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    download(){
      let link = document.createElement('a')
      link.style.display = 'none'
      link.href = 'https://www.baidu.com/';
      // http://futest.sctsjkj.com/template/消防火警联网部件设施数据表.xlsx
      link.click();
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
    },
    async back2(courseinfo,teachername){
      const res = await CourseService.mycourse(courseinfo,teachername)
      console.log(res.data)
      this.$router.push({
        path: '/my/course/homework',
        query: {
          courseinfo: res.data.data.courseinfo,
          teachername: res.data.data.teachername,
          week: this.week,
          courseId: res.data.data.courseid
        }
      })
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

.ta{
  background-color: whitesmoke ;
  width:100%;
}
</style>