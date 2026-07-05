<script>
import DateUtils from "./DateUtils";

export default {
  name: "HomeHome",
  components: {
    DateUtils
  },
  data() {
    return {
      user: {
        name: "",
        no: "",
        age: "",
        phone: "",
        sex: "",
        roleId: ""
      }
    };
  },
  computed: {
    welcomeTitle() {
      return this.user.name ? "欢迎你，" + this.user.name : "个人资料";
    },
    sexLabel() {
      return Number(this.user.sex) === 1 ? "男" : "女";
    },
    sexTagType() {
      return Number(this.user.sex) === 1 ? "warning" : "danger";
    },
    sexIcon() {
      return Number(this.user.sex) === 1 ? "el-icon-male" : "el-icon-female";
    },
    roleLabel() {
      const roleId = Number(this.user.roleId);
      return roleId === 0 ? "超级管理员" : (roleId === 1 ? "管理员" : "用户");
    }
  },
  methods: {
    init() {
      const storedUser = sessionStorage.getItem("CurUser");
      if (!storedUser) {
        return;
      }

      try {
        this.user = {
          ...this.user,
          ...JSON.parse(storedUser)
        };
      } catch (error) {
        console.error("读取当前用户信息失败", error);
      }
    },
    formatValue(value) {
      return value !== undefined && value !== null && value !== "" ? value : "未填写";
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<template>
  <div class="profile-page">
    <div class="welcome-panel">
      <h1>个人中心</h1>
      <p>欢迎来到集成仓库管理系统</p>
    </div>

    <el-card shadow="never" class="profile-card">
      <el-descriptions :title="welcomeTitle" :column="2" border>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            姓名
          </template>
          {{ formatValue(user.name) }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-custom"></i>
            账号
          </template>
          {{ formatValue(user.no) }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            电话
          </template>
          {{ formatValue(user.phone) }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-date"></i>
            年龄
          </template>
          {{ formatValue(user.age) }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-edit-outline"></i>
            性别
          </template>
          <el-tag :type="sexTagType" disable-transitions>
            <i :class="sexIcon"></i>
            {{ sexLabel }}
          </el-tag>
        </el-descriptions-item>

        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            角色
          </template>
          <el-tag type="success" disable-transitions>
            {{ roleLabel }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div class="time-panel">
      <DateUtils />
    </div>
  </div>
</template>

<style scoped>
.profile-page {
  width: 100%;
  min-height: 100%;
  padding: 24px;
  box-sizing: border-box;
  background-color: #f5f7fa;
}

.welcome-panel {
  margin-bottom: 20px;
}

.welcome-panel h1 {
  margin: 0;
  font-size: 28px;
  color: #303133;
}

.welcome-panel p {
  margin: 10px 0 0;
  color: #606266;
}

.profile-card {
  border-radius: 12px;
}

.time-panel {
  margin-top: 20px;
  padding: 16px 20px;
  border-radius: 12px;
  background-color: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}
</style>
