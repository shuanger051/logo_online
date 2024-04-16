<template>
  <div class="page-wrap">
    <a-list :data-source="streetArr" :border="false">
      <a-list-item
        slot="renderItem"
        slot-scope="item"
        :key="item.id"
        @click="onNext(item.id)"
      >
        {{ item.name }}
      </a-list-item>
    </a-list>
    <div class="action-bar">
      <a-button v-if="$route.query.streetType !== '2'" @click="onJump"
        >跳过</a-button
      >
    </div>
  </div>
</template>
<script>
import evnetBus from "@/core/eventBus";

export default {
  data() {
    return {
      params: {},
      streetArr: [],
    };
  },

  created() {
    const { streetType } = this.$route.query;
    const list = window.pageContentJson.streetView;
    const data = list.find((item) => item.id == streetType);
    if (data) {
      evnetBus.$emit("subtitle", data.name);
      this.streetArr = data.street;
    }
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

  .action-bar {
    margin-top: 24px;
  }
}
</style>
