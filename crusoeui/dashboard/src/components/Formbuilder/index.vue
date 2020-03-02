<template>
  <div class="container">
    <i-row>
      <i-col span="12" class="sortable_container">
        <Form :label-width="100" class="b-a">
          <draggable :clone="cloneData" :list="form_list" :options="dragOptions1">
            <transition-group
              class="form-list-group"
              type="transition"
              :name="'flip-list'"
              tag="div"
            >
              <renders
                v-for="(element,index) in form_list"
                :key="index"
                :ele="element.ele"
                :obj="element.obj || {}"
              />
            </transition-group>
          </draggable>
        </Form>
      </i-col>
      <i-col span="12" class="sortable_item">
        <Form
          ref="formValidate"
          class="b-a"
          :label-width="100"
          :model="formData"
          @submit.native.prevent
        >
          <Alert style="margin: 15px 15px 0;" type="warning" show-icon>未绑定数据字典控件无效</Alert>
          <draggable :list="sortable_item" :options="dragOptions2">
            <transition-group
              class="form-list-group"
              type="transition"
              :name="'flip-list'"
              tag="div"
            >
              <renders
                v-for="(element,index) in sortable_item"
                :key="index"
                :index="index"
                :ele="element.ele"
                :obj="element.obj || {}"
                :data="formData"
                :sortable-item="sortable_item"
                :config-icon="true"
                @handleRemoveEle="removeEle"
                @handleConfEle="confEle"
                @changeVisibility="changeVisibility"
                @handleChangeVal="val => handleChangeVal(val,element)"
              />
            </transition-group>
          </draggable>
          <FormItem>
            <Button type="primary" @click="handleSubmit('formValidate')">Submit</Button>
            <Button type="ghost" style="margin-left: 8px" @click="handleReset('formValidate')">Reset</Button>
          </FormItem>
        </Form>
      </i-col>
      <Modal
        v-model="showModal"
        :title="'配置' + modalFormData.modalTitle + '属性'"
        :mask-closable="false"
      >
        <Form ref="modalFormData" class="form_content" :label-width="80" :model="modalFormData">
          <FormItem v-if="typeof modalFormData.label != 'undefined'" label="控件名称：">
            <i-input v-model="modalFormData.label" placeholder="请输入控件名称" :maxlength="4" />
          </FormItem>
          <FormItem v-if="showModal" label="数据字典：">
            <Select v-model="modalFormData.dict" filterable @on-change="handleDataDictChange">
              <!-- value绑定json字符串的原因是，需要用到parent_name，当handleDataDictChange触发，赋值到modalFormData -->
              <Option
                v-for="item in dataDictList"
                :key="item.id"
                :disabled="dataDictSelected.indexOf(item.id) >= 0"
                :value="JSON.stringify({
                  id: item.id, parent_name: item.parent_name})"
              >{{ item.label }}</Option>
            </Select>
          </FormItem>
          <FormItem v-if="typeof modalFormData.name != 'undefined'" label="name属性：">
            <i-input v-model="modalFormData.name" placeholder disabled />
          </FormItem>
          <FormItem v-if="typeof modalFormData.relation != 'undefined'" label="关联数据：">
            <!-- 当绑定name并且当前relationList存在数据时候才可以关联字段 -->
            <Checkbox
              v-model="modalFormData.relation"
              :disabled="!modalFormData.name || !relationList.length"
            >是否关联字段</Checkbox>
          </FormItem>
          <FormItem
            v-if="typeof modalFormData.relation != 'undefined' && modalFormData.relation"
            label="关联配置："
          >
            <Select
              v-model="modalFormData.relation_name"
              class="inline-block"
              style="width: 150px"
              @on-change="_=>modalFormData.relation_value = ''"
            >
              <Option
                v-for="(item,index) in relationList"
                :key="index"
                :disabled="item.obj.name == modalFormData.name"
                :value="item.obj.name"
              >{{ item.obj.label }}</Option>
            </Select>
            <p class="inline-block padder-sm">等于</p>
            <Select
              v-model="modalFormData.relation_value"
              class="inline-block"
              style="width: 150px"
            >
              <Option
                v-for="(item,index) in relationValue"
                :key="index"
                :value="item.label_value"
              >{{ item.label_name }}</Option>
            </Select>
          </FormItem>
          <FormItem v-if="typeof modalFormData.placeholder != 'undefined'" label="placeholder：">
            <i-input v-model="modalFormData.placeholder" placeholder="请输入placeholder" />
          </FormItem>
          <FormItem v-if="typeof modalFormData.maxLength != 'undefined'" label="最大长度：">
            <InputNumber v-model="modalFormData.maxLength" placeholder="请输入文本限制最大长度" />
          </FormItem>
          <FormItem v-if="typeof modalFormData.maxSize != 'undefined'" label="最大限制：">
            <InputNumber
              v-model="modalFormData.maxSize"
              :formatter="value => `${value}kb`"
              :parser="value => value.replace('kb', '')"
              placeholder="请输入上传文件最大限制"
            />
          </FormItem>
          <FormItem v-if="typeof modalFormData.marginTop != 'undefined'" label="上边距：">
            <InputNumber
              v-model="modalFormData.marginTop"
              :formatter="value => `${value}px`"
              :parser="value => value.replace('px', '')"
              placeholder="请输入标签上边距"
            />
          </FormItem>
          <FormItem v-if="typeof modalFormData.marginBottom != 'undefined'" label="下边距：">
            <InputNumber
              v-model="modalFormData.marginBottom"
              :formatter="value => `${value}px`"
              :parser="value => value.replace('px', '')"
              placeholder="请输入标签下边距"
            />
          </FormItem>
          <FormItem v-if="typeof modalFormData.details_address != 'undefined'" label="详细地址：">
            <Checkbox v-model="modalFormData.details_address">是否需要详细地址</Checkbox>
          </FormItem>
          <FormItem v-if="typeof modalFormData.require != 'undefined'" label="是否必填：">
            <Checkbox v-model="modalFormData.require">必填</Checkbox>
          </FormItem>
          <FormItem v-if="typeof modalFormData.ruleError != 'undefined'" label="校验错误：">
            <i-input v-model="modalFormData.ruleError" placeholder="请输入校验错误提示" />
          </FormItem>
          <FormItem
            v-if="typeof modalFormData.multiple != 'undefined' && modalFormData.type != 'address'"
            label="是否多选："
          >
            <Checkbox v-model="modalFormData.multiple">多选</Checkbox>
          </FormItem>
          <FormItem v-if="typeof modalFormData.format != 'undefined'" label="时间格式：">
            <RadioGroup v-model="modalFormData.format">
              <Radio label="yyyy年MM月dd日" />
              <Radio label="yyyy-MM-dd HH:mm" />
            </RadioGroup>
          </FormItem>
          <FormItem v-if="typeof modalFormData.inlineBlock != 'undefined'" label="行内元素：">
            <Checkbox v-model="modalFormData.inlineBlock">是</Checkbox>
          </FormItem>
          <FormItem v-if="typeof modalFormData.maxRows != 'undefined'" label="显示行数：">
            <Slider v-model="modalFormData.maxRows" :min="2" :max="10" />
          </FormItem>
          <FormItem v-if="typeof modalFormData.level != 'undefined'" label="标题大小：">
            <InputNumber v-model="modalFormData.level" :max="6" :min="1" />
          </FormItem>
          <FormItem v-if="typeof modalFormData.color != 'undefined'" label="字体颜色：">
            <ColorPicker v-model="modalFormData.color" />
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="text" @click="handleCancel">取消</Button>
          <Button type="primary" :loading="modalFormData.loading" @click="handleOk">确定</Button>
        </div>
      </Modal>
    </i-row>
  </div>
