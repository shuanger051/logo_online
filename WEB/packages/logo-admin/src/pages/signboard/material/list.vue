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
        {
          key: "1",
          name: "胡彦斌",
          sex: "男",
          id_no: "337689199009102201",
          remark: "2222",
          time: "2022/03/28 08:12:00",
        },
        {
          key: "2",
          name: "胡彦祖",
          sex: "男",
          id_no: "337689199009102201",
          remark: "2222",
          time: "2022/03/28 08:12:00",
        },
      ],
      columns: [
        {
          title: "店主姓名",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "性别",
          dataIndex: "sex",
          key: "sex",
        },
        {
          title: "手机号",
          dataIndex: "mobile",
          key: "mobile",
        },
        {
          title: "身份证号",
          dataIndex: "id_no",
          key: "id_no",
        },
        {
          title: "备注",
          dataIndex: "remark",
          key: "remark",
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
