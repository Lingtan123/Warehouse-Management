<script>
export default {
  name: "HomeAside",
  data(){
    return {
    }
  },
  props:{
    isCollapsed:Boolean
  }
}
</script>

<template>
    <el-menu
        background-color="#545c64"
        text-color="#fff" active-text-color="#ffd04b"
        default-active="/Home"
        class="aside-menu"
        :collapse="isCollapsed"
        :collapse-transition="false"
    >
      <el-menu-item index="/Home">
        <i class="el-icon-s-home"></i>
        <span slot="title">首页</span>
      </el-menu-item>

      <el-menu-item index="/One">
        <i class="el-icon-s-flag"></i>
        <span slot="title">导航一</span>
      </el-menu-item>

      <el-menu-item index="/Two">
        <i class="el-icon-s-opportunity"></i>
        <span slot="title">导航二</span>
      </el-menu-item>
    </el-menu>
</template>

<style>
.aside-menu {
  height: 100%;
  width: 100%;
  border-right: none;
  margin-left: 0;
}
</style>