<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form" :model="formData">
      <a-form-item label="用户名" name="userName">
        <a-input v-model="formData.userName" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="邮箱" name="email">
        <a-input v-model="formData.email" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="是否超管" name="isAdmin">
        <a-select
          v-model="formData.isAdmin"
          style="width: 120px"
          allowClear
          placeholder="请选择"
        >
          <a-select-option value="1">是</a-select-option>
          <a-select-option value="0">否</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="是否禁用" name="isDisabled">
        <a-select
          v-model="formData.isDisabled"
          style="width: 120px"
          allowClear
          placeholder="请选择"
        >
          <a-select-option value="1">是</a-select-option>
          <a-select-option value="0">否</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
    <!-- 操作栏 -->
    <div class="serach-action-bar">
      <a-space>
        <a-button type="primary" @click="onSerach">查询</a-button>
        <a-button type="danger" @click="onReset">重置</a-button>
      </a-space>
    </div>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :bordered="true"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      @change="onChange"
    >
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <a-button type="link" size="small" @click="onView({ record })"
          >详情</a-button
        >
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import { mapState } from "vuex";
import { logsService } from "@/services";
import useTable from "@/hooks/useTable";
export default {
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "操作人",
          dataIndex: "userName",
          key: "userName",
        },
        {
          title: "操作类型",
          dataIndex: "type",
          key: "type",
        },
        {
          title: "操作内容",
          dataIndex: "content",
          key: "content",
        },
        {
          title: "IP地址",
          dataIndex: "ip",
          key: "ip",
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      list,
      page,
      onSerach,
      onReset,
      onChange,
      createModalEvent,
    } = useTable(logsService.getLogInfoListByPage);

    // 查看
    const onView = createModalEvent(Detail, { title: "日志详情" });

    return {
      formData,
      list,
      page,
      onView,
      onSerach,
      onReset,
      onChange,
    };
  },
};
</script>
<style lang="less" scoped></style>
