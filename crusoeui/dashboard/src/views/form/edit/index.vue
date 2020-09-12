<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="name"  placeholder="Title" style="width: 200px;" class="filter-item" />
      <el-input v-model="source" placeholder="Title" style="width: 200px;" class="filter-item" />

      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >部署</el-button>
    </div>
    <EmbbedFormCreate />
<!--     <split-pane split="vertical">
      <template slot="paneL">
        <div class="editor-container">
          <json-editor ref="jsonEditor" v-model="value" @changed="onChange" />
        </div>
      </template>
      <template slot="paneR">
        <div class="editor-container">
          <form-create v-model="$data.$f" :rule="rule" :option="option" @on-submit="onSubmit" />

        </div>
      </template>
    </split-pane> -->
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import splitPane from 'vue-splitpane'
import JsonEditor from '@/components/JsonEditor'
import embbedFormCreate from '@/components/EmbbedFormCreate'

import {
  createDeployment,
  getResourceData,
  getRepository
} from '@/api/repository'

export default {

  data() {
    return {
      // 表单实例对象
      $f: {},
      model: {},
      value: {},
      // 表单生成规则
      rule: [],
      option: {
        resetBtn: false,
        submitBtn: false
      },
      id: null,
      resourceId: null,
      name: null,
      source: null
    }
  },
  methods: {
    onSubmit(formData) {
      // TODO 提交表单
      console.log(JSON.stringify(formData))
    },
    onChange(data) {
      console.log(data)
      this.$data.rule = JSON.parse(data)
      // this.$data.rule = JSON.parse(this.$data.value)
      // this.formData.create(JSON.parse(this.rule))
    },
    deploy() {
      // this.download('xml', xml)
      const formData = new FormData()
      formData.append('deployment-name', this.$data.name)
      formData.append('deployment-source', this.$data.source)
      formData.append('enable-duplicate-filtering', true)
      formData.append('data', new Blob([this.$data.value]), this.$data.name + '.form')

      createDeployment(formData).then(response => {
        console.log(response)
      })
    }
  },
  components: {
    formCreate: FormCreate.$form(),
    splitPane,
    JsonEditor
  },
  mounted() {
    // this.model = this.$f.model();

    this.$data.value = this.$data.rule
  },
  created() {
    console.log(this.$route.params)
    this.$data.id = this.$route.params.id
    this.$data.resourceId = this.$route.params.resourceId
    getResourceData(this.$data.id, this.$data.resourceId).then(response => {
      this.rule = response
      this.value = this.$data.rule
    })
    getRepository(this.$data.id).then(response => {
      this.$data.name = response.name
      this.$data.source = response.source
    })
  }
}
</script>
<style lang="scss">
.app-container {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}
.editor-container {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow-y: scroll;
  white-space: nowrap;
}
</style>
