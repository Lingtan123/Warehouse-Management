<script>
import request from "@/api/Request";

export default {
  name: "HomeLogin",
  data() {
    return {
      confirm_disabled: false,
      loginForm:{
        no: '',
        password: '',
      },
      rules: {
        no:[
          {required: true, message: '请输入账号', trigger: 'blur'},
        ],
        password:[
          {required: true, message: '请输入密码', trigger: 'blur'},
        ]
      }
    }
  },
  methods: {
    confirm(){
      this.confirm_disabled = true
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          request.post("/user/login",this.loginForm).then(res => {
            if (res.code == 200) {
              //存储，跳转到主页
              sessionStorage.setItem("CurUser",JSON.stringify(res.data.user));
              this.$store.commit("setMenu",res.data.menu);
              this.$router.replace('/Home');
            }else{
              this.confirm_disabled = false
              alert('校验失败，请检查用户名和密码');
              return false;
            }
          })
        }else{
          this.confirm_disabled = false
          console.log('error');
          return false;
        }
      })
    }
  }
}
</script>

<template>
  <div class="loginBody">
    <div class="loginDiv">
      <div class="loginContent">
        <h1 class="login-title">用户登录</h1>
        <el-form :model="loginForm" label-width="100px" :rules="rules" ref="loginForm">
          <el-form-item label="账号" prop="no">
            <el-input style="" type="text" v-model="loginForm.no"
                      auto-complete="off" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="loginForm.password"
                      auto-complete="off" size="small" @keyup.enter.native="confirm"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="confirm" :disabled="confirm_disabled">确定</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  background: #B3C0D1;
}
.loginDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
  width: 500px;
  height: 400px;
  background: #fff;
  border-radius: 5%;
}
.loginContent {
  width: 400px;
  height: 250px;
  position: absolute;
  top: 25px;
  left: 25px;
}
.login-title{
  margin: 20px 0;
  margin-left: 50px;
  text-align: center;
}
</style>