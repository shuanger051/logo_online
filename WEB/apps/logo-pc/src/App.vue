<template>
  <div id="app">
    <basic-layout>
      <router-view />
    </basic-layout>
    <!-- 登录弹窗 -->
    <modal-login />
  </div>
</template>
<script>
import { BasicLayout } from "@/layouts";
import ModalLogin from "@/components/ModalLogin";
export default {
  components: { ModalLogin, BasicLayout },
};
</script>
<style lang="less">
body {
  background-color: #fff !important;
  #app {
    min-height: 100%;
  }
}
</style>