</template>
<script>
import draggable from 'vuedraggable'
import form_list from './custom_form/FormList'
import renders from './custom_form/Render'
export default {
  components: {
    draggable,
    renders
  },
  data() {
    return {
      form_list: form_list,
      sortable_item: [],
      showModal: false,
      // 深拷贝对象，防止默认空对象被更改
      // 颜色选择器bug，对象下color不更新
      modalFormData: {
        color: '',
        loading: false
      },
      formData: {},
      dataDict: []
    }
  },
  computed: {
    // 数据字典已选择项
    dataDictSelected() {
      return this.sortable_item.map(v => {
        const obj = JSON.parse(v.obj.dict || '{}')
        return obj.id || -1
      })
    },
    // 对应控件的数据字典
    dataDictList() {
      return this.dataDict.filter(v => {
        return v.type == this.modalFormData.type
      })
    },
    // 拖拽表单1
    dragOptions1() {
      return {
        animation: 0,
        ghostClass: 'ghost',
        // 分组
        group: {
          name: 'shared',
          pull: 'clone',
          revertClone: false
        },
        // 禁止拖动排序
        sort: false
      }
    },
    // 拖拽表单2
    dragOptions2() {
      return {
        animation: 0,
        ghostClass: 'ghost',
        group: {
          // 只允许放置shared的控件,禁止pull
          put: ['shared']
        }
      }
    },
    // 被关联字段列表
    relationList() {
      // 只有type内三项可作为被关联字段
      const type = ['select', 'radio', 'checkbox']
      const arr = this.sortable_item.filter(k => {
        return type.indexOf(k.ele) >= 0 && !!k.obj.name
      })
      return arr
    },
    // 被关联字段数据
    relationValue() {
      const name = this.modalFormData.relation_name
      let items = []
      if (!name) return items
      for (const i in this.sortable_item) {
        if (this.sortable_item[i].obj.name == name) {
          items = this.sortable_item[i].obj.items
        }
      }
      return items
    }
  },
  watch: {
    showModal(val) {
      if (!val) {
        this.handleCancel()
      }
    }
  },
  created() {
    // /static/label.json
    const label =
      '{ "items": [{ "id": 1, "label": "姓名", "type": "input", "parent_name": null }, { "id": 3, "label": "电话", "type": "input", "parent_name": null }, { "id": 5, "label": "性别", "type": "radio", "parent_name": null }, { "id": 8, "label": "投诉方式", "type": "select", "parent_name": null }, { "id": 12, "label": "上传图片附件", "type": "uploads", "parent_name": null }, { "id": 14, "label": "案件资料是否收齐", "type": "select", "parent_name": null }, { "id": 17, "label": "标识缺少的资料", "type": "input", "parent_name": "is_case" }, { "id": 19, "label": "地址", "type": "address", "parent_name": null }], "status": "success", "code": 200 }'

    // this.$http.get('/static/label.json').then(d => {
    //  this.dataDict = d.data.items
    // })

    this.dataDict = JSON.parse(label).items
    this.sortable_item = JSON.parse(
      localStorage.getItem('template_form') || '[]'
    )
  },
  methods: {
    // 克隆表单提交事件
    handleSubmit(name) {
      // this.$refs[name].validate((valid) => {
      //   if (valid) {
      //     this.$Message.success('Success!');
      //   } else {
      //     debugger;
      //     this.$Message.error('Fail!');
      //   }
      // })

      localStorage.setItem(
        'template_form',
        JSON.stringify(
          this.sortable_item.filter(v => {
            return !!v.obj.name
          })
        )
      )
      this.$router.push('/render')
    },
    // 清空克隆表单
    handleReset() {
      this.sortable_item = []
    },
    // modal内数据字典选项发生改变触发事件
    handleDataDictChange(val) {
      // 选中后，val默认赋值到modalFormData.dict
      const obj = JSON.parse(val)
      // 数据加载中，禁止modal_submit提交按钮
      this.$set(this.modalFormData, 'loading', true)
      this.$http.get(`/static/label.${obj.id}.json`).then(d => {
        this.modalFormData = Object.assign({}, this.modalFormData, {
          name: d.data.name,
          loading: false,
          items: d.data.items,
          parent_name: obj.parent_name
        })
      })
    },
    // 控件回填数据
    handleChangeVal(val, element) {
      this.$set(this.formData, element.obj.name, val)
    },
    // https://github.com/SortableJS/Vue.Draggable#clone
    // 克隆,深拷贝对象
    cloneData(original) {
      // 添加一个modal标题
      original.obj.modalTitle = original.obj.label || ''
      // 深拷贝对象，防止默认空对象被更改
      return JSON.parse(JSON.stringify(original))
    },
    // modal点击确定执行事件
    handleOk() {
      const index = this.modalFormData.listIndex
      this.sortable_item[index].obj = Object.assign(
        {},
        this.sortable_item[index].obj,
        this.modalFormData
      )
      this.handleCancel()
    },
    // modal点击取消执行事件，清空当前modal内容
    handleCancel() {
      this.showModal = false
      setTimeout(_ => {
        this.modalFormData = {
          color: '',
          loading: false
        }
      }, 500)
    },
    // 显示modal,配置被克隆控件
    confEle(index) {
      const list_temp = Object.assign({}, this.sortable_item[index])
      for (const i in list_temp.obj) {
        this.modalFormData[i] = list_temp.obj[i]
      }
      // 配置项中未找到color，删除modalFormData中自带color属性
      if (!list_temp.obj['color']) delete this.modalFormData.color
      // 设置被配置控件的index，便于完成配置找到相应对象赋值
      this.modalFormData.listIndex = index
      // Vue 不能检测到对象属性的添加或删除
      this.modalFormData = Object.assign({}, this.modalFormData)
      this.showModal = true
    },
    // 删除克隆控件
    removeEle(index) {
      const name = this.sortable_item[index].obj.name
      this.sortable_item.splice(index, 1)
      if (!name) return
      for (const i in this.sortable_item) {
        // 当relation为true并且关联字段被确认
        if (
          this.sortable_item[i].obj.relation &&
          this.sortable_item[i].obj.relation_name === name
        ) {
          this.$set(this.sortable_item[i].obj, 'relation', false)
          this.$set(this.sortable_item[i].obj, 'relation_name', '')
          this.$set(this.sortable_item[i].obj, 'relation_value', '')
          break
        }
      }
    },
    // 更改当前渲染字段是否显示
    changeVisibility(index, visibility) {
      this.$set(this.sortable_item[index].obj, 'visibility', visibility)
    }
  }
}
</script>
<style>
.inline {
  display: inline-block;
}

