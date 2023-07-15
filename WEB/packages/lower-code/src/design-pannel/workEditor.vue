<template>
  <div :class="['work-editor', isFullscreen ? 'fullscreen' : '']">
    <menu-panel ref="menuPanelRef" :showPreviewDialog.sync="showPreviewDialog"
      :showLongPicDialog.sync="showLongPicDialog" @getWorksContent="getWorksContent" :worksInfo="worksInfo"
      @workDetailFlag="workDetailFlag"></menu-panel>

    <div class="works-container">
      <!-- 元素组件面板 -->
      <transition name="fade">
        <components-panel class="components-panel" v-show="showComponentsPanel" :componentsList="componentsList"
          :worksContent="worksContent"></components-panel>
      </transition>
      <!-- 元素组件面板收缩按键 -->
      <div class="collsable-wrap collsable-components-wrap" @click="toggleComponentsPanel">
        <span class="collsable-click-wrap collsable-components-click-wrap">
          <h-icon :name="showComponentsPanel ? 'arrow-left-b icon-arrow-left-b' : 'arrow-right-b icon-arrow-right-b'"
            :size="14"></h-icon>
        </span>
      </div>
      <!-- 编辑器画布 -->
      <center-panel class="center-panel"></center-panel>
      <!-- 属性面板收缩按键 -->
      <div class="collsable-wrap collsable-setting-wrap" @click="toggleSettingPanel">
        <span class="collsable-click-wrap collsable-setting-click-wrap">
          <h-icon :name="showSettingPanel ? 'arrow-right-b icon-arrow-right-b' : 'arrow-left-b icon-arrow-left-b'"
            :size="14"></h-icon>
        </span>
      </div>
      <!-- 属性面板 -->
      <transition name="fade">
        <setting-panel class="setting-panel" v-show="showSettingPanel" :worksInfo="worksInfo"></setting-panel>
      </transition>
    </div>

    <!-- 预览弹框 -->
    <preview-dialog :show.sync="showPreviewDialog" ::worksInfo="worksInfo" :worksContent="worksContent"
      :linkUrl="linkUrl" />
  </div>

</template>

<script>
import { mapActions, mapState } from 'vuex'
import { cloneDeep } from 'lodash'

import componentsPanel from './workPanel/componentsPanel/index.vue'
import settingPanel from './workPanel/settingPanel/index.vue'
import menuPanel from './workPanel/menuPanel/index.vue'
import centerPanel from './workPanel/centerPanel/index.vue'
import previewDialog from './workPanel/previewDialog/index.vue'
import { tempCompatible } from '@Utils/utils'

import eEditPanel from '@h5Designer/components/e-edit-panel'
import WorkManager from '@Utils/workManager.js'
import { getComponentList, getWorksContent, getDefaultWorksTitle, getWorksTmplContent } from '@Apis/works.js'
import { getParameterByCode } from '@Apis/business.js'
import actruleInfo from '@Mixin/actruleInfo' // 查询规则信息
// import undoRedoPlugin from '@Components/h5-designer/history' // 编辑器撤销还原stroe

function clickHandler(e) {
  e.preventDefault()
}

