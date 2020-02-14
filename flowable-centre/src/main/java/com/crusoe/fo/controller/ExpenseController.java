package com.crusoe.fo.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "expense")
public class ExpenseController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private HistoryService historyService;

    /*************** 此处为业务代码 ******************/

    @RequestMapping(value = "start")
    @ResponseBody
    public String start(String userId, String processKey) {
        // 启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, map);
        return "提交成功.流程Id为：" + processInstance.getId();
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        List<String> taskIdList = new ArrayList<String>();
        for (Task task : tasks) {
            System.out.println(task.toString());
            taskIdList.add(task.getId());
        }
        return taskIdList;
    }

    /**
     * 批准
     *
     * @param taskId 任务ID
     */
    @RequestMapping(value = "apply")
    @ResponseBody
    public String apply(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("流程不存在");
        }
        // 通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "通过");
        taskService.complete(taskId, map);
        return "processed ok!";
    }

    /**
     * 移动节点
     */
    @RequestMapping(value = "move/{proInstId}/{nodeId}/{toNodeId}")
    public void move(@PathVariable("proInstId") String proInstId, @PathVariable("nodeId") String nodeId,
            @PathVariable("toNodeId") String toNodeId) {
        runtimeService.createChangeActivityStateBuilder().processInstanceId(proInstId)
                .moveActivityIdTo(nodeId, toNodeId).changeState();
    }

    /**
     * 增加流程执行实例
     * 
     * @param nodeId
     * @param proInstId
     * @param assigneeStr 以逗号隔开的字符串
     */
    @RequestMapping(value = "addExecution/{nodeId}/{proInstId}/{assignees}")
    public void addExecution(@PathVariable("nodeId") String nodeId, @PathVariable("proInstId") String proInstId,
            @PathVariable("assignees") String assigneeStr) {
        String[] assignees = assigneeStr.split(",");
        for (String assignee : assignees) {
            runtimeService.addMultiInstanceExecution(nodeId, proInstId,
                    Collections.singletonMap("assignee", (Object) assignee));
        }
    }

    /**
     * 删除流程执行实例
     * 
     * @param excutionId
     * @param complated  是否完成此流程执行实例
     */
    @RequestMapping(value = "delExecution/{excutionId}/{complated}")
    public void delExecution(@PathVariable("excutionId") String excutionId,
            @PathVariable("complated") Boolean complated) {
        runtimeService.deleteMultiInstanceExecution(excutionId, complated);
    }

    /**
     * 移动到子流程
     * 
     * @param proInstId
     * @param subProcess
     * @param subNodeId
     * @param parentNodeId
     */
    @RequestMapping(value = "moveToSub/{proInstId}/{parentNodeId}/{subNodeId}/{subProcess}")
    public void moveToSub(@PathVariable("proInstId") String proInstId, @PathVariable("subProcess") String subProcess,
            @PathVariable("subNodeId") String subNodeId, @PathVariable("parentNodeId") String parentNodeId) {
        runtimeService.createChangeActivityStateBuilder().processInstanceId(proInstId)
                .moveActivityIdToSubProcessInstanceActivityId(parentNodeId, subNodeId, subProcess).changeState();
    }

    /**
     * 移动到父节点
     * 
     * @param subProInstId
     * @param subNodeId
     * @param parentNodeId
     */
    @RequestMapping(value = "moveToParent/{subProInstId}/{subNodeId}/{parentNodeId}")
    public void moveToParent(@PathVariable("subProInstId") String subProInstId,
            @PathVariable("subNodeId") String subNodeId, @PathVariable("parentNodeId") String parentNodeId) {
        runtimeService.createChangeActivityStateBuilder().processInstanceId(subProInstId)
                .moveActivityIdToParentActivityId(subNodeId, parentNodeId).changeState();
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @RequestMapping(value = "processDiagram")
    @ResponseBody
    public void genProcessDiagram(String processId, HttpServletResponse httpServletResponse) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        // 流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        // 使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(InstanceId).list();

        // 通过流程实例ID获取流程中已经执行的节点，按照执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstanceList = getHistoricActivityInstAsc(processId);
        // 将已经执行的节点ID放入高亮显示节点集合
        List<String> highLightedActivitiIdList = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            highLightedActivitiIdList.add(historicActivityInstance.getActivityId());
            // logger.info("已执行的节点[{}-{}-{}-{}]", historicActivityInstance.getId(),
            // historicActivityInstance
            // .getActivityId(), historicActivityInstance.getActivityName(),
            // historicActivityInstance
            // .getAssignee());
        }

        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        // ProcessDiagramGenerator diagramGenerator = new
        // CustomProcessDiagramGenerator();
        engconf.setActivityFontName("宋体");
        engconf.setLabelFontName("宋体");
        engconf.setAnnotationFontName("宋体");

        // 得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        List<HistoricActivityInstance> historyProcess = getHistoryProcess(processId);

        for (HistoricActivityInstance hi : historyProcess) {
            String activityType = hi.getActivityType();
            if (activityType.equals("sequenceFlow") || activityType.equals("exclusiveGateway")) {
                flows.add(hi.getActivityId());
            } else if (activityType.equals("userTask") || activityType.equals("startEvent")) {
                // activityIds.add(hi.getActivityId());
            }
        }

        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "jpg", activityIds, flows,
                engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
                engconf.getClassLoader(), 1.0, true);

        // 设置响应的类型格式为图片格式
        httpServletResponse.setContentType("image/jpg");
        // 禁止图像缓存。
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);

        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 获取已流经的流程线，需要高亮显示高亮流程已发生流转的线id集合
     *
     * @param bpmnModel
     * @param historicActivityInstanceList
     * @return
     */
    public List<String> getHighLightedFlows(BpmnModel bpmnModel,
            List<HistoricActivityInstance> historicActivityInstanceList) {
        // 已流经的流程线，需要高亮显示
        List<String> highLightedFlowIdList = new ArrayList<>();
        // 全部活动节点
        List<FlowNode> allHistoricActivityNodeList = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstanceList = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            // 获取流程节点
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess()
                    .getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);
            // 结束时间不为空，当前节点则已经完成
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstanceList.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        HistoricActivityInstance currentActivityInstance;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (int k = 0; k < finishedActivityInstanceList.size(); k++) {
            currentActivityInstance = finishedActivityInstanceList.get(k);
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess()
                    .getFlowElement(currentActivityInstance.getActivityId(), true);
            // 当前节点的所有流出线
            List<SequenceFlow> outgoingFlowList = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已流转的 满足如下条件认为已流转：
             * 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             * (第2点有问题，有过驳回的，会只绘制驳回的流程线，通过走向下一级的流程线没有高亮显示)
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType())
                    || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow outgoingFlow : outgoingFlowList) {
                    // 获取当前节点流程线对应的下级节点
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(),
                            true);
                    // 如果下级节点包含在所有历史节点中，则将当前节点的流出线高亮显示
                    if (allHistoricActivityNodeList.contains(targetFlowNode)) {
                        highLightedFlowIdList.add(outgoingFlow.getId());
                    }
                }
            } else {
                /**
                 * 2、当前节点不是并行网关或兼容网关 【已解决-问题】如果当前节点有驳回功能，驳回到申请节点，
                 * 则因为申请节点在历史节点中，导致当前节点驳回到申请节点的流程线被高亮显示，但实际并没有进行驳回操作
                 */
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                // 当前节点ID
                String currentActivityId = currentActivityInstance.getActivityId();
                int size = historicActivityInstanceList.size();
                boolean ifStartFind = false;
                boolean ifFinded = false;
                HistoricActivityInstance historicActivityInstance;
                // 循环当前节点的所有流出线
                // 循环所有历史节点
                // logger.info("【开始】-匹配当前节点-ActivityId=【{}】需要高亮显示的流出线", currentActivityId);
                // logger.info("循环历史节点");
                for (int i = 0; i < historicActivityInstanceList.size(); i++) {
                    // //
                    // 如果当前节点流程线对应的下级节点在历史节点中，则该条流程线进行高亮显示（【问题】有驳回流程线时，即使没有进行驳回操作，因为申请节点在历史节点中，也会将驳回流程线高亮显示-_-||）
                    // if
                    // (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef()))
                    // {
                    // Map<String, Object> map = new HashMap<>();
                    // map.put("highLightedFlowId", sequenceFlow.getId());
                    // map.put("highLightedFlowStartTime",
                    // historicActivityInstance.getStartTime().getTime());
                    // tempMapList.add(map);
                    // // highLightedFlowIdList.add(sequenceFlow.getId());
                    // }
                    // 历史节点
                    historicActivityInstance = historicActivityInstanceList.get(i);
                    // logger.info("第【{}/{}】个历史节点-ActivityId=[{}]", i + 1, size,
                    // historicActivityInstance.getActivityId());
                    // 如果循环历史节点中的id等于当前节点id，从当前历史节点继续先后查找是否有当前节点流程线等于的节点
                    // 历史节点的序号需要大于等于已完成历史节点的序号，防止驳回重审一个节点经过两次是只取第一次的流出线高亮显示，第二次的不显示
                    if (i >= k && historicActivityInstance.getActivityId().equals(currentActivityId)) {
                        // logger.info("第[{}]个历史节点和当前节点一致-ActivityId=[{}]", i + 1,
                        // historicActivityInstance.getActivityId());
                        ifStartFind = true;
                        // 跳过当前节点继续查找下一个节点
                        continue;
                    }
                    if (ifStartFind) {
                        // logger.info("[开始]-循环当前节点-ActivityId=【{}】的所有流出线", currentActivityId);

                        ifFinded = false;
                        for (SequenceFlow sequenceFlow : outgoingFlowList) {
                            // 如果当前节点流程线对应的下级节点在其后面的历史节点中，则该条流程线进行高亮显示
                            // 【问题】
                            // logger.info("当前流出线的下级节点=[{}]", sequenceFlow.getTargetRef());
                            if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                                // logger.info("当前节点[{}]需高亮显示的流出线=[{}]", currentActivityId,
                                // sequenceFlow.getId());
                                highLightedFlowIdList.add(sequenceFlow.getId());
                                // 暂时默认找到离当前节点最近的下一级节点即退出循环，否则有多条流出线时将全部被高亮显示
                                ifFinded = true;
                                break;
                            }
                        }
                        // logger.info("[完成]-循环当前节点-ActivityId=【{}】的所有流出线", currentActivityId);
                    }
                    if (ifFinded) {
                        // 暂时默认找到离当前节点最近的下一级节点即退出历史节点循环，否则有多条流出线时将全部被高亮显示
                        break;
                    }
                }
                // logger.info("【完成】-匹配当前节点-ActivityId=【{}】需要高亮显示的流出线", currentActivityId);
                // if (!CollectionUtils.isEmpty(tempMapList)) {
                // // 遍历匹配的集合，取得开始时间最早的一个
                // long earliestStamp = 0L;
                // String highLightedFlowId = null;
                // for (Map<String, Object> map : tempMapList) {
                // long highLightedFlowStartTime =
                // Long.valueOf(map.get("highLightedFlowStartTime").toString());
                // if (earliestStamp == 0 || earliestStamp <= highLightedFlowStartTime) {
                // highLightedFlowId = map.get("highLightedFlowId").toString();
                // earliestStamp = highLightedFlowStartTime;
                // }
                // }
                // highLightedFlowIdList.add(highLightedFlowId);
                // }

            }

        }
        return highLightedFlowIdList;
    }

    /**
     * 通过流程实例ID获取流程中已经执行的节点，按照执行先后顺序排序
     *
     * @param procInstId
     * @return
     */
    public List<HistoricActivityInstance> getHistoricActivityInstAsc(String procInstId) {
        return historyService.createHistoricActivityInstanceQuery().processInstanceId(procInstId)
                .orderByHistoricActivityInstanceId().asc().list();
    }

    /**
     * 任务历史
     *
     * @param processId 部署id
     */
    public List<HistoricActivityInstance> getHistoryProcess(String processId) {
        List<HistoricActivityInstance> list = historyService // 历史相关Service
                .createHistoricActivityInstanceQuery() // 创建历史活动实例查询
                .processInstanceId(processId) // 执行流程实例id
                .finished().list();
        return list;
    }

}
