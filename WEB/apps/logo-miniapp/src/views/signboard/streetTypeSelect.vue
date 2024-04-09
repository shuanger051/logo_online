<template>
  <div class="page-wrap">
    <!-- 街区选择 -->
    <van-panel title="街区类型">
      <van-radio-group v-model="streetType">
        <van-row>
          <van-col v-for="item in streetTypeArr" :span="12" :key="item.value">
            <van-radio :name="item.value">{{ item.label }}</van-radio>
          </van-col>
        </van-row>
      </van-radio-group>
    </van-panel>
    <submit-bar>
      <van-button type="primary" block @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import { appGetItemsByDictKeyInDB } from "core/api";
export default {
  data() {
    return {
      materialType: null,
      streetType: null,
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
      const { streetType } = this;
      if (!streetType)
        this.$notify({ type: "warning", message: "请选择街区类型" });
      else
        this.$router.push({
          path: "/signboard/streetSelect",
          query: {
            streetType,
          },
        });
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
