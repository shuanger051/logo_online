<template>
  <div id="app">
    <router-view />
    <!-- 登录弹窗 -->
    <modal-login />
  </div>
</template>
<script>
import ModalLogin from "@/components/ModalLogin";
export default {
  components: { ModalLogin },
};
</script>
