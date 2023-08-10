<template>
  <div class="edit-wrap">
    <div class="edit-wrap-header">
      <van-button type="primary" class="recover" size="small">恢复</van-button>
      <span>保存</span>
    </div>
    <div class="edit-wrap-content">
      <div style="width: 90vw; ">
        <edit-panel :elements = "elements" :style="editPanelStyle"></edit-panel>
      </div>
    </div>
    <props-panel />
  </div>
</template>
<script>
  import PropsPanel from './propsPanel.vue'
  import EditPanel from './editPanel.js'
  import { mapState, mapActions, mapMutations } from "vuex";
  import store from "core/store/index";

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
        editPanelStyle: {
          width: '0px',
          height: '0px',
          transform: 'scale(1)',
        }
      }
    },
    methods: {
      ...mapActions("editor", ["fetchWork", 'setEditingPage']),
      calcRate(workWidth) {
        let w = document.documentElement.clientWidth
        return  (w/workWidth)
      }
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
