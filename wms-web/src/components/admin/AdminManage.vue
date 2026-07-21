<script>
import request from '@/api/Request'

export default{
  name: "AdminManage",
  data() {
    let checkAge = (rule, value, callback) => {
      if (value > 150) {
        callback(new Error('年龄过大'));
      }else{
        callback();
      }
    };
    let checkDuplicate = (rule, value, callback) => {
      if(this.form.id){
        callback();
        return;
      }
      request.get('user/findByNo?no='+this.form.no).then(res => {
        if(res.code != 200){
          callback();
        }else{
          callback(new Error('账号已存在'));
        }
      })
    }
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name: '',
      sex: '',
      sexs: [
        {
          value: '1',
          label: '男'
        }, {
          value: '0',
          label: '女'
        },
      ],
      centerDialogVisible: false,
      form: {
        id: '',
        name: '',
        age: '',
        phone: '',
        no: '',
        password: '',
        sex: '',
        roleId: '1'
      },
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 2, max: 8, message: '长度在2-8位', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输入年龄', trigger: 'blur'},
          {min: 1, max: 3, message: '长度在1-3位', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/, message: '请输入正确年龄', trigger: 'blur'},
          {validator:checkAge, trigger:'blur'}
        ],
        phone: [
          {required: true, message: '请输入电话', trigger: 'blur'},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: '请输入正确电话', trigger: 'blur' },
        ],
        no: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '账号长度在3-8位', trigger: 'blur'},
          {validator:checkDuplicate, trigger:'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 8, message: '密码长度在3-8位', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.loadPost();
    },
    getDefaultForm() {
      return {
        id: '',
        name: '',
        age: '',
        phone: '',
        no: '',
        password: '',
        sex: '',
        roleId: '1'
      }
    },
    loadPost(){
      request.post('/user/listPageC1',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param: {
          name: this.name,
          sex: this.sex,
          roleId: '1'
        }
      }).then(res=>{
        if(res.code == 200){
          this.tableData = res.data
          this.total = res.total
        }else{
          alert("查询失败")
        }
      })
    },
    resetParam(){
      this.name = ''
      this.sex = ''
    },
    resetForm(){
      this.form = this.getDefaultForm();
      this.form.id = '';
      this.$refs.form.resetFields();
    },
    start(){
      this.resetForm();
      this.centerDialogVisible = true
    },
    isClose(done) {
      this.$confirm('确定要关闭吗')
          .then(() => {
            this.resetForm()
            this.centerDialogVisible = false
            done()
          })
          .catch(() => {})
    },
    save(){
      this.$refs.form.validate(valid => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            this.doSave();
          }
        }else{
          console.log('error')
          return false
        }
      })
    },
    doSave(){
      request.post('/user/save',this.form).then(res=>{
        console.log(res)
        if(res.code == 200){
          this.$message({
            message: '保存成功',
            type: 'success'
          })
          this.resetForm();
          this.centerDialogVisible = false
        }else{
          this.$message({
            message: '保存失败',
            type: 'error'
          })
        }
        this.loadPost();
      })
    },
    mod(row){
      this.form.id = row.id;
      this.form.name = row.name;
      this.form.age = row.age + '';
      this.form.sex = row.sex + '';
      this.form.phone = row.phone;
      this.form.no = row.no;
      this.form.password = row.password;
      this.form.roleId = row.roleId + '';
      this.centerDialogVisible = true;
    },
    doMod(){
      request.post('/user/mod',this.form).then(res=>{
        if(res.code == 200){
          this.$message({
            message: '修改成功',
            type: 'success'
          })
          this.resetForm();
          this.centerDialogVisible = false
        }else{
          this.$message({
            message: '修改失败',
            type: 'error'
          })
        }
        this.loadPost();
      })
    },
    del(id){
      request.get('/user/delete?id='+id).then(res=>{
        if(res.code == 200){
          this.$message({
            message: '删除成功',
            type: 'success'
          })
        }else{
          this.$message({
            message: '删除失败',
            type: 'error'
          })
        }
        this.loadPost();
      })
    }
  },
  beforeMount() {
    this.loadPost();
  }
}
</script>

<template>
  <div>
    <div style="margin-bottom:15px">
      <el-input v-model="name" placeholder="请输入姓名" suffix-icon="el-icon-search"
                @keyup.enter.native="loadPost"  style="width: 200px"></el-input>

      <el-select v-model="sex" filterable placeholder="请输入性别" style="margin-left: 15px; width:200px">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="warning" style="margin-left: 10px" @click="start">新增</el-button>
    </div>

    <el-table :data="tableData"
              :header-cell-style="{ background:'rgb(242,245,252)', color:'black' }"
              border
    >
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="no" label="账号" width="200">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="150">
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80">
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="80">
        <template slot-scope="scope">
          <el-tag
              :type=" scope.row.sex === 1 ? 'primary' : 'warning' "
              disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="roleId" label="角色" width="150">
        <template slot-scope="scope">
          <el-tag
              :type=" scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'success' : 'warning' ) "
              disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '项目组长' : '员工') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="220">
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button size="small" type="primary" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除这条数据吗？" @confirm="del(scope.row.id)" style="margin-left: 7px">
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>


    <el-dialog
        title="人员信息清单"
        :visible.sync="centerDialogVisible"
        :before-close="isClose"
        width="30%"
        center
    >
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-col :span="20">
            <el-input v-model="form.age"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号" prop="no">
          <el-col :span="20">
            <el-input v-model="form.no"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-radio-group v-model="form.roleId">
            <el-radio label="0">超级管理员</el-radio>
            <el-radio label="1">项目组长</el-radio>
            <el-radio label="2">员工</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<style>

</style>
