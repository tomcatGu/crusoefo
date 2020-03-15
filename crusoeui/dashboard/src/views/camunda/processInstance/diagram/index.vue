<template>
  <div ref="content" class="containers">
    <div ref="canvas" class="canvas" />
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
</template>
<script>
// 引入相关的依赖
import { getProcessXML } from '@/api/processes'
import BpmnViewer from 'bpmn-js'
import tokenSimulation from 'bpmn-js-token-simulation/lib/viewer'
import minimap from 'diagram-js-minimap'

import { getActivityInstances } from '@/api/history'

import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
import 'bpmn-js-token-simulation/assets/css/bpmn-js-token-simulation.css'
import 'diagram-js-minimap/assets/diagram-js-minimap.css'

export default {
  data() {
    return {
      // bpmn建模器
      bpmnViewer: null,
      container: null,
      canvas: null,
      scale: 1,
      id: null,
      definitionId: null
    }
  },
  created() {
    this.id = this.$route.params.id
    this.definitionId = this.$route.params.definitionId
  },
  mounted() {
    // 获取到属性ref为“content”的dom节点
    this.container = this.$refs.content
    // 获取到属性ref为“canvas”的dom节点
    const canvas = this.$refs.canvas

    // 建模，官方文档这里讲的很详细
    this.bpmnViewer = new BpmnViewer({
      container: canvas,
      additionalModules: [tokenSimulation, minimap]
    })
    this.createNewDiagram(this.bpmnViewer)
  },
  methods: {
    createNewDiagram() {
      const that = this
      getProcessXML(this.definitionId).then(response => {
        const bpmnXmlStr = response.bpmn20Xml
        // 将字符串转换成图显示出来
        this.bpmnViewer.importXML(bpmnXmlStr, function(err) {
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
            const params = {
              processInstanceId: that.id
            }
            getActivityInstances(params).then(response => {
              const canvas = that.bpmnViewer.get('canvas')
              const nodes = []
              response.forEach(activity => {
                // const nodeCodes2 = elementRegistry.get(activity.activityId)
                nodes.push(activity.activityId)
              })
              that.setNodeColor(nodes, 'nodeSuccess', canvas)
            })

            that.bpmnViewer.get('minimap').open()
          }
        })
      })

      var eventBus = this.bpmnViewer.get('eventBus')

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
          if (event === 'element.click') {
            const elementRegistry = that.bpmnViewer.get('elementRegistry')
            const task = elementRegistry.get(e.element.id)
            const businessObject = task.businessObject

            const outgoingConnections = businessObject.outgoing
            const incoming = businessObject.incoming
            console.log('outgoingConnections', outgoingConnections)

            const canvas = that.bpmnViewer.get('canvas')
            const nodeCodes2 = [incoming[0].id, outgoingConnections[0].id]
            const colorClass2 = 'lineSuccess'
            that.setNodeColor(nodeCodes2, colorClass2, canvas)
          }
        })
      })
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
      this.bpmnViewer.saveXML({ format: true }, (err, xml) => {
        if (err) {
          alert('导出错误，请重试')
        } else {
          this.download('xml', xml)
        }
      })
    },
    // 下载 SVG 格式
    saveImg() {
      this.bpmnViewer.saveSVG({ format: true }, (err, xml) => {
        if (err) {
          alert('导出错误，请重试')
        } else {
          this.download('svg', xml)
        }
      })
    },

    // 流程图放大缩小
    handleZoom(radio) {
      const newScale = !radio
        ? 1.0 // 不输入radio则还原
        : this.scale + radio <= 0.2 // 最小缩小倍数
          ? 0.2
          : this.scale + radio

      this.bpmnViewer.get('canvas').zoom(newScale)
      this.scale = newScale
      // this.setState({
      //    scale: newScale,
      // });
    },
    resetView() {
      // 恢复到原位
      const vm = this
      this.scale = 1.0
      vm.bpmnViewer.get('canvas').zoom('fit-viewport')
    },
    // 给已经走过的流程添加颜色
    setNodeColor(nodeCodes, colorClass, canvas) {
      for (let i = 0; i < nodeCodes.length; i++) {
        canvas.addMarker(nodeCodes[i], colorClass)
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
.nodeSuccess:not(.djs-connection) .djs-visual > :nth-child(1) {
  stroke: green !important;
  stroke-width: 3px;
  /* elements as success */
}

.nodeError:not(.djs-connection) .djs-visual > :nth-child(1) {
  stroke: red !important;
  stroke-width: 3px;
  /* elements as error */
}

.lineSuccess:not(.djs-shape) .djs-visual :last-child {
  stroke: green !important;
}

.lineError:not(.djs-shape) .djs-visual :last-child {
  stroke: red !important;
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
</style>
