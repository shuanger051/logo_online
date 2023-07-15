<template>
  <div class="cms-uf3 unified-wrap">
    <div class="bread-form">
      <div class="bread-form-head-wrap clearfix">
        <div class="title left">{{title}}</div>
        <div class="btn-back right" @click="closeCallback()">
          <h-button type="text" size="small" icon="u-a-left">返回</h-button>
        </div>
      </div>
      <h-tabs>
        <h-tab-pane label="作品浏览数据" name="singleWork">
          <div class="cms-uf3 bread-form-content-wrap" ref="content">
            <component v-if="currentComponent.name" :is="currentComponent.name" :data="currentComponent.data"
              :extra="currentComponent.extra" :closeEvent="backBtnCallback" />
          </div>
        </h-tab-pane>
        <h-tab-pane label="表单组件数据" name="formData" v-if="currentComponent.data.form_flag">
          <div class="cms-uf3 bread-form-content-wrap" ref="content">
            <formDataList :worksId="currentComponent.data.worksId" />
          </div>
        </h-tab-pane>
        <h-tab-pane label="活动数据" name="activity" v-if="!currentComponent.hiddenActivityData && showActList">
          <slot name="activity">
          </slot>
        </h-tab-pane>
      </h-tabs>
    </div>
  </div>
</template>

<script>
import Components from './components'
import formDataList from '@RootViews/cmsStat/formDataList.vue'
import { on, off } from 'ucp-components/lib/utils/commonUtil'
export default {
  name: 'cmsUnifiedOpt',
  components: {
    ...Components,
    formDataList
  },
  props: {
    currentComponent: {
      type: Object,
      default: () => { }
    },
    title: '',
    backBtnCallback: {
      type: Function,
      default() {
        return ''
      }
    }
  },
  computed: {
    sidebar() {
      return this.$store.getters.sidebar
    }
  },
  data() {
    return {
      showActList: window.CMS_CONFIG.SHOW_ACTWIDGETS === 'true'
    }
  },
  watch: {
    sidebar: {
      handler() {
        this.selfAdaption()
      },
      deep: true
    }
  },
  mounted() {
    this.selfAdaption()
    on(window, 'resize', this.selfAdaption)
  },
  methods: {
    closeCallback() {
      this.backBtnCallback()
    },
    calculateHeight() {
      // let appObj = document.getElementsByClassName('app-main')
      // let appOffsetTop = appObj.length === 0 ? 0 : appObj[0].offsetTop
      // let objects = {
      //   panelObj: window.document.getElementsByClassName('h-panel')[0],
      //   titleObj: window.document.getElementsByClassName('h-datagrid-title')[0],
      //   toolbarObj: window.document.getElementsByClassName('h-datagrid-toolbar')[0],
      //   pageObj: window.document.getElementsByClassName('h-page')[0]
      // }
      // let extralHeight = 0
      // Object.keys(objects).forEach(function (key, index) {
      //   if (typeof objects[key] !== 'undefined') {
      //     extralHeight += objects[key].clientHeight
      //   }
      // })
      // 56 ===> bread-form-head-wrap  8 ====> offset bottom
      // return window.innerHeight - extralHeight - appOffsetTop - 56 - 8

      // top 值为表格到浏览器可视窗口最高位置的距离
      let top = this.$refs.content.getClientRects()[0].top
      return window.innerHeight - top - 10
    },
    // 高度自适应
    selfAdaption() {
      if (this.$refs.content) {
        this.selfHeight = this.calculateHeight()
        this.fixedHeight = this.selfHeight
        if (this.selfHeight > 50) {
          this.$refs.content.style.height = this.selfHeight.toString() + 'px'
        }
      }
    }
  },
  activated() {
    this.selfAdaption()
    on(window, 'resize', this.selfAdaption)
  },
  deactivated() {
    off(window, 'resize', this.selfAdaption)
  },
  beforeDestroy() {
    off(window, 'resize', this.selfAdaption)
  }
}
</script>
<style lang="scss" scoped>
.unified-wrap {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 8;
  background: #fff;
}
.bread-form-head-wrap {
  margin-bottom: 16px;
  padding: 12px 20px;
  border-bottom: 1px solid #d7dde4;
  line-height: 14px;
  height: 40px;

  .title {
    border-left: 6px solid #037df3;
    padding-left: 6px;
    font-weight: bold;
    font-size: 14px;
  }

  .btn-back {
    cursor: pointer;
    padding-left: 10px;
    position: relative;
  }
}
.bread-form-content-wrap {
  position: relative;
  overflow-y: auto;
}
/deep/ .cms-page-wrap {
  height: calc(100vh - 210px);
}
</style>
