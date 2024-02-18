<template>
  <div id="app">
    <router-view />
    <!-- 登录弹窗 -->
    <popup-login />
    <!-- 权利阳光需要加footer操作弹窗 -->
    <div v-if="isInIframe" class="ssss">
      <van-button type="default">关闭</van-button>
    </div>
  </div>
</template>
<script>
import PopupLogin from "./components/PopupLogin.vue";
export default {
  components: { PopupLogin },
  data() {
    // 不一致表明被ifram引用
    const isInIframe = window.self !== window.top;
    return {
      isInIframe,
    };
  },
  created() {
    console.log("是否被iframe引用：", this.isInIframe);
  },
};
</script>
