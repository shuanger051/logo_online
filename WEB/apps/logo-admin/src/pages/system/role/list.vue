<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form" :model="formData">
      <a-form-item label="角色名" name="roleName">
        <a-input v-model="formData.roleName" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="角色等级" name="roleLevel">
        <a-input v-model="formData.roleLevel" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="角色状态" name="roleStatus">
        <a-input v-model="formData.roleStatus" placeholder="请输入" />
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
    >
      <template slot="operation" slot-scope="text, record">
        <a-button type="link" size="small" @click="onDel(record)"
          >删除</a-button
        >
      </template>
    </a-table>
  </div>
</template>
<script>
import _ from 'lodash';
import { mapState } from "vuex";
import { systemService } from "@/services";
import useTable from "@/hooks/useTable";
export default {
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    columns() {
      return [
        {
          title: "角色名称",
          dataIndex: "roleName",
          key: "roleName",
        },
        {
          title: "角色等级",
          dataIndex: "roleLevel",
          key: "roleLevel",
        },
        {
          title: "角色状态",
          dataIndex: "roleStatus",
          key: "roleStatus",
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
    } = useTable(systemService.getSysRoleListByPage);

    // event：删除
    const onDel = createDelEvent((data) =>
      systemService.deleteSysRoleById(_.pick(data, ["id"]))
    );

    return {
      formData,
      list,
      page,
      onDel,
      onSerach,
      onReset,
      onChange,
    };
  },
};
</script>
<style lang="less" scoped>
.serach-form {
  margin-bottom: 24px;
}
</style>
