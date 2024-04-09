<template>
  <div class="page-wrap">
    <a-divider orientation="left">街区类型</a-divider>
    <a-checkbox-group
      v-model="formData.streetType"
      name="material"
      :options="streetTypeArr"
    >
    </a-checkbox-group>
    <div class="action-bar">
      <a-button type="primary" @click="onNext">下一步</a-button>
    </div>
  </div>
</template>
<script>
import { appGetItemsByDictKeyInDB } from "core/api";
export default {
  data() {
    return {
      formData: {},
      // 属性列表
      streetTypeArr: [],
    };
  },
  created() {
    appGetItemsByDictKeyInDB({ dictKey: "streetType" }).then(({ data }) => {
      this.streetTypeArr = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
  },
  methods: {
    onNext() {
      const { streetType } = this.formData;
      if (!streetType) this.$message.warn("请选择街区类型");
      // 特色街区-进入一街一景
      else
        this.$router.push({
          path: "/signboard/streetSelect",
          query: {
            streetType,
          },
        });
    }
  }
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
  :deep(.ant-checkbox-group) {
    margin-bottom: 12px;
    &-item > span {
      font-size: 15px;
    }
  }
  :deep(.ant-divider) {
    &-inner-text {
      font-size: 15px;
      color: #444;
    }
  }
  .action-bar {
    margin-top: 24px;
  }
}
</style>
