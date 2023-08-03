<template>
  <div class="base-layout">
    <van-nav-bar
      :safe-area-inset-top="true"
      :title="$route.meta.title"
      :fixed="true"
      :z-index="2"
    >
      <!-- 左侧图标 -->
      <template slot="left">
        <!-- button：后退 -->
        <van-icon name="arrow-left" @click="onNativeBack" />
        <!-- button：关闭 -->
        <van-icon name="cross" @click="onBackHome" />
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
export default {
  name: "BasicLayout",
  methods: {
    onNativeBack() {
      this.$router.back();
    },
    onBackHome() {
      this.$router.push({ path: "/home" });
    },
  },
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
