<template>
  <div class="page-wrap" :class="[isOldVersion && 'old-version']">
    <van-grid :column-num="2" :border="false">
      <van-grid-item
        icon-prefix="iconfont icon"
        :icon="IconImg.icon01"
        text="店招设置规范与公告"
        to="/article/4/list"
      />
      <van-grid-item
        icon-prefix="iconfont icon"
        :icon="IconImg.icon02"
        text="菜单式店招设计"
        to="/signboard/negative"
      />
      <van-grid-item
        icon-prefix="iconfont icon"
        :icon="IconImg.icon03"
        text="店招材质参考"
        to="/material/list"
      />
      <van-grid-item
        icon-prefix="iconfont icon"
        :icon="IconImg.icon04"
        text="精品素材参考"
        to="/sample/list"
      />
      <van-grid-item
        v-if="showCache"
        icon-prefix="iconfont icon"
        :icon="IconImg.icon04"
        text="历史记录"
        to="/signboard/editSignboard/cache"
      />
    </van-grid>
    <!-- 首次使用提示 -->
    <dl class="tips-box">
      <dt>温馨提示</dt>
      <dd>
        首次使用店招在线设计的用户，请先进入信息公告，仔细阅读《店招设计使用流程》和《杭州市户外招牌设置负面清单》后再进行操作。
      </dd>
    </dl>
  </div>
</template>

<script>
import { mapState } from "vuex";
import icon01 from "@/assets/icon-dzszgfygg.png";
import icon02 from "@/assets/icon-cdsdzsj.png";
import icon03 from "@/assets/icon-dzczck.png";
import icon04 from "@/assets/icon-jpscck.png";
import store from "core/mobile/store/index";

export default {
  name: "Home",
  computed: {
    ...mapState({
      isOldVersion: (state) => state.app.isOldVersion,
    }),
    // 图标
    IconImg: () => ({ icon01, icon02, icon03, icon04 }),
    showCache: () => {
      return !!store.state.editor.signboardCache 
    },
    // 宫格数
    columnNum() {
      if (this.isOldVersion) return 2;
      return 4;
    }
  }
};
</script>
<style lang="less" scoped>
.page-wrap {
  span {
    font-size: 14px;
  }
  position: relative;
  padding: 32px 0 0;
  min-height: 100%;
  box-sizing: border-box;
  background-color: @white;
  background-image: url("../assets/app-footer-bg.png");
  background-repeat: no-repeat;
  background-position: bottom center;
  background-size: 115% auto;
  background-attachment: fixed;
  :deep(.van-panel) {
    &:not(:last-child) {
      margin-bottom: 12px;
    }
    &__header {
      .van-cell__title {
        &::before {
          content: "";
          display: inline-block;
          height: 10px;
          width: 2px;
          margin-right: 8px;
          background-color: @blue;
        }
      }
    }
  }
  :deep(.van-grid) {
    &-item__icon {
      font-size: 42px;
      display: inline-block;
      color: @blue;
    }
    &-item__text {
      font-size: 14px;
      text-align: center;
    }
  }
  .tips-box {
    padding: 12px 12px;
    font-size: 14px;
    line-height: 1.4em;
    color: @white;
    position: absolute;
    bottom: 12px;
    margin: 0;
    dt{
      margin-bottom: 6px;
      font-weight: 700;
    }
    dd {
      margin-left: 0;
    }
  }
  // 适老版适配样式
  &.old-version {
    :deep(.van-panel) {
      &__header {
        .van-cell__title {
          & > span {
            font-size: 18px;
          }
          &::before {
            content: "";
            display: inline-block;
            height: 12px;
            width: 4px;
            margin-right: 8px;
          }
        }
      }
    }
    :deep(.van-grid) {
      &-item__icon {
        font-size: 28px;
        width: 48px;
        height: 48px;
        line-height: 48px;
      }
      &-item__text {
        font-size: 18px;
      }
    }
  }
}
</style>
