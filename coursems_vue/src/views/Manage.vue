<template xmlns:el-col="http://www.w3.org/1999/html">
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
          <el-breadcrumb-item>用户管理</el-breadcrumb-item>
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

      <el-card style="width: 100%">
        <div style="margin-left: 35px;width: 90%">
          <el-row :gutter="25">
             <el-input v-model="searchKey" placeholder="请输入搜索内容" clearable></el-input>
          </el-row>
        </div>
        <el-main>
          <table style="width: 100%">
            <tr>
              <!-- 用户列表 -->
              <el-table empty-text="没有数据" :data="searchUsers.records" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="学号" prop="username"></el-table-column>
                <el-table-column label="姓名" prop="name"></el-table-column>
                <el-table-column label="密码" prop="password"></el-table-column>
                <el-table-column label="角色" prop="type" ></el-table-column>
                <el-table-column label="状态" prop="status">
                  <!-- scope.row 就是当前行的信息 -->
                  <template v-slot="scope">
                    <el-switch v-model="scope.row.status" active-value="1" inactive-value="0" @change="changeStatus($event,scope.row.id)"></el-switch>
                  </template>
                </el-table-column>
              </el-table>
            </tr>
            <tr>
              <!-- 分页 -->
              <el-pagination
                  @current-change="handleCurrentChange"
                  :current-page="this.searchUsers.current"
                  page-size="10"
                  layout=" prev, pager, next"
                  :total="this.searchUsers.total"
              ></el-pagination>
            </tr>
          </table>
        </el-main>
      </el-card>
    </el-container>
  </el-container>
  <el-footer class="foot"><h6>©2021 叫什么无所谓小组.All rights reserved.</h6> </el-footer>
</template>


<script lang="js">
import ManageService from "@/services/ManageService";
import LoginService from "@/services/LoginService";

export default {
  data() {
    return {
      name:null,
      users: [],
      searchUsers:[],
      isCollapse: true,
      searchKey:'',
      origin: [],
      allRecords:[],
      seen: true, //管理部分可隐藏，只为管理员显示，当值为false时，不显示
      // 请求数据
      total: 0, // 最大数据记录
    };
  },
  async mounted() {
    this.name = localStorage.name
    await this.getUserList(1)
    this.searchUsers=this.users
    for(var i=1;i<=this.users.pages;i++){
      const res = await ManageService.getAllUsers(i);
      this.allRecords=this.allRecords.concat(res.data.data.records)
      this.origin=this.origin.concat(res.data.data.records)
    }
    //console.log("allRecords",this.allRecords)
  },
  methods: {
    async getUserList(currentPage) {
      const res = await ManageService.getAllUsers(currentPage);
      this.users=res.data.data;
      console.log(this.users);
      for(var i=0;i<this.users.records.length;i++) {
        if (this.users.records[i].type == '1')
          this.users.records[i].type='学生'
        else this.users.records[i].type='教师'
      }
      console.log(this.users)
    },
    toMyPage(){
      this.$router.push({ path: "/my"});
    },
    // 监听pageNum改变的事件
    async handleCurrentChange(newPage) {
      for(var i=0;i<this.allRecords.length;i++) {
        if (this.allRecords[i].type == '1'||this.allRecords[i].type=='学生')
          this.allRecords[i].type='学生'
        else this.allRecords[i].type='教师'
      }
      this.searchUsers.current=newPage
      this.searchUsers.orders=[]
      this.searchUsers.pages=Math.ceil(this.allRecords.length/10)
      this.searchUsers.records = this.allRecords.slice(10*(newPage-1),10*newPage)
      this.searchUsers.searchCount=true
      this.searchUsers.size=10
      this.searchUsers.total=this.allRecords.length
      //console.log("searchUsers",this.searchUsers)

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
    // 修改用户状态
    async changeStatus(event,id) {
      if(event==0) {
        const res = await ManageService.freezeUser(id);
        console.log(res);
      }
      else {
        const res = await ManageService.unfreezeUser(id);
        console.log(res);
      }
      location.reload()
    },
    // 监听添加用户
    addDialogClosed(){
      this.$refs.addFormRef.resetFields();// 重置表单项
    },
    search(key){
      return this.origin.filter(item=>{
        if(item.username.includes(key)||item.name.includes(key)||item.type.includes(key)){
          return item
        }
      })
    }
  },
  watch: {
    searchKey: async function (val) {
      if (val == '') {
        await this.getUserList(1)
        this.searchUsers = this.users
        this.allRecords=[]
        for(var i=1;i<=this.users.pages;i++){
          const res = await ManageService.getAllUsers(i);
          this.allRecords=this.allRecords.concat(res.data.data.records)
        }
      } else {
        this.allRecords=this.search(val)
        for(var i=0;i<this.allRecords.length;i++) {
          if (this.allRecords[i].type == '1'||this.allRecords[i].type == '学生')
            this.allRecords[i].type='学生'
          else this.allRecords[i].type='教师'
        }
        this.searchUsers.current=1
        this.searchUsers.orders=[]
        this.searchUsers.pages=Math.ceil(this.allRecords.length/10)
        this.searchUsers.records = this.allRecords.slice(0,10)
        this.searchUsers.searchCount=true
        this.searchUsers.size=10
        this.searchUsers.total=this.allRecords.length
        console.log("allRecords",this.allRecords)
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
  padding-top: 1px;
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
