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
      list: [
       
      ],
      columns: [
        {
          title: "商户名称",
          dataIndex: "merchantName",
          key: "merchantName",
        },
        {
          title: "商户状态",
          dataIndex: "merchantStatus",
          key: "merchantStatus",
        },
        {
          title: "性别",
          dataIndex: "gender",
          key: "gender",
        },
        {
          title: "身份证号",
          dataIndex: "idCard",
          key: "idCard",
        },
        {
          title: "手机号",
          dataIndex: "phone",
          key: "phone",
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
