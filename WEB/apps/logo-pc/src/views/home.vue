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
    </a-row>
    <a-divider />
    <!-- 温馨提示 -->
    <a-alert message="温馨提示" type="info" show-icon>
      <template slot="description">
        本应用用于在线设计店招并制作效果图用于店招备案审批，请预先准备好店铺正面门头全景实拍照片
      </template>
    </a-alert>
  </div>
</template>
<script>
import { Icon } from "ant-design-vue";
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
  created() {
    // accountService.queryCustomerByIdAPI().then((res) => {
    //   console.log(res);
    // });
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 52px 0;
  min-height: 100%;
  max-width: 1000px;
  margin: 0 auto;
  box-sizing: border-box;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
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
