<template>
  <div ref="content" class="containers">
    <div ref="canvas" class="canvas" />
    <div id="js-properties-panel" class="panel" />
    <ul class="buttons">
      <li>下载</li>
      <li>
        <a
          ref="saveDiagram"
          @click="handleDownloadXml"
          href="javascript:"
          title="download cmmn diagram"
          class="active"
        >cmmn diagram</a>
      </li>
      <li>
        <a
          ref="saveImg"
          @click="saveImg"
          href="javascript:"
          title="download as SVG image"
          class="active"
        >SVG image</a>
      </li>
      <li>
        <a @click="handleZoom(0.2)" href="javascript:" class="active">放大</a>
      </li>
      <li>
        <a @click="handleZoom(-0.2)" href="javascript:" class="active">缩小</a>
      </li>
      <li>
        <a @click="resetView" href="javascript:" class="active">还原</a>
      </li>
    </ul>
  </div>
</template>
<script>
// 引入相关的依赖
import CmmnViewer from "cmmn-js";
import CmmnModeler from "cmmn-js/lib/Modeler";
import propertiesPanelModule from "cmmn-js-properties-panel";
import propertiesProviderModule from "cmmn-js-properties-panel/lib/provider/camunda";
import camundaModdleDescriptor from "camunda-cmmn-moddle/resources/camunda";


import "cmmn-js/dist/assets/diagram-js.css";
import "cmmn-js/dist/assets/cmmn-font/css/cmmn.css";
import "cmmn-js/dist/assets/cmmn-font/css/cmmn-codes.css";
import "cmmn-js/dist/assets/cmmn-font/css/cmmn-embedded.css";
/*右边工具栏样式*/
import 'cmmn-js-properties-panel/styles/properties.less';


