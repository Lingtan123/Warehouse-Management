<script>
import request from '@/api/Request'

  export default{
    name: "HomeMain",
    data() {

      return {
        tableData: []
      }
    },
    methods: {
      loadGet(){
        request.get('/user/list').then(res=>{
          if(res.code == 200){
            this.tableData = res.data
          }else{
            alert("获取数据失败")
          }
        })
      },
      loadPost(){
        request.post('/user/listP',{
          "name": "管理员"
        }).then(res=>{
          console.log(res)
        })
      }
    },
    beforeMount() {
      this.loadGet();
      this.loadPost();
    }
  }
</script>

<template>
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
      <template slot-scope="scope">
        <el-tag
            :type=" scope.row.sex === 1 ? 'primary' : 'warning' "
            disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="sex" label="性别" width="80">
    </el-table-column>
    <el-table-column prop="roleId" label="角色" width="150">
      <template slot-scope="scope">
        <el-tag
            :type=" scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'success' : 'warning' ) "
            disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '用户') }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="phone" label="电话" width="220">
    </el-table-column>
    <el-table-column prop="operate" label="操作">
      <el-button size="small" type="primary">编辑</el-button>
      <el-button size="small" type="danger">删除</el-button>
    </el-table-column>
  </el-table>
</template>

<style>

</style>