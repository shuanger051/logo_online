<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form">
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
        <a-button type="primary" @click="onAdd">新增</a-button>
      </a-space>
    </div>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :bordered="true"
      :data-source="list"
      :columns="columns"
      :pagination="page"
      @change="onChange"
    >
      <template slot="operation" slot-scope="text, record">
        <!-- btn:修改 -->
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <!-- btn:删除 -->
        <a-popconfirm title="是否确认删除该角色？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import { mapState } from "vuex";
import { systemService } from "@/services";
import useTable from "@/hooks/useTable";
import { message } from "ant-design-vue";
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
        { title: "描述", dataIndex: "description", key: "description" },
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
      createModalEvent,
    } = useTable(systemService.getSysRoleListByPage);

    // 新增
    const onAdd = createModalEvent(Detail, {
      title: "新增角色",
      props: {
        action: "add",
      },
    });

    // 编辑
    const onEdit = createModalEvent(Detail, {
      title: "修改",
      props: {
        action: "edit",
      },
    });

    return {
      formData,
      list,
      page,
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
    // event：删除
    onDel(record) {
      systemService
        .deleteSysRoleById(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
