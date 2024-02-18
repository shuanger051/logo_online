<template>
  <div class="base-layout">
    <a-page-header
      :ghost="false"
      :title="$route.meta.title"
      @back="onNativeBack"
    >
      <!-- 左侧图标 -->
      <template slot="extra">
        <a-button v-if="isInIframe" @click="onClose">关闭</a-button>
      </template>
    </a-page-header>
    <!-- main：页面主体 -->
    <div>
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
      isCanBack: true,
    };
  },
  watch: {
    $route: {
      handler(nRoute) {
        this.isCanBack = _.get(nRoute, "meta.isCanBack", true);
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
  position: relative;
  height: 100%;
  :deep(.ant-page-header) {
    position: sticky;
    width: 100%;
    top: 0;
    z-index: 2;
    margin-bottom: -8px;
    border-radius: 4px;
    box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 25px 0px;
    &-heading {
      max-width: 1000px;
      margin: 0 auto;
    }
  }
}
</style>
