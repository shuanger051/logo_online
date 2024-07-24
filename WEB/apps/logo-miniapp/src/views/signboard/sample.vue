<template>
  <div class="page-wrap">
    <van-grid :column-num="2" :border="false" :center="false">
      <van-grid-item
        v-for="item in sample"
        :key="item.key"
        :text="item.title"
        :to="item.href"
      >
        <van-icon
          slot="icon"
          class-prefix="iconfont icon"
          :name="item.icon"
          :color="item.iconColor"
        />
      </van-grid-item>
    </van-grid>
    <div class="btns-wrap">
      <van-button size="small" type="primary" @click="onJump">跳过</van-button>
    </div>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
export default {
  components: { SubmitBar },
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
        path: "/signboard/selfEdit",
        query,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.page-wrap {
  box-sizing: border-box;
  padding: 24px 12px 64px;
  background-color: @white;
  min-height: 100%;
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("../../assets/app-footer-bg.png");
    background-repeat: no-repeat;
    background-position: bottom center;
    background-size: 115% auto;
    background-attachment: fixed;
    filter: opacity(40%);
  }
  .btns-wrap {
    text-align: center;
    position: fixed;
    width: 100%;
    bottom: 40px;
  }

  :deep(.van-grid) {
    &-item {
      padding-left: 24px;
      &__content {
        align-items: center;
        background-color: transparent;
      }
      &__icon-wrapper {
        margin-bottom: 8px;
        .iconfont {
          font-size: 32px;
        }
      }
      &__text {
        font-size: 14px;
      }
    }
  }
}
</style>
