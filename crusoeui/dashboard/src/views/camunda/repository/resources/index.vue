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
        <template slot-scope="scope">{{ (page-1) * pageSize + scope.$index }}</template>
      </el-table-column>
      <el-table-column label="Id" width="110">
        <template slot-scope="scope">{{ scope.row.id }}</template>
      </el-table-column>
      <el-table-column label="Name" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="100">
        <template slot-scope="scope">

          <router-link :to="'/form/form-edit/'+ id + '/' +scope.row.id">
            <el-button type="info">编辑</el-button>
          </router-link>
          <el-button type="info">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
// import { getList } from '@/api/table'
import { getResources } from '@/api/repository'
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
      id: null
    }
  },
  created() {
    this.id = this.$route.params.id
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getResources(this.$data.id).then(response => {
        this.list = response
        // console.log(this.list)
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
