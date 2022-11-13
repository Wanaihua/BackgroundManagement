<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%;overflow-x: hidden;"
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect"
           class="el-menu-vertical-demo">
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" style="width:20px;position: relative;top: 3px;margin-right: 5px">
      <span style="color: white" v-show="logoTextShow">后台管理系统</span>
    </div>

    <el-menu-item index="/home">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">主页</span>
      </template>
    </el-menu-item>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/user">
        <template slot="title">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/role">
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span slot="title">角色管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/menu">
        <template slot="title">
          <i class="el-icon-s-grid"></i>
          <span slot="title">菜单管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/file">
        <template slot="title">
          <i class="el-icon-document"></i>
          <span slot="title">文件管理</span>
        </template>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props:{
    isCollapse:Boolean,
    logoTextShow:Boolean
  },
  methods:{
    handleSelect() {
      this.$route.fullPath.split("/")
    }
  }
}
</script>

<style scoped>

</style>