<script>
import request from '@/api/Request'
import SelectUser from "@/components/user/SelectUser.vue";

export default{
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule,value,callback) => {
      if(value>9999){
        callback(new Error('数量过大'))
      }else{
        callback()
      }
    }
    let checkCountEmpty = (rule,value,callback) => {
      if(this.form1.action !== '2'){
        callback()
        return
      }
      request.post('/goods/getCount',this.form1).then(res=>{
        if(res.code != 200){
          callback(new Error('数量不足'))
        }else{
          callback()
        }
      }).catch(()=>{
        callback(new Error('校验失败'))
      })
    }
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name: '',
      //新增表单
      centerDialogVisible: false,
      //入库表单
      inDialogVisible: false,
      //二级选入库人表单
      userVisible: false,
      isAdd: false,
      isIn:false,
      isOut:false,
      form: {
        id: '',
        name: '',
        userId:'',
        userName:'',
        adminId:'',
        adminName:'',
        storage: '',
        goodstype: '',
        count: '',
        remake: '',
      },
      form1:{
        id:'',
        goodsName:'',
        count:'',
        userId:'',
        userName:'',
        adminId:'',
        remake:'',
        action: '0',
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
      rules1:{
        count:[
          {required: true, message: '请输入数量', trigger: 'blur'},
          {validator:checkCountEmpty , trigger: 'blur'}
        ]
      },
      storageData:[],
      goodstypeData:[],
      storage:'',
      goodstype:'',
      currentRow: {},
      CurUser : JSON.parse(sessionStorage.getItem('CurUser')) ,
      //tempUser是选择入库操作时在所有入库人员中被选中的人
      tempUser: {},
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
    getDefaultForm(){
      return {
        id: '',
        name: '',
        userId:'',
        userName:'',
        adminId:'',
        adminName:'',
        storage: '',
        goodstype: '',
        count: '',
        remake: '',
      }
    },
    getDefaultForm1(){
      return {
        id:'',
        goodsName:'',
        count:'',
        userId:'',
        userName:'',
        adminId:'',
        remake:'',
        action: '0',
      }
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
    selectCurrentChange(val){
      this.currentRow = val;
    },
    resetParam(){
      this.name = ''
      this.storage = ''
      this.goodstype = ''
    },
    resetForm(){
      this.form = this.getDefaultForm();
      this.form.id = ''
      this.$refs.form.resetFields();
    },
    resetInForm(){
      this.$refs.form1.resetFields();
    },
    start(){
      this.isAdd = true
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm();
        this.form.adminId = this.CurUser.id;
        this.form.adminName = this.CurUser.name;
      })
    },
    isClose(done) {
      this.$confirm('确定要关闭吗')
          .then(() => {
            if(this.$refs.form){
              this.resetForm()
              this.centerDialogVisible = false
            }
            if(this.$refs.form1){
              this.resetInForm()
              this.inDialogVisible = false
            }
            done()
          })
          .catch(() => {})
          .finally(()=>{
            this.isAdd = false
            this.isIn = false
          })
    },
    save(){
      this.$refs.form.validate(valid => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            console.log(this.form)
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
      }).finally(()=>{
        this.isAdd = false
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
            message: '删除失败，可能有出入库记录未删除，请检查对应记录',
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
    },
    selectUser(){
      this.tempUser = {};
      this.userVisible = true
    },
    confirmUser(){
      //执行入库操作
      if(this.isIn){
        this.form1.userId =this.tempUser.id;
        this.form1.userName =this.tempUser.name;
        //console.log(this.form1);
        this.userVisible = false
      }
      //执行新增操作
      if(this.isAdd){
        this.form.userId = this.tempUser.id;
        this.form.userName =this.tempUser.name;
        this.userVisible = false
      }
      //执行入库操作
      if(this.isOut){
        this.form1.userId =this.tempUser.id;
        this.form1.userName =this.tempUser.name;
        //console.log(this.form1);
        this.userVisible = false
      }
    },
    doSelectUser(val){
      console.log(val);
      this.tempUser = val;
    },
    inGoods(){
      if(!this.currentRow.id){
        alert('请选择记录')
        return;
      }
      this.$nextTick(()=>{
        if(this.$refs.form1){
          this.resetInForm();
        }
      })
      this.form1.goodsName = this.currentRow.name;
      this.form1.id = this.currentRow.id;
      this.form1.adminId = this.CurUser.id;
      this.form1.adminName = this.CurUser.name;
      this.form1.action = '1';
      this.isIn = true;
      this.inDialogVisible = true
    },
    doInGoods(){
      this.$refs.form1.validate(valid => {
        if(valid){
          request.post('/record/save',this.form1).then(res=>{
            console.log(res)
            if(res.code == 200){
              this.$message({
                message: '保存成功',
                type: 'success'
              })
              this.inDialogVisible = false
              this.resetInForm();
              this.loadPost();
            }else{
              this.$message({
                message: '保存失败',
                type: 'error'
              })
              this.resetInForm();
              this.loadPost();
            }
          }).finally(() => {
            this.isIn = false;
            this.isOut = false;
          })
        }else{
          console.log('error')
        }
      })
    },
    outGoods(){
      if(!this.currentRow.id){
        alert('请选择记录')
        return;
      }
      this.$nextTick(()=>{
        if(this.$refs.form1){
          this.resetInForm();
        }
      })
      this.form1.goodsName = this.currentRow.name;
      this.form1.id = this.currentRow.id;
      this.form1.adminId = this.CurUser.id;
      this.form1.adminName = this.CurUser.name;
      this.form1.action = '2';
      this.isOut = true;
      this.inDialogVisible = true
    },
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
      <el-button type="primary" style="margin-left: 10px" @click="start" v-if="this.CurUser.roleId!=2">新增</el-button>
      <el-button type="warning" style="margin-left: 10px" @click="inGoods" v-if="this.CurUser.roleId!=2">入库</el-button>
      <el-button type="warning" style="margin-left: 10px" @click="outGoods" v-if="this.CurUser.roleId!=2">出库</el-button>
    </div>

    <el-table :data="tableData"
              :header-cell-style="{ background:'rgb(242,245,252)', color:'black' }"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
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

      <el-table-column prop="operate" label="操作" v-if="this.CurUser.roleId!=2">
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
        title="物品信息清单"
        :visible.sync="centerDialogVisible"
        :before-close="isClose"
        width="30%"
        center
    >
      <el-dialog
          width="60%"
          title="选择入库人员"
          :visible.sync="userVisible"
          v-if="userVisible"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="userVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmUser">确定</el-button>
        </span>
      </el-dialog>

      <el-form ref="form" :rules="rules" :model="form" label-width="100px" style="margin-left: 12px">
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
        <el-form-item label="操作人员姓名" prop="userName">
          <el-col :span="20">
            <el-input v-model="form.userName" @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="管理人员姓名">
          <el-col :span="20">
            <el-input v-model="form.adminName" readonly></el-input>
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

    <el-dialog
        title="物品信息清单"
        :visible.sync="inDialogVisible"
        :before-close="isClose"
        width="30%"
        center
    >
      <el-dialog
        width="60%"
        title="选择操作人员"
        :visible.sync="userVisible"
        v-if="userVisible"
        append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="userVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmUser">确定</el-button>
        </span>
      </el-dialog>

      <el-form ref="form1" :rules="rules1" :model="form1" label-width="100px" style="margin-left:20px">
        <el-form-item label="物品名" prop="goodsName">
          <el-col :span="20">
            <el-input v-model="form1.goodsName" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="操作人员姓名" prop="userName">
          <el-col :span="20">
            <el-input v-model="form1.userName" @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="管理人员姓名">
          <el-col :span="20">
            <el-input v-model="form1.adminName" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form1.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注信息" prop="remake">
          <el-col :span="20">
            <el-input type="textarea" v-model="form1.remake"></el-input>
          </el-col>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button @click="inDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="doInGoods">确定</el-button>
        </span>
    </el-dialog>

  </div>
</template>

<style>

</style>
