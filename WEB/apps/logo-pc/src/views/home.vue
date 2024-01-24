<template>
  <div class="page-wrap">
    <a-row class="sudoku-menu">
      <a-col class="gutter-row" :span="6">
        <router-link to="/signboard/attribute">
          <icon-font class="iconfont" type="icon-zhengfuxinxigongkaishenqing" />
          <div class="name">店招设计</div>
        </router-link>
      </a-col>
      <!-- <a-col class="gutter-row" :span="6">
        <router-link to="/article/1/detail?pid=482">
          <icon-font class="iconfont" type="icon-zhengfuxinxigongkaizhinan" />
          <div class="name">备案流程</div>
        </router-link>
      </a-col> -->
      <a-col class="gutter-row" :span="6">
        <router-link to="/article/4/list">
          <icon-font class="iconfont" type="icon-fadingzhudonggongkaineirong" />
          <div class="name">信息公告</div>
        </router-link>
      </a-col>
    </a-row>
  </div>
</template>
<script>
// import { accountService } from "@/services";
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
      .name{
        font-size: 15px;
        color: #333;
        line-height: 2.2em;
      }
    }
  }
}
</style>
