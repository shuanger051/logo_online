<template>
  <div class="page-wrap">
    <van-cell
      v-for="item in streetArr"
      :key="item.id"
      :border="false"
      @click="onNext(item.id)"
      >{{ item.name }}</van-cell
    >
    <van-cell :border="false" v-if="$route.query.streetType !== '2'">
      <van-button type="primary" size="small" @click="onJump">跳过</van-button>
    </van-cell>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
export default {
  components: { SubmitBar },
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
  padding: 24px 12px 12px;
}
</style>
