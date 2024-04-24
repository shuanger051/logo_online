<template>
  <div class="page-wrap">
    <a-row class="sudoku-menu" :gutter="12">
      <a-col class="gutter-row" :span="12">
        <router-link to="/article/4/list">
          <div class="menu-icon icon-dzszgfygg"></div>
          <div class="menu-name">店招设置规范与公告</div>
        </router-link>
      </a-col>
      <a-col class="gutter-row" :span="12">
        <router-link to="/signboard/negative">
          <div class="menu-icon icon-cdsdzsj"></div>
          <div class="menu-name">菜单式店招设计</div>
        </router-link>
      </a-col>
      <a-col class="gutter-row" :span="12">
        <router-link to="/material/list">
          <div class="menu-icon icon-dzczck"></div>
          <div class="menu-name">店招材质参考</div>
        </router-link>
      </a-col>
      <a-col class="gutter-row" :span="12">
        <router-link to="/sample/list">
          <div class="menu-icon icon-jpscck"></div>
          <div class="menu-name">精品素材参考</div>
        </router-link>
      </a-col>
      <a-col class="gutter-row" :span="12"  v-if="showCache">
        <router-link to="/signboard/editSignboard/cache">
          <div class="menu-icon icon-jpscck"></div>
          <div class="menu-name">历史记录</div>
        </router-link>
      </a-col>
    </a-row>
    <!-- 首次使用提示 -->
    <dl class="tips-box">
      <dt>温馨提示</dt>
      <dd>
        首次使用店招在线设计的用户，请先进入信息公告，仔细阅读《店招设计使用流程》和《杭州市户外招牌设置负面清单》后再进行操作。
      </dd>
    </dl>
  </div>
</template>
<script>
import { Icon } from "ant-design-vue";
import store from "core/pc/store/index";

const IconFont = Icon.createFromIconfontCN({
  scriptUrl: require("@/styles/iconfont/iconfont.js"),
  extraCommonProps: {
    style: {
      color: "#1492ff",
    },
  },
});
export default {
  components: { IconFont },
  computed: {
    showCache: () => {
      return !!store.state.editor.signboardCache 
    }
  },
  created() {
    window.a = store.state.editor
    console.log(store.state.editor.signboardCache , 888)
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  // position: relative;
  padding: 52px 0;
  min-height: 100%;
  max-width: 1000px;
  margin: 0 auto;
  box-sizing: border-box;
  border-radius: 4px;
  background-color: #fff;
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("../assets/app-footer-bg.png");
    background-repeat: no-repeat;
    background-position: bottom center;
    background-size: 115% auto;
    background-attachment: fixed;
  }
  .tips-box {
    padding: 12px 24px;
    font-size: 14px;
    line-height: 1.4em;
    position: absolute;
    color: #fff;
    bottom: 12px;
    margin: 0;
    dt {
      margin-bottom: 6px;
      font-weight: 700;
    }
    dd {
      margin-left: 0;
    }
  }
  .sudoku-menu {
    .ant-col {
      text-align: center;
      margin-bottom: 52px;
      .iconfont {
        font-size: 42px;
        margin-bottom: 8px;
      }
      .menu-icon {
        display: inline-block;
        box-sizing: content-box;
        width: 44px;
        height: 44px;
        padding: 12px;
        color: #1492ff;
        margin-bottom: 12px;
        background-size: 80% auto;
        background-position: center;
        background-repeat: no-repeat;
        &.icon-dzszgfygg {
          background-image: url("../assets/icon-dzszgfygg.png");
        }
        &.icon-cdsdzsj {
          background-image: url("../assets/icon-cdsdzsj.png");
        }
        &.icon-dzczck {
          background-image: url("../assets/icon-dzczck.png");
        }
        &.icon-jpscck {
          background-image: url("../assets/icon-jpscck.png");
        }
      }
      .menu-name {
        font-size: 16px;
        color: #333;
        line-height: 1.4em;
      }
    }
  }
  :deep(.ant-alert) {
    margin: 0 24px;
  }
}
</style>
