<template>
  <div class="page-wrap">
    <van-notice-bar
      v-if="$route.query.streetType !== '2'"
      wrapable
      :scrollable="false"
      text="请先阅读您商铺所在街道的一街一景介绍，如您的商铺所在街道不在列表范围内可点击跳过继续下一步"
    />
    <div class="content">
      <van-cell
        v-for="item in streetArr"
        :key="item.id"
        :border="false"
        @click="onNext(item.id)"
        >{{ item.name }}</van-cell
      >
      <van-cell :border="false" v-if="$route.query.streetType !== '2'">
        <van-button type="primary" size="small" @click="onJump"
          >跳过</van-button
        >
      </van-cell>
    </div>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
import evnetBus from "../../core/eventBus";

export default {
  components: { SubmitBar },
  data() {
    return {
      params: {},
      streetArr: [],
    };
  },
  created() {
    const { streetType } = this.$route.query;
    let title;
    if (streetType == 1) {
      title = "商业街道";
    } else if (streetType == 2) {
      title = "特色街道";
    } else if (streetType == 3) {
      title = "一般街道";
    }
    if (title) {
      evnetBus.$emit("customTitle", title);
    }
    const list = window.pageContentJson.streetView;
    const data = list.find((item) => item.id == streetType);
    if (data) this.streetArr = data.street;
  },
  methods: {
    onNext(id) {
      const { query } = this.$route;
      this.$router.push({
        path: "/signboard/streetIntro",
        query: {
          ...query,
          streetId: id,
        },
      });
    },
    onJump() {
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
  .content {
    padding: 12px;
  }
}
</style>
