<template xmlns:v-bind="http://xmlns.jcp.org/xml/ns/javaee">
  <div class="div1"></div>
  <div class="div2"></div>
  <div class="div3"></div>
<!--  <div class="register_container">-->
  <el-container class="container">
    <div class="register_box">
      <div style="color: #3c3c3d;position: absolute;top:15px;left:10px;font-size: 16px" @click="back">
        <p style="margin: 0;padding: 0;cursor: pointer;font-family: 幼圆;font-weight: bold">
          <i class="el-icon-arrow-left"></i>返回
        </p>
      </div>
      <div class="avatar_box" >
        <img id="img" src="../assets/XiaoHui.png" alt />
      </div>
      <el-form
          ref="registerFormRef"
          :model="this"
          :rules="registerRules"
          class="register_form"
          label-width="0px"
      >
        <div style="font-size: xx-large;">注册</div>
        <p></p>
        <el-form-item prop="username">
          <el-input v-model="username" placeholder="请输入学号" oninput="value=value.replace(/\D/g,'')"></el-input>
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="name" placeholder="请输入姓名"></el-input>
        </el-form-item>

        <el-form-item style="margin-top: -20px;margin-bottom: -5px">
          <el-radio-group v-model="type">
            <el-radio label="教师"></el-radio>
            <el-radio label="学生"></el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item prop="password">
          <el-input  v-model="password" @input="checkPassword" placeholder="请输入密码" type="password"></el-input>
        </el-form-item>
        <ul >
          <div >密码强度:{{password_strength}}<p></p></div>
          <li v-bind:class="{ is_valid: contains_eight_to_sixteen_characters }">长度为8-16个字符</li>
          <li v-bind:class="{ is_valid: contains_number }">包含数字</li>
          <li v-bind:class="{ is_valid: contains_Upper_and_lower_case_letters }">包含大小写字母</li>
          <li v-bind:class="{ is_valid: contains_special_character }">包含特殊符号</li>
        </ul>
        <div class="checkmark_container" v-bind:class="{ show_checkmark: valid_password }">
          <svg width="50%" height="50%" viewBox="0 0 140 100">
            <path class="checkmark" v-bind:class="{ checked: valid_password }" d="M10,50 l25,40 l95,-70" />
          </svg>
        </div>
        <el-form-item prop="password2">
          <el-input v-model="password2" @input="checkPassword2" placeholder="确认密码" type="password"></el-input>
        </el-form-item>

        <el-form-item class="btns">
          <el-button type="primary" @click="register">注册</el-button>
          <el-button type="danger" @click="resetregisterForm">重置</el-button>
        </el-form-item>

      </el-form>
    </div>
  </el-container>
<!--  </div>-->
</template>


<script lang="js">
import LoginService from "@/services/LoginService";

