<template>
  <div class="page-wrap">
    <template v-if="detail">
      <a-list
        :grid="{ gutter: 12, column: 2 }"
        :data-source="detail.imgs"
        :pagination="{ pageSize: 8, hideOnSinglePage: true }"
      >
        <a-list-item slot="renderItem" slot-scope="item">
          <img class="example-img" :src="item" fit="cover" />
        </a-list-item>
      </a-list>
    </template>
    <div class="action-bar">
      <a-button type="primary" @click="onNext">下一步</a-button>
    </div>
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
    const { streetId, streetType } = this.$route.query;
    const list = window.pageContentJson.streetView;
    const streetDtm = list.find((item) => streetType == item.id);
    // 存在街道
    if (streetDtm) {
      this.detail = streetDtm.street.find((item) => item.id == streetId);
      evnetBus.$emit("subtitle", this.detail.name);
    }
  },
  methods: {
    onNext() {
      const { query } = this.$route;
      this.$router.push({
        path: "/signboard/attribute",
        query,
      });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px 60px;
  max-width: 1000px;
  margin: 0 auto;
  margin-top: 24px;
  :deep(.ant-col) {
    margin-bottom: 12px;
    img {
      width: 100%;
    }
  }

  .action-bar {
    margin-top: 24px;
  }
}
</style>
