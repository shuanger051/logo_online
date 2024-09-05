<template>
  <div class="page-wrap">
    <a-list
      :grid="{ gutter: 12, column: 3 }"
      :data-source="detail.imgs"
      :pagination="{ pageSize: 9, hideOnSinglePage: true }"
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <img class="example-img" :src="item" fit="cover" />
      </a-list-item>
    </a-list>
  </div>
</template>
<script>
import evnetBus from "@/core/eventBus";

export default {
  data() {
    return {
      detail: null,
    };
  },
  created() {
    const { name = "", type } = this.$route.query;
    const list = window.pageContentJson.material[type];
    const names = name.split(",");
    const data = list.find((item) => names.includes(item.key));
    if (data) this.detail = data.content;
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px;
  padding-top: 24px;
  font-size: 14px;
  max-width: 1000px;
  margin: 0 auto;
  line-height: 1.6em;
  :deep(.ant-list) {
    &-item {
      text-align: center;
    }
  }
  .example-img {
    max-width: 100%;
    height: 156px;
  }
}
</style>
