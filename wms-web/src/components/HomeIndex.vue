<script>
import HomeAside from "@/components/HomeAside.vue";
import HomeHeader from "@/components/HomeHeader.vue";

export default {
  name: "HomeIndex",
  components: {HomeAside,HomeHeader},
  data(){
    return {
      isCollapsed: false,
      aside_width: '300px',
      icon: 'el-icon-s-fold',
    }
  },
  methods: {
    doCollapse(){
      this.isCollapsed = !this.isCollapsed;
      if(this.isCollapsed) {
        this.aside_width = '64px';
        this.icon = "el-icon-s-unfold"
      }
      else {
        this.aside_width = '300px'
        this.icon = "el-icon-s-fold"
      }
    }
  }
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside :width="aside_width" class="layout-aside">
      <HomeAside :isCollapsed="isCollapsed"></HomeAside>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <HomeHeader @doCollapse="doCollapse" :icon="icon"></HomeHeader>
      </el-header>

      <el-main class="layout-main">
        <router-view/>
      </el-main>
    </el-container>

  </el-container>

</template>

<style>
 .layout-container {
   height: 100%;
 }
.layout-aside {
  height: 100%;
  background-color: rgb(238, 241, 246);
}
.layout-header {
  text-align: right;
  font-size: 12px;
  border-bottom: rgba(169, 169, 169, 0.3) 1px solid;
  color: #333;
  line-height: 60px;
}
.layout-main {
  padding: 5px;
}
</style>

