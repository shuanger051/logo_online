<template>
  <div id="app">
    <basic-layout>
      <router-view />
    </basic-layout>
    <!-- 登录弹窗 -->
    <popup-login />
  </div>
</template>
<script>
import { BasicLayout } from "@/layouts";
import PopupLogin from "./components/PopupLogin.vue";
export default {
  components: { PopupLogin, BasicLayout },
};
</script>
