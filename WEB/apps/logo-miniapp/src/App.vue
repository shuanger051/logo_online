<template>
  <div id="app">
    <router-view />
    <!-- 登录弹窗 -->
    <popup-login />
  </div>
</template>
<script>
import PopupLogin from "./components/PopupLogin.vue";
export default {
  components: { PopupLogin },
};
</script>
