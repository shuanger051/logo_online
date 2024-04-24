<template>
  <div class="edit-wrap">
    <div class="edit-wrap--left">
      <props-panel :beforeRead="showPicConfirm"></props-panel>
    </div>
    <div
      class="edit-wrap--main"
      :class="{
        'active-screen-shot': tokenScreenShotStatus,
      }"
    >
      <tool-bar
        class="edit-wrap-main--top"
        :showPicConfirm="showPicConfirm"
      ></tool-bar>
      <edit-panel :elements="elements" :style="getEditStyle()"></edit-panel>
    </div>
    <a-modal v-model:visible="picConfirmShow" title="确认" cancelText="取消" okText="确定" @ok="changeConfig(true)">
      <div style="padding: 10px">
        <p style="font-size: 14px">
          请确保上传的图片不侵犯他人知识产权，如有侵权，一切后果由上传人承担
        </p>
        <div style="display: flex; align-items: center">
          <a-switch
            v-model="picConfirm"
            @change="changeConfirm"
            style="margin-right: 10px"
          /><span style="font-size:14px">下次不在提示</span>
        </div>
      </div>
    </a-modal>
  </div>
</template>
<script>
import toolBar from "./toolBar";
import editPanel from "./editPanel";
import propsPanel from "./propsPanel";
import { mapState, mapActions } from "vuex";
import { later } from "@editor/utils/tool";
import store from "core/pc/store/index";
export default {
  store,
  components: {
    toolBar,
    editPanel,
    propsPanel,
  },
  data() {
    return {
      picConfirmShow: false,
      picConfirm: sessionStorage.getItem("picConfirm") == "true",
    };
  },
  computed: {
    ...mapState("editor", {
      editingElement: (state) => state.editingElement,
      elements: (state) => state.editingPage.elements,
      work: (state) => state.work,
      tokenScreenShotStatus: (state) => state.tokenScreenShotStatus,
    }),
  },
  methods: {
    ...mapActions("editor", [
      "fetchWork",
      "setEditingPage",
      "mCreateCover",
      "clearWork",
      "setEditingElement",
    ]),
    changeConfirm(value) {
      sessionStorage.setItem("picConfirm", value);
    },
    showPicConfirm() {
      return new Promise((resolve, reject) => {
        if (this.picConfirm) {
          resolve();
        } else {
          this.picConfirmShow = true;
          this.changeConfig = (flag) => {
            this.picConfirmShow = false;
            later(() => {
              flag ? resolve() : reject();
            }, 200);
          };
        }
      });
    },
    async initEdit() {
      if (this.$route.params.id == "cache") {
        if (this.$store.state.editor.signboardCache) {
          this.fetchWork({
            cache: this.$store.state.editor.signboardCache,
          });
        } else {
          this.$router.push({
            path: "/",
          });
        }
        return
      } 
      if (this.$route.params.id) {
        const toast = this.$message.loading("加载中...", 0);
        await this.fetchWork({ id: this.$route.params.id });
        toast();
      }
    },
    getEditStyle() {
      return {
        width: this.work.width + "px",
        height: this.work.height + "px",
      };
    },
  },
  created() {
    this.clearWork();
    this.setEditingPage();
    this.initEdit();
  },
};
</script>
<style lang="scss" scoped>
.edit-wrap {
  width: 1000px;
  margin: 0px auto;
}
</style>
