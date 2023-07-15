<template>
  <div class="top-container">
    <div class="title-wrap">
      <h-input v-model="worksProxy.works_title" :filterRE="/[<>]/g" placeholder="请输入作品名称" :maxlength="40"></h-input>
    </div>
    <div class="panel-button-wrap">
        <div @click.stop="adjustElement('pre')" v-if="!castratedVersion"
          :class="[ preLayerStatus ? 'single-button-wrap-normal' : 'single-button-wrap-disabled', 'single-button-wrap']">
          <!-- <div> -->
          <h-tooltip content="前置一层" placement="bottom" relativeRoot>
                  <img :src="preLayerStatus ? preImg : preDisabledImg">
          </h-tooltip>
          <!-- <object :data=" preLayerStatus ? preImg : preDisabledImg" type="image/svg+xml"></object> -->
          <!-- </div> -->
          <!-- <div>前置一层</div> -->
        </div>

      <div @click.stop="adjustElement('post')" v-if="!castratedVersion"
        :class="[ postLayerStatus ? 'single-button-wrap-normal' : 'single-button-wrap-disabled', 'single-button-wrap']">
        <h-tooltip content="后置一层" placement="bottom" relativeRoot>
          <img :src="postLayerStatus ? postImg : postDisabledImg">
          <!-- <object :data=" postLayerStatus ? postImg : postDisabledImg" type="image/svg+xml"></object> -->
        </h-tooltip>
        <!-- <div>后置一层</div> -->
      </div>
      <div @click.stop="undoOrRedo(true)" v-if="!castratedVersion"
        :class="[$store.$$canUndo ? 'single-button-wrap-normal' : 'single-button-wrap-disabled', 'single-button-wrap']">
        <h-tooltip content="撤销一步" placement="bottom" relativeRoot>
          <img :src="$store.$$canUndo ? cancelStepImg : cancelStepDisabledImg">
          <!-- <object :data="preStepStatus ? cancelStepImg : cancelStepDisabledImg" type="image/svg+xml"></object> -->
        </h-tooltip>
        <!-- <div>撤销一步</div> -->
      </div>
      <div @click.stop="undoOrRedo(false)" v-if="!castratedVersion"
        :class="[$store.$$canRedo ? 'single-button-wrap-normal' : 'single-button-wrap-disabled', 'single-button-wrap']">
        <h-tooltip content="还原一步" placement="bottom" relativeRoot>
          <img :src="$store.$$canRedo ? backStepImg : backStepDisabledImg">
          <!-- <object :data=" postStepStatus ? backStepImg : backStepDisabledImg" type="image/svg+xml"></object> -->
        </h-tooltip>
        <!-- <div>还原一步</div> -->
      </div>
      <div class="scaleGroup">
        <h-icon name="android-remove icon-android-remove" @on-click="handleZoomIn"></h-icon>
        <h-input style="width: 80px;" :value="scalePercent" @on-enter="changeScaleNum">
          <span slot="append">%</span>
        </h-input>
        <h-dropdown placement="bottom-start" @on-click="changeScale">
          <a href="javascript:void(0)">
            <h-icon name="unfold" color="#333"></h-icon>
          </a>
          <h-dropdown-menu slot="list">
            <h-dropdown-item>25%</h-dropdown-item>
            <h-dropdown-item>50%</h-dropdown-item>
            <h-dropdown-item>75%</h-dropdown-item>
            <h-dropdown-item>100%</h-dropdown-item>
            <h-dropdown-item>125%</h-dropdown-item>
            <h-dropdown-item>150%</h-dropdown-item>
            <h-dropdown-item>200%</h-dropdown-item>
            <h-dropdown-item>300%</h-dropdown-item>
            <h-dropdown-item>400%</h-dropdown-item>
          </h-dropdown-menu>
        </h-dropdown>
        <h-icon name="android-add icon-android-add" @on-click="handleZoomOn"></h-icon>
      </div>
      <div @click="previewWork" class="single-button-wrap single-button-wrap-normal">
        <h-tooltip content="预览" placement="bottom" relativeRoot>
          <img :src="previewImg">
        </h-tooltip>
        <!-- <div>预览</div> -->
      </div>
      <div @click.stop="exportLongImg" v-if="!castratedVersion" class="single-button-wrap-normal single-button-wrap">
       <h-tooltip content="生成长图" placement="bottom" relativeRoot>
          <img :src="longImg">
       </h-tooltip>
        <!-- <div>生成长图</div> -->
      </div>
    </div>
    <div class="save-button-wrap">
      <h-button-group>
        <h-button @click="cancelWork">退出</h-button>
        <h-button @click="saveAsDraft(worksInfo.works_id)"
          v-if="worksInfo.works_status !== 'C' && worksInfo.works_status !== 'D'">{{draftText}}</h-button>
        <h-button v-if="worksInfo.works_status !== 'E'" @click="publishWork">发布</h-button>
        <h-button v-if="worksInfo.works_status === 'E'" @click="startWorksEnableProcess(worksInfo.works_status)">启用
        </h-button>
        <h-button v-if="showSaveAsTem && !castratedVersion" @click="submitEdit(worksInfo.works_id)">另存为模板</h-button>
        <h-button @click="h5preview" v-if='isDev'>h5预览</h-button>
        <h-button @click="toggleFullscreen">{{ this.isFullscreen ? '缩小' : '全屏'}}</h-button>
      </h-button-group>
    </div>

    <h-msg-box v-model="temlateEidtMsg" title="模板设置" :loading="temLoadState" :mask-closable="false" @on-close="handeleClose">
      <!-- <div>
        <span>模板名称：</span>
        <h-input v-model="temForm.works_title" placeholder="请输入模板名称" style="width: 300px;" :maxlength="40"></h-input>
      </div>
      <div>
        <span>模板分类：</span>
        <template v-if="temlateEidtMsg">
        <h-select v-model="temForm.class_id" style="width: 300px;">
          <h-option v-for="item in categories" :value="item.class_id" :key="item.class_id">{{ item.class_name }}
          </h-option>
        </h-select>
        </template>
      </div> -->
      <div>
        <h-form :model="temForm" :rules="ruleValidate" :label-width="80" ref="classTypeSetFrom">
          <h-form-item label="模板名称" prop="works_title">
            <h-input v-model="temForm.works_title" maxlength="40" placeholder="请输入模板名称"></h-input>
          </h-form-item>

          <h-form-item label="模板描述" prop="worksTmplDesc">
            <h-input
              v-model="temForm.worksTmplDesc"
              type="textarea"
              showWordLimit
              maxlength="200"
              :canResize="false"
              :rows="6"
              placeholder="请输入模板描述..."
            ></h-input>
          </h-form-item>

          <h-form-item label="生命周期" prop="tmplCycleId">
            <h-select v-model="temForm.tmplCycleId" transfer placeholder="请选择生命周期">
              <h-option
                v-for="item in cycleList"
                :value="item.class_id"
                :key="item.class_name"
              >
                {{ item.class_name }}
              </h-option>
            </h-select>
          </h-form-item>

          <h-form-item label="适用场景" prop="tmplSceneId">
            <h-select v-model="temForm.tmplSceneId" transfer placeholder="请选择适用场景">
              <h-option
                v-for="item in sceneList"
                :value="item.class_id"
                :key="item.class_name"
              >
                {{ item.class_name }}
              </h-option>
            </h-select>
          </h-form-item>

        </h-form>
      </div>
      <p slot="footer">
        <h-button @click="handeleClose">取消</h-button>
        <h-button type="primary" @click="handleSaveTemplate">确定</h-button>
      </p>
    </h-msg-box>

     <div class="export-img-container"></div>
  </div>
