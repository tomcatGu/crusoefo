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
      <el-table-column label="businessKey" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.businessKey || '未命名' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ended" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.ended }}</template>
      </el-table-column>
      <el-table-column label="suspended" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.suspended }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="100">
        <template slot-scope="scope">
          <router-link :to="'processInstance/'+scope.row.id+'/diagram/'+scope.row.definitionId">
            <el-button type="info">查看流程图</el-button>
          </router-link>
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
import { getProcessInstances, getProcessInstanceCount } from '@/api/processes'
import { MessageBox, Message } from 'element-ui'
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
      pageSize: 2,
      page: 1,
      listLoading: true,
      startFormKey: null
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
      getProcessInstanceCount(params).then(response => {
        this.listCount = response.count
      })
      getProcessInstances(params).then(response => {
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
