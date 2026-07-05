<script>
import DateUtils from "./DateUtils";
export default {
  name: "HomeHome",
  components: {
    DateUtils
  },
  data(){
    return {
      user:{}
    }
  },
  computed:{

  },
  methods:{
    init(){
      this.user = JSON.parse(sessionStorage.getItem("CurUser"));
    }

  }

}

</script>

<template>
  <div style="width:100%;">欢迎来到集成仓库管理系统</h1>
  <el-descriptions :title="'欢迎你'+user.name" :column="2" size="40" border>
    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-s-custom"></i>
        账号
      </template>
      {{user.no}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-mobile-phone"></i>
        电话
      </template>
      {{user.phone}}
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-edit-outline"></i>
        性别
      </template>
      <el-tag
        :type="user.sex==1 ? 'warning' : 'danger'"
        disable-transitions><i :class="user.sex==1 ? 'el-icon-male':'el-icon-female'"></i>{{user.sex==1?"男":"女"}} </el-tag>
    </el-descriptions-item>

    <el-descriptions-item>
      <template slot="label">
        <i class="el-icon-tickets"></i>
        角色
      </template>
      <el-tag
          :type="'success'"
          disable-transitions>{{user.roleId==0?"超级管理员":(user.roleId==1?"管理员":"用户")}} </el-tag>
    </el-descriptions-item>
  </el-descriptions>
  <DateUtils></DateUtils>
  </div>
</template>

<style scoped>

</style>