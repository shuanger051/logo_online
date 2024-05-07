<template>
  <div class="page-wrap">
    <van-row v-if="detail">
      <van-col v-for="(val, idx) in detail.imgs" :key="idx" :span="24">
        <van-image :src="val" />
      </van-col>
    </van-row>
    <van-empty v-else image="search" description="未找到相关内容" />
    <submit-bar>
      <van-button block type="primary" @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import evnetBus from "../../core/eventBus";

export default {
  data() {
    return {
      detail: null,
    };
  },
  created() {
    const { streetId, streetType } = this.$route.query;
    let title
    if (streetType == 1) {
      title = '商业街道'
    } else if (streetType == 2) {
      title = '特色街道'
    } else if (streetType == 3) {
      title = '一般街道'
    }
    if (title) {
      evnetBus.$emit('customTitle', title)
    }
    const list = window.pageContentJson.streetView;
    const streetDtm = list.find((item) => streetType == item.id);
    // 存在街道
    if (streetDtm) {
      this.detail = streetDtm.street.find((item) => item.id == streetId);
    }
  },
  methods: {
    onNext() {
      const { query } = this.$route;
      this.$router.push({
        path: "/signboard/selfEdit",
        query,
      });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 12px 64px;
}
</style>
