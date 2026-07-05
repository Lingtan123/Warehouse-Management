<script>
export default {
  name: 'HomeHeader',
    data(){
      return {
        user : JSON.parse(sessionStorage.getItem('CurUser')) ,
      }
    },
  props:{
    icon: String
  },
  methods: {
    toUser(){
      console.log('toUser');
      this.$router.push('/Home');
    },
    logout(){
      console.log('logout');
      this.$confirm('确定退出登录吗？',{
        type: 'warning',
        confirmButtonText: '确定',
        center: true
      }).then(()=>{
        this.$message({
          type: 'success',
          message: '成功退出'
        })
        this.$router.push('/');
        sessionStorage.clear();
      }).catch(()=>{
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })

    },
    collapse(){
      this.$emit('doCollapse');
    },
  },
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
       {{ user.name }}
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