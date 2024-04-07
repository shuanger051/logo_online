<template>
  <div class="page-wrap" :class="[isOldVersion && 'old-version']">
    <van-grid :column-num="2" :border="false">
      <van-grid-item
        icon-prefix="iconfont icon"
        icon="fadingzhudonggongkaineirong"
        text="店招设置规范与公告"
        to="/article/4/list"
      />
      <van-grid-item
        icon-prefix="iconfont icon"
        icon="zhengfuxinxigongkaishenqing"
        text="菜单式店招设计"
        to="/signboard/negative"
      />
      <van-grid-item
        icon-prefix="iconfont icon"
        icon="zhengfuxinxigongkaishenqing"
        text="店招材质参考"
        to="/material/list"
      />
      <van-grid-item
        icon-prefix="iconfont icon"
        icon="fadingzhudonggongkaineirong"
        text="精品素材参考"
        to="/sample/list"
      />
    </van-grid>
    <!-- 首次使用提示 -->
    <div class="tips-box">
      首次使用店招在线设计的用户，请先进入信息公告，仔细阅读<router-link to=""
        >《店招设计使用流程》</router-link
      >和<router-link to="">《杭州市户外招牌设置负面清单》</router-link
      >后再进行操作。
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
// @ is an alias to /src
export default {
  name: "Home",
  computed: {
    ...mapState({
      isOldVersion: (state) => state.app.isOldVersion,
    }),
    // 宫格数
    columnNum() {
      if (this.isOldVersion) return 2;
      return 4;
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  span {
    font-size: 14px;
  }
  padding: 12px 0;
  min-height: 100vh;
  background-color: @white;
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
    line-height: 1.8em;
    color: @gray-7;
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
