<template>
  <div class="edit-wrap">
    <div class="edit-wrap-header">
      <van-button type="primary" class="recover" size="small">恢复</van-button>
      <span @click="createShopSign">保存</span>
    </div>
    <div class="edit-wrap-content">
      <div style="min-height: 320px;">
        <edit-panel :elements="elements" :style="editPanelStyle"></edit-panel>
      </div>
    </div>
    <props-panel />
    <van-overlay :show="showOverlay" class="edit-overlay" />
  </div>
</template>
<script>
import PropsPanel from "./propsPanel.vue";
import EditPanel from "./editPanel.js";
import { mapState, mapActions } from "vuex";
import store from "core/store/mobileIndex";
import { Notify } from "vant";
import { Toast } from "vant";
import { ImagePreview } from 'vant';
import { resolveImgUrl } from "core/support/imgUrl";

const sleep = async (time) => {
  return new Promise((r) => {
    setTimeout(() => r(), time);
  });
};

const later = (fn, time) => {
  setTimeout(() => {
    fn();
  }, time);
};
export default {
  components: { PropsPanel, EditPanel },
  store,
  computed: {
    ...mapState("editor", {
      editingPage: (state) => state.editingPage,
      editingElement: (state) => state.editingElement,
      elements: (state) => state.editingPage.elements,
      pages: (state) => state.work.pages,
      work: (state) => state.work,
      currentShopSign: (state) => state.mobile.currentShopSign,
    }),
    ...mapState("loading", [
      "saveWork_loading",
      "previewWork_loading",
      "setWorkAsTemplate_loading",
      "uploadWorkCover_loading",
    ]),
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
    ...mapActions("editor", ["fetchWork", "setEditingPage", "mCreateCover"]),
    calcRate(workWidth) {
      let w = document.documentElement.clientWidth;
      return w / workWidth;
    },
    async createShopSign() {
      this.showOverlay = true;
      const toast = Toast.loading({
        message: "生成中...",
        forbidClick: true,
        duration: 0,
      });
      const ts = this.editPanelStyle.transform;
      this.editPanelStyle.transform = `scale(1)`;
      await sleep(300);
      try {
        await this.mCreateCover({ el: "#content_edit" });
        Notify({ type: "success", message: "创建成功" });
        // ImagePreview([resolveImgUrl(this.currentShopSign)])
        later(() => {
          this.$router.push({
            name: "uploadLive",
            query: {
              shopId: this.$route.query.shopId,
            },
          });
        },1000);
      } catch (e) {
        Notify({ type: "danger", message: "创建失败" });
      }
      this.editPanelStyle.transform = ts;
      toast.clear();
      this.showOverlay = false;
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

    if (this.$route.params.id) {
      this.fetchWork(this.$route.params.id);
    }
  },
};
</script>

<style lang="scss" scoped>
.edit-overlay {
  background-color: rgba(155, 155, 155, 0.7);
}
</style>
