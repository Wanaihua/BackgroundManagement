<template>
  <div style="height:100%">
    <el-container style="min-height: 100vh;">

      <el-aside :width="sideWidth+'px'" style="background-color: rgb(238,241,246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
      </el-aside>

      <el-container>
        <el-header style="border-bottom: 1px solid #ccc;">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user" />
        </el-header>

        <el-main>
<!--          表示当前页面子路由会在router-view中显示-->
          <router-view @refreshUser="getUser"/>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue';
import Aside from '@/components/Aside';
import Header from "@/components/Header";

export default {
  components: {Header, Aside},
  data() {
          return {
            collapseBtnClass:"el-icon-s-fold",
            isCollapse:false,
            sideWidth:200,
            logoTextShow:true,
            pathName:"",
            user:{}
          }
  },
  created() {
    this.getUser();
  },
  methods:{
    collapse(){
      this.isCollapse = !this.isCollapse;
      if(this.isCollapse){
        this.sideWidth = 64;
        this.collapseBtnClass="el-icon-s-unfold"
        this.logoTextShow = false
      }else{
        this.sideWidth = 200;
        this.collapseBtnClass="el-icon-s-fold"
        this.logoTextShow = true
      }
    },
    getUser(){
      let username=localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")).username:"";
      this.request.get("/user/username/"+username).then(res=>{
        //重新赋值后台的最新User信息
        this.user=res.data;
      })
    }
  }
}
</script>
<style>
.headerBg{
  background: #eee!important;
}
.el-menu-vertical-demo:not(.el-menu--collapse){
  width: 200px;
  height: 100%;
}
.el-menu-vertical-demo{
  transition: width 1s;
}
</style>
