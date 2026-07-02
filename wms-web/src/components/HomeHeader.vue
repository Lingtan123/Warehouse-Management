<script>
export default {
  name: 'HomeHeader',
  props:{
    icon: String
  },
  methods: {
    toUser(){
      console.log('toUser');
    },
    logout(){
      console.log('logout');
    },
    collapse(){
      this.$emit('doCollapse');
    }
  }
}
</script>

<template>
<div style="display: flex;line-height: 60px;">

  <div style="font-size: 20px;line-height: 60px;cursor: pointer">
    <i :class="icon" @click="collapse"></i>
  </div>

  <div style="flex: 1;text-align: center;font-size: 25px;">
    <span>欢迎来到集成重工仓库管理系统</span>
  </div>

  <el-dropdown trigger="click">
    <span class="el-dropdown-link">
      王小虎
      <i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
    </span>

    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
      <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>

</div>
</template>

<style>

</style>