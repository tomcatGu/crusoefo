<template>
  <div class="app-container">
    <a href="javascript:" class="active" @click="deploy">部署</a>
    <split-pane split="vertical">
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
    </split-pane>
  </div>
</template>

<script>
import FormCreate from '@form-create/element-ui'
import splitPane from 'vue-splitpane'
import JsonEditor from '@/components/JsonEditor'

import { createDeployment } from '@/api/repository'

export default {
  data() {
    return {
      // 表单实例对象
      $f: {},
      model: {},
      value: {},
      // 表单生成规则
      rule: [
        {
          type: 'input',
          field: 'goods_name',
          title: '商品名称'
        },
        {
          type: 'datePicker',
          field: 'created_at',
          title: '创建时间'
        },
        {
          type: 'ElButton',
          field: 'btn1',
          children: ['test']
        },
        {
          type: 'DatePicker',
          field: 'section_day',
          title: '活动日期',
          value: ['2018-02-20', new Date()],
          // input值, type为daterange,datetimerange value为数组 [start_value,end_value]
          props: {
            type: 'datetimerange',
            // 显示类型，可选值为 date、daterange、datetime、datetimerange、year、month
            format: 'yyyy-MM-dd HH:mm:ss',
            // 展示的日期格式
            placement: 'bottom-start',
            //	日期选择器出现的位置，可选值为toptop-starttop-endbottombottom-startbottom-endleftleft-startleft-endrightright-startright-end
            placeholder: '请选择获得时间',
            // 占位文本
            confirm: false,
            // 是否显示底部控制栏，开启后，选择完日期，选择器不会主动关闭，需用户确认后才可关闭
            size: 'default',
            // 尺寸，可选值为large、small、default或者不设置
            disabled: false,
            // 是否禁用选择器
            clearable: true,
            // 是否显示清除按钮
            readonly: false,
            // 完全只读，开启后不会弹出选择器
            editable: false
            // 文本框是否可以输入
          }
        }
      ],
      option: {
        resetBtn: false,
        submitBtn: false
      }
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
      formData.append('deployment-name', 'test')
      formData.append('deployment-source', 'test form')
      formData.append('enable-duplicate-filtering', true)
      formData.append('data', new Blob([this.$data.value]), 'test.form')

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