</template>

<script>
import { mapGetters, mapState, mapActions } from 'vuex'
import { accAdd, accSub, downloadFile, dataURLtoFile } from '@Utils/utils'
import { startWorksEnableProcess, putWorks, getSysWorksTmplClassList } from '@Apis/works.js'

import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
// eslint-disable-next-line no-unused-vars
import * as upload from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
import { apiPost } from '@Utils/request'
import { isDev, generateUID, worksAdminVlidate } from '@h5Designer/utils'
import previewImg from '@Root/assets/images/preview.svg'
import longImg from '@Root/assets/images/longImg.svg'
import preImg from '@Root/assets/images/preLayer.svg'
import postImg from '@Root/assets/images/postLayer.svg'
import cancelStepImg from '@Root/assets/images/cancel.svg'
import backStepImg from '@Root/assets/images/back.svg'
import preDisabledImg from '@Root/assets/images/preLayer-disabled.svg'
import postDisabledImg from '@Root/assets/images/postLayer-disabled.svg'
import cancelStepDisabledImg from '@Root/assets/images/cancel-disabled.svg'
import backStepDisabledImg from '@Root/assets/images/back-disabled.svg'
import html2canvas from 'html2canvas'
import { uploadFilePlt, postMaterialFile, postImageMaterial } from '@Apis/common'
import hMessage from 'h_ui/dist/lib/Message'
import hotkeys from 'hotkeys-js'
import { cloneDeep } from 'lodash'
import emojiRegex from 'emoji-regex'

window.OSS = OSS

const validateName = (rule, value, callback) => {
  const emojiTest = emojiRegex()
  if (value === '') {
    callback(new Error('模板名称不能为空'))
  } else if (emojiTest.test(value)) {
    callback(new Error('请输入非表情内容'))
  } else {
    callback()
  }
}

const validateEmoji = (rule, value, callback) => {
  console.log(value)
  const emojiTest = emojiRegex()
  if (emojiTest.test(value)) {
    callback(new Error('请输入非表情内容'))
  } else {
    callback()
  }
}

