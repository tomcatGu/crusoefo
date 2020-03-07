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
        <template slot-scope="scope">{{ (page-1)*pageSize+scope.$index }}</template>
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
          　　　　　　<el-button type="info">查看流程图</el-button>
          　　　　　　<el-button type="info">删除</el-button>
        　　　　</template>
      　　</el-table-column>
    </el-table>
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[1, 2, 3, 5, 10]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="listCount"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
// import { getList } from '@/api/table'
import { getTasks, getTasksCount } from '@/api/task'
export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listCount: 0,
      page: 1,
      pageSize: 2,
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      const params = {
        firstResult: (this.$data.page - 1) * this.$data.pageSize,
        maxResults: this.$data.pageSize
      }
      getTasksCount(params).then(response => {
        this.$data.listCount = response.count
      })
      getTasks(params).then(response => {
        this.list = response
        console.log(this.list)
        this.listLoading = false
      })
    },
    handleCurrentChange(page) {
      this.$data.page = page
      this.fetchData()
      console.log(this.$data.list)
    },
    handleSizeChange(pageSize) {
      this.$data.pageSize = pageSize
      this.fetchData()
    }
  }
}
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
