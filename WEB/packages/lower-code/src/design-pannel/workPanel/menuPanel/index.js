import './index.scss'

import { mapGetters, mapState, mapActions } from 'vuex'
import { accAdd, accSub, downloadFile, dataURLtoFile } from '@Utils/utils'
import { startWorksEnableProcess, putWorks } from '@Apis/works.js'
import hotkeys from 'hotkeys-js'
import optaionBtns from './options'

import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
// eslint-disable-next-line no-unused-vars
import * as upload from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
import { apiPost } from '@Utils/request'
import { isDev, generateUID } from '@h5Designer/utils'
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
import { uploadFilePlt, postMaterialFile } from '@Apis/common'
import hMessage from 'h_ui/dist/lib/Message'

window.OSS = OSS

export default {
  props: {
    worksInfo: {
      type: Object,
      default: () => {},
    },
  },
  components: {},
  data() {
    return {
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
      useAliyun:
        window.CMS_CONFIG.USE_ALIYUN &&
        window.CMS_CONFIG.USE_ALIYUN == 'true' &&
        (window.CMS_CONFIG.ALIYUN_TYPE == 'all' || window.CMS_CONFIG.ALIYUN_TYPE == 'video'),
      aliregion: window.CMS_CONFIG.Ali_region,
      aliuserId: window.CMS_CONFIG.Ali_userId,
      loading: null,
      castratedVersion: window.CMS_CONFIG.CASTRATED_VERSION && window.CMS_CONFIG.CASTRATED_VERSION == 'true', // 是否打开Lite版
    }
  },
  computed: {
    ...mapState('cms/common', ['isFullscreen']),
    ...mapState('cms/works', {
      works: (state) => state,
    }),
    ...mapState('cms/pages', {
      pages: (state) => state.items,
    }),
    ...mapState('cms/editState', ['scaleRate', 'scalePercent', 'selectedElement']),
    ...mapGetters('cms/editState', ['scalePercent']),
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
        },
      })
    },
    draftText() {
      if (this.worksInfo.works_status === 'E') {
        return '保存并退出'
      } else {
        return '保存并退出'
      }
    },
  },
  methods: {
    ...mapActions('cms/common', ['toggleFullscreen']),
    updateWorks(key, value) {
      this.$store.dispatch('cms/works/updateWorks', {
        [key]: value,
      })
    },
    changeScale(name) {
      if (name === '') return
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: (Number(parseFloat(name)) / 100).toFixed(2),
      })
    },
    onVisibleChange() {
      // this.$refs['input'].focus()
      // this.$nextTick(() => {
      //   console.log(this.$refs)
      //   this.$refs['input'].value = '60%'
      // })
    },
    handleZoomIn() {
      let newScaleRate = this.scaleRate
      if (newScaleRate > 0.2) {
        newScaleRate = accSub(newScaleRate, 0.1)
      }
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: newScaleRate,
      })
    },
    h5preview() {
      let { cms } = this.$store.state
      let previewJSON = {
        pages: cms.pages.items,
        elements: cms.elements.items,
        events: cms.events.items,
        works: cms.works,
        works_id: this.worksInfo.works_id,
      }
      window.sessionStorage.setItem('cms-h5', JSON.stringify(previewJSON))
      window.open('/cms-h5.html?preview=true', '_blank')
    },
    handleZoomOn() {
      let newScaleRate = this.scaleRate
      if (newScaleRate < 4) {
        newScaleRate = accAdd(newScaleRate, 0.1)
      }
      this.$store.dispatch('cms/editState/updateEditState', {
        scaleRate: newScaleRate,
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
      let temple = new Promise((resolve) => {
        setTimeout(resolve, ms)
      })
      return temple
    },
    exportLongImg() {
      this.loading = this.$loading({
        lock: true,
        text: '长图生成中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      // 生成长图
      let newState = {
        currentState: 'preview',
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
          html2canvas(document.querySelector('.preview-panel'), {
            scale: 1,
            async: true,
            logging: false,
            useCORS: true,
            allowTaint: false,
          })
            .then(function (canvas) {
              that.$hMessage.destroy()
              const dataUrl = canvas.toDataURL('image/png', 1)
              // const blob = dataURItoBlob(dataUrl)
              // const file = new window.File([blob], fileName, { type: 'image/png' })
              downloadFile('作品长图', dataUrl)
              // that.isShowLongImg = true
              // that.imgsrc = dataUrl
            })
            .finally(() => {
              that.$store.dispatch('cms/editState/updateEditState', {
                currentState: 'edit',
              })
              this.showStyle('edit')
              this.loading.close()
            })
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
        this.$hMsgBox.confirm({
          title: '提示',
          content: `是否继续退出？`,
          okText: `退出`,
          cancelText: `不退出`,
          onOk: () => {
            this.$emit('workDetailFlag', false)
            this.$store.dispatch('cms/initWorksState')
            this.$store.$$init()
          },
        })
      } else {
        this.$hMsgBox.confirm({
          title: '提示',
          content: `是否继续退出？`,
          okText: `退出`,
          cancelText: `不退出`,
          onOk: () => {
            // 保存作品
            // this.saveAsDraft()
            this.$emit('workDetailFlag', false)
            this.$store.dispatch('cms/initWorksState')
            this.$store.$$init()
          },
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
    async saveAction(type, title) {
      const that = this
      let { cms } = this.$store.state
      let pages = cms.pages.items
      pages &&
        pages.forEach((j) => {
          let elements = cms.elements.items[j.uuid]
          if (elements && elements.length) {
            elements.forEach((item) => {
              if (item.name === 'hs-cms-form-radio') {
                item.extra.form_info = {
                  qu_id: item.property.qu_id,
                  qu_stem: item.property.title,
                  qu_desc: item.property.questiondesc,
                  is_required: item.property.required ? '1' : '0',
                  qu_option: item.property.options,
                }
              }
              if (item.name === 'hs-cms-form-checkbox') {
                item.extra.form_info = {
                  qu_id: item.property.qu_id,
                  qu_stem: item.property.title,
                  qu_desc: item.property.questiondesc,
                  is_required: item.property.required ? '1' : '0',
                  qu_option: item.property.options,
                  min_checked_num: parseInt(item.property.minCheckNumber),
                  max_checked_num: parseInt(item.property.maxCheckNumber),
                }
              }
              if (item.name === 'hs-cms-form-completion') {
                item.extra.form_info = {
                  qu_id: item.property.qu_id,
                  qu_stem: item.property.title,
                  qu_desc: item.property.questiondesc,
                  is_required: item.property.required ? '1' : '0',
                  qu_hint: item.property.questionhint,
                }
              }
              if (item.name === 'hs-cms-form-smsVerifiCode') {
                item.extra.form_info = {
                  qu_id: item.property.qu_id
                }
              }
            })
          }
        })
      if (type == 'draft') {
        that.$store.dispatch('cms/saveWorks', that.worksInfo.works_id)
        hMessage.success({
          duration: 4,
          content: `已保存`,
        })
        that.$store.dispatch('cms/initWorksState')
        that.$store.$$init()
        that.$emit('workDetailFlag', true)
      }
      if (type == 'temp') {
        that.$store.dispatch('cms/saveTemplate', { works_id: that.worksInfo.works_id, type: 'system' })
        hMessage.success({
          duration: 4,
          content: `模板保存成功`,
        })
      }
      if (type == 'publish') {
        let worksInfo = {
          works_id: that.worksInfo.works_id || '',
          works_code: that.worksInfo.works_code || '',
          works_status: that.worksInfo.works_status || '',
          works_sub_status: that.worksInfo.works_sub_status || '',
        }
        that.$store.dispatch('cms/publishWorks', worksInfo)
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
            ...cms.works,
          },
        }
        // 修改
        putWorks({
          works_content: JSON.stringify(works_content),
          works_id: that.worksInfo.works_id,
        }).then((res) => {
          if (!res.data.error_info) {
            startWorksEnableProcess({ works_id: that.worksInfo.works_id }).then((res) => {
              if (!res.data.error_info) {
                that.$hMessage.info(`${title}已发起启用流程`)
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
      await this.$store.dispatch('cms/editState/updateEditState', {
        selectedPage: cms.pages.items[0].uuid,
        selectedElement: null,
      })
      let newState = {
        currentState: 'preview',
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
        background: 'rgba(0, 0, 0, 0.7)',
      })
      let that = this
      this.$nextTick(() => {
        this.sleep(100).then(() => {
          const element = document.querySelector('.preview-panel')
          setTimeout(() => {
            html2canvas(element, {
              useCORS: true,
              allowTaint: false,
              async: true,
              scale: 1,
              logging: false,
            })
              .then(async (canvas) => {
                const dataUrl = canvas.toDataURL('image/png')
                let fileData = dataURLtoFile(dataUrl, 'image/png')
                let file = new File([fileData], generateUID() + '.png')
                that.$store.dispatch('cms/editState/updateEditState', {
                  currentState: 'edit',
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
                    file: file,
                  }).then(async (res) => {
                    if (res.data.file_guid) {
                      let obj = res.data
                      Object.assign(obj, { file_type: 1 })
                      that.$store.dispatch('cms/works/updateWorks', {
                        video_poster_path: res.data.file_guid,
                      })
                      postMaterialFile(obj).then((res) => {
                        this.loading.close()
                      })
                    }
                    this.saveAction('draft')
                  })
                }
              })
              .finally(() => {
                this.$hMessage.destroy()
                this.showStyle('edit')
              })
          }, 100)
        })
      })
      // 保存为草稿
    },
    // 另存为模板
    async saveAsTemplate(id) {
      // 1.上传封面
      // 2.上传附件信息
      // 3.保存为模板
      if (!this.works.works_title) {
        this.$hMessage.info('请输入模板名称')
        return false
      }
      let { cms } = this.$store.state
      let xss = new RegExp(/[<>]/)
      if (xss.test(this.works.works_title)) {
        this.$hMessage.info('模板名称不能输入<或者>')
        return false
      }
      await this.$store.dispatch('cms/editState/updateEditState', {
        selectedPage: cms.pages.items[0].uuid,
        selectedElement: null,
      })
      let newState = {
        currentState: 'preview',
        selectedElement: null,
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.showStyle('preview')
      this.loading = this.$loading({
        lock: true,
        text: '保存中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      let that = this
      this.$nextTick(() => {
        this.sleep(1).then(() => {
          html2canvas(document.querySelector('.preview-panel'), {
            useCORS: true,
            allowTaint: false,
            async: true,
            scale: 1,
            logging: false,
          })
            .then(async (canvas) => {
              const dataUrl = canvas.toDataURL('image/png')
              let fileData = dataURLtoFile(dataUrl, 'image/png')
              let file = new File([fileData], generateUID() + '.png')
              that.$store.dispatch('cms/editState/updateEditState', {
                currentState: 'edit',
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
                  file: file,
                }).then(async (res) => {
                  if (res.data.file_guid) {
                    let obj = res.data
                    Object.assign(obj, { file_type: 1 })
                    that.$store.dispatch('cms/works/updateWorks', {
                      video_poster_path: res.data.file_guid,
                    })
                    postMaterialFile(obj).then((res) => {
                      this.loading.close()
                    })
                  }
                  this.saveAction('temp')
                })
              }
            })
            .finally(() => {
              this.$hMessage.destroy()
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
      let passValidate = true
      let hasFormWidget = false
      let hasFormSubmit = false
      let name = []
      let passValidateTips = ''
      let { cms } = this.$store.state
      cms.pages.items.forEach((item) => {
        let elements = cms.elements.items[item.uuid]
        let flag = true
        elements &&
          elements.forEach((j) => {
            if (j.property && j.property.passValidate) {
              for (var k in j.property.passValidate) {
                if (j.property.passValidate[k] == false) {
                  passValidate = false
                  flag = false
                  passValidateTips = j.property.passValidateTips[k]
                }
              }
            }
            if (
              j.name == 'hs-cms-form-radio' ||
              j.name == 'hs-cms-form-checkbox' ||
              j.name == 'hs-cms-form-completion'
            ) {
              hasFormWidget = true
            }
            if (j.name == 'hs-cms-form-submit') {
              hasFormSubmit = true
            }
          })
        if (!flag) {
          name.push(item.name)
        }
      })
      if (!passValidate) {
        console.log(passValidateTips, 'passValidateTips')
        if (name.length == 1) {
          const tips = passValidateTips || '有组件'
          this.$hMessage.error(`${name[0]}【${tips}】未配置完成，请完成【${tips}】配置后再发布`)
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
      this.$hMsgBox.confirm({
        title: '提示',
        closable: true,
        content: `进入审核后不可修改，是否继续提交?`,
        onOk: async () => {
          await this.$store.dispatch('cms/editState/updateEditState', {
            selectedPage: cms.pages.items[0].uuid,
            selectedElement: null,
          })
          let newState = {
            currentState: 'preview',
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
            background: 'rgba(0, 0, 0, 0.7)',
          })
          this.showStyle('preview')
          this.$nextTick(() => {
            this.sleep(1).then(() => {
              html2canvas(document.querySelector('.preview-panel'), {
                useCORS: true,
                allowTaint: false,
                async: true,
                scale: 1,
                logging: false,
                proxy: '*',
              })
                .then(async (canvas) => {
                  const dataUrl = canvas.toDataURL('image/png')
                  let fileData = dataURLtoFile(dataUrl, 'image/png')
                  let file = new File([fileData], generateUID() + '.png')

                  that.$store.dispatch('cms/editState/updateEditState', {
                    currentState: 'edit',
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
                      file: file,
                    }).then(async (res) => {
                      if (res.data.file_guid) {
                        that.$store.dispatch('cms/works/updateWorks', {
                          video_poster_path: res.data.file_guid,
                        })
                        let obj = res.data
                        Object.assign(obj, { file_type: 1 })
                        postMaterialFile(obj).then((res) => {
                          this.loading.close()
                        })
                      }
                      this.saveAction('publish')
                    })
                  }
                })
                .finally(() => {
                  this.$hMessage.destroy()
                  this.showStyle('edit')
                })
            })
          })
        },
      })
    },
    showStyle(type) {
      let elems = document.querySelectorAll('.hotspot-edit-content')
      var index = 0,
        length = elems.length
      if (type === 'edit') {
        for (; index < length; index++) {
          elems[index].style.background = '#F6FFD0'
        }
      } else {
        for (; index < length; index++) {
          elems[index].style.background = 'transparent'
        }
      }
    },
    // 发起作品启用流程
    startWorksEnableProcess(id, title, status) {
      if (status !== 'E') {
        return
      }
      let { cms } = this.$store.state
      let name = []
      let passValidate = true
      let hasFormWidget = false
      let hasFormSubmit = false
      cms.pages.items.forEach((item) => {
        let elements = cms.elements.items[item.uuid]
        elements &&
          elements.forEach((j) => {
            if (j.property && j.property.passValidate) {
              for (var k in j.property.passValidate) {
                if (j.property.passValidate[k] == false) {
                  passValidate = false
                  name.push(item.name)
                }
              }
            }
            if (
              j.name == 'hs-cms-form-radio' ||
              j.name == 'hs-cms-form-checkbox' ||
              j.name == 'hs-cms-form-completion'
            ) {
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
        selectedElement: null,
      }
      this.$store.dispatch('cms/editState/updateEditState', newState)
      this.showStyle('preview')
      this.loading = this.$loading({
        lock: true,
        text: '启用中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      let that = this
      that.$nextTick(() => {
        that.sleep(2).then(() => {
          html2canvas(document.querySelector('.preview-panel'), {
            useCORS: true,
            allowTaint: false,
            async: true,
            scale: 1,
            logging: false,
            proxy: '*',
          })
            .then(async (canvas) => {
              const dataUrl = canvas.toDataURL('image/png')
              let fileData = dataURLtoFile(dataUrl, 'image/png')
              let file = new File([fileData], generateUID() + '.png')
              that.$store.dispatch('cms/editState/updateEditState', {
                currentState: 'edit',
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
                  file: file,
                }).then(async (res) => {
                  if (res.data.file_guid) {
                    let obj = res.data
                    Object.assign(obj, { file_type: 1 })
                    that.$store.dispatch('cms/works/updateWorks', {
                      video_poster_path: res.data.file_guid,
                    })
                    postMaterialFile(obj).then((res) => {
                      this.loading.close()
                    })
                  }
                  this.saveAction('start', title)
                })
              }
            })
            .finally(() => {
              this.$hMessage.destroy()
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
        addFileSuccess: function (uploadInfo) {},
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
            file_path: uploadInfo.object,
            file_name: uploadInfo.file.name,
            file_guid: uploadInfo.videoId,
            file_size: uploadInfo.file.size,
            file_extension: uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1),
            file_type: 1,
          }
          let imgUrl = ''
          if (window.CMS_CONFIG.USE_ALIYUN_PROXY && window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true') {
            imgUrl =
              window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS +
              '/' +
              uploadInfo.bucket +
              '.' +
              uploadInfo.endpoint.substring(8) +
              '/' +
              uploadInfo.object +
              '?' +
              uploadInfo.videoId
          } else {
            imgUrl =
              'https://' +
              uploadInfo.bucket +
              '.' +
              uploadInfo.endpoint.substring(8) +
              '/' +
              uploadInfo.object +
              '?' +
              uploadInfo.videoId
          }
          await self.$store.dispatch('cms/works/updateWorks', {
            video_poster_path: imgUrl,
          })
          postMaterialFile(obj).then((res) => {
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
        onUploadCanceled: function (uploadInfo, code, message) {},
        // 上传凭证超时
        onUploadTokenExpired: async function (uploadInfo) {
          const refreshUrl = window.CMS_CONFIG.CCM_URL + '/public/refreshVodUploadPassport'
          const res = await apiPost({ video_id: uploadInfo.videoId }, refreshUrl)
          const uploadAuth = res.data.upload_auth
          uploader.resumeUploadWithAuth(uploadAuth)
        },
        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function (uploadInfo, totalSize, progress) {},
        // 全部文件上传结束,
        onUploadEnd: function (uploadInfo) {},
      })
      return uploader
    },
  },
  render() {
    let map = {
      undo: this.$store.$$canUndo,
      redo: this.$store.$$canRedo,
      pre: this.preLayerStatus,
      post: this.postLayerStatus,
      default: true,
    }

    return (
      <div class="top-container">
        <div class="title-wrap">
          <h-input
            v-model="worksProxy.works_title"
            filterRE="/[<>]/g"
            placeholder="请输入作品名称"
            maxlength="40"
          ></h-input>
        </div>

        <div class="panel-button-wrap">
          <h-button-group>
            {!this.castratedVersion
              ? optaionBtns.map((btnInfo) => (
                  <h-tooltip effect="dark" placement="bottom" content={btnInfo.i18nTooltip}>
                    {!btnInfo.unShow && (
                      <h-button
                        block
                        disabled={!map[btnInfo.disabled]}
                        onClick={() => btnInfo.action && btnInfo.action.call(this)}
                      >
                        {btnInfo.icon ? (
                          <i class={['iconfont ', `icon-${btnInfo.icon}`]} aria-hidden="true" />
                        ) : (
                          btnInfo.text
                        )}
                      </h-button>
                    )}
                  </h-tooltip>
                ))
              : ''}
          </h-button-group>
          <h-poptip placement="bottom" width="140">
            <h-tooltip effect="dark" placement="bottom" content="画布缩放比例">
              <h-button onClick={() => this.onVisibleChange()}>
                {this.scalePercent}%<h-icon name="unfold"></h-icon>
              </h-button>
            </h-tooltip>
            <div class="api" slot="content">
              <ul>
                <li>
                  <h-input
                    ref="input"
                    autofocus={true}
                    focusAllSelect={true}
                    style="width: 80px;"
                    value={this.scalePercent}
                    onInput={(e) => {
                      this.changeScale(e)
                    }}
                  >
                    <span slot="append">%</span>
                  </h-input>
                </li>
                <li onClick={() => this.handleZoomOn()}>放大 Ctrl + ↑</li>
                <li onClick={() => this.handleZoomIn()}>缩小 Ctrl + ↓</li>
                <li onClick={() => this.changeScale('50%')}>50%</li>
                <li onClick={() => this.changeScale('100%')}>100%</li>
                <li onClick={() => this.changeScale('200%')}>200%</li>
              </ul>
            </div>
          </h-poptip>
        </div>

        {/* <div class="scaleGroup">
          <h-icon
            name="android-remove icon-android-remove"
            onOn-click={() => this.handleZoomIn()}
          ></h-icon>
          <h-input
            style="width: 80px;"
            value={this.scalePercent}
            readonly
          >
            <span slot="append">%</span>
          </h-input>
          <h-dropdown
            placement="bottom-start"
            onOn-click={(e) => this.changeScale(e)}>
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
          <h-icon
            name="android-add icon-android-add"
            onOn-click={() => this.handleZoomOn()}
          ></h-icon>
        </div> */}

        <div class="save-button-wrap">
          <h-button-group>
            <h-button onClick={() => this.cancelWork()}>退出</h-button>
            <h-button
              onClick={() => this.saveAsDraft(this.worksInfo.works_id)}
              v-if={this.worksInfo.works_status !== 'C' && this.worksInfo.works_status !== 'D'}
            >
              {this.draftText}
            </h-button>
            <h-button v-if={this.worksInfo.works_status !== 'E'} onClick={() => this.publishWork()}>
              发布
            </h-button>
            <h-button
              v-if={this.worksInfo.works_status === 'E'}
              onClick={() =>
                this.startWorksEnableProcess(
                  this.worksInfo.works_id,
                  this.worksInfo.works_title,
                  this.worksInfo.works_status
                )
              }
            >
              启用
            </h-button>
            <h-button
              v-if={this.showSaveAsTem && !this.castratedVersion}
              onClick={() => this.saveAsTemplate(this.worksInfo.works_id)}
            >
              另存为模板
            </h-button>
            <h-button onClick={() => this.h5preview()} v-if={this.isDev}>
              h5预览
            </h-button>
            <h-button onClick={() => this.toggleFullscreen()} v-if={this.isDev}>
              {this.isFullscreen ? '缩小' : '全屏'}
            </h-button>
          </h-button-group>
        </div>
      </div>
    )
  },
  mounted() {
    optaionBtns.map((btnInfo) => {
      btnInfo.hotkey &&
        hotkeys(btnInfo.hotkey, { splitKey: '&' }, (event, handler) => {
          event.preventDefault()
          event.stopPropagation()
          btnInfo.action && btnInfo.action.call(this)
        })
    })
  },
}
