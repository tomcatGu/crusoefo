<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="index" width="95">
        <template slot-scope="scope">{{ scope.$index }}</template>
      </el-table-column>
      <el-table-column label="Id" width="110">
        <template slot-scope="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column label="Name" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Source" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.source }}</template>
      </el-table-column>
      <el-table-column label="DeploymentTime" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.deploymentTime }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="100">
　　　　<template slot-scope="scope">
　　　　　　<el-button type="text" @click="startProcess(scope.row.id)">启动流程</el-button>
　　　　　　<el-button type="info" >查看流程图</el-button>
　　　　　　<el-button type="info" >删除</el-button>
　　　　</template>
　　</el-table-column>
    </el-table>
  </div>
</template>

<script>
//import { getList } from '@/api/table'
import { getTasks } from "@/api/task";
export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "gray",
        deleted: "danger"
      };
      return statusMap[status];
    }
  },
  data() {
    return {
      list: null,
      listLoading: true
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      getTasks().then(response => {
        this.list = response;
        console.log(this.list)
        this.listLoading = false;
      });
    }
  }
};
</script>
<style lang="scss">
.app-container {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
    overflow-y: scroll;
  white-space: nowrap;
}

</style>
