<template>
  <div class="page-wrap">
    <div class="img-content">
      <!-- 负面清单图片列表 -->
      <img class="img-box" v-for="(img, idx) in list" :src="img" :key="idx" />
    </div>
    <!-- 阅读并同意 -->
    <div class="">
      <a-checkbox
        :checked="isCheck"
        @change="(e) => (isCheck = e.target.checked)"
      >
        本人确认已阅读《杭州市户外招牌设置负面清单》的全部内容，承诺充分了解并愿意遵守负面清单的相关规定。如有违反，本人愿承担全部责任
      </a-checkbox>
      <a-button type="primary" size="large" @click="onNext">我知道了</a-button>
    </div>
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
        this.$router.push({ path: "/signboard/attribute" });
      } else this.$message.warning("请先阅读并同意");
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
  border-radius: 4px;
  background-color: #fff;
  .img-content {
    text-align: center;
  }
  .img-box {
    max-width: 100%;
  }
}
</style>
