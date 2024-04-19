<template>
  <div class="base-layout">
    <a-page-header
      :ghost="false"
      :backIcon="isCanBack"
      :title="$route.meta.title"
      :sub-title="subtitle"
      @back="onNativeBack"
    >
      <!-- 左侧图标 -->
      <template slot="extra">
        <a-button class="btn-close" @click="onClose" type="link">退出菜单式店招设计</a-button>
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
import evnetBus from "@/core/eventBus";
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
      subtitle: "",
    };
  },
  watch: {
    $route: {
      handler(nRoute) {
        this.subtitle = "";
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
  created() {
    evnetBus.$on("subtitle", (val) => {
      this.subtitle = val;
    });
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
      else this.$router.push({ path: "/" });
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
    .btn-close{
      color: #fff;
    }
    &-heading {
      max-width: 1000px;
      margin: 0 auto;
      &-title {
        color: #fff;
      }
      &-sub-title {
        color: #fff;
      }
    }
    &-back-button {
      color: #fff;
    }
  }
}
</style>
