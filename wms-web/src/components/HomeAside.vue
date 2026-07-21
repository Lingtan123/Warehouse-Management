<script>
export default {
  name: "HomeAside",
  data(){
    return {
      /*menu:[{
          menuClick:'Admin',
          menuName:'项目组长管理',
          menuIcon:'el-icon-s-custom',
        },{
          menuClick:'User',
          menuName:'员工管理',
          menuIcon:'el-icon-user-solid',
        }
      ]*/
    }
  },
  computed:{
    "menu":{
      get(){
        return this.$store.state.menu;
      }
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
        router
    >
      <el-menu-item index="/Home">
        <i class="el-icon-s-home"></i>
        <span slot="title">首页</span>
      </el-menu-item>

      {{ /*动态生成menu, 进行导航一和导航二的跳转*/ }}

      <el-menu-item :index="'/'+item.menuClick" v-for="(item,i) in menu" :key="i" >
        <i :class="item.menuIcon"></i>
        <span slot="title">{{item.menuName}}</span>
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
