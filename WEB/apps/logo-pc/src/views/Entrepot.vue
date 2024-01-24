<template>
  <div>请稍等</div>
</template>
<script>
export default {
  created() {
    // 是否在PC端
    if (
      window.navigator.userAgent.match(
        /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
      )
    ) {
      window.location = "http://47.96.150.44:8081/";
    } else {
      this.$router.push({ path: "/" });
    }
  },
};
</script>
