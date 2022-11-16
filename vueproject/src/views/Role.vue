<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email" class="ml-5"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>-->
      <el-button class="ml-5" type="primary" @click="query">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset" >重置</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i> </el-button>
      <el-button type="danger" @click="deleteBatch">批量删除<i class="el-icon-remove-outline"></i> </el-button>
<!--      <el-upload
          action="http://localhost:8090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block"> &lt;!&ndash;上传地址&ndash;&gt;
      <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i> </el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top"></i> </el-button>-->
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection"  width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="name" label="名称" width="120">
      </el-table-column>
      <el-table-column prop="flag" label="唯一标识" width="120">
      </el-table-column>
      <el-table-column prop="description" label="描述">
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu"></i></el-button>
          <el-button type="text" size="small" @click="handleEdit(scope.row.id)">编辑<i class="el-icon-edit"></i> </el-button>
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

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px">
        <el-form-item label="" style="display: none">
          <el-input v-model="form.id" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%" style="padding: 0 50px">
      <el-tree :props="props"
        :data="menuData"
        show-checkbox
        node-key="id"
        ref="tree"
        :default-expanded-keys="expends"
        :default-checked-keys="checks">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span><i :class="data.icon"></i>{{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "role",
  data() {
    return {
      collapseBtnClass: "el-icon-s-fold",
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: "headerBg",
      pageNum: 1,
      pageSize: 5,
      name: "",
      total: 0,
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      tableData: [],
      form:{},
      menuData: [],
      props: {
        children: 'children',
        label: 'name'
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag:''
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
      this.request.get("/role/page?",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
        }}).then(res=>{
        this.tableData = res.data.records;
        this.total=res.data.total;
      })
    },
    reset() {
      this.name = "";
      this.query();
    },
    handleAdd(){
      this.dialogFormVisible = true;
      this.form = {};
    },
    save(){
      this.dialogFormVisible = false;
      this.request.post("/role",this.form).then(res=>{
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
    saveRoleMenu(){
      this.request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res=>{
        if(res.code==='200'){
          this.$message({
            message: '保存成功',
            type: 'success'
          });
          this.menuDialogVis = false;

          //操作管理员的时候后需要重新登陆
          if(this.roleFlag==='ROLE_ADMIN'){
            this.$store.commit('setMenuData', this.$refs.tree.getCheckedNodes());
          }

          this.$store.commit("logout")
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
        this.request.delete("/role/"+id).then(res=>{
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
        this.request.post("/role/deleteBatch", ids).then(res => {
          if (res.data) {
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
    selectMenu(role){
      this.menuDialogVis = true;
      this.roleId = role.id;
      this.roleFlag=role.roleFlag;

      //请求菜单数据
      this.request.get("/menu").then(res=>{
        this.menuData = res.data;
        //把后台返回的菜单数据处理成id数组
        this.expends=this.menuData.map(v => v.id)
      })
      //请求用户拥有的菜单权限
      this.request.get("/role/roleMenu/"+this.roleId).then(res=>{
        this.checks = res.data;
        this.request.get("/menu/ids").then(r=>{
          const ids=r.data;
          ids.forEach(id =>{
            if(!this.checks.includes(id)){
              this.$refs.tree.setChecked(id,false);
            }
          })
        })
      })
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
