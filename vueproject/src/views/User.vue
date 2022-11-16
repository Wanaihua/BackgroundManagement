<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email" class="ml-5"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>
      <el-button class="ml-5" type="primary" @click="query">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset" >重置</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i> </el-button>
      <el-button type="danger" @click="deleteBatch">批量删除<i class="el-icon-remove-outline"></i> </el-button>
      <el-upload
          action="http://localhost:8090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block"> <!--上传地址-->
      <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i> </el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top"></i> </el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection"  width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="username" label="姓名" width="120">
      </el-table-column>
      <el-table-column prop="role" label="角色">
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120">
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="200">
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="120">
      </el-table-column>
      <el-table-column prop="address" label="地址">
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
      <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i> </el-button>
          <el-button type="text" size="small" @click="handleDelete(scope.row.id)">删除<i class="el-icon-remove-outline"></i> </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px">
        <el-form-item label="" style="display: none">
          <el-input v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option
                v-for="item in roles"
                :key="item.name"
                :label="item.name"
                :value="item.flag">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      collapseBtnClass: "el-icon-s-fold",
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: "headerBg",
      pageNum: 1,
      pageSize: 5,
      username: "",
      address: "",
      email: "",
      total: 0,
      dialogFormVisible: false,
      multipleSelection: [],
      tableData: [],
      form:{},
      roles:[]
    }
  },
  created() {
    //分页查询
    this.query();
  },
  methods:{
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize=val;
      this.pageNum=1;
      this.query();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val;
      this.query();
    },
    query(){
      this.request.get("/user/page?",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          address:this.address,
          email:this.email
        }}).then(res=>{
        this.tableData = res.data.records;
        this.total=res.data.total;
      })
      this.request.get("/role").then(res=>{
        this.roles=res.data;
      })
    },
    reset() {
      this.username = "";
      this.address = "";
      this.email = "";
      this.query();
    },
    handleAdd(){
      this.dialogFormVisible = true;
      this.form = {};
    },
    save(){
      this.dialogFormVisible = false;
      this.request.post("/user",this.form).then(res=>{
        if(res.code==='200'){
          this.$message({
            message: '保存成功',
            type: 'success'
          });
          this.query();
        }else {
          this.$message.error('保存失败');
        }
      })
    },
    handleEdit(row){
      this.form = row;
      this.dialogFormVisible = true;
    },
    handleDelete(id){
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete("/user/"+id).then(res=>{
          if(res.code==='200'){
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.query();
          }else {
            this.$message.error('删除失败');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSelectionChange(val){
      console.log(val);
      this.multipleSelection = val;
    },
    deleteBatch() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请至少选择一条数据',
          type: 'warning'
        });
        return;
      }
      this.$confirm('此操作将永久删除这些用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = [];
        this.multipleSelection.forEach(item => {
          ids.push(item.id);
        })
        console.log(ids);
        this.request.post("/user/deleteBatch", ids).then(res => {
          if (res.code==='200') {
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.query();
          } else {
            this.$message.error('删除失败');
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    exp(){
      this.$confirm('此操作将导出用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        window.open("http://localhost:8090/user/export");
        this.$message({
          message: '导出成功',
          type: 'success'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消导出'
        });
      });
    },
    handleExcelImportSuccess(){
      this.$message({
        message: '导入成功',
        type: 'success'
      });
      this.query();
    },
  }
}
</script>

<style>
.headerBg{
  background: #eee!important;
}
.el-menu-vertical-demo:not(.el-menu--collapse){
  width: 200px;
  height: 100%;
}
.el-menu-vertical-demo{
  transition: width 1s;
}
</style>
