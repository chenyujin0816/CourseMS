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
    <el-header style="font-size: 10px" class="mh" height="50px">
      <el-container>
        <el-radio-group v-model="isCollapse"  size="small" style="margin-top:10px;">
          <el-radio-button :label="false" type="info">展开</el-radio-button>
          <el-radio-button :label="true" type="info">收起</el-radio-button>
        </el-radio-group>
        <el-breadcrumb separator-class="el-icon-arrow-right" class="bre">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: '/discuss' }">论坛</el-breadcrumb-item>
          <el-breadcrumb-item @click="refresh">{{'帖子:'+this.root.title}}</el-breadcrumb-item>
        </el-breadcrumb>
      </el-container>
    </el-header>

    <el-container class="main">
      <el-aside width=auto @mouseenter="collapseOpen"  @mouseleave="collapseClose" style="background-color: rgb(238, 241, 246)">
        <div class="menu">
          <el-menu router :default-active="$route.path"
                   background-color="white"
                   class="el-menu-vertical-demo"
                   active-text-color="lightsteelblue "
                   @open="handleOpen"
                   @close="handleClose"
                   :collapse="isCollapse"
                   default-openeds="['3']"  >
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
        <el-container>
          <div class="block1">
            <el-dialog title="回帖" v-model="dialog2FormVisible">
              <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
                <el-form-item label="内容" prop="content">
                  <el-input type="textarea" rows="18" v-model="ruleForm.content"></el-input>
                </el-form-item>
              </el-form>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialog2FormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="submitComment('ruleForm')">确 定</el-button>
                </span>
              </template>
            </el-dialog>
            <el-card class="box-card">
              <div style="text-align: right">
                <el-button type="danger" icon="el-icon-circle-plus" circle @click="dialog2FormVisible=true"></el-button>
              </div>
              <span>
                <h4 style="text-align: left;margin-left: 10px;margin-top: 0px">{{ this.root.title }}</h4>
                <p style="text-align: left;margin-left: 10px;">{{ this.root.description }}</p>
                <p style="text-align: right;margin-bottom: 0px">{{this.root.creatorName}} 发布于 {{ this.root.createTime }}</p>
              </span>

            </el-card>
            <div class="block">
              <el-timeline :reverse="reverse" class="bloc">
                <el-timeline-item v-for="(comment,index) in comments" placement="bottom">
                  <el-card shadow = "hover" class="reply">
                    <div class="replyPlus">
                      <el-dropdown>
                        <i class="el-icon-circle-plus"></i>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item @click="firstReply(comment.commentatorId,comment.id)">回复层主</el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                    <div style="padding: 14px;">
                      <span>
                        <p style="text-align: left;margin-left: 10px;margin-top: -10px">{{ comment.content }}</p>
                        <p style="text-align: right;margin: 100px 0px 0px 10px;">{{index+2}}楼<br><br>{{ comment.creatorName }} 回复于 {{ comment.createTime }}</p>
                      </span>
                      <el-table
                          :data="replies[index]"
                          @row-click="showRow"
                          empty-text="暂无回复"
                          v-if="replies[index] !== undefined&&replies[index].length!=0"
                          style="width: 100%">
                        <el-table-column
                            prop="replyString"
                            label="回复"
                            width="940">
                        </el-table-column>
                        <el-table-column
                            prop="createTime"
                            label=""
                            width="155">
                        </el-table-column>
                      </el-table>
                      <el-dialog title="回复" v-model="dialogFormVisible" class="replyDialog">
                        <el-form :model="replyForm"
                                 :rules="rules"
                                 ref="replyForm"
                                 @keyup.enter.native="submitReply('replyForm',this.currentToId,this.currentCommentId)">
                          <el-form-item label="回复内容" prop="content">
                            <el-input v-model="replyForm.content"></el-input>
                            <input itype="text" v-show="false" />
                          </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                          <el-button @click="dialogFormVisible = false">取 消</el-button>
                          <el-button type="primary" @click="submitReply('replyForm',this.currentToId,this.currentCommentId)">确 定</el-button>
                        </div>
                      </el-dialog>
                    </div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
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
import NewsService from "@/services/NewsService";

