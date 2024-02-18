<template>
  <div class="page-wrap">
    <!-- 负面清单图片列表 -->
    <van-image v-for="(img, idx) in list" :src="img" :key="idx" />
    <!-- 阅读并同意 -->
    <submit-bar>
      <van-button type="primary" block @click="onNext">我知道了</van-button>
    </submit-bar>
  </div>
</template>
<script>
export default {
  data() {
    return {
      list: [],
    };
  },
  created() {
    const getImgName = (name) => `${name}`.padStart(4, 0);
    const arr = new Array(22).fill(0);
    this.list = arr.map((val, idx) =>
      require(`@/assets/doc/fmqd/${getImgName(idx + 1)}.jpg`)
    );
  },
  methods: {
    onNext() {
      // 记录到session
      this.$store.commit("app/setIsReadNegative", true);
      // 跳转下一步
      this.$router.push({ path: "/signboard/attribute" });
    },
  },
};
</script>
