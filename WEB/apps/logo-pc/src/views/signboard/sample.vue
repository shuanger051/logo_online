<template>
  <div class="page-wrap">
    <a-row :gutter="12" class="grid-wrapper">
      <a-col v-for="item in sample" class="grid-item" :span="4" :key="item.key">
        <router-link :to="item.href">
          <i
            :class="`grid-item-icon iconfont icon-${item.icon}`"
            :style="{ color: item.iconColor }"
          ></i>
          <div class="grid-item-title">{{ item.title }}</div>
        </router-link>
      </a-col>
    </a-row>
    <div class="action-bar">
      <a-button type="primary" @click="onJump">进入店招设计</a-button>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    const { query } = this.$route;
    const arr = [];

    // 非商业街区
    if (query.streetType == "3")
      arr.push({
        title: "非商业街区",
        value: "3",
        icon: "shangye",
        href: "/sample/detail?name=normal&type=street",
        iconColor: "#2f63f1",
      });
    else if (query.streetType == "1,2")
      arr.push({
        title: "商业街区",
        value: "1,2",
        icon: "shangye",
        href: "/sample/detail?name=commercial,characteristics&type=street",
        iconColor: "#f200ff",
      });

    arr.push({
      title: "材质参考",
      value: "0",
      icon: "material",
      href: "/material/list",
      iconColor: "#de8f30",
    });

    return {
      sample: arr,
    };
  },
  methods: {
    onJump() {
      const { query } = this.$route;
      this.$router.push({
        path: "/signboard/attribute",
        query,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px 60px;
  max-width: 1000px;
  margin: 0 auto;
  margin-top: 24px;
  .grid-wrapper {
    .grid-item {
      text-align: center;
      &-icon {
        font-size: 42px;
        margin-bottom: 8px;
      }
      &-title {
        color: #333;
      }
    }
  }

  .action-bar {
    margin-top: 24px;
  }
}
</style>
