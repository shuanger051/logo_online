<template>
  <div class="edit-wrap">
    <div class="edit-wrap-header">
      <van-button type="primary" class="recover" size="small">恢复</van-button>
      <span @click = "createShopSign">保存</span>
    </div>
    {{tt}}
    <div class="edit-wrap-content">
      <div>
        <edit-panel :elements = "elements" :style="editPanelStyle"></edit-panel>
      </div>
    </div>
    <van-uploader :after-read="afterRead" />
    <props-panel />
  </div>
</template>
<script>
  import PropsPanel from './propsPanel.vue'
  import EditPanel from './editPanel.js'
  import { mapState, mapActions, mapMutations } from "vuex";
  import store from "core/store/mobileIndex";
  import { ImagePreview } from 'vant';
  import {resolveImgUrl} from 'core/support/imgUrl'
  import {appSaveLogoInfoAPI} from "core/api"
  import { Notify } from 'vant';

  const sleep = async (time) => {
  return new Promise((r) => {
    setTimeout(() => r(), time);
  });
};

  export default {
    components: {PropsPanel, EditPanel},
    store,
    computed: {
      ...mapState('editor', {
        editingPage: state => state.editingPage,
        editingElement: state => state.editingElement,
        elements: state => state.editingPage.elements,
        pages: state => state.work.pages,
        work: state => state.work,
        currentShopSign: (state) =>state.mobile.currentShopSign
      }),
      ...mapState('loading', [
        'saveWork_loading',
        'previewWork_loading',
        'setWorkAsTemplate_loading',
        'uploadWorkCover_loading'
      ])
    },
    data() {
      return {
        tt: '',
        editPanelStyle: {
          width: '0px',
          height: '0px',
          transform: 'scale(1)',
        }
      }
    },
    methods: {
      ...mapActions("editor", ["fetchWork", 'setEditingPage', 'mCreateCover']),
      calcRate(workWidth) {
        let w = document.documentElement.clientWidth
        return  (w/workWidth)
      },
      async createShopSign() {
        const ts = this.editPanelStyle.transform
        this.editPanelStyle.transform = `scale(1)`
        await sleep(300)
        try{
          await this.mCreateCover({el: "#content_edit"})
          Notify({ type: 'success', message: '创建成功' });
          ImagePreview([resolveImgUrl(this.currentShopSign)]);
        } catch(e) {
          Notify({ type: 'danger', message: '创失败' });
        }
        this.editPanelStyle.transform = ts

      },
    },

    created() {
      this.$watch(() => [this.work.width, this.work.height], () => {
        let r = this.calcRate(this.work.width)
        this.editPanelStyle = {
          width: this.work.width + 'px',
          height: this.work.height + 'px',
          transform: `scale(${r})`,
          transformOrigin: 'center center'
        }
      })
      this.setEditingPage()
      
      if (this.$route.params.id) {
        this.fetchWork(this.$route.params.id);
      }
    }
  }
</script>