export default {
  name: "emm",
  data() {
    return {
      dialog2FormVisible:false,
      dialogFormVisible: false,
      replyFormVisible: false,
      currentToId: 0,
      currentCommentId: 0,
      name: null,
      postId: 0,
      comments: [],
      replies: [],
      root: {},
      ruleForm: {
        content: ''
      },
      replyForm:{
        content: ''
      },
      rules: {
        content: [
          { required: true, message: '请输入内容！', trigger: 'change' }
        ]
      },

      input: '',
      options: [{
        value: 'true ',
        label: 'true'
      }, {
        value: 'false',
        label: 'false'
      }],
      value: '',
      formInline: {
        user: '',
      },
      currentDate: new Date(),

      // collapseBtnClick: false,
      isCollapse: true,
      isAdmin: false

    }
  },
  async mounted() {
    this.setName()
    if(localStorage.type=='3')
      this.isAdmin=true
    this.postId = await this.$route.query.postId
    await this.getRoot(this.postId)
    await this.getComments(this.postId)
    for(var i=0;i<this.comments.length;i++)
      this.comments[i].creatorName = await this.getName(this.comments[i].commentatorId);

    for(var i=0;i<this.comments.length;i++) {
      await this.getReplies(this.comments[i].id)
    }

    for(var i=0;i<this.replies.length;i++){
      for(var j=0;j<this.replies[i].length;j++){
        this.replies[i][j].fromName=await this.getName(this.replies[i][j].fromId)
        this.replies[i][j].toName=await this.getName(this.replies[i][j].toId)
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
        var date = new Date(this.replies[i][j].createTime);
        this.replies[i][j].createTime=date.format("yyyy-MM-dd hh:mm:ss")
        this.replies[i][j].replyString=this.replies[i][j].fromName+' 回复 '
            +this.replies[i][j].toName+'：'
            +this.replies[i][j].content
      }
      this.replies[i].size=this.replies[i].length
    }
    console.log(this.replies)
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
    async getRoot(id){
      const res=await ForumService.getPost(id)
      if (res.data.data) {
        this.root=res.data.data
      }
      const nameRes= await LoginService.getName(this.root.creatorId);
      this.root.creatorName=nameRes.data.data
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
      var date = new Date(this.root.createTime);
      this.root.createTime=date.format("yyyy-MM-dd hh:mm:ss")
    },
    setName(){
      this.name=localStorage.name;
    },
    async getComments(postId){
      const res = await ForumService.getComments(postId);
      this.comments=res.data.data.records;
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
      for (var i=0;i<this.comments.length;i++){
        var date = new Date(this.comments[i].createTime);
        this.comments[i].createTime=date.format("yyyy-MM-dd hh:mm:ss")
      }
    },
    async getReplies(commentId){
      const res = await ForumService.getReplies(commentId);
      var reply=[];
      reply=res.data.data;///////////////////////////////////////////////
      console.log(reply)
      Date.prototype.format = function (format) {
        var o = {
          "M+": this.getMonth() + 1,
          "d+": this.getDate(),
          "h+": this.getHours(),
          "m+": this.getMinutes(),
          "s+": this.getSeconds(),
          "q+": Math.floor((this.getMonth() + 3) / 3),
          "S": this.getMilliseconds()
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(format))
          format = format.replace(RegExp.$1,
              RegExp.$1.length == 1 ? o[k] :
                  ("00" + o[k]).substr(("" + o[k]).length));
        return format;
      }
      for (var i=0;i<reply.length;i++){
        var date = new Date(reply[i].createTime);
        reply[i].createTime=date.format("yyyy-MM-dd hh:mm:ss")
      }
      this.replies.push(reply)

    },
    async getName(userId){
      const res = await LoginService.getName(userId);
      return res.data.data;
    },
    submitComment(formName){
      this.$refs[formName].validate(async valid =>{
        if(valid){
          const res = await ForumService.comment(this.ruleForm.content, localStorage.id, this.postId);
          if (res.data.code == "200") {
            this.$message.success("回帖成功");
            location.reload()
          } else {
            this.$message.error(res.data.msg);
          }
        }
        else return;
      });
    },
    submitReply(formName,toId,commentId){
      this.$refs[formName].validate(async valid =>{
        if(valid){
          const res = await ForumService.reply(this.replyForm.content, toId, commentId);
          if (res.data.code == "200") {
            this.$message.success("回复成功");
            location.reload()
          } else {
            this.$message.error(res.data.msg);
          }
        }
        else return;
      });
    },
    firstReply(toId,commentId){
      this.dialogFormVisible=true
      this.currentToId=toId
      this.currentCommentId=commentId
    },
    showRow(row){
      this.dialogFormVisible=true
      this.currentToId=row.fromId
      this.currentCommentId=row.parentId
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    refresh(){
      location.reload()
    }
  }
};
</script>

<style scoped>
.row2{
  margin-left: 5%;
  margin-right: 5%;
  margin-top: 2%;
}

.box-card {
  margin-left: 70px;
  margin-bottom: 20px;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.bloc{
  margin-top: 20px;
  padding-top: 20px;
}
.clearfix:after {
  clear: both
}
.huifu{
  margin-right: 0px;
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
  padding-right: 30px;
}
.leftH >h1{
  margin-left:10px;
}
.rightH >span{
  margin-left: 10px;
  margin-right:15px;
}
span {
  padding-left: 10px;
}

.row{
  margin-left: 800px;
  height: 80px;
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
  display: block;
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
  /*height: 100vh;*/
}
.block1{
  width: 100%;
  /*height: 960px;*/
}
.block{
  width: 100%;
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
.replyPlus{

  position: absolute;
  right: 20px;
  top: 20px;
}
.el-icon-circle-plus{
  font-size: 25px;
}
.box:hover { flex: 1 1 50%; }
.box:hover > img {
  width: 100%;
  height: 100%;
}
.replyDialog{
  text-align: left;
}
</style>