<template>
  <!-- 搜索列表页进行缓存 -->
  <keep-alive v-if="$route.meta.keepAlive">
    <router-view />
  </keep-alive>
  <router-view v-else />
</template>
<script>
export default {
  name: "BlankLayout",
};
</script>
