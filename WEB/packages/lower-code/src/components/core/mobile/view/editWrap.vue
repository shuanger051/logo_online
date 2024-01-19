<template>
  <div class="edit-wrap">
    <div class="edit-wrap-header">
      <van-button type="primary" class="recover" @click="createShopSign" size="small">生成效果图</van-button>
      <span @click="picDownload">下载图片</span>
      <span @click="xlslDownload" class="downLoadXLSL">下载文件</span>
    </div>
    <div class="edit-wrap-content" @click="() => this.setEditingElement()">
      <div style="min-height: 320px">
        <edit-panel :elements="elements" :style="editPanelStyle"></edit-panel>
      </div>
    </div>
   <tool-bar />
    <props-panel />
  </div>
</template>
<script>
import PropsPanel from "./propsPanel.vue";
import ToolBar from "./toolBar.vue";

import EditPanel from "./editPanel.js";
import { mapState, mapActions } from "vuex";
import store from "core/mobile/store/index";
import { download,  downLoadXLSL} from "core/support/download.js";
import {sleep, later} from '@editor/utils/tool'
import { Notify } from "vant";
import { Toast } from "vant";


export default {
  components: { PropsPanel, EditPanel, ToolBar },
  store,
  computed: {
    ...mapState("editor", {
      editingPage: (state) => state.editingPage,
      editingElement: (state) => state.editingElement,
      elements: (state) => state.editingPage.elements,
      pages: (state) => state.work.pages,
      work: (state) => state.work,
      currentShopSign: (state) => state.mobile.currentShopSign,
    })
  },
  data() {
    return {
      showOverlay: false,
      editPanelStyle: {
        width: "0px",
        height: "0px",
        transform: "scale(1)",
      },
    };
  },
  methods: {
    ...mapActions("editor", ["fetchWork", "setEditingPage", "mCreateCover", 'setEditingElement','setPic']),
    async picDownload() {
      const toast = Toast.loading({
        message: "生成中...",
        forbidClick: true,
        duration: 0,
      });
      try {
        const info=await this.mCreateCover({ el: "#content_edit" });
        download(info.data,urlPath, +new Date() + '.png')
      } catch (e) {
        Notify({ type: "danger", message: "下载失败" });
      }
      toast.clear();
    },
    async xlslDownload() {
      const toast = Toast.loading({
        message: "处理中...",
        forbidClick: true,
        duration: 0,
      });
      try {
        downLoadXLSL(this.$store.work)
      } catch (e) {
        Notify({ type: "danger", message: "下载失败" });
      }
      toast.clear();
    },
    calcRate(workWidth) {
      let w = document.documentElement.clientWidth;
      return w / workWidth;
    },
    async createShopSign() {
      const toast = Toast.loading({
        message: "生成中...",
        forbidClick: true,
        duration: 0,
      });
      try {
        const info=await this.mCreateCover({ el: "#content_edit" });
        this.setPic({
          type: 'signboardPic',
          value:info.data.urlPath
        })
        Notify({ type: "success", message: "创建成功" });
        later(() => {
          this.$router.push({
            name: "editLive",
            query: {
              shopId: this.$route.query.shopId,
            },
          });
        }, 1000);
      } catch (e) {
        console.log(e)
         Notify({ type: "danger", message: "创建失败" });
      }
      toast.clear();
    },
    async initEdit() {
      if (this.$route.params.id) {
        const toast = Toast.loading({
          message: "加载中...",
          forbidClick: true,
          duration: 0,
        });
        this.fetchWork({id: this.$route.params.id, hasWork: this.$route.query.hasWork});
        await sleep(1000);
        toast.clear();
      }
    },
  },
  created() {
    this.$watch(
      () => [this.work.width, this.work.height],
      () => {
        let r = this.calcRate(this.work.width);
        this.editPanelStyle = {
          width: this.work.width + "px",
          height: this.work.height + "px",
          transform: `scale(${r})`,
          transformOrigin: "center center",
        };
      }
    );
    this.setEditingPage();
    this.initEdit();
  },
};
</script>