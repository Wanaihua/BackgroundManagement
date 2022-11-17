<template>
  <el-card style="width: 500px;padding: 20px;">
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="原密码" prop="oldPass">
        <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>

</template>

<script>
export default {
  name: "UpdatePassWord",

  created() {
    this.getUser().then(res => {
      this.truePassWord = res.password;
      this.id=res.id;
    })
  },
  data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        user: JSON.parse(localStorage.getItem('user')),
        truePassWord:"",
        id:0,
        ruleForm: {
          oldPass:'',
          pass: '',
          checkPass: '',
        },
        form:{},
        rules: {
          oldPass: [
              { validator: validatePass, trigger: 'blur' }
          ],
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
        }
      };
    },
    methods: {
      async getUser(){
        return (await this.request.get("/user/username/"+this.user.username)).data;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.truePassWord===this.ruleForm.oldPass){
                  this.form.id=this.id;
                  this.form.password=this.ruleForm.pass;
                  console.log(this.form);
                  this.request.post("/user",this.form).then(res=>{
                    if(res.code==='200'){
                      this.$message({
                        message: '保存成功',
                        type: 'success'
                      });
                      //重定向到登录页面
                      this.$router.push('/login');
                    }else {
                      this.$message.error('保存失败');
                    }
                  })
            }else{
              this.$message.error('原密码错误');
              return false;
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
}


</script>

<style>

</style>