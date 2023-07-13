
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
          title: "栏目ID",
          dataIndex: "channelId",
          key: "channelId",
        },
        {
          title: "是否推荐",
          dataIndex: "isRecommend",
          key: "isRecommend",
        },
        {
          title: "状态",
          dataIndex: "status",
          key: "status",
        },
        {
          title: "日访问数",
          dataIndex: "viewsDay",
          key: "viewsDay",
        },
        {
          title: "审核信息",
          dataIndex: "contentCheck",
          key: "contentCheck",
        },
        {
          title: "文章ID",
          dataIndex: "contentId",
          key: "contentId",
        },
        {
          title: "审核步数",
          dataIndex: "checkStep",
          key: "checkStep",
        },
        {
          title: "审核意见",
          dataIndex: "checkOpinion",
          key: "checkOpinion",
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
