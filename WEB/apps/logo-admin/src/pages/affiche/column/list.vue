<template>
  <div class="page-wrap" :style="`min-height: ${pageMinHeight}px`">
    <!-- 搜索条件栏 -->
    <a-form layout="inline" class="serach-form" :model="formData">
      <a-form-item
        label="用户名"
        name="username"
        :rules="[{ required: true, message: 'Please input your username!' }]"
      >
        <a-input v-model="formData.username" placeholder="请输入" />
      </a-form-item>
      <a-form-item
        label="手机号"
        name="mobile"
        :rules="[{ required: true, message: 'Please input your username!' }]"
      >
        <a-input v-model="formData.mobile" placeholder="请输入" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary">查询</a-button>
      </a-form-item>
    </a-form>
    <!-- 结果列表 -->
    <a-table bordered :data-source="list" :columns="columns"></a-table>
  </div>
</template>
<script>
import { mapState } from "vuex";
export default {
  computed: {
    ...mapState("setting", ["pageMinHeight"]),
  },
  data() {
    return {
      formData: {},
      list: [],
      columns: [
        {
          title: "模块ID",
          dataIndex: "modelId",
          key: "modelId",
        },
        {
          title: "栏目名称",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "排序编号",
          dataIndex: "orderNo",
          key: "orderNo",
        },
        {
          title: "是否显示",
          dataIndex: "isDisplay",
          key: "isDisplay",
        },
        {
          title: "描述",
          dataIndex: "description",
          key: "description",
        },
        {
          title: "栏目访问级别",
          dataIndex: "commentControl",
          key: "commentControl",
        },
        {
          title: "是否开放",
          dataIndex: "allowUpdown",
          key: "allowUpdown",
        },
        {
          title: "栏目路径",
          dataIndex: "channelPath",
          key: "channelPath",
        },
      ],
    };
  },
};
</script>
<style lang="less" scoped>
.serach-form {
  margin-bottom: 24px;
}
</style>
