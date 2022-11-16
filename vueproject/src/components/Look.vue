<template>
<div>
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
</div>
</template>

<script>
export default {
  name: "Look"
}
</script>

<style scoped>

</style>