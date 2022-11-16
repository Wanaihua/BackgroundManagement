<template>
  <div style="font-size: 12px;line-height: 60px;display: flex">

    <div style="flex:1;font-size: 20px">
      <span :class="collapseBtnClass" style="cursor: pointer;font-size: 18px;" @click="collapse"></span>

      <el-breadcrumb separator="/" style="display: inline-block; margin-left:10px; ">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{currentPathName}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>


    <el-dropdown style="width: 100px;cursor: pointer">
      <div style="display: inline-block">
        <img :src="user.avatarUrl" alt="" style="width: 30px;border-radius: 50%;position: relative;top: 10px;right: 5px">
        <span>{{user.nickname}}</span>
        <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>

      <el-dropdown-menu slot="dropdown" style="height: 70px; width: 80px;text-align: center">
        <el-dropdown-item style="font-size: 14px;padding: 5px 0 0 0px;color: #666666;">
          <router-link to="/PersonInfo" style="text-decoration: none;color: #666666">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px;padding: 5px 5px 0 0px;">
          <span style="text-decoration: none;" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </div>
</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass:String,
    collapse:Function,
    user:Object,
  },
  computed:{
    currentPathName(){
      return this.$store.state.currentPathName; //需要监听的路径
    }
  },
  watch:{
     currentPathName(newVal,oldVal){
       console.log(newVal)
     }
  },
  data(){
    return {

    }
  },
  methods:{
    logout(){
      this.$store.commit("logout");
      this.$message.success('退出成功');
    },
  }

}
</script>

<style scoped>

</style>