<template>
  <div class="page-wrap">
    <a-row class="sudoku-menu">
      <a-col class="gutter-row" :span="6">
        <router-link to="/signboard/attribute">
          <icon-font class="iconfont" type="icon-zhengfuxinxigongkaishenqing" />
          <div class="name">店招设计</div>
        </router-link>
      </a-col>
      <a-col class="gutter-row" :span="6">
        <router-link to="/article/4/list">
          <icon-font class="iconfont" type="icon-fadingzhudonggongkaineirong" />
          <div class="name">信息公告</div>
        </router-link>
      </a-col>
    </a-row>
    <a-divider />
    <!-- 温馨提示 -->
    <a-alert message="温馨提示" type="info" show-icon>
      <template slot="description">
        首次使用店招在线设计的用户，请先进入信息公告，仔细阅读<router-link to=""
          >《店招设计使用流程》</router-link
        >和<router-link to="">《杭州市户外招牌设置负面清单》</router-link
        >后再进行操作。
      </template>
    </a-alert>
  </div>
</template>
<script>
import { Icon } from "ant-design-vue";
const IconFont = Icon.createFromIconfontCN({
  scriptUrl: require("@/styles/iconfont/iconfont.js"),
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
  padding: 24px 0;
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
      .iconfont {
        font-size: 42px;
        margin-bottom: 8px;
      }
      .name {
        font-size: 15px;
        color: #333;
        line-height: 2.2em;
      }
    }
  }
  :deep(.ant-alert) {
    margin: 0 24px;
  }
}
</style>
