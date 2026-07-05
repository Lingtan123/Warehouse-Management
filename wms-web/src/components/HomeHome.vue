<script>
import DateUtils from "./DateUtils";
import request from '@/api/Request'

export default {
  name: "HomeHome",
  components: {
    DateUtils
  },
  data() {
    let checkAge = (rule, value, callback) => {
      if (value > 150) {
        callback(new Error('年龄过大'));
      }else{
        callback();
      }
    };
    return {
      centerDialogVisible: false,
      user: {
        name: "",
        no: "",
        age: "",
        phone: "",
        sex: "",
        roleId: "",
        password: "",
      },
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 2, max: 8, message: '长度在2-8位', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输入年龄', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/, message: '请输入正确年龄', trigger: 'blur'},
          {validator:checkAge, trigger:'blur'}
        ],
        phone: [
          {required: true, message: '请输入电话', trigger: 'blur'},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确电话', trigger: 'blur' },
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '密码长度在3-8位', trigger: 'blur'}
        ],
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
    },
    isClose(done) {
      this.$confirm('确定要关闭吗')
          .then(() => {
            this.centerDialogVisible = false
            done()
          })
          .catch(() => {})
    },
    mod() {
      this.centerDialogVisible = true;
    },
    save(){
      this.$refs.user.validate(valid => {
        if (valid) {
          request.post('/user/mod',this.user).then(res=>{
            if(res.code == 200){
              this.$message({
                message: '修改成功',
                type: 'success'
              })
              this.centerDialogVisible = false
            }else{
              this.$message({
                message: '修改失败',
                type: 'error'
              })
            }
            sessionStorage.setItem("CurUser", JSON.stringify(this.user));
            this.init();
          })
        }else{
          console.log('error')
          return false
        }
      })

    },
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
      <el-table-column>
        <el-button size="small" type="warning" style="margin-top: 15px" @click="mod()">编辑个人信息</el-button>
      </el-table-column>
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

    <div>
      <el-dialog
          title="个人信息"
          :visible.sync="centerDialogVisible"
          :before-close="isClose"
          width="30%"
          center
      >
        <el-form ref="user" :rules="rules" :model="user" label-width="80px">
          <el-form-item label="姓名" prop="name">
            <el-col span="20">
              <el-input v-model="user.name"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-col span="20">
              <el-input v-model="user.age"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-radio-group v-model="user.sex">
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-col span="20">
              <el-input v-model="user.password"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-col span="20">
              <el-input v-model="user.phone"></el-input>
            </el-col>
          </el-form-item>
        </el-form>

        <span slot="footer" class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
      </el-dialog>
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
