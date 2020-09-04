<template>
  <div>
    <el-button
      class="filter-item"
      style="margin-left: 10px;"
      type="primary"
      icon="el-icon-edit"
      @click="deploy"
    >部署</el-button>
    <el-tabs v-model="editableTabsValue" type="card" editable @edit="handleTabsEdit">
      <el-tab-pane name="modeler" label="modeler" style="height:900px">
        <el-input v-model="bpmnName" placeholder="请输入流程图名称" />
        <div ref="content" class="containers">
          <div ref="canvas" class="canvas" />
          <div id="js-properties-panel" class="panel" />
          <ul class="buttons">
            <li>下载</li>
            <li>
              <a
                ref="saveDiagram"
                href="javascript:"
                title="download BPMN diagram"
                class="active"
                @click="handleDownloadXml"
              >BPMN diagram</a>
            </li>
            <li>
              <a
                ref="saveImg"
                href="javascript:"
                title="download as SVG image"
                class="active"
                @click="saveImg"
              >SVG image</a>
            </li>
            <li>
              <a href="javascript:" class="active" @click="deploy">部署</a>
            </li>
            <li>
              <a href="javascript:" class="active" @click="handleZoom(0.2)">放大</a>
            </li>
            <li>
              <a href="javascript:" class="active" @click="handleZoom(-0.2)">缩小</a>
            </li>
            <li>
              <a href="javascript:" class="active" @click="resetView">还原</a>
            </li>
          </ul>
        </div>
      </el-tab-pane>
      <el-tab-pane
        v-for="(item, index) in editableTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name"
        style="height:900px;"
      >
        <tab-component :is="item.content" ref="eform" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
// 引入相关的依赖
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
import AlignToOrigin from '@bpmn-io/align-to-origin'

import customTranslate from '../customTanslate'

import { createDeployment } from '@/api/repository'
import { MessageBox, Message } from 'element-ui'
import { getProcessXML } from '@/api/processes'
import { getResources } from '@/api/repository'

import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
/* 右边工具栏样式*/
import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'

