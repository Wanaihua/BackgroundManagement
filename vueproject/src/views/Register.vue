<template>
<div class="wrapper">
  <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 380px; padding: 20px; border-radius:10px ">
    <div style="margin: 20px 0; text-align: center; font-size: 24px">注册</div>
    <el-form :model="user" :rules="rules" ref="ruleForm">
      <el-form-item prop="username">
        <el-input size="medium"  style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input size="medium"  style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
      </el-form-item>
      <el-form-item prop="ConfirmPassword">
        <el-input size="medium"  style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.ConfirmPassword"></el-input>
      </el-form-item>
      <el-form-item style="margin: 10px 0; text-align: right">
        <el-button type="primary" size="small" autocomplete="off" @click="register('ruleForm')">注册</el-button>
        <el-button type="warning" size="small" autocomplete="off" @click="$router.push('Login')">返回登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 12, message: "长度在 6 到 12 个字符", trigger: "blur" }
        ],
        ConfirmPassword: [
          { required: true, message: "请再次输入密码", trigger: "blur" }
        ]
      }
    }
  },
  methods: {
    register() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if(this.user.password !== this.user.ConfirmPassword) {
            this.$message.error('两次输入密码不一致');
            return false;
          }else{
            this.request.post('/user/register', this.user).then(res => {
              if (res.code==='200') {
                localStorage.setItem('user', JSON.stringify(res.data)) // 将用户信息存储到本地
                this.$message({
                  message: '注册成功',
                  type: 'success'
                });
                this.$router.push('/Register');
              } else {
                this.$message.error(res.msg);
              }
            })
          }
        }
      });
    }
  }
}
</script>
<style>
 .wrapper {
   height: 100vh;
   background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
   overflow: hidden;
}
</style>