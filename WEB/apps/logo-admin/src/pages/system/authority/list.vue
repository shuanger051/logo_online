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
      ],
      columns: [
        {
          title: "权限类型",
          dataIndex: "permissionType",
          key: "permissionType",
        },
        {
          title: "权限等级",
          dataIndex: "permissionLevel",
          key: "permissionLevel",
        },
        {
          title: "权限标题",
          dataIndex: "permissionName",
          key: "permissionName",
        },
        {
          title: "权限地址",
          dataIndex: "permissionPath",
          key: "permissionPath",
        },
        {
          title: "权限分组",
          dataIndex: "permissionGroup",
          key: "permissionGroup",
        },
        {
          title: "排序",
          dataIndex: "sortNo",
          key: "sortNo",
        },
        {
          title: "图标",
          dataIndex: "iconPath",
          key: "iconPath",
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
