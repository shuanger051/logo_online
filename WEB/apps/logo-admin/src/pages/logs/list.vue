<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach :fields="serachFields" @serach="onSerach"></form-serach>
    <!-- 结果列表 -->
    <a-table
      rowKey="id"
      size="small"
      :bordered="true"
      :loading="loading"
      :data-source="list"
      :pagination="page"
      :columns="columns"
      @change="onChange"
    >
    </a-table>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { logsService } from "@/services";
import FormSerach from "@/components/form/FormSerach.vue";
import useTable from "@/hooks/useTable";
export default {
  components: { FormSerach },
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
      ];
    },
    // 查询字段
    serachFields() {
      return [
        { name: "userName", label: "操作人" },
        { name: "type", label: "操作类型" },
        { name: "content", label: "操作内容" },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      list,
      page,
      loading,
      onSerach,
      onChange,
    } = useTable(logsService.getLogInfoListByPage);

    return {
      formData,
      loading,
      list,
      page,
      onSerach,
      onChange,
    };
  },
};
</script>
<style lang="less" scoped></style>
