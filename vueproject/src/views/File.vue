<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="query">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset" >重置</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-upload
          action="http://localhost:8090/file/upload" :show-file-list="false" accept="xlsx" :on-success="handleFileUploadSuccess" style="display: inline-block"> <!--上传地址-->
        <el-button type="primary" class="ml-5">上传<i class="el-icon-top"></i> </el-button>
      </el-upload>
      <el-button type="danger" @click="deleteBatch" style="display:inline-block; margin-left: 10px;">批量删除<i class="el-icon-remove-outline"></i> </el-button>

    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection"  width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="name" label="文件名称" width="120">
      </el-table-column>
      <el-table-column prop="type" label="文件类型" width="120">
      </el-table-column>
      <el-table-column prop="size" label="文件大小(kb)" width="200">
      </el-table-column>
      <el-table-column  label="下载" width="120">
        <template slot-scope="scope">
          <el-button type="primary" @click="downLoad(scope.row.url)">下载
            <i class="el-icon-download"></i> </el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch
              v-model="scope.row.enable"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleEnable(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
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

  </div>
</template>

<script>
export default {
  name: "File",
  data(){
    return{
      tableData:[],
      name:'',
      multipleSelection: [],
      headerBg: "headerBg",
      pageNum:1,
      pageSize:5,
      total:0
    }
  },
  created() {
    this.query();
  },
  methods: {
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize = val;
      this.pageNum = 1;
      this.query();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.query();
    },
    query() {
      this.request.get("/file/page?", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records;
        this.total = res.data.total;
      })
    },
    reset() {
      this.name = "";
      this.query();
    },
    handleEdit(row) {
      this.form = row;
      this.dialogFormVisible = true;
    },
    handleDelete(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.request.delete("/file/" + id).then(res => {
          if (res.code === '200') {
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
    handleSelectionChange(val) {
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
      this.$confirm('此操作将永久删除这些文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = [];
        this.multipleSelection.forEach(item => {
          ids.push(item.id);
        })
        console.log(ids);
        this.request.post("/file/deleteBatch", ids).then(res => {
          if (res.code === '200') {
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
    handleFileUploadSuccess(res) {
      console.log(res);
      this.query();
    },
    downLoad(url) {
      window.open(url);
    },
    handleEnable(row){
      this.request.post("/file/update",row).then(res=>{
        if(res.code==='200'){
          this.$message({
            message: '操作成功',
            type: 'success'
          });
        }else{
          this.$message.error('操作失败');
        }
      })
    }
  }
}
</script>

<style scoped>
.headerBg{
  background: #eee!important;
}
</style>