<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach :fields="serachFields" @serach="onSerach">
      <a-button type="primary" @click="onAdd">新增</a-button>
    </form-serach>
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
import FormSerach from "@/components/form/FormSerach.vue";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "条目键值",
          dataIndex: "dictKey",
          key: "dictKey",
        },
        {
          title: "条目名称",
          dataIndex: "dictName",
          key: "dictName",
        },
        {
          title: "备注",
          dataIndex: "remark",
          key: "remark",
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    serachFields() {
      return [
        { name: "dictKey", label: "条目键值" },
        { name: "dictName", label: "条目名称" },
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
      onChange,
      createDelEvent,
      createModalEvent,
    } = useTable(systemService.getDictListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, { title: "新增字典项" });
    // 编辑事件
    const onEdit = createModalEvent(Detail, { title: "编辑字典项" });
    // 删除事件
    const onDel = createDelEvent((data) =>
      systemService.deleteDictById(_.pick(data, ["id"]))
    );

    return {
      formData,
      list,
      page,
      onDel,
      onAdd,
      onEdit,
      onSerach,
      onChange,
    };
  },
  created(){
    this.onSerach()
  }
};
</script>
<style lang="less" scoped></style>