export default {
  name: 'HEditor',
  props: {
    worksId: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'add'
    }
    // pannel_config: {
    //   SHOW_LOGIN_SWITCH: 'true',
    //   showSaveAsTem: 'true',
    //   CASTRATED_VERSION: 'false', // 是否Lite版
    //   SHOW_ACTWIDGETS: 'false', // 是否展示活动组件
    //   SHOW_SHARE: 'true',
    // },
    // server_config: {
    //   CCM_URL: 'http://127.0.0.1:8091/g/bpo.cms.ccm/v',
    //   OMS_URL: 'http://127.0.0.1:8091/g/hswealth.oms/v',
    //   OMC_URL: 'http://127.0.0.1:8091/g/hsxone.omc/v',
    //   WFS_URL: 'http://127.0.0.1:8091/wf-server-systems/wf-server/v',
    //   MMS_URL: 'http://127.0.0.1:8091/g/hsxone.mms.materialscenter/v',
    //   ACM_URL: 'http://127.0.0.1:8091/g/mplus-skills-service/v',
    //   SHOW_LOGIN_SWITCH: 'true',
    //   CUSTOMER_CODE: '12222',
    //   SHOW_UV_DATA: 'true',
    //   Ali_region: 'cn-shanghai',
    //   Ali_userId: '1414954340995114',
    //   USE_ALIYUN: 'false',
    //   NEED_SA: 'false',
    //   SA_FROM_CHANNEL: '融e通',
    //   Aliyun_type: 'ALIYUN_VOD',
    //   ALIYUN_TYPE: 'all',
    //   USE_ALIYUN_PROXY: 'true', // 是否使用阿里云代理
    //   ALIYUN_PROXY_ADDRESS: 'http://127.0.0.1:8088', // 阿里云代理地址
    //   IMAGE_URL: 'http://127.0.0.1:8091/', // 图片域名
    //   showCusterManage: 'true',
    //   ALIYUN_DOMAIN: 'https://outin-203ae798567e11e9817500163e1c94a4.oss-cn-shanghai.aliyuncs.com',
    //   STORE_TYPE: 'disk',
    //   FSTORE_IP: '',
    //   logPicToMMS: 'true',
    //   ORG_CODE: 'ZYGJ001' // 组织编号
    // }
  },
  data() {
    return {
      classifys: [],
      componentsList: [],
      showComponentsPanel: true,
      showSettingPanel: true,
      showPreviewDialog: false,
      showLongPicDialog: false,
      workManager: '',
      worksContent: '',
      worksInfo: {
        works_id: '',
        works_code: '',
        version_no: '',
        version_ext: '',
        works_status: '',
        works_sub_status: '',
        works_title: '',
        description: '',
        qrcode_json: '',
        link_url: '',
        user_id: '',
        create_date_time: '',
        update_date_time: '',
        works_editable_flag: '',
        publish_date_time: ''
      }
    }
  },
  computed: {
    ...mapState('cms/common', [
      'isFullscreen'
    ])
  },
  watch: {
    worksId: {
      handler(val) {
        if (val && val !== '') {
          this.worksId = val
          this.init()
        }
      },
      deep: true
      // immediate: true // immediate会引起模板保存触发两次
    },
    type: {
      handler() {
        this.workManager.initRuntime()
        // this.workManager = new WorkManager()
        // this.workManager.init({ vueInstance: this, type: val }).then(res => {
        //   this.worksInfo = res
        //   this.worksContent = res
        // })
      },
      deep: true
    }
    // '$route' (to, from) {
    //   this.addByTem()
    // }
  },
  components: {
    componentsPanel,
    settingPanel,
    menuPanel,
    eEditPanel,
    centerPanel,
    previewDialog
    // longPicDialog
  },
  mixins: [
    actruleInfo
  ],
  created() {
    this.init()
    if (window.CMS_CONFIG.SHOW_ACTWIDGETS === 'true') {
      // 查询系统参数
      this.getSysParamValFunc('10015,10016,10017,10019,10020,10021')
    } else {
      if (window.CMS_CONFIG.SHOW_LOGIN_SWITCH === 'true') {
        getParameterByCode({ param_code: '10019' }).then((res) => {
          console.log(res)
          const params_json = JSON.parse(res.data.param_value) || []
          console.log(params_json)
          const param = {}
          param['10019'] = params_json
          this.updateWorks('works_login_user_type', params_json[0].value)
          this.$store.dispatch('cms/actState/changeSysParamsState', param)
        })
      }
    }
  },
  destroyed() {
    this.$store.dispatch('cms/cleanWorksState')
    this.$store.$$init()
  },
  methods: {
    updateWorks(key, value) {
      this.$store.dispatch('cms/works/updateWorks', {
        [key]: value
      })
    },
    ...mapActions('cms/editState', [
      'updateEditState'
    ]),
    toggleComponentsPanel() {
      this.showComponentsPanel = !this.showComponentsPanel
    },
    toggleSettingPanel() {
      this.showSettingPanel = !this.showSettingPanel
    },
    getComponentList() {
      getComponentList().then(res => {
        this.componentsList = res.data.rows
      })
    },
    workDetailFlag(item) {
      this.$emit('workDetailFlag', item)
    },
    addWork() {
    },
    getWorksContent() {
      let worksContent = cloneDeep(this.$store.state.cms)
      //去除验证数据
      worksContent.works.curRef = ''
      worksContent.works.passValidate = {
        vlidateFunc: ''
      }
      this.worksContent = worksContent
      let previewJSON = {
        pages: worksContent.pages.items,
        elements: worksContent.elements.items,
        events: worksContent.events.items,
        works: worksContent.works,
        works_id: this.worksId
      }
      window.sessionStorage.setItem('cms-h5', JSON.stringify(previewJSON))
      this.linkUrl = process.env.NODE_ENV === 'development' ? `${window.location.origin}/cms-h5.html?preview=true` : `${window.location.origin}/cms-h5/index.html?preview=true`
    },
    // addByTem() {
    //   const tempId = this.getQueryString('tempId')
    //   const type = this.getQueryString('type')
    //   if (type == 'addByTemp' && tempId) {
    //     window.location.replace(`${window.location.hash.substr(0, window.location.hash.indexOf('?'))}`)
    //     this.$emit('workDetailFlag', true)
    //     this.$store.dispatch('cms/cleanWorksState')
    //     this.type = 'addByTemp'
    //     this.worksId = tempId
    //     this.init()
    //   }
    // },
    init() {
      this.getWorksContent()
      // 请求组件列表、如果是修改请求作品数据并
      this.getComponentList()
      // 创建作品管理器
      this.workManager = new WorkManager()
      this.workManager.init({ vueInstance: this, type: this.type })
      this.$emit('getType', this.type)

      if (this.type === 'edit' && this.worksId) {
        getWorksContent({
          works_id: this.worksId
        }).then(res => {
          let result = res.data
          // const works_id = this.worksInfo.link_url.substr(this.worksInfo.link_url.indexOf('?') + 1)
          let worksContent = JSON.parse(result.works_content)
          // let runtimeContext = this.workManager.runtimeContext
          // 保留历史作品属性数据
          tempCompatible(worksContent.elements)
          result.works_content = JSON.stringify(worksContent)
          // for (let key in worksContent.elements) {
          //   worksContent.elements[key].forEach(item => {
          //     if (item.name === 'hs-cms-video') {
          //       item.property.videoSourceType = item.property.videoSourceType||'1'
          //     }
          //     if ('hs-cms-button') {
          //       item.property = Object.assign(runtimeContext.configGetter.getConfig(item.name).defaultProperty, item.property)
          //     }
          //   })
          // }
          this.worksInfo = result

          this.worksContent = worksContent
          Object.keys(worksContent).forEach((key) => {
            if (key !== 'works') {
              worksContent[key] = { items: worksContent[key] }
            }
          })
          this.updateEditState({ selectedPage: worksContent.pages.items[0].uuid })
          this.$store.dispatch('cms/setWorksState', worksContent)
          this.$store.$$init()
          // 编辑修改规则查询
          if (window.CMS_CONFIG.SHOW_ACTWIDGETS === 'true') {
            const worksCode = res.data.works_code || ''
            this.getPageUuid(worksCode, worksContent)
            this.getWidgetListOfChooseFunc(res.data.works_code)
          }
        })
      } else if (this.type === 'addByTemp' && this.worksId) {
        getWorksTmplContent({
          works_id: this.worksId
        }).then(res => {
          let worksContent = JSON.parse(res.data.works_content)
          // let runtimeContext = this.workManager.runtimeContext
          // 保留历史作品属性数据
          tempCompatible(worksContent.elements)
          // for (let key in worksContent.elements) {
          //   worksContent.elements[key].forEach(item => {
          //     if (item.name === 'hs-cms-video') {
          //       item.property.videoSourceType = item.property.videoSourceType||'1'
          //     }
          //     if ('hs-cms-button') {
          //       item.property = Object.assign(runtimeContext.configGetter.getConfig(item.name).defaultProperty, item.property)
          //     }
          //   })
          // }
          getDefaultWorksTitle({works_title: worksContent.works.works_title}).then(res => {
            if (res.data.works_title) {
              worksContent.works.works_title = res.data.works_title
            } else {
              worksContent.works.works_title = ''
            }
          }).catch(() => {
            worksContent.works.works_title = ''
          }).finally(() => {
            this.worksContent = worksContent
            Object.keys(worksContent).forEach((key) => {
              if (key !== 'works') {
                worksContent[key] = { items: worksContent[key] }
              } else {
                // 清空works_id
                worksContent[key].works_id = ''
              }
            })
            if (window.CMS_CONFIG.SHOW_ACTWIDGETS == 'true') {
              // 查询已发布的widget-id
              this.getWidgetListOfChooseFunc()
              // 保存widget-id
              this.saveWidgetId(worksContent)
            } else {
              this.updateEditState({ selectedPage: worksContent.pages.items[0].uuid })
              this.$store.dispatch('cms/setWorksState', worksContent)
              this.$store.$$init()
              // 作品保存
              // this.$store.dispatch('cms/saveWorks', '')
            }
          })
        })
      } else {
        if (this.type === 'add') {
          // 新增作品, 默认显示作品名称
          getDefaultWorksTitle().then(res => {
            this.$store.dispatch('cms/initWorksState')
            this.updateWorks('works_title', res.data.works_title)
            this.worksInfo.works_title = res.data.works_title
            this.$store.$$init()
            // 作品保存
            // this.$store.dispatch('cms/saveWorks', '')
            if (window.CMS_CONFIG.SHOW_ACTWIDGETS === 'true') {
              // 查询已发布的widget-id
              this.getWidgetListOfChooseFunc()
              window.sessionStorage.setItem('currentWidgetList', '[]')
            }
          })
        }
      }
    },
    getQueryString(name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
      var r = decodeURI(this.$route.fullPath.substr(this.$route.fullPath.indexOf('?') + 1)).match(reg)
      if (r != null) return unescape(r[2])
      return null
    }
  },
  mounted() {
    document.body.addEventListener('contextmenu', clickHandler)
  },
  beforeDestroy() {
    document.body.removeEventListener('contextmenu', clickHandler, false)
  }
}
</script>
<style lang="scss" scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.fullscreen {
  z-index: 999;
  height: 100%;
  width: 100%;
  position: fixed;
  left: 0px;
  top: 0px;
  background: #fff;
  .works-container {
    height: 100%;
  }
  /deep/ .el-tabs__content {
    height: calc(100vh - 120px);
    overflow-x: hidden;
    overflow-y: auto;
  }
  /deep/ .el-tab-pane {
    height: calc(100vh - 120px);
    overflow-y: auto;
    overflow-x: hidden;
  }
  /deep/ .el-tabs__content {
    height: calc(100vh - 120px);
    overflow-y: auto;
  }
}
</style>