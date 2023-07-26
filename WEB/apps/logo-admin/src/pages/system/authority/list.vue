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
      <a-space>
        <a-button type="primary" @click="onAdd">新增</a-button>
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
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <a-button type="link" size="small" @click="onDel(record)"
          >删除</a-button
        >
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import useTable from "@/hooks/useTable";
import { mapState } from "vuex";
import { systemService } from "@/services";
export default {
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "ID",
          dataIndex: "id",
          key: "id",
        },
        {
          title: "权限名称",
          dataIndex: "permissionName",
          key: "permissionName",
        },
        {
          title: "父级ID",
          dataIndex: "parentId",
          key: "parentId",
        },
        {
          title: "权限类型",
          dataIndex: "permissionType",
          key: "permissionType",
        },
        {
          title: "权限等级",
          dataIndex: "permissionLevel",
          key: "permissionLevel",
        },
        {
          title: "权限路径",
          dataIndex: "permissionPath",
          key: "permissionPath",
        },
        {
          title: "权限组",
          dataIndex: "permissionGroup",
          key: "permissionGroup",
        },
        {
          title: "图标",
          dataIndex: "iconPath",
          key: "iconPath",
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
      createDelEvent,
      createModalEvent,
    } = useTable(systemService.getSysPermissionListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, { title: "新增用户" });
    // 编辑事件
    const onEdit = createModalEvent(Detail, { title: "编辑用户" });
    // 删除事件
    const onDel = createDelEvent((data) =>
      systemService.deleteSysPermissionById(_.pick(data, ["id"]))
    );

    return {
      formData,
      list,
      page,
      onDel,
      onAdd,
      onEdit,
      onSerach,
      onReset,
      onChange,
    };
  },
  created() {
    this.onSerach();
  },
  methods: {
    // 重置密码
    onResetPwd() {},
  },
};
</script>
<style lang="less" scoped></style>