.m-l-lg {
  margin-left: 30px;
}

.wrapper {
  padding: 15px;
}

.inline-block {
  display: inline-block;
}

.padder-sm {
  padding-right: 10px;
  padding-left: 10px;
}

.b-a {
  border: 1px solid #ccc;
}

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.form-list-group {
  min-height: 200px;
  padding: 20px !important;
}

/* 设置items下所有鼠标样式为 move */

.items,
.items * {
  cursor: move;
}

/* 配置按钮默认位置 */

/* 例如P Hr Title按钮 */

.items .item-icon {
  transition: all 2s ease;
  position: absolute;
  top: -18px;
  right: 0px;
  opacity: 0;
  max-height: 0;
  overflow: hidden;
}

/* form控件下配置按钮位置 */

.ivu-form-item.items .item-icon {
  top: -25px;
}

/* 配置按钮样式 */

.item-icon i {
  cursor: pointer !important;
  margin-right: 5px;
}

.items:hover .item-icon {
  transition: inherit;
  opacity: 1;
  max-height: 50px;
}

/* 提交按钮下方无 margin-bottom */

.form_content .ivu-form-item:last-child {
  margin-bottom: 0;
}

/* 表单校验选项样式 */

.ivu-form-item-required .ivu-form-item-label:before {
  content: "";
}

.items.sortable-items-required .ivu-form-item-label:before {
  content: "*";
  display: inline-block;
  margin-right: 4px;
  line-height: 1;
  font-family: SimSun;
  font-size: 12px;
  color: #ed3f14;
}
</style>
