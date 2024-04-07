<template>
  <div class="page-wrap">
    <!-- 店招牌类型 -->
    <van-panel title="材质">
      <van-checkbox-group v-model="formData.material">
        <van-row>
          <van-col v-for="item in attrs.material" :key="item.value" :span="12">
            <van-checkbox :name="item.value">{{ item.label }}</van-checkbox>
          </van-col>
        </van-row>
      </van-checkbox-group>
    </van-panel>
    <van-panel title="风格">
      <van-checkbox-group v-model="formData.styles">
        <van-row>
          <van-col v-for="item in attrs.styles" :key="item.value" :span="12">
            <van-checkbox :name="item.value">{{ item.label }}</van-checkbox>
          </van-col>
        </van-row>
      </van-checkbox-group>
    </van-panel>
    <submit-bar>
      <van-button block type="primary" @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
import { appGetItemsByDictKeyInDB } from "core/api";
export default {
  components: { SubmitBar },
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
  box-sizing: border-box;
  padding: 24px 12px 64px;
  background-color: @gray-2;
  height: 100%;
  :deep(.van-panel) {
    margin-bottom: 12px;
    border-radius: 8px;
    overflow: hidden;
    &__header {
      position: relative;
      line-height: 24px;
      font-size: 16px;
      &::before {
        content: "";
        display: inline-block;
        margin-right: 8px;
        transform: translateY(5px);
        width: 4px;
        height: 14px;
        background-color: @blue;
      }
    }
    &__content {
      padding: 12px 0;
      .van-col {
        box-sizing: border-box;
        padding: 12px 24px;
      }
      .van-radio {
        font-size: 14px;
      }
    }
  }
}
</style>
