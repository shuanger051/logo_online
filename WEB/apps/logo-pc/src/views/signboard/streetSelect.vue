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
      <a-space>
        <a-button @click="onJump">跳过</a-button>
        <a-button type="primary" @click="onNext">下一步</a-button>
      </a-space>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      params: {},
      streetArr: [
        { name: "太平门直街(秋涛北路-新塘路)", id: "1" },
        { name: "横长寿路(延安路-长寿路)", id: "2" },
        { name: "马腾路(文二路-天目山路段)", id: "3" },
        { name: "滨盛路(火炬大道-东信大道)", id: "4" },
        { name: "金城路(博奥路-金山路)", id: "5" },
        { name: "海鸥路", id: "6" },
        { name: "西大街", id: "7" },
        { name: "福雷德支路(学源街-学林街)", id: "8" },
        { name: "花坞南路(桂花路直富春街段)", id: "9" },
        { name: "万马路(石镜街-吴越街)", id: "10" },
      ],
    };
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
