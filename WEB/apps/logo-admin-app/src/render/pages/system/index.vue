<template>
  <div>
    <a-divider>系统设置</a-divider>

    <div class="system-setting">
      <a-spin tip="处理中..." :spinning=spinning>
        <a-row>
          <a-col :span="8">文件本地存放路径：</a-col>
          <a-col :span="12">{{ path }}  <a-button type="primary"  style="margin-left: 10px" @click="openDir()"
                >打开文件夹</a-button
              ></a-col>
        </a-row>
        <a-row style="margin-top: 20px; display: flex; align-items: center">
          <a-col :span="8">新闻列表操作：</a-col>
          <a-col :span="12">
            <a-tooltip title="此操作会用OSS的新闻列表文件覆盖本地，请谨慎操作">
              <a-button type="primary" @click="getAssert('news')"
                >本地同步</a-button
              >
            </a-tooltip>
            <a-tooltip
              title="此操作会用本地文件覆盖线上OSS的新闻列表文件，请谨慎操作"
            >
              <a-button
                type="primary"
                @click="saveAssert('news')"
                style="margin-left: 10px"
                >线上同步</a-button
              >
            </a-tooltip>
          </a-col>
        </a-row>
        <a-row style="margin-top: 20px">
          <a-col :span="8">模版列表操作：</a-col>
          <a-col :span="12">
            <a-tooltip title="此操作会用OSS的模版列表文件覆盖本地，请谨慎操作">
              <a-button type="primary" @click="getAssert('template')"
                >本地同步</a-button
              >
            </a-tooltip>
            <a-tooltip
              title="此操作会用本地文件覆盖线上OSS的模版列表文件，请谨慎操作"
            >
              <a-button
                @click="saveAssert('template')"
                type="primary"
                style="margin-left: 10px"
                >线上同步</a-button
              >
            </a-tooltip>
          </a-col>
        </a-row>
      </a-spin>
    </div>
  </div>
</template>

<script>
import { message } from "ant-design-vue";

export default {
  name: "PageView",
  data() {
    return {
      path: ``,
      spinning: false,
    };
  },
  methods: {
    async loading(fn) {
      this.spinning = true;
      await fn();
      this.spinning = false;
    },
    async getAssert(name) {
      await this.loading(async () => {
        try {
          await window.electronAPI.getOssAssert(name, true);
          message.success("更新成功");
        } catch (e) {
          message.error("更新失败");
        }
      });
    },
    async saveAssert(name) {
      await this.loading(async () => {
        try {
          await window.electronAPI.saveOssAssert(name, null, true);
          message.success("更新成功");
        } catch (e) {
          message.error("更新失败");
        }
      });
    },
    openDir() {
       window.electronAPI.openDir(this.path);
    }
  },
  created() {
    window.electronAPI.getPath().then((path) => {
      this.path = path;
    });
  },
};
</script>

<style lang="less" scoped>
.system-setting {
  padding: 20px;
}
</style>
