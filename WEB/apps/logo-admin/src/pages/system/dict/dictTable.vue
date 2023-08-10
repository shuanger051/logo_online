<template>
  <div>
    <div class="action-bar">
      <a-button type="primary" @click="onAdd">新增</a-button>
    </div>
    <a-table
      rowKey="id"
      size="small"
      :bordered="true"
      :scroll="{ y: 300 }"
      :custom-row="customRow"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      @change="onChange"
    >
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <!-- 修改 -->
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <!-- 删除 -->
        <a-popconfirm title="是否确认删除该字典项？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import { systemService } from "@/services";
import useTable from "@/hooks/useTable";
import DictDetail from "./dictDetail";
export default {
  computed: {
    // 表格列配置
    columns() {
      return [
        {
          title: "条目名称",
          dataIndex: "dictName",
          key: "dictName",
        },
        {
          title: "条目键值",
          dataIndex: "dictKey",
          key: "dictKey",
        },
        {
          title: "操作",
          key: "operation",
          width: "120px",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    // 行自定义
    customRow() {
      return (record) => {
        return {
          on: {
            click: () => this.$emit("update:selected", record.dictKey),
          },
        };
      };
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      list,
      page,
      onSerach,
      onChange,
      createModalEvent,
    } = useTable(systemService.getDictListByPage);

    // 新增事件
    const onAdd = createModalEvent(DictDetail, { title: "新增字典项" });
    // 编辑事件
    const onEdit = createModalEvent(DictDetail, { title: "编辑字典项" });

    return {
      formData,
      list,
      page,
      onAdd,
      onEdit,
      onSerach,
      onChange,
    };
  },
  created() {
    this.onSerach();
  },
  methods: {
    // 删除字典项
    onDel(record) {
      systemService
        .deleteDictById(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>

<style lang="less" scoped>
.action-bar {
  margin-bottom: 12px;
}
</style>
