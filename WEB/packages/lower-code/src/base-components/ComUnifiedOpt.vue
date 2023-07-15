<template>
  <div class="mms-uf3">
    <div class="bread-form">
      <div class="bread-form-head-wrap clearfix">
        <div class="title left title-left-border" :title="title">{{title}}</div>
        <div class="btn-back right" @click="closeCallback()">
          <h-button type="text" size="small" icon="u-a-left">返回</h-button>
        </div>
      </div>
      <div class="mms-uf3 bread-form-content-wrap" ref="content">
        <component
          v-if="currentComponent.name"
          :is="currentComponent.name"
          :data="currentComponent.data || {}"
          :extra="currentComponent.extra || {}"
          :closeEvent="backBtnCallback"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Components from './components'
import { on, off } from 'ucp-components/lib/utils/commonUtil'
export default {
  name: 'mmsUnifiedOpt',
  components: {
    ...Components
  },
  props: {
    currentComponent: {
      type: Object,
      default: () => {}
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
      let appObj = document.getElementsByClassName('app-main')
      let appOffsetTop = appObj.length === 0 ? 0 : appObj[0].offsetTop
      // 40 ===> bread-form-head-wrap, 16 ==>bread-form-head-wrap的margin-bottom, 8 ====> offset bottom
      return window.innerHeight - appOffsetTop - 40 - 16 - 8
    },
    // 高度自适应
    selfAdaption() {
      if (this.$refs.content) {
        this.selfHeight = this.calculateHeight()
        this.$refs.content.style.height = this.selfHeight.toString() + 'px'
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
.bread-form-head-wrap {
  margin-bottom: 16px;
  padding: 12px;
  border-bottom: 1px solid #d7dde4;
  line-height: 14px;
  height: 40px;

  .title {
    // border-left: 6px solid #037df3;
    padding-left: 6px;
    font-weight: bold;
    font-size: 14px;
    width: 90%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .title-left-border {
    border-left: 4px solid #037df3;
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
  padding-bottom: 18px;
}
</style>
