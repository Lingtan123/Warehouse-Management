<script>
import request from '@/api/Request'

export default{
  name: "RecordManage",
  data() {
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name: '',
      centerDialogVisible: false,
      form: {
        id: '',
        name: '',
        storage: '',
        goodstype: '',
        count: '',
        remake: '',
      },
      storageData:[],
      goodstypeData:[],
      storage:'',
      goodstype:'',
      CurUser : JSON.parse(sessionStorage.getItem('CurUser')) ,
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
    loadPost(){
      request.post('/record/list',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param: {
          name: this.name,
          storage: this.storage+'',
          goodstype: this.goodstype+'',
          roleId:this.CurUser.roleId+'',
          userId:this.CurUser.id+'',
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
    loadStorage(){
      request.get('/storage/list').then(res=>{
        if(res.code === 200){
          this.storageData = res.data
        }else{
          alert('查询失败')
        }
      })
    },
    loadGoodstype(){
      request.get('/goodstype/list').then(res=>{
        if(res.code === 200){
          this.goodstypeData = res.data
        }else{
          alert('查询失败')
        }
      })
    },
    resetParam(){
      this.name = ''
      this.storage = ''
      this.goodstype = ''
    },
    resetForm(){
      this.form.id = ''
      this.$refs.form.resetFields();
    },
    formatStorage(row){
      let temp = this.storageData.find(item=>{
        return item.id == row.storage;
      })
      return temp && temp.name;
    },
    formatGoodstype(row){
      let temp = this.goodstypeData.find(item=>{
        return item.id == row.goodstype;
      })
      return temp && temp.name;
    },
    del(id){
      request.get('/record/delete?id='+id).then(res=>{
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
    this.loadStorage();
    this.loadPost();
    this.loadGoodstype();
  }
}
</script>

<template>
  <div>
    <div style="margin-bottom:15px">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search"
                @keyup.enter.native="loadPost"  style="width: 200px"></el-input>
      <el-select v-model="storage" placeholder="请选择要查找的仓库" style="margin-left: 10px;">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodstype" placeholder="请选择要查找的分类" style="margin-left: 10px;">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>

    <el-table :data="tableData"
              :header-cell-style="{ background:'rgb(242,245,252)', color:'black' }"
              border
    >
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="goodsName" label="物品" width="150">
      </el-table-column>
      <el-table-column prop="storageName" label="仓库" width="180" >
      </el-table-column>
      <el-table-column prop="goodstypeName" label="分类" width="180">
      </el-table-column>
      <el-table-column prop="adminName" label="管理者" width="120">
      </el-table-column>
      <el-table-column prop="userName" label="具体实施者" width="120">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="120">
      </el-table-column>
      <el-table-column prop="time" label="操作时间" width="180">
      </el-table-column>
      <el-table-column prop="remake" label="备注">
      </el-table-column>

      <el-table-column prop="operate" label="操作" v-if="this.CurUser.roleId!=2">
        <template slot-scope="scope">
          <el-popconfirm title="确定删除这条数据吗？" @confirm="del(scope.row.id)" style="margin-left: 7px">
            <el-button slot="reference" size="small" type="danger" >删除</el-button>
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
  </div>
</template>

<style>

</style>