export default {
  data() {
    return {
      // cmmn建模器
      CmmnModeler: null,
      container: null,
      canvas: null,
      scale: 1
    };
  },
  mounted() {
    // 获取到属性ref为“content”的dom节点
    this.container = this.$refs.content;
    // 获取到属性ref为“canvas”的dom节点
    const canvas = this.$refs.canvas;

    // 建模，官方文档这里讲的很详细
    this.CmmnModeler = new CmmnModeler({
      container: canvas,
      // 添加控制板
      propertiesPanel: {
        parent: "#js-properties-panel"
      },
      additionalModules: [
        // 左边工具栏以及节点
        propertiesProviderModule,
        // 右边的工具栏
        propertiesPanelModule
      ],
      moddleExtensions: {
        camunda: camundaModdleDescriptor
      }
    });
    this.createNewDiagram(this.CmmnModeler);
  },
  methods: {
    createNewDiagram() {
      const cmmnXmlStr = '<?xml version="1.0" encoding="UTF-8"?>\n' +
			'<cmmn:definitions xmlns:dc="http://www.omg.org/spec/CMMN/20151109/DC" xmlns:cmmndi="http://www.omg.org/spec/CMMN/20151109/CMMNDI" xmlns:cmmn="http://www.omg.org/spec/CMMN/20151109/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" id="Definitions_02xmelg" targetNamespace="http://bpmn.io/schema/cmmn" exporter="Camunda Modeler" exporterVersion="3.4.1">\n' +
  			'<cmmn:case id="Case_1cozor9">\n' +
    		'<cmmn:casePlanModel id="CasePlanModel_0ln15ek" name="A CasePlanModel">\n' +
      		'<cmmn:planItem id="PlanItem_1" definitionRef="Task_1" />\n' +
      		'<cmmn:task id="Task_1" />\n' +
    		'</cmmn:casePlanModel>\n' +
  			'</cmmn:case>\n' +
  			'<cmmndi:CMMNDI>\n' +
  			'<cmmndi:CMMNDiagram id="init_cmmn_id" name="init_cmmn_name">\n' +
  			'<cmmndi:Size width="500" height="500" />\n' +
  			'<cmmndi:CMMNShape id="DI_CasePlanModel_0ln15ek" cmmnElementRef="CasePlanModel_0ln15ek">\n' +
			'<dc:Bounds x="154" y="99" width="534" height="389" />\n' +
			'<cmmndi:CMMNLabel />\n' +
			'</cmmndi:CMMNShape>\n' +
			'<cmmndi:CMMNShape id="PlanItem_1_di" cmmnElementRef="PlanItem_1">\n' +
			'<dc:Bounds x="190" y="132" width="100" height="80" />\n' +
			'<cmmndi:CMMNLabel />\n' +
			'</cmmndi:CMMNShape>\n' +
			'</cmmndi:CMMNDiagram>\n' +
			'</cmmndi:CMMNDI>\n' +
			'</cmmn:definitions>\n'
      // 将字符串转换成图显示出来
      this.CmmnModeler.importXML(cmmnXmlStr, function(err) {
        if (err) {
          console.error(err);
        } else {
          // 这里还没用到这个，先注释掉吧
          // that.success()
        }
      });
    },
    // 实时保存
    saveDiagram() {
      const vm = this;
      vm.CmmnModeler.saveXML({ format: true }, (err, xml) => {
        if (err) {
          alert(err);
          return;
        }
        vm.cmmnInfo.xmlStr = xml;
      });
      vm.CmmnModeler.saveSVG({ format: true }, (err, data) => {
        if (err) {
          return;
        }
      });
    },
    // 上传文件
    handleOpenFile(e) {
      const vm = this;
      const input = document.createElement("input");
      input.type = "file";
      input.click(); // 打开文件选择框
      input.onchange = function() {
        const file = input.files[0];
        if (window.FileReader) {
          try {
            var fr = new FileReader();
            fr.readAsText(file); // 将文件读取为文本
            fr.onload = function(e) {
              vm.cmmnInfo.xmlStr = fr.result;
              vm.createNewDiagram();
            };
          } catch (e) {
            errorInfo(e.toString());
          }
        } else {
          errorInfo("您的浏览器可能不支持此操作");
        }
      };
      document.body.removeChild(input);
    },
    // 下载xml/svg
    download(type, data, name) {
      // 下载xml/svg
      let dataTrack = "";
      const a = document.createElement("a");
      switch (type) {
        case "xml":
          dataTrack = "cmmn";
          break;
        case "svg":
          dataTrack = "svg";
          break;
        default:
          break;
      }
      const reName = name || `diagram.${dataTrack}`;
      a.setAttribute(
        "href",
        `data:application/cmmn20-xml;charset=UTF-8,${encodeURIComponent(data)}`
      );
      a.setAttribute("target", "_blank");
      a.setAttribute("dataTrack", `diagram:download-${dataTrack}`);
      a.setAttribute("download", reName);
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    },
    // 下载 XML 格式
    handleDownloadXml() {
      this.CmmnModeler.saveXML({ format: true }, (err, xml) => {
        if (err) {
          alert("导出错误，请重试");
        } else {
          this.download("xml", xml);
        }
      });
    },
    // 下载 SVG 格式
    saveImg() {
      this.CmmnModeler.saveSVG({ format: true }, (err, xml) => {
        if (err) {
          alert("导出错误，请重试");
        } else {
          this.download("svg", xml);
        }
      });
    },
    // 前进
    handleRedo() {
      this.CmmnModeler.get("commandStack").redo();
    },
    // 后退
    handleUndo() {
      this.CmmnModeler.get("commandStack").undo();
    },
    // 流程图放大缩小
    handleZoom(radio) {
      const newScale = !radio
        ? 1.0 // 不输入radio则还原
        : this.scale + radio <= 0.2 // 最小缩小倍数
        ? 0.2
        : this.scale + radio;

      this.CmmnModeler.get("canvas").zoom(newScale);
      this.scale = newScale;
      // this.setState({
      //    scale: newScale,
      //});
    },
    resetView() {
      //恢复到原位
      const vm=this;
      vm.CmmnModeler.get("canvas").zoom("fit-viewport");
    }
  }
};
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
