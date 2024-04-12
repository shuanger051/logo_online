<template>
  <div class="base-layout">
    <van-nav-bar
      :safe-area-inset-top="true"
      :title="title"
      :fixed="true"
      :z-index="2"
    >
      <!-- 左侧图标 -->
      <template slot="left">
        <!-- button：后退 -->
        <van-icon v-if="isCanBack" name="arrow-left" @click="onNativeBack" />
        <!-- button：关闭 -->
        <van-icon name="cross" @click="onClose" />
      </template>
    </van-nav-bar>
    <!-- main：页面主体 -->
    <div id="page-container" class="page-container--nostep">
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
import evnetBus from "../core/eventBus";

export default {
  name: "BasicLayout",
  computed: {
    ...mapState({
      isInIframe: (state) => state.app.isInIframe,
    }),
    title() {
      console.log(11)
      return this.customTitle || this.$route.meta.title
    }
  },
  data() {
    return {
      isCanBack: true,
      customTitle: null
    };
  },
  watch: {
    $route: {
      handler(nRoute) {
        this.isCanBack = _.get(nRoute, "meta.isCanBack", true);
        this.customTitle = null
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
  created() {
    evnetBus.$on('customTitle', (val) => {
      this.customTitle = val
    })
  }
};
</script>

<style lang="less" scoped>
.base-layout {
  position: absolute;
  width: 100%;
  height: 100%;
  & /deep/ .van-nav-bar__left {
    .van-icon {
      font-size: 16px;
      &:not(:last-child) {
        margin-right: 12px;
      }
    }
  }
}
.page-container--withstep {
  margin-top: 80px;
  height: calc(100% - 80px);
}
.page-container--nostep {
  margin-top: 44px;
  height: calc(100% - 44px);
  position: relative;
  overflow: auto;
}
</style>
