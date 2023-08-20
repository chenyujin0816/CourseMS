<template>
  <el-container>
    <el-header class="head">
      <el-container class="leftH">
        <img src="../assets/img.png" alt class="logo_img"/>
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
          <el-breadcrumb-item @click="refresh">论坛</el-breadcrumb-item>
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

      <el-container class="container">
        <el-header class="fentitle">论坛</el-header>
        <el-container>
          <div class="block">
            <el-row class="row" :gutter="20">
              <div style="margin-left: 80px;margin-right: 15px;margin-bottom: 20px; width: 83%">
                <el-input v-model="searchKey" placeholder="请输入内容" ></el-input>
              </div>
              <el-col :lg="1" :xs="24">
                <el-button type="primary" icon="el-icon-search" circle></el-button>
              </el-col>
              <el-col :lg="1" :xs="24">
                <div>
                  <el-button type="danger" icon="el-icon-edit" circle @click="newdis"></el-button>
                </div></el-col>
            </el-row>
            <div v-if="searchPosts.length==0" style="font-size: 20px;margin-top: 5%">无结果</div>
            <el-timeline :reverse="reverse" class="bloc">
              <el-timeline-item v-for="post in searchPosts" placement="top">
                <el-card style="cursor: pointer"  @click="todetail(post.id)" :id="post.id">
                  <div v-if="post.creatorId==id||isAdmin" style="position: absolute;right: 15px;top: 25px">
                    <el-popconfirm
                        confirmButtonText="确定"
                        cancelButtonText="取消"
                        icon="el-icon-info"
                        iconColor="red"
                        title="确认删除吗？"
                        @confirm="deletePost(post.id)"
                    >
                      <template #reference>
                        <el-button size="mini" icon="el-icon-delete" circle @click.stop=""></el-button>
                      </template>
                    </el-popconfirm>
                  </div>
                  <h4 style="text-align: left; margin-top: 10px; margin-left: 10px">{{ post.title }}</h4>
                  <p style="text-align: right;margin-bottom: 5px">{{ post.creatorName }} 发表于 {{ post.createTime }}</p>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-container>

      </el-container>

    </el-container>

    <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
  </el-container>
</template>


<script lang="js">
import ForumService from "@/services/ForumService";
import LoginService from "@/services/LoginService";

export default {
  name: "Discuss",
  data() {
    return {
      name: '',
      id: '',
      reverse: true,
      searchKey: '',
      posts: [],
      searchPosts:[],
      options: [{
        value: '选项1',
        label: '升序'
      }, {
        value: '选项2',
        label: '降序'
      }],
      value: '',
      formInline: {
        user: '',
      },

      // collapseBtnClick: false,
      isCollapse: true,
      isAdmin: false, //管理部分可隐藏，只为管理员显示，当值为false时，不显示

    }
  },
  async mounted() {
    this.id=localStorage.id
    await this.setName()
    await this.getPosts()
    if(localStorage.type=='3')
      this.isAdmin=true
    console.log(this.posts)
    for(var i=0;i<this.posts.length;i++)
      this.posts[i].creatorName = await this.getName(this.posts[i].creatorId);
    this.searchPosts=this.posts
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
    async getPosts(){
      const res = await ForumService.getPosts();
      this.posts=res.data.data.records;
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
      for (var i=0;i<this.posts.length;i++){
        var date = new Date(this.posts[i].createTime);
        this.posts[i].createTime=date.format("yyyy-MM-dd hh:mm:ss")
      }
    },
    async getName(userId){
      const res = await LoginService.getName(userId);
      return res.data.data;
    },

    setName(){
      this.name=localStorage.name;
    },
    onSubmit() {
      console.log('submit!');
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    async deletePost(postId){
      const res=await ForumService.deletePost(postId)
      if(res.data.code=="200")
        this.$message.success("删除成功")
      else
        this.$message.error("删除失败")
      this.refresh()
    },
    todetail(id){
      this.$router.push({
        path:'/discuss/detail',
        query:{
          postId: id
        }
      })
    },
    search(keywords) {
      return this.posts.filter(item =>{
        if(item.title.includes(keywords)||item.creatorName.includes(keywords)) {
          return item
        }
      })
    },
    refresh(){
      location.reload()
    },
    newdis(){
      this.$router.push('/discuss/newdis')
    }
  },
  watch: {
    searchKey: function (val) {
      if (val == '') {
        this.searchPosts = this.posts
      } else {
        this.searchPosts = this.search(val)
      }
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
.fentitle{
  font-size: 24px;font-weight: bold;
}
.leftH{
  display:flex;
  padding: 0;
  margin:0;
}
.logo{
  height: 100%;
}
/*.bloc{*/
/*  margin-top: 20px;*/
/*  overflow-y: scroll;*/
/*  height:600px;*/
/*}*/
/*.bloc::-webkit-scrollbar{*/
/*  display: none;*/
/*}*/
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
}
span {
  padding-left: 10px;
}

.row{
  margin-left: 800px;
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
.input{
  margin-right: 20px;
}
.block{
  width: 1800px;
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
.inline-block {
  display: inline-block;
}
.radio{
  margin-right: 1000px;
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