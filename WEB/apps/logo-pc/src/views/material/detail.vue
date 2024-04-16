<template>
  <div class="page-wrap">
    <template v-if="detail">
      <div style="margin-bottom: 12px">{{ detail.text }}</div>
      <a-list
        :grid="{ gutter: 12, column: 4 }"
        :data-source="detail.imgs"
        :pagination="{ pageSize: 16, hideOnSinglePage: true }"
      >
        <a-list-item slot="renderItem" slot-scope="item">
          <img class="example-img" :src="item" fit="cover" />
        </a-list-item>
      </a-list>
    </template>
  </div>
</template>
<script>
import evnetBus from '@/core/eventBus';
export default {
  data() {
    return {
      detail: null,
    };
  },
  created() {
    const { name } = this.$route.query;
    const list = window.pageContentJson.texture;
    const data = list.find((item) => item.key == name);
    if (data) {
      evnetBus.$emit("subtitle", data.name);
      this.detail = data.content;
    }
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
  .example-img {
    max-width: 100%;
  }
}
</style>
