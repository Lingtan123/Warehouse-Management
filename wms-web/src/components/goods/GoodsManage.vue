<script>
import request from '@/api/Request'

export default{
  name: "GoodsManage",
  data() {
    let checkCount = (rule,value,callback) => {
      if(value>9999){
        callback(new Error('数量过大'))
      }else{
        callback()
      }
    }
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
      rules: {
        name: [
          {required: true, message: '请输入物品名 ', trigger: 'blur'},
          {min: 2, max: 8, message: '长度在2-8位', trigger: 'blur'}
        ],
        storage: [
          {required: true, message: '请选择仓库 ', trigger: 'blur'},
        ],
        goodstype: [
          {required: true, message: '请选择分类 ', trigger: 'blur'},
        ],
        count:[
          {required: true, message: '请输入数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数',trigger: 'blur'},
          {validator:checkCount , trigger: 'blur'}
        ]
      },
      storageData:[],
      goodstypeData:[],
      storage:'',
      goodstype:'',
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
      request.post('/goods/listPageC',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param: {
          name: this.name,
          storage: this.storage+'',
          goodstype: this.goodstype+'',
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
    start(){
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
      })
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
      request.post('/goods/save',this.form).then(res=>{
        console.log(res)
        if(res.code == 200){
          this.$message({
            message: '保存成功',
            type: 'success'
          })
          this.centerDialogVisible = false
          this.resetForm();
          this.loadPost();
        }else{
          this.$message({
            message: '保存失败',
            type: 'error'
          })
          this.resetForm();
          this.loadPost();
        }
      })
    },
    mod(row){
      this.form.id = row.id;
      this.form.name = row.name;
      this.form.storage = row.storage+'';
      this.form.goodstype = row.goodstype+'';
      this.form.count = row.count+'';
      this.form.remake = row.remake;
      this.centerDialogVisible = true;
    },
    doMod(){
      request.post('/goods/mod',this.form).then(res=>{
        if(res.code == 200){
          this.$message({
            message: '修改成功',
            type: 'success'
          })
          this.centerDialogVisible = false
          this.resetForm();
          this.loadPost();
        }else{
          this.$message({
            message: '修改失败',
            type: 'error'
          })
          this.resetForm();
          this.loadPost();
        }
      })
    },
    del(id){
      request.get('/goods/delete?id='+id).then(res=>{
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
      <el-button type="warning" style="margin-left: 10px" @click="start">新增</el-button>
    </div>

    <el-table :data="tableData"
              :header-cell-style="{ background:'rgb(242,245,252)', color:'black' }"
              border
    >
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="name" label="物品" width="150">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="分类" width="180" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="remake" label="备注">
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
        title="仓库信息清单"
        :visible.sync="centerDialogVisible"
        :before-close="isClose"
        width="30%"
        center
    >
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库名" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" placeholder="请选择仓库" style="margin-left: 10px;">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="分类名" prop="goodstype">
          <el-col :span="20">
            <el-select v-model="form.goodstype" placeholder="请选择分类" style="margin-left: 10px;">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注信息" prop="remake">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remake"></el-input>
          </el-col>
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
