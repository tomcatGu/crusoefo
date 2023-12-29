<template>
  <div>


    <el-tabs v-model="editableTabsValue" type="card" editable @edit="handleTabsEdit">
      <el-tab-pane name="modeler" label="modeler" style="height:900px">
        <el-input v-model="bpmnName" placeholder="请输入流程图名称" />
        <div ref="content" class="containers">
          <div ref="canvas" class="canvas" />
          <div id="js-properties-panel" class="panel" />
          <ul class="buttons">
            <li>下载</li>
            <li>
              <a ref="saveDiagram" href="javascript:" title="download BPMN diagram" class="active"
                @click="handleDownloadXml">BPMN diagram</a>
            </li>
            <li>
              <a ref="saveImg" href="javascript:" title="download as SVG image" class="active" @click="saveImg">SVG
                image</a>
            </li>
            <li>
              <a href="javascript:" class="active" @click="handleOpenFile">载入</a>
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
      <el-tab-pane v-for="(item, index) in editableTabs" :key="item.name" :label="item.title" :name="item.name"
        style="height:900px;">
        <keep-alive>
        <component :is="item.view" ref="eform" />
        </keep-alive>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
// 引入相关的依赖
import { defineComponent,defineAsyncComponent, reactive, toRefs, ref, onMounted, computed ,shallowRef} from 'vue'
import BpmnModeler from 'bpmn-js/lib/Modeler'

import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from 'bpmn-js-properties-panel';
//import propertiesPanelModule from 'bpmn-js-properties-panel'
//import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
//import AlignToOrigin from '@bpmn-io/align-to-origin'

//import customTranslate from './customTanslate'

import { createDeployment } from '@/api/repository'
import EmbbedFormCreate from '@/components/EmbbedFormCreate'

import { ElMessageBox, ElMessage } from 'element-plus'

import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
/* 右边工具栏样式*/
import '@bpmn-io/properties-panel/assets/properties-panel.css'
//import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'

export default defineComponent({
  name: 'BPMN',
  components: {
    EmbbedFormCreate
  },
  data() {
    return {
      // bpmn建模器
      bpmnModeler: null,
      container: null,
      canvas: null,
      scale: 1,
      editableTabsValue: 'modeler',
      editableTabs: [
      ],
      tabIndex: 1,
      bpmnName: null,
      files: []
    }
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
        BpmnPropertiesPanelModule,
        BpmnPropertiesProviderModule
      ],
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    })
    this.createNewDiagram(this.bpmnModeler)
  },
  methods: {
    createNewDiagram() {
      const that = this
      const bpmnXmlStr =
        '<?xml version="1.0" encoding="UTF-8"?>' +
        '<bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd" id="sample-diagram" targetNamespace="http://bpmn.io/schema/bpmn">' +
        '<bpmn2:process id="Process_1" isExecutable="false">' + '\n' +
        '<bpmn2:startEvent id="StartEvent_1"/>' + '\n' +
        '</bpmn2:process>' + '\n' +
        ' <bpmndi:BPMNDiagram id="BPMNDiagram_1">' + '\n' +
        '<bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">' + '\n' +
        '<bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">' + '\n' +
        '<dc:Bounds height="36.0" width="36.0" x="412.0" y="240.0"/>' + '\n' +
        '</bpmndi:BPMNShape>' + '\n' +
        '</bpmndi:BPMNPlane>' + '\n' +
        '</bpmndi:BPMNDiagram>' + '\n' +
        '</bpmn2:definitions>'
      // 将字符串转换成图显示出来
      try {
        this.bpmnModeler.importXML(bpmnXmlStr)
        /*       var eventBus = this.bpmnModeler.get('eventBus')
        
              // you may hook into any of the following events
              var events = [
                'element.hover',
                'element.out',
                'element.click',
                'element.dblclick',
                'element.mousedown',
                'element.mouseup'
              ]
        
              events.forEach(function (event) {
                eventBus.on(event, function (e) {
                  // e.element = the model element
                  // e.gfx = the graphical element
        
                  console.log(event, 'on', e.element.id)
                })
              }) 
            }*/
      } catch (err) {
        console.error(err);

      }

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
      input.multiple = 'multiple'
      input.click() // 打开文件选择框
      input.onchange = function () {
        for (var i = 0; i < input.files.length; i++) {
          // console.log(input.files[i])
          const file = input.files[i]
          if (window.FileReader) {
            try {
              var fr = new FileReader()
              fr.readAsText(file) // 将文件读取为文本
              fr.onloadend = function (e) {
                if (file.name.endsWith('.bpmn')) {
                  vm.bpmnInfo.xmlStr = this.result
                  vm.createNewDiagram()
                }
                if (file.name.endsWith('.form')) {
                  const newTabName = ++vm.tabIndex + ''
                  // console.log('new a tab...')
                  vm.editableTabs.push({
                    title: file.name,
                    name: newTabName,
                    content: embbedFormCreate
                  })
                  vm.editableTabsValue = newTabName

                  // vm.files[vm.tabIndex - 2] = this.result
                  // console.log(this.result)
                  const that = this
                  vm.$nextTick(() => {
                    // console.log(vm.$refs)
                    // console.log(vm.$refs.eform)
                    // console.log(vm.tabIndex)
                    // console.log(vm.files)
                    // console.log(vm.$refs.eform[vm.tabIndex - 2])
                    // vm.$refs.eform[vm.tabIndex - 2].value = that.result
                    vm.$refs.eform[vm.tabIndex - 2].loadForm(that.result)
                    vm.$refs.eform[vm.tabIndex - 2].createRandomKey()
                  })
                }
              }
            } catch (e) {
              console.error(e.toString())
            }
          } else {
            console.error('您的浏览器可能不支持此操作')
          }
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
          formData.append('tenant-id', this.$store.getters.name)
          formData.append('flow', new Blob([xml]), this.bpmnName + '.bpmn')

          for (var i = 0; i < this.$refs.eform.length; i++) {
            // console.log(this.$refs.eform[i].value)
            // console.log(this.editableTabs[i].title)
            formData.append(this.editableTabs[i].title, new Blob([this.$refs.eform[i].value]), 'forms/' + this.editableTabs[i].title + '.form')
          }

          createDeployment(formData).then(response => {
            // console.log(response)
            ElMessage({
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
            view: shallowRef(EmbbedFormCreate)
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
        this.editableTabs = tabs.filter(tab => tab.name !== targetName)
      }
    }
  }
})
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

  &>li {
    display: inline-block;
    margin: 5px;

    &>a {
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
