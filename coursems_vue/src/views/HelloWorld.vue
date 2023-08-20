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
    <el-header style=" text-align: left;font-size: 10px"  class="mh" height="50px">
      <el-container>
        <el-radio-group v-model="isCollapse"  size="small" style="margin-top:10px;">
          <el-radio-button :label="false" type="info">展开</el-radio-button>
          <el-radio-button :label="true" type="info">收起</el-radio-button>
        </el-radio-group>
        <el-breadcrumb separator-class="el-icon-arrow-right" class="bre">
          <el-breadcrumb-item :to="{path:'/home'}" @click="refresh">首页</el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh">新闻</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="tem">
          <el-dropdown>
          <i class="el-icon-circle-plus" style="font-size: 25px;bottom: 6px;position: relative" v-show="type"></i>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="tonews">发布新闻</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        </div>
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
      <el-container class="container">

        <div class="box" v-for="(box_,index) in List" :key="index">
          <h1 class="top">{{box_.num}}</h1>
          <img :src="box_.url">

          <h1 class="title_">{{box_.title}}</h1>
          <h1 class="creator">{{box_.creator}}</h1>
          <div class="content_">{{box_.content}}</div>

          <span><i class="el-icon-more"></i></span>
        </div>
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
      isCollapse: true,
      type: null,
      isAdmin: false,
      name: null,
      news: null,
      id:null,
      List:[
        {
          num:"1",
          url:"https://source.unsplash.com/1000x800",
          title:"第1条新闻标题",
          content:"第1条新闻的内容",
          creator:"123"
        },
        {
          num:"2",
          url:"https://source.unsplash.com/1000x802",
          title:"第2条新闻标题",
          content:"第2条新闻的内容",
          creator:""
        },
        {
          num:"3",
          url:"https://source.unsplash.com/1000x804",
          title:"第3条新闻标题",
          content:"第3条新闻的内容",
          creator:""
        },
        {
          num:"4",
          url:"https://source.unsplash.com/1000x806",
          title:"第4条新闻标题",
          content:"第4条新闻的内容",
          creator:""
        },
        {
          num:"5",
          url:"https://source.unsplash.com/1000x808",
          title:"第5条新闻标题",
          content:"第5条新闻的内容",
          creator:""
        }

      ]
    }
  },
  mounted() {
    // this.loginTime =localStorage.time_last_login;
    // this.id = localStorage.id;
    // // this.ifLogin();
    // this.timer = setInterval(this.confirmStatus, 1000);
    // this.confirmStatus()

    this.setType()
    this.setName()
    this.getNews()
    if(localStorage.type=='3')
      this.isAdmin=true
  },
  // beforeDestroy() {
  //   clearInterval(this.timer)
  // },
  methods: {
    // async confirmStatus() {
    //
    //   const res = await LoginService.confirmStatus(this.id)
    //   if(res.data.data=="1")
    //   {
    //     //
    //   }
    //   else {
    //     clearInterval(this.timer);
    //     this.timer = null;
    //     this.$alert('账号被封禁！', '警告', {
    //       confirmButtonText: '确定',
    //       callback: action => {
    //         localStorage.clear();
    //         this.$router.push({path:'/'})
    //         this.$message.warning("返回登录页")
    //       },
    //     })
    //   }
    //   const res1 = await LoginService.confirmLoginTime(this.id,this.loginTime);
    //   if(res1.data.data!=this.loginTime)
    //   {
    //     clearInterval(this.timer);
    //     this.timer = null;
    //     this.$alert('账号在别处登录！', '警告', {
    //       confirmButtonText: '确定',
    //       callback: action => {
    //         this.$router.push({path:'/'})
    //         this.$message.warning("返回登录页")
    //       },
    //     })
    //     localStorage.clear();
    //   }
    //
    // },
    async goInformation(){
      await this.$router.push({path: "/information"});
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
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
        this.type=false;
      else this.type=true;
    },
    setName(){
      this.name=localStorage.name;
    },
    async getNews(){
      const res = await NewsService.getNews();
      for (var i=0;i<5;i++){
        this.List[i].title=res.data.data.record[i].title;
        this.List[i].content=res.data.data.record[i].content;
        this.List[i].creator=res.data.data.record[i].creatorName;
      }

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
    tonews(){
      this.$router.push('/news')
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
.title_ {
  font-size: 30px;
  text-decoration: underline;
  transition: 1.5s;
  opacity: 0;
  margin: 0;
  padding: 0;
  text-align: left;
  position:relative;
  top:-100px;
  left: 10px;
  color: white;
}
.creator{
  font-size: 30px;
  transition: 1.5s;
  opacity: 0;
  margin: 0;
  padding: 0;
  text-align: right;
  position:relative;
  top:-100px;
  right: 40px;
  color: white;
}
.top {
  font-size: 30px;
  text-decoration: none;
  transition: 0.5s;
  opacity: 1;
  margin: 0;
  padding: 0;
  text-align: left;
  position:relative;
  top:30px;
  left: 10px;
  color: white;
}
.content_ {
  transition: 1.5s;
  opacity: 0;
  margin: 0;
  padding: 0;
  text-align: left;
  position:relative;
  top:-50px;
  left: 15px;
  color: white;
  font-size: 20px;
}

ul {
  list-style-type: none;
  padding: 0;
}
/*li {*/
/*  display: inline-block;*/
/*  margin: 0 10px;*/
/*}*/
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

.box {
  border-radius: 10px;
  flex: 1;
  overflow: hidden;
  transition: 1s;
  margin: 0 1%;
  box-shadow: 0 20px 30px rgba(0,0,0,.1);
  line-height: 0;
  cursor: pointer;
}

.box > img {
  width: 200%;
  height: calc(100% - 5vh);
  object-fit: cover;
  transition: .5s;
}

.box > span {
  background: white;
  font-size: 3vh;
  display: block;
  text-align: center;
  height: 5vh;
  line-height: 1.9;
}

.box:hover
{
  flex: 0 0 50%;/*0,0设置图片展开后继续延展的大小*/
}
.box:hover > .title_ {
  opacity: 1;
}
.box:hover > .top {
  opacity: 0;
}
.box:hover > .content_ {
  opacity: 1;
}
.box:hover > .creator {
  opacity: 1;
}
.box:hover > img {
  width: 100%;
  height: 100%;
}
.tem{
  margin-top:18px;
  position: absolute;
  right: 5vw;
  /*background-color: yellow;*/
}
</style>