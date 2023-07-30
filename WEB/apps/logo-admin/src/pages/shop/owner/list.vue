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
        <!-- btn:删除 -->
        <a-popconfirm title="是否确认删除该商户信息？" @confirm="onDel(record)">
          <a-button type="link" size="small">删除</a-button>
        </a-popconfirm>
      </template>
    </a-table>
  </div>
</template>
<script>
import Detail from "./detail";
import useTable from "@/hooks/useTable";
import { mapState } from "vuex";
import { shopService } from "@/services";
import { message } from "ant-design-vue";
import FormSerach from "@/components/form/FormSerach.vue";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "商户ID",
          dataIndex: "id",
          key: "id",
        },
        {
          title: "商户名称",
          dataIndex: "merchantName",
          key: "merchantName",
        },
        {
          title: "性别",
          dataIndex: "gender",
          width: "80px",
          key: "gender",
        },
        {
          title: "商户状态",
          dataIndex: "merchantStatus",
          width: "80px",
          key: "merchantStatus",
        },
        {
          title: "联系电话",
          dataIndex: "phone",
          width: "140px",
          key: "phone",
        },
        {
          title: "证件号码",
          dataIndex: "idCard",
          width: "200px",
          key: "idCard",
        },
        {
          title: "备注",
          dataIndex: "remark",
          key: "remark",
        },
        {
          title: "操作",
          key: "operation",
          width: "120px",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    // 查询字段
    serachFields() {
      return [
        { name: "merchantName", label: "商户名称" },
        {
          name: "merchantStatus",
          label: "商户状态",
          component: "select",
          props: {
            options: [
              // 1-注销，2-开业，3-停业，4-未开业
              { value: "1", label: "注销" },
              { value: "2", label: "开业" },
              { value: "3", label: "停业" },
              { value: "4", label: "未开业" },
            ],
          },
        },
        { name: "phone", label: "联系电话" },
        {
          name: "gender",
          label: "性别",
          component: "select",
          props: {
            options: [
              { value: "0", label: "保密" },
              { value: "1", label: "男" },
              { value: "2", label: "女" },
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
      list,
      page,
      onSerach,
      onReset,
      onChange,
      createDelEvent,
      createModalEvent,
    } = useTable(shopService.getMerchantInfoListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, {
      title: "新增用户",
      props: {
        action: "add",
      },
    });
    // 编辑事件
    const onEdit = createModalEvent(Detail, {
      title: "编辑用户",
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
      shopService
        .deleteMerchantInfoById(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.pick(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped></style>
