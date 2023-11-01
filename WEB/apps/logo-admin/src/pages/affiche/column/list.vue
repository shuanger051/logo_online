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
      :loading="loading"
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
        <!-- btn:删除 -->
        <a-popconfirm title="是否确认删除该栏目？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import { mapState } from "vuex";
import { afficheService } from "@/services";
import useTable from "@/hooks/useTable";
import FormSerach from "@/components/form/FormSerach";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "栏目ID",
          dataIndex: "id",
          key: "id",
          width: "80px",
        },
        {
          title: "栏目名称",
          dataIndex: "name",
          key: "name",
          width: "240px",
        },
        {
          title: "栏目说明",
          dataIndex: "description",
          key: "description",
        },
        // {
        //   title: "模块ID",
        //   dataIndex: "modelId",
        //   key: "modelId",
        //   width: "80px",
        // },
        // {
        //   title: "父级ID",
        //   dataIndex: "parentId",
        //   key: "parentId",
        //   width: "80px",
        // },
        {
          title: "排序编号",
          dataIndex: "orderNo",
          key: "orderNo",
          width: "80px",
        },
        {
          title: "是否展示",
          dataIndex: "isDisplay",
          key: "isDisplay",
          width: "80px",
          customRender: (val) => {
            const dtm = { 0: "否", 1: "是" };
            return dtm[val];
          },
        },
        {
          title: "访问级别",
          dataIndex: "commentControl",
          key: "commentControl",
          width: "80px",
        },
        {
          title: "是否开放",
          dataIndex: "allowUpdown",
          key: "allowUpdown",
          width: "80px",
          customRender: (val) => {
            return val ? "是" : "否";
          },
        },
        {
          title: "栏目路径",
          dataIndex: "channelPath",
          key: "channelPath",
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
          width: "120px",
        },
      ];
    },
    // 搜索条件
    serachFields() {
      return [
        { name: "name", label: "栏目名称" },
        { name: "modelId", label: "模块编号" },
        {
          name: "isDisplay",
          label: "是否展示",
          component: "select",
          props: {
            options: [
              { value: "1", label: "是" },
              { value: "0", label: "否" },
            ],
          },
        },
      ];
    },
  },
  setup() {
    // 表格列表功能
    const {
      formData,
      loading,
      list,
      page,
      onSerach,
      onChange,
      createModalEvent,
    } = useTable(afficheService.getChannelListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, { title: "新增栏目" });
    // 编辑事件
    const onEdit = createModalEvent(Detail, { title: "编辑栏目" });

    return {
      formData,
      list,
      loading,
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
    // event：删除
    onDel(record) {
      afficheService
        .deleteChannelByID(_.pick(record, ["id"]))
        .then(() => this.$message.success("删除成功"))
        .catch((err) =>
          this.$message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