export default {
  data() {
    return {
      // bpmn建模器
      bpmnModeler: null,
      container: null,
      canvas: null,
      scale: 1,
      editableTabsValue: 'modeler',
      editableTabs: [],
      tabIndex: 1,
      bpmnName: null,
      id: null
    }
  },
  created() {
    this.id = this.$route.params.id
  },
  mounted() {
    // 获取到属性ref为“content”的dom节点
    this.container = this.$refs.content
    // 获取到属性ref为“canvas”的dom节点
    const canvas = this.$refs.canvas
    // 建模，官方文档这里讲的很详细
    this.bpmnModeler = new BpmnModeler({
      container: canvas,
      // 添加控制板
      propertiesPanel: {
        parent: '#js-properties-panel'
      },
      alignToOrigin: {
        alignOnSave: true,
        offset: 150,
        tolerance: 50
      },
      additionalModules: [
        // 左边工具栏以及节点
        propertiesProviderModule,
        // 右边的工具栏
        propertiesPanelModule,
        {
          translate: ['value', customTranslate]
        },
        AlignToOrigin
      ],
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    })
    this.createNewDiagram(this.bpmnModeler)
  },
  methods: {
    createNewDiagram() {
      var that = this
      var bpmnXmlStr
      getProcessXML(this.id).then((response) => {
        bpmnXmlStr = response.bpmn20Xml
        // 将字符串转换成图显示出来
        this.bpmnModeler.importXML(bpmnXmlStr, function(err) {
          if (err) {
            console.error(err)
          } else {
            // 这里还没用到这个，先注释掉吧
            // that.success()
            // 就是在这里写死了
            // const canvas = that.bpmnViewer.get('canvas')
            // const nodeCodes = ['StartEvent_1', 'Task_0qg0mca', 'Task_0307aue']
            // const colorClass = 'nodeSuccess'
            // that.setNodeColor(nodeCodes, colorClass, canvas)

            // const nodeCodes2 = ['SequenceFlow_1u5gq9e', 'SequenceFlow_1n5pril']
            // const colorClass2 = 'lineSuccess'
            // that.setNodeColor(nodeCodes2, colorClass2, canvas)
            // that.bpmnModeler.get('minimap').open()
          }
        })
      })

      // 将字符串转换成图显示出来
      this.bpmnModeler.importXML(bpmnXmlStr, function(err) {
        if (err) {
          console.error(err)
        } else {
          // 这里还没用到这个，先注释掉吧
          // that.success()
          // that.bpmnModeler.get('minimap').open()
        }
      })
      var eventBus = this.bpmnModeler.get('eventBus')

      // you may hook into any of the following events
      var events = [
        'element.hover',
        'element.out',
        'element.click',
        'element.dblclick',
        'element.mousedown',
        'element.mouseup'
      ]

      events.forEach(function(event) {
        eventBus.on(event, function(e) {
          // e.element = the model element
          // e.gfx = the graphical element

          console.log(event, 'on', e.element.id)
        })
      })
    },
    // 实时保存
    saveDiagram() {
      const vm = this
      vm.bpmnModeler.saveXML({ format: true }, (err, xml) => {
        if (err) {
          alert(err)
          return
        }
        vm.bpmnInfo.xmlStr = xml
      })
      vm.bpmnModeler.saveSVG({ format: true }, (err, data) => {
        if (err) {
          return
        }
      })
    },
    // 上传文件
    handleOpenFile(e) {
      const vm = this
      const input = document.createElement('input')
      input.type = 'file'
      input.click() // 打开文件选择框
      input.onchange = function() {
        const file = input.files[0]
        if (window.FileReader) {
          try {
            var fr = new FileReader()
            fr.readAsText(file) // 将文件读取为文本
            fr.onload = function(e) {
              vm.bpmnInfo.xmlStr = fr.result
              vm.createNewDiagram()
            }
          } catch (e) {
            errorInfo(e.toString())
          }
        } else {
          errorInfo('您的浏览器可能不支持此操作')
        }
      }
      document.body.removeChild(input)
    },
    // 下载xml/svg
    download(type, data, name) {
      // 下载xml/svg
      let dataTrack = ''
      const a = document.createElement('a')
      switch (type) {
        case 'xml':
          dataTrack = 'bpmn'
          break
        case 'svg':
          dataTrack = 'svg'
          break
        default:
          break
      }
      const reName = name || `diagram.${dataTrack}`
      a.setAttribute(
        'href',
        `data:application/bpmn20-xml;charset=UTF-8,${encodeURIComponent(data)}`
      )
      a.setAttribute('target', '_blank')
      a.setAttribute('dataTrack', `diagram:download-${dataTrack}`)
      a.setAttribute('download', reName)
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
    // 下载 XML 格式
    handleDownloadXml() {
      this.bpmnModeler.saveXML({ format: true }, (err, xml) => {
        if (err) {
          alert('导出错误，请重试')
        } else {
          this.download('xml', xml)
        }
      })
    },
    // 下载 SVG 格式
    saveImg() {
      this.bpmnModeler.saveSVG({ format: true }, (err, xml) => {
        if (err) {
          alert('导出错误，请重试')
        } else {
          this.download('svg', xml)
        }
      })
    },
    // 前进
    handleRedo() {
      this.bpmnModeler.get('commandStack').redo()
    },
    // 后退
    handleUndo() {
      this.bpmnModeler.get('commandStack').undo()
    },
    // 流程图放大缩小
    handleZoom(radio) {
      const newScale = !radio
        ? 1.0 // 不输入radio则还原
        : this.scale + radio <= 0.2 // 最小缩小倍数
          ? 0.2
          : this.scale + radio

      this.bpmnModeler.get('canvas').zoom(newScale)
      this.scale = newScale
      // this.setState({
      //    scale: newScale,
      // });
    },
    resetView() {
      // 恢复到原位
      const vm = this
      this.scale = 1.0
      vm.bpmnModeler.get('canvas').zoom('fit-viewport')
    },
    deploy() {
      this.bpmnModeler.saveXML({ format: true }, (err, xml) => {
        if (err) {
          alert('导出错误，请重试')
        } else {
          // this.download('xml', xml)
          const formData = new FormData()
          formData.append('deployment-name', this.bpmnName)
          formData.append('deployment-source', this.bpmnName + ' Process')
          formData.append('enable-duplicate-filtering', true)
          formData.append('flow', new Blob([xml]), this.bpmnName + '.bpmn')

          for (var i = 0; i < this.$refs.eform.length; i++) {
            // console.log(this.$refs.eform[i].value)
            // console.log(this.editableTabs[i].title)
            formData.append(
              this.editableTabs[i].title,
              new Blob([this.$refs.eform[i].value]),
              'forms/' + this.editableTabs[i].title + '.form'
            )
          }

          createDeployment(formData).then((response) => {
            // console.log(response)
            Message({
              message: '部署流程及表单成功。',
              type: 'success',
              duration: 2 * 1000
            })
          })
        }
      })
    },
    handleTabsEdit(targetName, action) {
      if (action === 'add') {
        this.$prompt('请输入名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({ value }) => {
          const newTabName = ++this.tabIndex + ''
          this.editableTabs.push({
            title: value,
            name: newTabName,
            content: embbedFormCreate
          })
          this.editableTabsValue = newTabName
        })
      }
      if (action === 'remove') {
        const tabs = this.editableTabs
        let activeName = this.editableTabsValue
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              const nextTab = tabs[index + 1] || tabs[index - 1]
              if (nextTab) {
                activeName = nextTab.name
              }
            }
          })
        }

        this.editableTabsValue = activeName
        this.editableTabs = tabs.filter((tab) => tab.name !== targetName)
      }
    }
  }
}
</script>
<style lang="scss">
/*左边工具栏以及编辑节点的样式*/

.containers {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
  overflow-y: scroll;
  //overflow-x: scroll;
  white-space: nowrap;
}
.canvas {
  width: 100%;
  height: 100%;
}
.panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
}
.buttons {
  position: absolute;
  left: 20px;
  bottom: 20px;
  & > li {
    display: inline-block;
    margin: 5px;
    & > a {
      color: #999;
      background: #eee;
      cursor: not-allowed;
      padding: 8px;
      border: 1px solid #ccc;
      &.active {
        color: #333;
        background: #fff;
        cursor: pointer;
      }
    }
  }
}
.editor-container {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow-y: scroll;
  white-space: nowrap;
}
</style>
