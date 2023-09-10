<template>
  <van-popup
    class="popup-agreement"
    v-model="show"
    :round="true"
    get-container="body"
    position="bottom"
    style="height: 90%"
  >
    <van-image v-for="(img, idx) in imgs" :src="img" :key="idx" />
    <!-- 阅读并同意 -->
    <submit-bar>
      <van-button type="primary" block @click="onClose"
        >我知道了</van-button
      >
    </submit-bar>
  </van-popup>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
export default {
  components: { SubmitBar },
  data() {
    return {
      show: false,
      readKey: "",
      doc: {
        tiaoli: [],
        guifang: [],
      },
    };
  },
  computed: {
    imgs() {
      const { readKey, doc } = this;
      return _.get(doc, readKey, []);
    },
  },
  created() {
    const getImgName = (name) => `${name}`.padStart(4, 0);
    let arr1 = new Array(18).fill(0),
      arr2 = new Array(20).fill(0);
    this.doc.tiaoli = arr1.map((val, idx) =>
      require(`@/assets/doc/hwggsshzpggpgltl/${getImgName(idx + 1)}.jpg`)
    );
    this.doc.guifang = arr2.map((val, idx) =>
      require(`@/assets/doc/hwzpszglgf/${getImgName(idx + 1)}.jpg`)
    );
  },
  methods: {
    onShow(data) {
      this.readKey = data.type;
      this.show = true;
    },
    onClose() {
      this.show = false;
      this.readKey = "";
      this.$emit("confirm");
    },
  },
};
</script>
<style lang="less" scoped>
.popup-agreement {
  padding-bottom: 54px;
  box-sizing: border-box;
}
</style>
