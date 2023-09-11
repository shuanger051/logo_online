<template>
  <div class="edit-select">
    <van-panel
      title="请选择"
      desc="点击需要处理的项目"
    >
    </van-panel>
    <div class="content">
      <div
        class="line-desing"
        @click="
          $router.push(`/signboard/attribute?shopId=${$route.query.shopId}`)
        "
      >
        <span>菜单式<br/>在线设计</span>
        <icon-fa icon="fluent:design-ideas-20-regular" color="#e98c49" width="80%" />
      </div>
      <van-uploader
        :after-read="afterRead"
        :max-size="1024 * 1024 * 2"
        @oversize="onOversize"
      >
        <span>已有设计上传</span>
        <icon-fa icon="fa:upload" color="#82b6f8" width="80%" height="80%" />
      </van-uploader>
    </div>
  </div>
</template>
<script>
import store from "core/store/mobileIndex";
import { mapActions } from "vuex";
import { Toast } from "vant";
const sleep = async (time) => {
  return new Promise((r) => {
    setTimeout(() => r(), time);
  });
};
export default {
  store,

  data() {
    return {};
  },
  methods: {
    ...mapActions("editor", ["mCreateCover"]),

    async afterRead(file) {
      const toast = Toast.loading({
        message: "上传中",
        forbidClick: true,
        duration: 0,
      });
      await this.mCreateCover({ file: file.file });
      toast.clear();
      await sleep(300);
      this.$router.push({
        name: "editLive",
        query: {
          shopId: this.$route.query.shopId,
        },
      });
    },
    onOversize(file) {
      Toast("文件大小不能超过 2M");
    },
  },
};
</script>
<style lang="less" scoped>
.content {
  display: flex;
  margin-top: 10px;
  justify-content: space-between;
  .line-desing,
  :deep(.van-uploader) {
    margin: 0 10px;
    height: 100px;
    width: 170px;
    border-radius: 10px;
    background-color: #efefed;
    > span {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }
  .line-desing {
    display: flex;
    position: relative;
    align-items: center;
  }
  :deep(.van-uploader__wrapper) {
    display: block;
    width: 100%;
    height: 100px;
  }
  :deep(.van-uploader__input-wrapper) {
    height: 100%;
    display: flex;
    align-items: center;
    position: relative;
    > span {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }
}
</style>
