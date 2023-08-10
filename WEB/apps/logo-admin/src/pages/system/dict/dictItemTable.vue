<template>
  <div>
    <div class="action-bar">
      <a-button type="primary" @click="onAdd">新增</a-button>
    </div>
    <a-table
      rowKey="itemKey"
      size="small"
      :scroll="{ y: 300 }"
      :bordered="true"
      :data-source="list"
      :columns="columns"
    >
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <!-- 修改 -->
        <a-button type="link" size="small" @click="onEdit({ record, dictKey })"
          >修改</a-button
        >
        <!-- 删除 -->
        <a-popconfirm title="是否确认删除该字典子项？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import { systemService } from "@/services";
import useTable from "@/hooks/useTable";
import DictItemDetail from "./dictItemDetail";
export default {
  props: {
    // 字典项键值
    dictKey: String,
  },
  data() {
    return {
      loading: false,
      list: [],
    };
  },
  computed: {
    // table列配置
    columns() {
      return [
        {
          title: "子项值",
          dataIndex: "itemValue",
          key: "itemValue",
        },
        {
          title: "子项key",
          dataIndex: "itemKey",
          key: "itemKey",
        },
        {
          title: "操作",
          key: "operation",
          width: "120px",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
  },
  watch: {
    // 变化则更新列表
    dictKey(nVal, oVal) {
      if (nVal != oVal) {
        this.onSerach(nVal);
      }
    },
  },
  setup() {
    // 表格列表功能
    const { createModalEvent } = useTable();
    // 新增字典子项
    const doAdd = createModalEvent(DictItemDetail, { title: "新增字典子项" });
    // 编辑字典子项
    const onEdit = createModalEvent(DictItemDetail, { title: "编辑字典子项" });
    return {
      doAdd,
      onEdit,
    };
  },
  methods: {
    // 新增字典子项
    onAdd() {
      const { dictKey } = this;
      if (!dictKey) this.$message.warning("请先选择需要新增的字典项");
      else this.doAdd({ dictKey });
    },
    // 查询字典子项
    onSerach(dictKey) {
      this.loading = true;
      systemService
        .getItemsByDictKeyInDB({ dictKey })
        .finally(() => (this.loading = false))
        .then((res) => (this.list = res.data));
    },
    // 删除字典子项
    onDel(record) {
      systemService
        .deleteDictItemById(_.pick(record, ["id"]))
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
