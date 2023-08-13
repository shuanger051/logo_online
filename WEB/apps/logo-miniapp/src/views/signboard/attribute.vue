<template>
  <div class="page-wrap">
    <van-divider>设计风格</van-divider>
    <van-checkbox-group v-model="formData.styles" direction="horizontal">
      <van-checkbox
        v-for="item in attrs.styles"
        :key="item.value"
        :name="item.value"
      >
        {{ item.label }}
      </van-checkbox>
    </van-checkbox-group>
    <submit-bar>
      <van-button block type="primary" @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
export default {
  components: { SubmitBar },
  data() {
    return {
      formData: {},
      // 属性列表
      attrs: {
        styles: [
          { value: "1", label: "古典风" },
          { value: "2", label: "现代风" },
          { value: "3", label: "商务风" },
          { value: "4", label: "极简风" },
          { value: "5", label: "欧式风" },
          { value: "6", label: "美式风" },
          { value: "7", label: "原木风" },
          { value: "8", label: "工业风" },
          { value: "9", label: "田园风" },
        ],
      },
    };
  },
  methods: {
    onNext() {
      const { formData } = this;
      let query = Object.assign({}, this.$route.query);
      Object.keys(formData).forEach((key) => {
        query[key] = formData[key].join(",");
      });
      this.$router.push({ path: "/signboard/template", query });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px 60px;
  :deep(.van-checkbox) {
    margin-bottom: 8px;
  }
}
</style>
