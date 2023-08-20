<template>
  <div class="div1"></div>
  <div class="div2"></div>
  <div class="div3"></div>

  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <!--头像-->
        <img id="img" src="../assets/XiaoHui.png" alt />
      </div>
      <!--            添加表单-->
      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login_form"
          label-width="0px"
          @keyup.enter.native="login"
      >
        <div style="font-size: xx-large;">登录</div>
        <p></p>
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名（学号）" oninput="value=value.replace(/\D/g,'')"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" placeholder="密码" type="password"></el-input>
        </el-form-item>

        <span id="goregister" @click="goRegister">没有账号？去注册<p></p></span>


        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="danger" @click="resetLoginForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>


<script lang="js">

import LoginService from "../services/LoginService";


export default {
  name: "login",
  data() {
    return {
      id:null,
      loginForm: {
        username: "",
        password: ""
      },
      loginRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 8, max: 16, message: "长度在 8 到 16 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 8, max: 16, message: "密码为 8~16 位", trigger: "blur" }
        ]
      },

    }
  },
  mounted() {
    this.id = localStorage.id
  },
  methods: {
    ifLogin(){
      if(this.id!=null)
      {
        clearInterval(this.timer);
        this.timer = null;
        this.$alert('账号已登录！', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            this.$router.push({path:'/home'})
            this.$message.success("返回主页")
          },
        })
      }
    },
    getCurrentTime() {
      //获取当前时间并打印
      const _this = this;
      let yy = new Date().getFullYear();
      let mm = new Date().getMonth()+1;
      let dd = new Date().getDate();
      let hh = new Date().getHours();
      let mf = new Date().getMinutes()<10 ? '0'+new Date().getMinutes() : new Date().getMinutes();
      let ss = new Date().getSeconds()<10 ? '0'+new Date().getSeconds() : new Date().getSeconds();
      _this.gettime = yy+'/'+mm+'/'+dd+' '+hh+':'+mf+':'+ss;
      return _this.gettime;
    },
    goRegister(){
      this.$router.push('/regis')
    },
    resetLoginForm() {
      this.$refs.loginFormRef.resetFields();
    },
    login() {
       this.$refs.loginFormRef.validate(async valid => {
         if (!valid) return;
         // 调用post请求
         const res = await LoginService.login(this.loginForm.username, this.loginForm.password);
         if (res.data.data) {
           window.sessionStorage.setItem('flag','ok'); // session 放置
           console.log(res)
           const jwt=res.headers.authorization;
           const username=res.data.data.username;
           const name=res.data.data.name;
           const id=res.data.data.id;
           const classNumber=res.data.data.class_number;
           const type=res.data.data.type;
           const password=this.loginForm.password;
           const loginTime = this.getCurrentTime()
           await LoginService.setLoginTime(id,loginTime)
           localStorage.setItem("token",jwt)
           localStorage.setItem("username",username)
           localStorage.setItem("name",name)
           localStorage.setItem("id",id)
           localStorage.setItem("classNumber",classNumber)
           localStorage.setItem("type",type);
           localStorage.setItem("time_last_login",loginTime)
           localStorage.setItem("password",password)
           this.$message.success("登录成功！！！");
           this.$router.push({ path: "/home"});
         }else{
           this.$message.error(res.data.msg);
         }
       });
    },
  },
};
</script>

<style scoped>
.login_container {
  background-size: 100% 100%;
  height: 100vh;
}

.div1{
  background:url("../assets/main_bg.jpeg") ;
  animation: changeImg1 12s;
  animation-iteration-count:infinite;
  background-size: 100% 100%;
  width: 100vw;
  height: 100vh;
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}
.div2{
  background:url("../assets/bg_2.png") ;
  animation: changeImg2 12s;
  animation-iteration-count:infinite;
  background-size: 100% 100%;
  width: 100vw;
  height: 100vh;
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}
#goregister{
  cursor: pointer;
}
.div3{
  background:url("../assets/bg_3.png") ;
  animation: changeImg3 12s;
  animation-iteration-count:infinite;
  background-size: 100% 100%;
  width: 100vw;
  height: 100vh;
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}
@keyframes changeImg1 {
  0% {opacity: 1}
  28%{opacity: 1}
  33.3%{opacity: 0}
  94.6%{opacity: 0}
  100%{opacity: 1}
}
@keyframes changeImg2 {
  0% {opacity: 0}
  28%{opacity: 0}
  33.3%{opacity: 1}
  61.3%{opacity: 1}
  66.6%{opacity: 0}
  100%{opacity: 0}
}
@keyframes changeImg3 {
  0% {opacity: 0}
  61.3%{opacity: 0}
  66.6%{opacity: 1}
  94.6%{opacity: 1}
  100%{opacity: 0}
}
.login_box {
  width: 450px;
  height: 358px;
  background-color: rgba(255,255,255,0.90);

  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.avatar_box {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);

}
#img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.btns {
  display: flex;
  justify-content: flex-end;
}
.login_form {
  position: absolute;
  bottom: 0%;
  width: 100%;
  box-sizing: border-box;
  padding: 10px 20px 5px 20px;
}

</style>
