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
      <template slot="archives">
        <a-button type="link" size="small">查看</a-button>
      </template>
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <a-button type="link" size="small" @click="onEdit({ record })"
          >修改</a-button
        >
        <!-- btn:删除 -->
        <a-popconfirm
          title="删除后不可恢复，是否确认删除该商铺信息？"
          @confirm="onDel(record)"
        >
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
          title: "行业类型",
          dataIndex: "industryType",
          key: "industryType",
        },
        {
          title: "店铺属性",
          dataIndex: "shopsType",
          key: "shopsType",
        },
        {
          title: "店铺地址",
          dataIndex: "address",
          key: "address",
        },
        {
          title: "营业年限",
          dataIndex: "bizYears",
          key: "bizYears",
        },
        {
          title: "是否老店",
          dataIndex: "isOldShops",
          key: "isOldShops",
        },
        {
          title: "备注",
          dataIndex: "remark",
          key: "remark",
        },
        {
          title: "备案资料",
          key: "archives",
          scopedSlots: { customRender: "archives" },
        },
        ,
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    // 支持查询字段
    serachFields() {
      return [
        { name: "industryType", label: "行业类型" },
        { name: "address", label: "店铺地址" },
        {
          name: "isOldShops",
          label: "是否老店",
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
      list,
      page,
      onSerach,
      onChange,
      createModalEvent,
    } = useTable(shopService.getShopsInfoListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, { title: "新增用户" });
    // 编辑事件
    const onEdit = createModalEvent(Detail, { title: "编辑用户" });

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
    // 删除商铺信息
    onDel(record) {
      shopService
        .deleteShopsInfoById(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