export default {
  props: {
    worksInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  components: {
  },
  data() {
    return {
      temlateEidtMsg: false,
      temLoadState: false,
      temForm: {
        works_id: '',
        works_title: '',
        tmplSceneId: '',
        tmplCycleId: '',
        worksTmplDesc: ''
      },
      cycleList: [],
      sceneList: [],
      ruleValidate: {
        works_title: [{ required: true, validator: validateName, trigger: 'blur' }],
        tmplSceneId: [{ required: true, message: '请选择适用场景', trigger: 'blur' }],
        tmplCycleId: [{ required: true, message: '请选择生命周期', trigger: 'blur' }],
        worksTmplDesc: [{ required: false, validator: validateEmoji, trigger: 'blur' }]
      },
      categories: [],
      preImg: preImg,
      postImg: postImg,
      cancelStepImg: cancelStepImg,
      backStepImg: backStepImg,
      preDisabledImg,
      postDisabledImg,
      cancelStepDisabledImg,
      backStepDisabledImg,
      longImg: longImg,
      previewImg: previewImg,
      imgsrc: null, // 生成长图图片地址
      isShowLongImg: false,
      isDev: isDev(),
      showSaveAsTem: window.CMS_CONFIG.showSaveAsTem && window.CMS_CONFIG.showSaveAsTem == 'true',
      saveType: '',
      useAliyun: window.CMS_CONFIG.USE_ALIYUN && window.CMS_CONFIG.USE_ALIYUN == 'true' && (window.CMS_CONFIG.ALIYUN_TYPE == 'all' || window.CMS_CONFIG.ALIYUN_TYPE == 'video'),
      aliregion: window.CMS_CONFIG.Ali_region,
      aliuserId: window.CMS_CONFIG.Ali_userId,
      loading: null,
      castratedVersion: window.CMS_CONFIG.CASTRATED_VERSION && window.CMS_CONFIG.CASTRATED_VERSION == 'true' // 是否打开Lite版
    }
  },
  computed: {
    ...mapState('cms/common', [
      'isFullscreen'
    ]),
    ...mapState('cms/works', {
      works: state => state
    }),
    ...mapState('cms/pages', {
      pages: state => state.items
    }),
    ...mapState('cms/editState', [
      'scaleRate', 'scalePercent', 'selectedElement'
    ]),
    ...mapGetters('cms/editState', [
      'scalePercent'
    ]),
    preLayerStatus() {
      // 元素选中情况下才能前置
      return !!this.selectedElement
    },
    postLayerStatus() {
      // 元素选中情况下才能后置
      return !!this.selectedElement
    },
    worksProxy() {
      return new Proxy(this.works, {
        get: (target, name) => {
          return this.works[name] || ''
        },
        set: (target, name, value) => {
          this.updateWorks(name, value)
          return true
        }
      })
    },
    draftText() {
      if (this.worksInfo.works_status === 'E') {
        // return '保存并退出'
        return '保存'
      } else {
        // return '保存并退出'
        return '保存'
      }
    }
  },
  methods: {
    ...mapActions('cms/common', [
      'toggleFullscreen'
    ]),
    getSysWorksTmplClassList() {
      this.loading = true
      getSysWorksTmplClassList().then(res => {
        let categories = res.data.rows
        for (let i = 0; i < categories.length; i++) {
          if (categories[i].type_code === '1') {
            this.cycleList = categories[i].data
          } else if (categories[i].type_code === '2') {
            this.sceneList = categories[i].data
          }
        }
      }).finally(() => {
        this.loading = false
      })
    },
    updateWorks(key, value) {
      this.$store.dispatch('cms/works/updateWorks', {
        [key]: value
      })
    },
    changeScale(name) {
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: (Number(parseFloat(name)) / 100).toFixed(2)
      })
    },
    changeScaleNum(name) {
      let val = ''
      const regex = /^[0-9]*$/
      if (name.target) {
        val = name.target._value
      } else {
        val = name
      }
      if (regex.test(val)) {
        if (val > 400 || val < 25) {
          this.$hMessage.warning('超出画布范围')
          return false
        }
      } else {
        this.$hMessage.warning('请输入数字')
        return false
      }
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: (Number(parseFloat(val)) / 100).toFixed(2)
      })
    },
    handleZoomIn() {
      let newScaleRate = (Number(parseFloat(this.scaleRate))).toFixed(2)
      if (newScaleRate <= 0.25) {
        this.$hMessage.warning('超出画布范围')
        return false
      }
      if (newScaleRate > 0.25 && newScaleRate < 0.3) {
        this.$hMessage.warning('短于步长，无法调整')
        return false
      }
      if (newScaleRate > 0.25) {
        newScaleRate = accSub(newScaleRate, 0.05)
      }
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: newScaleRate
      })
    },
    h5preview() {
      let { cms } = this.$store.state
      // 去除验证的信息
      let worksContent = cloneDeep(cms.works)
      worksContent.curRef = ''
      worksContent.passValidate = {
        vlidateFunc: ''
      }
      let previewJSON = {
        pages: cms.pages.items,
        elements: cms.elements.items,
        events: cms.events.items,
        works: worksContent,
        works_id: this.worksInfo.works_id
      }
      window.sessionStorage.setItem('cms-h5', JSON.stringify(previewJSON))
      window.open('/cms-h5.html?preview=true', '_blank')
    },
    handleZoomOn() {
      let newScaleRate = (Number(parseFloat(this.scaleRate))).toFixed(2)
      if (newScaleRate >= 4) {
        this.$hMessage.warning('超出画布范围')
        return false
      }
      if (newScaleRate > 3.95 && newScaleRate < 4) {
        this.$hMessage.warning('短于步长，无法调整')
        return false
      }
      if (newScaleRate < 4) {
        newScaleRate = accAdd(newScaleRate, 0.05)
      }
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: newScaleRate
      })
    },
    adjustElement(type) {
      if ((this.preLayerStatus && type === 'pre') || (this.postLayerStatus && type === 'post')) {
        this.$store.dispatch('cms/elements/prePostElementIndex', type)
      }
    },
    previewWork() {
      // 预览
      this.$emit('getWorksContent')
      this.$emit('update:showPreviewDialog', true)
    },
    sleep(ms) {
      let temple = new Promise(
        (resolve) => {
          setTimeout(resolve, ms)
        })
      return temple
    },
    getNowFormatDate() {
      var date = new Date()
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      var strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      var currentdate = year + month + strDate
      return currentdate
    },
    exportLongImg() {
      this.loading = this.$loading({
        lock: true,
        text: '长图生成中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      // 生成长图
      let newState = {
        currentState: 'preview'
      }
      if (this.selectedElement) {
        newState.selectedElement = null
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.showStyle('preview')
      let that = this
      this.$nextTick(() => {
        // 线上环境若不加sleep，dom未完全更新
        this.sleep(1).then(() => {
          //克隆preview-panel元素，清除输入为空的placeholder
          let clonePanel = document.querySelector('.preview-panel').cloneNode(true)
          clonePanel.setAttribute('class','preview-panel export-img-template')
          let doms = clonePanel.getElementsByClassName('mTextarea')
          for(let i = 0; i < doms.length; i++) {
            if(!doms[i].innerText){
              doms[i].setAttribute('placeholder','')
            }
          }
          let scrollLeft = 0
          if(document.getElementsByClassName('van-tabs__nav') && document.getElementsByClassName('van-tabs__nav').length > 0){
              scrollLeft = document.getElementsByClassName('van-tabs__nav')[0].scrollLeft;
          }
          // let vanTabDoms = document.getElementsByClassName('van-tabs__nav')
          // for(let i = 0; i < vanTabDoms.length; i++) {
          //   let scrollLeft = vanTabDoms[i].scrollLeft
          //   console.log('scrollLeft',scrollLeft)
          //   // if(!vanTabDoms[i].innerText){
          //   //   vanTabDoms[i].setAttribute('placeholder','')
          //   // }
          // }
          setTimeout(()=>{
            document.querySelector('.export-img-container').appendChild(clonePanel)
            if(document.getElementsByClassName('van-tabs__nav') && document.getElementsByClassName('van-tabs__nav').length > 0){
              document.querySelector('.export-img-container').getElementsByClassName('van-tabs__nav')[0].scrollLeft = scrollLeft
            }
            html2canvas(document.querySelector('.export-img-template'),
              {
                scale: 1,
                async: true,
                logging: false,
                useCORS: true,
                allowTaint: false
              }
            ).then(function (canvas) {
              that.$hMessage.destroy()
              const dataUrl = canvas.toDataURL('image/png', 1)
              downloadFile('作品长图', dataUrl)
              if (window.CMS_CONFIG.logPicToMMS && window.CMS_CONFIG.logPicToMMS == 'true') {
                let fileData = dataURLtoFile(dataUrl, 'image/png')
                let fileName = that.works.works_title + '_' + that.getNowFormatDate() + '.png'
                let file = new File([fileData], fileName + '.png')
                postImageMaterial({
                  file: file,
                  fileName: fileName
                })
              }
            }).finally(() => {
              //清除export-img-container子元素
              const item = document.querySelector('.export-img-container')
              while (item.firstChild) {
                item.removeChild(item.firstChild)
              }
              that.$store.dispatch('cms/editState/updateEditState', {
                currentState: 'edit'
              })
              this.showStyle('edit')
              this.loading.close()
            })
          },0)
        })
      })
    },
    undoOrRedo(flag) {
      if (flag && this.$store.$$canUndo) {
        this.$store.$$undo()
      } else if (!flag && this.$store.$$canRedo) {
        this.$store.$$redo()
      }
    },
    cancelWork() {
      let works_status = this.worksInfo.works_status
      if (works_status === 'D' || works_status === 'C') {
        // 发布后
        this.$hMsgBox.confirm({
          title: '提示',
          content: `是否继续退出？`,
          okText: `退出`,
          cancelText: `不退出`,
          onOk: () => {
            hotkeys.unbind()
            this.$emit('workDetailFlag', false)
            this.$store.dispatch('cms/initWorksState')
            this.$store.$$init()
          }
        })
      } else {
        // 未发布
        this.$hMsgBox.confirm({
          title: '提示',
          content: `是否继续退出？`,
          okText: `退出`,
          cancelText: `不退出`,
          onOk: () => {
            // 保存作品
            // this.saveAsDraft()
            hotkeys.unbind()
            this.$emit('workDetailFlag', false)
            this.$store.dispatch('cms/initWorksState')
            this.$store.$$init()
          }
          // onCancel: () => {
          //   this.$emit('workDetailFlag', false)
          //   this.$store.dispatch('cms/initWorksState')
          //   this.$store.$$init()
          // }
        })
      }
    },
    dataURItoBlob(dataURI) {
      let byteString = window.atob(dataURI.split(',')[1])
      let mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]
      let ab = new ArrayBuffer(byteString.length)
      let ia = new Uint8Array(ab)
      for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i)
      }
      return new Blob([ab], { type: mimeString })
    },
    async saveAction(type) {
      const that = this
      let { cms } = this.$store.state
      let pages = cms.pages.items
      pages && pages.forEach(j => {
        let elements = cms.elements.items[j.uuid]
        if (elements && elements.length) {
          elements.forEach((item) => {
            if (item.name === 'hs-cms-form-radio') {
              item.extra.form_info = {
                'qu_id': item.property.qu_id,
                'qu_stem': item.property.title,
                'qu_desc': item.property.questiondesc,
                'is_required': item.property.required,
                'qu_option': item.property.options,
                'answer_and_score': item.property.answer_and_score,
                'set_score_flag': item.property.set_score_flag,
                'answer_ids': item.property.answer_ids,
                'qu_score': item.property.qu_score
              }
            }
            if (item.name === 'hs-cms-form-checkbox') {
              item.extra.form_info = {
                'qu_id': item.property.qu_id,
                'qu_stem': item.property.title,
                'qu_desc': item.property.questiondesc,
                'is_required': item.property.required,
                'qu_option': item.property.options,
                'answer_and_score': item.property.answer_and_score,
                'set_score_flag': item.property.set_score_flag,
                'answer_ids': item.property.answer_ids,
                'qu_score': item.property.qu_score,
                'min_checked_num': parseInt(item.property.minCheckNumber),
                'max_checked_num': parseInt(item.property.maxCheckNumber)
              }
            }
            if (item.name === 'hs-cms-form-completion') {
              item.extra.form_info = {
                'qu_id': item.property.qu_id,
                'qu_stem': item.property.title,
                'qu_desc': item.property.questiondesc,
                'is_required': item.property.required,
                'qu_hint': item.property.questionhint
              }
            }
            if (item.name === 'hs-cms-form-shortAnswerQuestion') {
              item.extra.form_info = {
                'qu_id': item.property.qu_id,
                'qu_stem': item.property.title,
                'qu_desc': item.property.questiondesc,
                'is_required': item.property.required,
                'qu_hint': item.property.questionhint
              }
            }
            if (item.name === 'hs-cms-form-smsVerifiCode') {
              item.extra.form_info = {
                'qu_id': item.property.qu_id
              }
            }
            if (item.name === 'hs-cms-form-submit') {
              item.extra.form_info = {
                'participate_cycle': item.property.participate_cycle,
                'tries_limit': item.property.tries_limit,
                'limit_flag': item.property.limit_flag,
                'showSubmitResult': item.property.showSubmitResult
              }
            }
            if (item.name === 'hs-cms-video') {
              if (/<\/iframe>/g.test(item.property.videoOutSrc)) {
                item.property.videoOutSrc = encodeURIComponent(item.property.videoOutSrc)
              }
            }
          })
        }
      })
      if (type == 'draft') {
        await that.$store.dispatch('cms/saveWorks', that.worksInfo.works_id)
        hMessage.success({
          duration: 4,
          content: `已保存`
        })
        that.$store.dispatch('cms/initWorksState')
        that.$store.$$init()
        that.$emit('workDetailFlag', true)
      }
      if (type == 'temp') {
        let param = {
          works_id: this.temForm.works_id,
          type: 'system',
          works_title: this.temForm.works_title,
          worksTmplDesc: this.temForm.worksTmplDesc,
          tmplCycleId: this.temForm.tmplCycleId,
          tmplSceneId: this.temForm.tmplSceneId
        }
        await that.$store.dispatch('cms/saveTemplate', param).then(res => {
          hMessage.success({
            duration: 4,
            content: `模板保存成功`
          })
          this.handeleClose()
        })
      }
      if (type == 'publish') {
        let worksInfo = {
          works_id: that.worksInfo.works_id || '',
          works_code: that.worksInfo.works_code || '',
          works_status: that.worksInfo.works_status || '',
          works_sub_status: that.worksInfo.works_sub_status || ''
        }
        await that.$store.dispatch('cms/publishWorks', worksInfo)
        that.$emit('workDetailFlag', false)
        that.$store.dispatch('cms/initWorksState')
        that.$store.$$init()
      }
      if (type == 'start') {
        let works_content = {
          pages: cms.pages.items,
          events: cms.events.items,
          elements: cms.elements.items,
          works: {
            ...cms.works
          }
        }
        // 修改
        putWorks({
          works_content: JSON.stringify(works_content),
          works_id: that.worksInfo.works_id
        }).then(res => {
          if (!res.data.error_info) {
            startWorksEnableProcess({ works_id: that.worksInfo.works_id }).then(res => {
              if (!res.data.error_info) {
                that.$hMessage.info(`${that.worksInfo.works_title}已发起启用流程`)
                that.$emit('workDetailFlag', false)
                that.$store.dispatch('cms/initWorksState')
                that.$store.$$init()
              }
            })
          }
        })
      }
    },
    // 保存草稿
    async saveAsDraft(id) {
      // 1.上传封面
      // 2.上传附件信息
      // 3.保存为草稿
      if (!this.works.works_title) {
        this.$hMessage.info('请输入作品名称')
        return false
      }
      let { cms } = this.$store.state
      let xss = new RegExp(/[<>]/)
      if (xss.test(this.works.works_title)) {
        this.$hMessage.info('作品名称不能输入<或者>')
        return false
      }
      if (!this.checkPageNameRepeat()) {
        this.$hMessage.warning('页面名称不可重复')
        return false
      }
      if(!this.checkEventForm()) return false
      await this.$store.dispatch('cms/editState/updateEditState', { selectedPage: cms.pages.items[0].uuid, selectedElement: null })
      let newState = {
        currentState: 'preview'
      }
      if (this.selectedElement) {
        newState.selectedElement = null
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.showStyle('preview')
      this.loading = this.$loading({
        lock: true,
        text: '保存中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      let that = this
      this.$nextTick(() => {
        this.sleep(100).then(() => {
          const element = document.querySelector('.preview-panel')
          setTimeout(() => {
            html2canvas(element,
              {
                useCORS: true,
                allowTaint: false,
                async: true,
                scale: 1,
                logging: false
              }
            ).then(async canvas => {
              const dataUrl = canvas.toDataURL('image/png')
              let fileData = dataURLtoFile(dataUrl, 'image/png')
              let file = new File([fileData], generateUID() + '.png')
              that.$store.dispatch('cms/editState/updateEditState', {
                currentState: 'edit'
              })
              // 如果对接阿里云就调取阿里云sdk，反之则调取接口上传
              if (this.useAliyun) {
                if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
                  if (!this.uploader) {
                    this.uploader = await this.createUploader()
                  }
                  this.saveType = 'draft'
                  this.imgUploadChange(file)
                }
              } else {
                uploadFilePlt({
                  file: file
                }).then(async res => {
                  if (res.data.file_guid) {
                    let obj = res.data
                    Object.assign(obj, { file_type: 1 })
                    if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
                      that.$store.dispatch('cms/works/updateWorks', {
                        video_poster_path: '/' + res.data.file_group + '/' + res.data.file_path
                      })
                    } else {
                      that.$store.dispatch('cms/works/updateWorks', {
                        video_poster_path: res.data.file_guid
                      })
                    }
                    postMaterialFile(obj).then(res => {
                      this.loading.close()
                    })
                  }
                  this.saveAction('draft')
                })
              }
            }).finally(() => {
              // this.$hMessage.destroy()
              this.showStyle('edit')
            })
          }, 100)
        })
      })
      // 保存为草稿
    },

    checkPageNameRepeat() {
      let { cms } = this.$store.state
      console.log('cms.pages.items', cms.pages.items)
      let names = cms.pages.items.map(item => item.name)
      let flag = true
      for (let k = 0; k < names.length; k++) {
        for (let n = k + 1; n < names.length; n++) {
          if (names[k] == names[n]) {
            flag = false
          }
        }
      }
      return flag
    },
    checkEventForm(){
      let { cms } = this.$store.state
      console.log('events', cms.events)
      let eventItems = cms.events.items
      let checkResult = true
      try {
        for(let uuid in eventItems){
          let events = eventItems[uuid]
          events.forEach( e =>{
            if(e.result.value === 'shareEvent'){
              let { share_event_content, share_event_img_url, share_event_page_url, share_event_title, share_event_url_type } = e.result.params
              if(!share_event_content || !share_event_img_url || (share_event_url_type === 'custom_url' && !share_event_page_url)){
                checkResult = false
                throw new Error('唤起分享交互事件必填项未填，请补充后再提交')
              }
              if(!share_event_title){
                this.$store.dispatch('cms/events/updateEvents', {
                  pageUuid: uuid,
                  uuid: e.uuid,
                  result: {
                    params: {
                      share_event_title: this.works.works_title.length > 20 ? this.works.works_title.substr(0, 20) : this.works.works_title
                    }
                  }
                })
              }
            }
          })  
        }
      } catch(e){
        this.$hMessage.error(e.message)
      }
      return checkResult
    },
    submitEdit(id) {
      getSysWorksTmplClassList().then(res => {
        let categories = res.data.rows
        this.temlateEidtMsg = true
        this.temForm.works_id = id
        this.temForm.works_title = this.works.works_title
        for (let i = 0; i < categories.length; i++) {
          if (categories[i].type_code === '1') {
            this.cycleList = categories[i].data
          } else if (categories[i].type_code === '2') {
            this.sceneList = categories[i].data
          }
        }
      }).finally(() => {
        this.loading = false
      })
    },
    handeleClose() {
      this.temForm.works_title = ''
      this.temForm.works_id = ''
      this.temForm.worksTmplDesc = ''
      this.temForm.tmplCycleId = ''
      this.temForm.tmplSceneId = ''
      this.cycleList = []
      this.sceneList = []
      this.$refs.classTypeSetFrom.resetFields()
      this.temlateEidtMsg = false
    },
    handleSaveTemplate() {
      this.$refs.classTypeSetFrom.validate((valid) => {
        if (valid) {
          this.saveAsTemplate()
        }
      })
    },
    // 另存为模板
    async saveAsTemplate() {
      // 1.上传封面
      // 2.上传附件信息
      // 3.保存为模板
      this.temLoadState = true

      if (!this.checkPageNameRepeat()) {
        this.$hMessage.warning('页面名称不可重复')
        return false
      }
      if(!this.checkEventForm()) return false
      // this.works.works_title = this.temForm.works_title
      let { cms } = this.$store.state
      let xss = new RegExp(/[<>]/)
      if (xss.test(this.works.works_title)) {
        this.$hMessage.info('模板名称不能输入<或者>')
        this.temLoadState = false
        return false
      }
      await this.$store.dispatch('cms/editState/updateEditState', { selectedPage: cms.pages.items[0].uuid, selectedElement: null })
      let newState = {
        currentState: 'preview',
        selectedElement: null
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.showStyle('preview')
      this.loading = this.$loading({
        lock: true,
        text: '保存中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      let that = this
      this.$nextTick(() => {
        this.sleep(1).then(() => {
          html2canvas(document.querySelector('.preview-panel'),
            {
              useCORS: true,
              allowTaint: false,
              async: true,
              scale: 1,
              logging: false
            }
          ).then(async canvas => {
            const dataUrl = canvas.toDataURL('image/png')
            let fileData = dataURLtoFile(dataUrl, 'image/png')
            let file = new File([fileData], generateUID() + '.png')
            that.$store.dispatch('cms/editState/updateEditState', {
              currentState: 'edit'
            })
            // 如果对接阿里云就调取阿里云sdk，反之则调取接口上传
            if (this.useAliyun) {
              if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
                if (!this.uploader) {
                  this.uploader = await this.createUploader()
                }
                this.saveType = 'temp'
                this.imgUploadChange(file)
              }
            } else {
              uploadFilePlt({
                file: file
              }).then(async res => {
                if (res.data.file_guid) {
                  let obj = res.data
                  Object.assign(obj, { file_type: 1 })
                  if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
                    that.$store.dispatch('cms/works/updateWorks', {
                      video_poster_path: '/' + res.data.file_group + '/' + res.data.file_path
                    })
                  } else {
                    that.$store.dispatch('cms/works/updateWorks', {
                      video_poster_path: res.data.file_guid
                    })
                  }
                  postMaterialFile(obj).then(res => {
                    this.loading.close()
                  })
                }
                this.saveAction('temp')
              })
            }
          }).finally(() => {
            // this.$hMessage.destroy()
            this.temLoadState = false
            this.showStyle('edit')
          })
        })
      })
    },
    publishWork() {
      // 1.上传封面
      // 2.上传信息
      // 3.提交审核
      if (!this.works.works_title) {
        this.$hMessage.info('请输入作品名称')
        return false
      }
      let xss = new RegExp(/[<>]/)
      if (xss.test(this.works.works_title)) {
        this.$hMessage.info('作品名称不能输入<>')
        return false
      }
      if (!this.checkPageNameRepeat()) {
        this.$hMessage.warning('页面名称不可重复')
        return false
      }
      //
      if (this.works.need_login === '1' && !this.works.works_login_user_type) {
        this.$hMessage.error('登录校验方式不能为空')
        return false
      }
      if(!this.checkEventForm()) return false
      let passValidate = true
      let hasFormWidget = false
      let hasFormSubmit = false
      // let name = []
      // let passValidateTips = ''
      let { cms } = this.$store.state

      if (!worksAdminVlidate(cms)) {
        return false
      }
      const pageList = cms.pages.items
      let unCheckPassCom = []
      let unCheckPassPage = []
      for (let i = 0; i < pageList.length; i++) {
        const item = pageList[i]
        let elements = cms.elements.items[item.uuid]
        unCheckPassCom = []
        if (elements) {
          const formComList = ['hs-cms-form-radio', 'hs-cms-form-checkbox', 'hs-cms-form-completion', 'hs-cms-form-shortAnswerQuestion','hs-cms-form-smsVerifiCode']
          const submitCom = 'hs-cms-form-submit'
          for (let i = 0; i < elements.length; i++) {
            const comItem = elements[i]
            if (formComList.includes(comItem.name)) {
              hasFormWidget = true
            }
            if (comItem.name == submitCom) {
              hasFormSubmit = true
            }
            if (comItem.property && comItem.property.passCheck) {
              if (!comItem.property.passCheck()) {
                passValidate = false
                unCheckPassCom.push(comItem)
                if (comItem.property.passValidate) {
                  comItem._tipsList = []
                  for (let k in comItem.property.passValidate) {
                    if (comItem.property.passValidate[k] == false) {
                      passValidate = false
                      if (comItem.property.passValidateTips && comItem.property.passValidateTips[k]) {
                        comItem._tipsList.push(comItem.property.passValidateTips[k])
                      }
                      break
                    }
                  }
                }
                break
              }
            }
            if (comItem.property && comItem.property.passValidate) {
              comItem._tipsList = []
              for (let k in comItem.property.passValidate) {
                if (comItem.property.passValidate[k] == false) {
                  passValidate = false
                  if (comItem.property.passValidateTips && comItem.property.passValidateTips[k]) {
                    comItem._tipsList.push(comItem.property.passValidateTips[k])
                  }
                  unCheckPassCom.push(comItem)
                  break
                }
              }
            }
          }
          if (!passValidate) {
            unCheckPassPage.push({
              pageName: item.name,
              unCheckPassCom: unCheckPassCom
            })
            console.log(unCheckPassPage, 'unCheckPassPage')
            // 若存在一个页面校验不通过则终止循环
            break
          }
        }
      }
      if (unCheckPassPage.length) {
        const tipsInfo = unCheckPassPage[0]
        let unCheckPassCom = tipsInfo.unCheckPassCom
        const pageName = tipsInfo.pageName
        if (unCheckPassCom.length) {
          let comTips = ''
          let cusTips = []
          unCheckPassCom.forEach(item => {
            if (item._tipsList && item._tipsList.length) {
              let tip = ''
              item._tipsList.forEach(passValidateTipsItem => {
                tip += `${passValidateTipsItem}`
              })
              cusTips.push(tip)
            }
            comTips += `【${item.element_name}】`
          })
          cusTips = cusTips.join(',')
          this.$hMessage.error({
            closable: true,
            customerClass: 'msgContent',
            content: `<span class="msgContent_title">${comTips} </span> <p class="msgContent_content">${cusTips}</p>`, // `${pageName} ${comTips}未配置完成，${cusTips} 请完成配置后再发布`,
            duration: 5
          })
          return false
        }
      }
      if (hasFormWidget && !hasFormSubmit) {
        this.$hMessage.error({
          closable: true,
          content: `作品仅有表单，请添加表单提交按钮`,
          duration: 5
        })
        return false
      }
      if (!hasFormWidget && hasFormSubmit) {
        this.$hMessage.error({
          closable: true,
          content: `作品仅有表单提交按钮，请添加表单`,
          duration: 5
        })
        return false
      }
      // cms.pages.items.forEach(item => {
      //   let elements = cms.elements.items[item.uuid]
      //   let flag = true
      //   elements && elements.forEach((j) => {
      //     if (j.property && j.property.passCheck) {
      //       if (!j.property.passCheck()) {
      //         passValidate = false
      //         flag = false
      //       }
      //     }
      //     if (j.property && j.property.passValidate) {
      //       for (var k in j.property.passValidate) {
      //         if (j.property.passValidate[k] == false) {
      //           passValidate = false
      //           flag = false
      //           passValidateTips = j.property.passValidateTips[k]
      //         }
      //       }
      //     }
      //     if (j.name == 'hs-cms-form-radio' || j.name == 'hs-cms-form-checkbox' || j.name == 'hs-cms-form-completion' || j.name == 'hs-cms-form-shortAnswerQuestion') {
      //       hasFormWidget = true
      //     }
      //     if (j.name == 'hs-cms-form-submit') {
      //       hasFormSubmit = true
      //     }
      //   })
      //   if (!flag) {
      //     name.push(item.name)
      //   }
      // })
      // if (!passValidate) {
      //   if (name.length == 1) {
      //     const tips = passValidateTips || '有组件'
      //     this.$hMessage.error(`${name[0]}【${tips}】未配置完成，请完成【${tips}】配置后再发布`)
      //   } else {
      //     this.$hMessage.error(`有多个页面有组件未配置完成，请完成组件配置后再发布`)
      //   }
      //   return false
      // }
      // if (hasFormWidget && !hasFormSubmit) {
      //   this.$hMessage.error('作品仅有表单，请添加表单提交按钮')
      //   return false
      // }
      // if (!hasFormWidget && hasFormSubmit) {
      //   this.$hMessage.error('作品仅有表单提交按钮，请添加表单')
      //   return false
      // }
      this.$hMsgBox.confirm({
        title: '提示',
        closable: true,
        content: `进入审核后不可修改，是否继续提交?`,
        onOk: async () => {
          await this.$store.dispatch('cms/editState/updateEditState', { selectedPage: cms.pages.items[0].uuid, selectedElement: null })
          let newState = {
            currentState: 'preview'
          }
          if (this.selectedElement) {
            newState.selectedElement = null
          }
          this.$store.dispatch('cms/editState/updateEditState', newState)
          let that = this
          this.loading = this.$loading({
            lock: true,
            text: '提交中...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })
          this.showStyle('preview')
          this.$nextTick(() => {
            this.sleep(1).then(() => {
              html2canvas(document.querySelector('.preview-panel'),
                {
                  useCORS: true,
                  allowTaint: false,
                  async: true,
                  scale: 1,
                  logging: false,
                  proxy: '*'
                }
              ).then(async canvas => {
                const dataUrl = canvas.toDataURL('image/png')
                let fileData = dataURLtoFile(dataUrl, 'image/png')
                let file = new File([fileData], generateUID() + '.png')

                that.$store.dispatch('cms/editState/updateEditState', {
                  currentState: 'edit'
                })
                // 如果对接阿里云就调取阿里云sdk，反之则调取接口上传
                if (this.useAliyun) {
                  if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
                    if (!this.uploader) {
                      this.uploader = await this.createUploader()
                    }
                    this.saveType = 'publish'
                    this.imgUploadChange(file)
                  }
                } else {
                  uploadFilePlt({
                    file: file
                  }).then(async res => {
                    if (res.data.file_guid) {
                      if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
                        that.$store.dispatch('cms/works/updateWorks', {
                          video_poster_path: '/' + res.data.file_group + '/' + res.data.file_path
                        })
                      } else {
                        that.$store.dispatch('cms/works/updateWorks', {
                          video_poster_path: res.data.file_guid
                        })
                      }
                      let obj = res.data
                      Object.assign(obj, { file_type: 1 })
                      postMaterialFile(obj).then(res => {
                        this.loading.close()
                      })
                    }
                    this.saveAction('publish')
                  })
                }
              }).finally(() => {
                // this.$hMessage.destroy()
                this.showStyle('edit')
              })
            })
          })
        }
      })
    },
    showStyle(type) {
      let elems = document.querySelectorAll('.hotspot-edit-content')
      var index = 0, length = elems.length
      if (type === 'edit') {
        for (; index < length; index++) {
          elems[index].style.background = 'rgba(181, 255, 178, 0.3)'
        }
      } else {
        for (; index < length; index++) {
          elems[index].style.background = 'transparent'
          console.log(elems[index].style)
        }
      }
    },
    // 发起作品启用流程
    startWorksEnableProcess(status) {
      if (status !== 'E') {
        return
      }
      let { cms } = this.$store.state
      let name = []
      let passValidate = true
      let hasFormWidget = false
      let hasFormSubmit = false
      cms.pages.items.forEach(item => {
        let elements = cms.elements.items[item.uuid]
        elements && elements.forEach((j) => {
          if (j.property && j.property.passValidate) {
            for (var k in j.property.passValidate) {
              if (j.property.passValidate[k] == false) {
                passValidate = false
                name.push(item.name)
              }
            }
          }
          if (j.name == 'hs-cms-form-radio' || j.name == 'hs-cms-form-checkbox' || j.name == 'hs-cms-form-completion' || j.name == 'hs-cms-form-shortAnswerQuestion') {
            hasFormWidget = true
          }
          if (j.name == 'hs-cms-form-submit') {
            hasFormSubmit = true
          }
        })
      })
      if (!passValidate) {
        if (name.length == 1) {
          this.$hMessage.error(`${name[0]}有组件未配置完成，请完成组件配置后再发布`)
        } else {
          this.$hMessage.error(`有多个页面有组件未配置完成，请完成组件配置后再发布`)
        }
        return false
      }
      if (hasFormWidget && !hasFormSubmit) {
        this.$hMessage.error('有页面仅有表单，请添加表单提交按钮')
        return false
      }
      if (!hasFormWidget && hasFormSubmit) {
        this.$hMessage.error('有页面仅有表单提交按钮无表单，请添加表单')
        return false
      }
      let newState = {
        currentState: 'preview',
        selectedElement: null
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.showStyle('preview')
      this.loading = this.$loading({
        lock: true,
        text: '启用中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      let that = this
      that.$nextTick(() => {
        that.sleep(2).then(() => {
          html2canvas(document.querySelector('.preview-panel'),
            {
              useCORS: true,
              allowTaint: false,
              async: true,
              scale: 1,
              logging: false,
              proxy: '*'
            }
          ).then(async canvas => {
            const dataUrl = canvas.toDataURL('image/png')
            let fileData = dataURLtoFile(dataUrl, 'image/png')
            let file = new File([fileData], generateUID() + '.png')
            that.$store.dispatch('cms/editState/updateEditState', {
              currentState: 'edit'
            })
            // 如果对接阿里云就调取阿里云sdk，反之则调取接口上传
            if (this.useAliyun) {
              if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
                if (!this.uploader) {
                  this.uploader = await this.createUploader()
                }
                this.saveType = 'start'
                this.imgUploadChange(file)
              }
            } else {
              uploadFilePlt({
                file: file
              }).then(async res => {
                if (res.data.file_guid) {
                  let obj = res.data
                  Object.assign(obj, { file_type: 1 })
                  if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
                    that.$store.dispatch('cms/works/updateWorks', {
                      video_poster_path: '/' + res.data.file_group + '/' + res.data.file_path
                    })
                  } else {
                    that.$store.dispatch('cms/works/updateWorks', {
                      video_poster_path: res.data.file_guid
                    })
                  }
                  postMaterialFile(obj).then(res => {
                    this.loading.close()
                  })
                }
                this.saveAction('start')
              })
            }
          }).finally(() => {
            // this.$hMessage.destroy()
            this.showStyle('edit')
          })
        })
      })
    },
    async imgUploadChange(file) {
      const userData = '{"Vod":{}}'
      await this.uploader.addFile(file, null, null, null, userData)
      setTimeout(() => {
        this.uploader.startUpload()
      }, 500)
    },
    /**
     * 创建一个Ali云上传对象
     * 使用 UploadAuth 上传方式
     */
    createUploader() {
      let self = this
      let uploader = new AliyunUpload.Vod({
        timeout: 60000,
        partSize: 1048576,
        parallel: 5,
        retryCount: 3,
        retryDuration: 2,
        region: self.aliregion,
        userId: self.aliuserId,
        // 添加文件成功
        addFileSuccess: function (uploadInfo) {
        },
        // 开始上传
        onUploadstarted: async function (uploadInfo) {
          let uploadTitile = uploadInfo.file.name.substring(0, uploadInfo.file.name.indexOf('.'))
          let image_extion = uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1)
          const createUrl = window.CMS_CONFIG.CCM_URL + '/public/getImageUploadPassport'
          const res = await apiPost({ title: uploadTitile, image_ext: image_extion }, createUrl)
          const uploadAuth = res.data.upload_auth
          const uploadAddress = res.data.upload_address
          const image_id = res.data.image_id
          uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, image_id)
        },
        // 文件上传成功
        onUploadSucceed: async function (uploadInfo) {
          let obj = {
            'file_path': uploadInfo.object,
            'file_name': uploadInfo.file.name,
            'file_guid': uploadInfo.videoId,
            'file_size': uploadInfo.file.size,
            'file_extension': uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1),
            'file_type': 1
          }
          let imgUrl = ''
          if (window.CMS_CONFIG.USE_ALIYUN_PROXY && window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true') {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              imgUrl = window.CMS_CONFIG.ALIYUN_DOMAIN + '/' + uploadInfo.object
            } else {
              imgUrl = window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS + '/' + uploadInfo.bucket + '.' + uploadInfo.endpoint.substring(8) + '/' + uploadInfo.object
            }
          } else {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              imgUrl = window.CMS_CONFIG.ALIYUN_DOMAIN + '/' + uploadInfo.object
            } else {
              imgUrl = 'https://' + uploadInfo.bucket + '.' + uploadInfo.endpoint.substring(8) + '/' + uploadInfo.object
            }
          }
          await self.$store.dispatch('cms/works/updateWorks', {
            video_poster_path: imgUrl
          })
          postMaterialFile(obj).then(res => {
            self.loading.close()
            if (self.saveType == 'draft') {
              self.saveAction('draft')
            }
            if (self.saveType == 'temp') {
              self.saveAction('temp')
            }
            if (self.saveType == 'publish') {
              self.saveAction('publish')
            }
            if (self.saveType == 'start') {
              self.saveAction('start')
            }
          })
        },
        // 文件上传失败
        onUploadFailed: function (uploadInfo, code, message) {
          self.$hMessage.error(message)
        },
        // 取消文件上传
        onUploadCanceled: function (uploadInfo, code, message) {
        },
        // 上传凭证超时
        onUploadTokenExpired: async function (uploadInfo) {
          const refreshUrl = window.CMS_CONFIG.CCM_URL + '/public/refreshVodUploadPassport'
          const res = await apiPost({ video_id: uploadInfo.videoId }, refreshUrl)
          const uploadAuth = res.data.upload_auth
          uploader.resumeUploadWithAuth(uploadAuth)
        },
        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function (uploadInfo, totalSize, progress) {
        },
        // 全部文件上传结束,
        onUploadEnd: function (uploadInfo) {
        }
      })
      return uploader
    }
  }
  // mounted() {
  //   optaionBtns.map(btnInfo => {
  //     btnInfo.hotkey && hotkeys(btnInfo.hotkey, { splitKey: '&' }, (event, handler) => {
  //       event.preventDefault()
  //       event.stopPropagation()
  //       btnInfo.action && btnInfo.action.call(this)
  //     })
  //   })
  // },
  // activated() {
  //   optaionBtns.map(btnInfo => {
  //     btnInfo.hotkey && hotkeys(btnInfo.hotkey, { splitKey: '&' }, (event, handler) => {
  //       event.preventDefault()
  //       event.stopPropagation()
  //       btnInfo.action && btnInfo.action.call(this)
  //     })
  //   })
  // },
  // deactivated() {
  //   hotkeys.unbind()
  // },
  // beforeDestroy() {
  //   hotkeys.unbind()
  // }
}
</script>

<style lang="scss" scoped>
.export-img-container{
  height: 0;
  width: 0;
  position: absolute;
  opacity: 0;
}
.scaleGroup {
  margin: 0 20px;
  display: flex;
  align-items: center;
  .h-icon {
    cursor: pointer;
  }
}
.single-button-wrap {
  width: 60px;
  height: 59px;
  text-align: center;
  img {
    width: 24px;
    height: 24px;
    margin-top: 18px;
  }
  object {
    width: 24px;
    height: 24px;
    margin-top: 9px;
  }
}
.single-button-wrap-normal {
  &:hover {
    background-color: #037df3;
    color: #fff;
    cursor: pointer;
  }
  object {
    &:hover {
      color: #fff;
      cursor: pointer;
    }
  }
}
.single-button-wrap-disabled {
  background: #ffffff;
  color: #cccccc;
  svg g polyline {
    color: red;
  }
  &:hover {
    cursor: not-allowed;
  }
  object {
    &:hover {
      cursor: not-allowed;
    }
  }
}
/deep/ .el-dialog {
  width: 417px;
}
</style>
