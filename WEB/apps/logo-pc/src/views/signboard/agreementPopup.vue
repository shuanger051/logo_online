<template>
  <a-modal
    class="popup-agreement"
    v-model="show"
    okText="我已阅读并同意"
    @ok="onClose"
  >
    <img v-for="(img, idx) in imgs" :src="img" :key="idx" />
  </a-modal>
</template>
<script>
export default {
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
