<template>
  <div>
    <h-msg-box v-model="show" :mask-closable="false" @on-close="closeHandler" class="preview-dialog" title=""
      :footerHide="true">
      <h-row>
        <h-col :span="spanValue">
          <el-tabs v-model="tab" v-if="showVersion" @tab-click="handleClick">
            <el-tab-pane label="当前版本" name="current">
              <div class="preview-wrap" v-if="worksInfo.works_status === 'D' || worksInfo.works_status === 'C'">
                <preview :workContent="workContent" :linkUrl="linkUrl" ref="previewCurrent"
                  :showInfo="spanValue === 12"></preview>
              </div>
            </el-tab-pane>
            <el-tab-pane label="审核中版本" name="audit">
              <div class="preview-wrap" v-if="worksInfo.works_status === 'B'">
                <preview :workContent="workContent" :linkUrl="linkUrl" :showInfo="spanValue === 12"></preview>
              </div>
            </el-tab-pane>
          </el-tabs>
          <div class="preview-wrap" v-if="show && !showVersion">
            <preview :style="spanValue === 24 ? 'margin:auto': ''" :worksContent="worksContent" :linkUrl="linkUrl"
              :showInfo="spanValue === 12">
            </preview>
          </div>
        </h-col>
        <h-col :span="spanValue" v-if="spanValue === 12">
          <info :worksInfo="worksInfo" :qrcodeUrl="qrcodeUrl" />
        </h-col>
      </h-row>
      <div slot="footer">
        <!-- <h-button @click="closeHandler">取消</h-button> -->
      </div>
    </h-msg-box>
  </div>
</template>

<script>
// import { getModifiedWorksContent, getWorksContent } from '@Apis/works.js'
import preview from './Preview'
import info from './Info'
import { mapActions } from 'vuex'

export default {
  props: [
    'show',
    'worksInfo',
    'worksContent',
    'qrcodeUrl',
    'showVersion',
    'version',
    'currentWorksId',
    'worksSubStatus',
    'tab',
    'linkUrl'
  ],
  data() {
    return {
    }
  },
  created() {
  },
  components: {
    preview,
    info
  },
  computed: {
    spanValue: function () {
      return (this.worksInfo && this.worksInfo.link_url) ? 12 : 24
    }
  },
  watch: {
    'worksInfo.works_status': {
      handler(val) {
        this.$store.dispatch('cms/initWorksState')
        if (this.$refs.previewCurrent) {
          this.$refs.previewCurrent.reflesh()
        }
      },
      deep: true
    }
  },
  methods: {
    ...mapActions('cms/editState', [
      'updateEditState'
    ]),
    closeHandler() {
      this.$emit('tab', 'current')
      this.$emit('showDialog', false)
      // this.$store.dispatch('cms/initWorksState')
      // this.$store.$$init()
      if (!this.version === '' && !this.version) {
        this.$store.dispatch('cms/cleanWorksState')
      }
      this.$emit('update:show', false)
    },
    handleClick(tab, event) {
      this.$emit('tab', tab.name)
    }
  }
}
</script>

<style scoped lang="scss">
.preview-dialog {
  .preview-wrap {
    background: url(../../../../assets/images/mobile-contain.png) no-repeat
      center center;
    position: relative;
  }
}
/deep/ .h-modal-content {
  width: 800px !important;
  left: 24% !important;
  top: 20px !important;
  height: 820px !important;
}
</style>
