<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <form-serach :fields="serachFields" @serach="onSerach" />
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
      <!-- 店招预览图 -->
      <template slot="logoImg" slot-scope="val, record">
        <img class="logo-img" :src="record.urlPath" />
      </template>
      <!-- 操作列 -->
      <template slot="operation" slot-scope="text, record">
        <!-- btn:删除 -->
        <a-popconfirm
          title="删除后不可恢复，是否确认删除？"
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
import { signboardService } from "@/services";
import FormSerach from "@/components/form/FormSerach.vue";
export default {
  components: { FormSerach },
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
    // 表格列配置
    columns() {
      return [
        {
          title: "商户编号",
          dataIndex: "merchantId",
          key: "merchantId",
        },
        {
          title: "商铺编号",
          dataIndex: "shopsId",
          key: "shopsId",
        },
        {
          title: "店招名称",
          dataIndex: "logoName",
          key: "logoName",
        },
        {
          title: "店招预览",
          dataIndex: "urlPath",
          key: "urlPath",
          scopedSlots: { customRender: "logoImg" },
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
        },
      ];
    },
    // 查询字段
    serachFields() {
      return [{ name: "logoName", label: "店招名称" }];
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
    } = useTable(signboardService.getLogoListByPage);

    // 新增事件
    const onAdd = createModalEvent(Detail, { title: "新增用户" });
    // 编辑事件
    const onEdit = createModalEvent(Detail, { title: "编辑用户" });

    return {
      formData,
      loading,
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
    // event：删除
    onDel(record) {
      return signboardService
        .deleteLogoInfoByID(_.pick(record, ["id"]))
        .then(() => message.success("删除成功"))
        .catch((err) =>
          message.error(`删除失败：${_.get(err, "msg", "未知错误")}`)
        );
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  .logo-img {
    max-width: 200px;
  }
}
</style>
