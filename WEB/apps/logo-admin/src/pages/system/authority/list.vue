<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form" :model="formData">
      <a-form-item label="权限类型" name="permissionType">
        <a-input v-model="formData.permissionType" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="权限名称" name="permissionName">
        <a-input v-model="formData.permissionName" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="权限路径" name="permissionPath">
        <a-input v-model="formData.permissionPath" placeholder="请输入" />
      </a-form-item>
    </a-form>
    <!-- 操作区 -->
    <div class="serach-action-bar">
      <a-space>
        <a-button type="primary" @click="onSerach">查询</a-button>
        <a-button type="danger" @click="onReset">重置</a-button>
      </a-space>
      <a-space>
        <a-button type="primary">新增</a-button>
      </a-space>
    </div>
    <!-- 结果列表 -->
    <a-table
      :data-source="list"
      :columns="columns"
      :pagination="page"
      @change="onChange"
    ></a-table>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { systemService } from "@/services";
import useTable from "@/hooks/useTable";
export default {
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    columns() {
      return [
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
          title: "权限标题",
          dataIndex: "permissionName",
          key: "permissionName",
        },
        {
          title: "权限图标",
          dataIndex: "iconPath",
          key: "iconPath",
        },
        {
          title: "权限地址",
          dataIndex: "permissionPath",
          key: "permissionPath",
        },
        {
          title: "权限分组",
          dataIndex: "permissionGroup",
          key: "permissionGroup",
        },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const { formData, list, page, onSerach, onReset, onChange } = useTable(
      systemService.getSysPermissionListByPage
    );

    return {
      formData,
      list,
      page,
      onSerach,
      onReset,
      onChange,
    };
  },
};
</script>
<style lang="less" scoped>
</style>
