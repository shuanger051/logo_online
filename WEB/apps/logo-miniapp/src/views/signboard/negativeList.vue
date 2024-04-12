<template>
  <div class="page-wrap">
    <!-- 负面清单图片列表 -->
    <van-image v-for="(img, idx) in list" :src="img" :key="idx" />
    <!-- 阅读并同意 -->
    <submit-bar>
      <template slot="tips">
        <van-checkbox v-model="isCheck" shape="square" icon-size="16px"
          >本人确认已阅读《杭州市户外招牌设置负面清单》的全部内容，承诺充分了解并愿意遵守负面清单的相关规定。如有违反，本人愿承担全部责任</van-checkbox
        >
      </template>
      <van-button type="primary" block @click="onNext">我知道了</van-button>
    </submit-bar>
  </div>
</template>
<script>
export default {
  data() {
    return {
      isCheck: false,
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
      if (this.isCheck) {
        // 记录到session
        this.$store.commit("app/setIsReadNegative", true);
        // 跳转下一步
        this.$router.push({ path: "/signboard/streetTypeSelect" });
      } else this.$notify({ type: "danger", message: "请先阅读并同意" });
    },
  },
};
</script>