export default {
  name: "message",
  data() {
    return {
      username:null,
      name:null,
      type:"教师",
      password: null,
      password2:null,
      password_length: 0,
      contains_eight_to_sixteen_characters: false,
      contains_number: false,
      contains_Upper_and_lower_case_letters: false,
      contains_special_character: false,
      valid_password: false,
      pwd_s:0,
      registerRules: {
        name: [
          { required: true, message: "请输入姓名", trigger: "blur" },
        ],
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 8, max: 16, message: "长度在 8 到 16 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 8, max: 16, message: "密码为 8~16 位", trigger: "blur" }
        ],
        password2: [
          { required: true, message: "请确认密码", trigger: "blur" },
          { min: 8, max: 16, message: "两次密码需要一致", trigger: "blur" }
        ]
      }
    }
  },
  methods: {
    back(){
      this.$router.push({ path: "/"});
    },
    checkPassword() {
      this.password_length = this.password.length;
      const format = /[ !@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/;

      if (this.password_length >= 8 && this.password_length <= 16) {
        this.contains_eight_to_sixteen_characters = true;
      } else {
        this.contains_eight_to_sixteen_characters = false;
      }

      this.contains_number = /\d/.test(this.password);
      this.contains_Upper_and_lower_case_letters = /[A-Za-z]/.test(this.password);
      this.contains_special_character = format.test(this.password);
      this.pwd_s = this.contains_eight_to_sixteen_characters+this.contains_number+this.contains_Upper_and_lower_case_letters+this.contains_special_character;
      if (this.contains_eight_to_sixteen_characters === true)
      {
        if(this.pwd_s == 2)
        {
          this.valid_password = false;
          this.password_strength = "弱"
        }
        else if(this.pwd_s == 3)
        {
          this.valid_password = true;
          this.password_strength = "中"
        }
        else if(this.pwd_s == 4)
        {
          this.valid_password = true;
          this.password_strength = "强"
        }
      }
      else
      {
        this.password_strength = ""
        this.valid_password = false
      }
    },
    checkPassword2(){
      //  检查两次密码是否一致
    },
    resetregisterForm() {
      this.$refs.registerFormRef.resetFields();
    },
    register() {
      this.$refs.registerFormRef.validate(async valid => {
        if (!valid) return;
        var userType;
        if(this.type=="教师")
          userType="2"
        else if(this.type=="学生")
          userType="1"
        const res = await LoginService.register(
            this.username,
            this.name,
            this.password,
            this.password2,
            userType
        );
        if (res.data.code=="200") {
          // window.sessionStorage.setItem('flag','ok'); // session 放置
          this.$message.success("注册成功，请等待审核通过");
          this.$router.push({ path: "/"});
        }else{
          this.$message.error(res.data.msg);
        }
      });
    },
  },
};
</script>

<style scoped>

h2 {
  text-align: center;
  color: #000000;
  font-weight: 400;
}

ul {
  padding-left: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

li {
  margin-bottom: 8px;
  color: #525f7f;
  position: relative;
}

li:before {
  content: "";
  width: 0%; height: 2px;
  background: #2ecc71;
  position: absolute;
  left: 0; top: 50%;
  display: block;
  transition: all .6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

input[type="password"] {
  line-height: 1.5;
  display: block;
  color: rgba(136, 152, 170, 1);
  font-weight: 300;
  width: 100%;
  height: calc(2.75rem + 2px);
  padding: .625rem .75rem;
  border-radius: .25rem;
  background-color: #fff;
  transition: border-color .4s ease;
  border: 1px solid #cad1d7;
  outline: 0;
}

input[type="password"]:focus {
  border-color: rgba(50, 151, 211, .45);
}


/* Checkmark & Strikethrough --------- */

.is_valid { color: rgba(136, 152, 170, 0.8); }
.is_valid:before { width: 100%; }

.checkmark_container {
  border-radius: 50%;
  position: absolute;
  top: 300px; right: 20px;
  background: #2ecc71;
  width: 30px; height: 30px;
  visibility: hidden;
  opacity: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity .4s ease;
}

.show_checkmark {
  visibility: visible;
  opacity: 1;
}

.checkmark {
  width: 100%;
  height: 100%;
  fill: none;
  stroke: white;
  stroke-width: 15;
  stroke-linecap: round;
  stroke-dasharray: 180;
  stroke-dashoffset: 180;
}

.checked { animation: draw 0.5s ease forwards; }

@keyframes draw {
  to { stroke-dashoffset: 0; }
}

.container {
  /*display: flex;*/
  width: 100%;
  padding: 1% 2%;
  box-sizing: border-box;
  height: 100vh;
}

.register_box {

  min-width: 500px;
  height: 88%;
  border-radius: 3px;
  position: relative;
  top: 60px;
  margin: 0 auto;
  /*transform: translate(-50%, -50%);*/
  background-color: rgba(255,255,255,0.90);
}
.avatar_box {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  position: relative;
  margin: 0 auto;
  top:-70px;
}
.div1{
  background:url("../assets/main_bg.jpeg") ;
  animation: changeImg1 12s;
  animation-iteration-count:infinite;
  background-size: 100% 100%;
  min-height: 74vh;
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
  min-height: 74vh;
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}
.div3{
  background:url("../assets/bg_3.png") ;
  animation: changeImg3 12s;
  animation-iteration-count:infinite;
  background-size: 100% 100%;
  min-height: 74vh;
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
img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.register_form {
  position: relative;
  top: -80px;
  padding: 10px 20px 20px 20px;
}


</style>