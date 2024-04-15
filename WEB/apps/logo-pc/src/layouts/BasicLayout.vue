<template>
  <div class="base-layout">
    <a-page-header
      :ghost="false"
      :backIcon="isCanBack"
      :title="$route.meta.title"
      @back="onNativeBack"
    >
      <!-- 左侧图标 -->
      <template slot="extra">
        <a-button v-if="isInIframe" @click="onClose">关闭</a-button>
      </template>
    </a-page-header>
    <!-- main：页面主体 -->
    <div class="main">
      <!-- 走缓存 -->
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive" />
      </keep-alive>
      <!-- 不走缓存 -->
      <router-view v-if="!$route.meta.keepAlive" />
    </div>
  </div>
</template>
<script>
import { mapState } from "vuex";
export default {
  name: "BasicLayout",
  computed: {
    ...mapState({
      isInIframe: (state) => state.app.isInIframe,
    }),
  },
  data() {
    return {
      isCanBack: false,
    };
  },
  watch: {
    $route: {
      handler(nRoute) {
        this.isCanBack = _.get(
          nRoute,
          "meta.isCanBack",
          <a-icon type="arrow-left" />
        );
      },
      immediate: true,
      deep: true,
    },
  },
  methods: {
    onNativeBack() {
      this.$router.back();
    },
    onClose() {
      // 在弹窗权利阳光中
      if (this.isInIframe) {
        const bridgeClient = new formbridgeClient();
        bridgeClient.close();
      }
      // 其他
      else this.$router.push({ path: "/home" });
    },
  },
};
</script>

<style lang="less" scoped>
.base-layout {
  // position: relative;
  min-height: 100%;
  :deep(.ant-page-header) {
    position: sticky;
    width: 100%;
    top: 0;
    z-index: 2;
    margin-bottom: -8px;
    border-radius: 4px;
    background-color: #2f63f1;
    color: #fff;
    &-heading {
      max-width: 1000px;
      margin: 0 auto;
    }
    &-heading-title {
      color: #fff;
    }
    &-back-button {
      color: #fff;
    }
  }
}
</style>
