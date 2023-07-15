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
    <a-table :data-source="list" :columns="columns"></a-table>
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
          age: 32,
          address: "西湖区湖底公园1号",
        },
        {
          key: "2",
          name: "胡彦祖",
          age: 42,
          address: "西湖区湖底公园1号",
        },
      ],
      columns: [
        {
          title: "姓名",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "年龄",
          dataIndex: "age",
          key: "age",
        },
        {
          title: "住址",
          dataIndex: "address",
          key: "address",
        },
      ],
    };
  },
};
</script>
<style lang="less" scoped>
.serach-form{
  margin-bottom: 24px;
}

</style>
