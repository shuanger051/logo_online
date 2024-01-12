<template>
  <div class="page-wrap">
    <a-divider orientation="left">材质</a-divider>
    <a-checkbox-group
      v-model="formData.material"
      name="material"
      :options="attrs.material"
    >
    </a-checkbox-group>
    <a-divider orientation="left">类型</a-divider>
    <a-checkbox-group
      v-model="formData.styles"
      name="styles"
      :options="attrs.styles"
    >
    </a-checkbox-group>
    <a-divider orientation="left">街道类型</a-divider>
    <a-checkbox-group
      v-model="formData.streetType"
      name="streetType"
      :options="attrs.streetType"
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
      attrs: {
        styles: [],
        material: [],
        streetType: [],
      },
    };
  },
  created() {
    appGetItemsByDictKeyInDB({ dictKey: "style" }).then(({ data }) => {
      this.attrs.styles = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
    appGetItemsByDictKeyInDB({ dictKey: "material" }).then(({ data }) => {
      this.attrs.material = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
    appGetItemsByDictKeyInDB({ dictKey: "streetType" }).then(({ data }) => {
      this.attrs.streetType = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
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
  max-width: 1000px;
  margin: 0 auto;
  :deep(.ant-checkbox-group) {
    margin-bottom: 12px;
  }
  :deep(.ant-divider) {
    &-inner-text {
      font-size: 14px;
      color: #444;
    }
  }
  .action-bar {
    margin-top: 24px;
  }
}
</style>
