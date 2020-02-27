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
      <el-table-column label="Version" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.version }}</template>
      </el-table-column>
      <el-table-column label="Diagram" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.diagram }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="start(scope.row.id)">启动流程</el-button>
          <router-link :to="'processes/diagram/'+scope.row.id">
            <el-button type="info">查看流程图</el-button>
          </router-link>
          <el-button type="info">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getProcesses, startProcess } from "@/api/processes";
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
      getProcesses().then(response => {
        this.list = response;
        console.log(this.list);
        this.listLoading = false;
      });
    },
    start(data) {
      this.listLoading = true;
      const id=data;
      startProcess(id).then(response => {
        this.listLoading = false;
        Message({
          message: "流程启动成功。",
          type: "info",
          duration: 5 * 1000
        });
      });
    }
  }
};
</script>
