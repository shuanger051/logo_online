<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach ref="serach" :fields="serachFields" @serach="onSerach" />
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
        <!-- btn:查看 -->
        <a-button type="link" size="small" @click="onView({ detail: record })"
          >备案详情</a-button
        >
        <!-- btn:审核 -->
        <a-button
          v-if="record.isFilings == '1'"
          type="link"
          size="small"
          @click="onAudit(record)"
          >审核</a-button
        >
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
import { mapDictObject } from "@/store/helpers";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 字典项
    ...mapState({
      // 店铺属性
      DictShopsType: mapDictObject("shopsType"),
      // 行业类型
      DictIndustryType: mapDictObject("industryType"),
      // 营业年限
      DictBizYears: mapDictObject("bizYears"),
    }),
    // 表格列配置
    columns() {
      return [
        {
          title: "商铺名称",
          dataIndex: "shopName",
          key: "shopName",
          width: "240px",
        },
        {
          title: "行业类型",
          dataIndex: "industryType",
          key: "industryType",
          width: "100px",
          customRender: (val) => this.DictIndustryType[val],
        },
        {
          title: "商铺属性",
          dataIndex: "shopsType",
          key: "shopsType",
          width: "100px",
          customRender: (val) => this.DictShopsType[val],
        },
        {
          title: "商铺地址",
          dataIndex: "address",
          key: "address",
          width: "240px",
        },
        {
          title: "营业年限",
          dataIndex: "bizYears",
          key: "bizYears",
          width: "120px",
          customRender: (val) => this.DictBizYears[val],
        },
        {
          title: "经办人",
          dataIndex: "handledByName",
          key: "handledByName",
          width: "100px",
        },
        {
          title: "经办人手机号",
          dataIndex: "handledByPhone",
          key: "handledByPhone",
          width: "120px",
        },
        {
          title: "是否老店",
          dataIndex: "isOldShops",
          key: "isOldShops",
          align: "center",
          width: "80px",
          customRender: (val) => (val == "1" ? "是" : "否"),
        },
        {
          title: "备案状态",
          dataIndex: "isFilings",
          key: "isFilings",
          width: "80px",
          customRender: (val) => {
            const dtm = { 0: "未备案", 1: "待审核", 2: "已备案" };
            return dtm[val];
          },
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
          width: "140px",
        },
      ];
    },
    // 支持查询字段
    serachFields() {
      return [
        { name: "shopName", label: "商铺名称" },
        { name: "handledByName", label: "经办人姓名" },
        { name: "handledByPhone", label: "经办人手机号" },
        { name: "industryType", label: "行业类型" },
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
    // 查看事件
    const onView = createModalEvent(Detail, { title: "备案详情" });

    return {
      formData,
      list,
      page,
      onAdd,
      onEdit,
      onView,
      onSerach,
      onChange,
    };
  },
  created() {
    // 获取字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["shopsType", "industryType", "bizYears"],
    });
  },
  mounted() {
    // 获取查询参数
    const { query } = this.$route;
    this.$refs.serach.setFieldsValue(query);
    // console.log(query)
    this.onSerach(query);
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
    // 审核
    onAudit(data) {},
  },
};
</script>
