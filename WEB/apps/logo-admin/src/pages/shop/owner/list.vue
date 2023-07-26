<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form" :model="formData">
      <a-form-item label="用户名" name="userName">
        <a-input v-model="formData.userName" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="邮箱" name="email">
        <a-input v-model="formData.email" placeholder="请输入" />
      </a-form-item>
      <a-form-item label="是否超管" name="isAdmin">
        <a-select
          v-model="formData.isAdmin"
          style="width: 120px"
          allowClear
          placeholder="请选择"
        >
          <a-select-option value="1">是</a-select-option>
          <a-select-option value="0">否</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="是否禁用" name="isDisabled">
        <a-select
          v-model="formData.isDisabled"
          style="width: 120px"
          allowClear
          placeholder="请选择"
        >
          <a-select-option value="1">是</a-select-option>
          <a-select-option value="0">否</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
    <!-- 操作栏 -->
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
export default {
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
          key: "gender",
        },
        {
          title: "商户状态",
          dataIndex: "merchantStatus",
          key: "merchantStatus",
        },
        {
          title: "联系电话",
          dataIndex: "phone",
          key: "phone",
        },
        {
          title: "证件号码",
          dataIndex: "idCard",
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
