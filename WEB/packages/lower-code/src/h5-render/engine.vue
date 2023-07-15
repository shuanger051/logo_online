<template>
  <div class="phone-wrap">
    <div v-if="link_status==='0' || expired_flag ==='1'">
      <div v-if="expired_flag ==='1'" style="text-align: center">
        <div style="margin-top: 40px; margin-bottom: 20px;">
          <img :src="cannotFind">
        </div>
        <div style="margin-top:20px">抱歉，您访问的页面不存在</div>
      </div>
      <div v-if="expired_flag ==='0' && link_status==='0'" style="text-align: center">
        <div style="margin-top: 40px; margin-bottom: 20px;">
          <img :src="underMaintenance">
        </div>
        <div style="margin-top:20px">正在维护中…</div>
      </div>
    </div>
    <e-phone-single-page v-if="loading" :page=pages :elements=elements||[] :events=events||[] :works=works
      :runtimeContext=runtimeContext :style="stylePage" :works_id="works_id" :works_title="works_title"
      :publish_date_time="publish_date_time" :isPreview="isPreview" :allPages="allPages" :viewType="viewType"
      :works_code="works_code" :act_preview="act_preview" />
    <!-- 活动组件引入-->
    <act-components :page=pages :elements=elements||[] :events=events||[] :works=works></act-components>
  </div>
</template>
<script>
import ePhoneSinglePage from '@h5Designer/components/e-phone-single-page'
import cannotFind from '@Root/assets/images/404.png'
import underMaintenance from '@Root/assets/images/403.png'

import { getPreviewRuntimeContext } from '@h5Designer/core/runtime_context'
import { getMobileWorksContent, getMobileWorksContentNoAuth, postTrackWorksVisit, postTrackWorksStayTime, getMobileAuthorityInfo, getMobileCheckUserAuth } from './api'
import { baseOMSUrl } from '@Utils/request'
import { getQueryString, delQueStr, tempCompatible } from '@Utils/utils'
// 活动组件引入
import actComponents from '@Components/act-components'
// 活动登录校验
import { postCheckMpcLoginStatusExt } from './api'
import StayTimer from './utils/stayTime'
// 引用埋点
import { trackLoadFunc, trackFunc, onLoadFunc, nativeShareFunc } from '@Utils/appSDK'
import { setPermanentDataFunc, getPermanentDataFunc, getLoginUrlFunc } from '@h5Render/utils/index'
import { checkUserInfo } from '@h5Render/utils/actutils'

export default {
  name: 'preview',
  components: {
    ePhoneSinglePage,
    actComponents
  },
  data() {
    return {
      serial_guid: '', // 单次访问作品后的id
      works_link_id: '', // 正式访问作品id
      works_id: '', // 作品id
      works_title: '', // 作品标题
      cannotFind, // 默认404图片
      underMaintenance, // 默认维护中图片
      link_status: '1', // 链接启停用状态 0停用 1启用
      expired_flag: '0', // 是否过期 0否 1是
      timer: '', // 计时器
      loading: false, // 加载状态
      stylePage: {}, // 页面样式
      inTime: null, // 进入时间
      leaveTime: null, // 离开时间
      works: null, // 作品data
      viewType: 'preview', // 作品状态 preview：预览
      publish_date_time: '', // 发布时间
      works_code: '', // 作品code
      mpc_user_info: '', // mpc用户信息
      loading_toast: null, // 加载toast
      share_info: { // 作品分享信息
        share_title: '',
        share_content: '',
        share_image: '',
        // share_type:'',
        share_url: ''
      },
      outer_token: '', // 外部token
      outer_uid: '', // 外部uid
      act_preview: '' // 是否为活动预览
    }
  },
  async created() {
    // 返回事件触发
    onLoadFunc()
    // 右上角分享事件
    nativeShareFunc(() => { return {...this.share_info} })
    // 通过sessionStorage活动登录信息
    getPermanentDataFunc('mpcUserInfo', true).then(res => {
      const mpcUserInfo = res || '{}'
      this.mpc_user_info = JSON.parse(mpcUserInfo)
      console.log(this.mpc_user_info, '中转页mpc用户信息')
    })
    this.works_link_id = getQueryString('works_link_id') || ''
    this.works_id = getQueryString('works_id') || ''
    const pageId = getQueryString('pageId')
    this.inTime = Date.now()
    // window.shareInitFunc(() => { return {...this.share_info} })
    // window.webViewJavascriptBridgeInit.resgisterHandles('getShareInfo', ()=>{
    //     return {...this.share_info}
    // })

    this.act_preview = getQueryString('actPreview') || ''

    this.loading_toast = this.$toast({
      type: 'loading',
      message: '加载中...',
      forbidClick: true
    })

    if (this.works_link_id) {
      // 获取已发布作品基本信息
      this.getPublicWorksContent(pageId)
    } else if (this.works_id) {
      // 获取作品基本信息
      this.getWorksContent(pageId)
    } else if (/preview\s*=\s*true/.test(location.search)) {
      let data = JSON.parse(window.sessionStorage.getItem('cms-h5'))
      this.works_id = data.works_id
      this.isPreview = true
      this.works = data
      this.share_info = { // 作品分享信息
        share_title: data.works.share_title,
        share_content: data.works.share_content,
        share_image: data.works.share_img_url,
        share_url: data.works.share_url_type === 'page_url' || data.works.share_url_type === ' ' || !data.works.share_url_type ? delQueStr(window.location.href, 'pageId') : data.works.share_page_url
      }
      this.viewType = data.viewType || 'preview'
      this.initRuntime().then(async () => {
        // this.loading = true
        if (pageId) {
          const index = data.pages.findIndex(item => { return item.uuid == pageId })
          this.updateViewport(data.pages[index].style.width)
        } else {
          this.updateViewport(data.pages[0].style.width)
        }
        await this.resolveData(data, pageId)
        this.loading = true
      }).catch((e) => {
        console.warn(e)
      }).finally(() => {
        this.loading_toast.clear()
      })
    } else {
      this.link_status = 0
      this.loading_toast.clear()
    }
  },
  mounted() {
    const that = this
    if (this.works_link_id) {
      // this.timer = setInterval(this.postTrackWorksStayTime, 5000)
      window.onunload = function () {
        // if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
        //   const duration = JSON.stringify(parseInt((Date.now() - that.inTime) / 1000))
        //   const journey_id = getQueryString('journey_id')
        //   sensors.track('Page_duration', {
        //     platform_type: 'H5',
        //     business_type: `营销平台_运营活动_${that.works_title}_${that.publish_date_time}`,
        //     from_channel: window.CMS_CONFIG.SA_FROM_CHANNEL,
        //     work_id: that.works_id,
        //     page_id: that.pages.uuid,
        //     page_title: that.pages.name,
        //     journey_id: journey_id,
        //     event_duration: duration
        //   })
        // }
        if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
          const duration = JSON.stringify(parseInt((Date.now() - that.inTime) / 1000))
          trackFunc('Page_duration', {
            event_duration: duration
          })
        }
      }
    }
  },
  methods: {
    // 初始化引用组件
    initRuntime() {
      this.runtimeContext = getPreviewRuntimeContext()
      return this.runtimeContext.initComponents()
    },
    // 更新页面适配
    updateViewport(workWidth) {
      let scale = 1
      let w = document.documentElement.clientWidth || workWidth
      scale = w / workWidth
      let viewport = `width=${workWidth}, initial-scale=${scale}, maximum-scale=${scale}, user-scalable=no`
      document.getElementById('viewport').setAttribute('content', viewport)
    },
    // alert提示信息
    showNoViewMsg(title, message) {
      this.$dialog.alert({
        title: title,
        message: message
      })
    },
    // 获取作品信息
    getWorksContent(pageId) {
      getMobileWorksContentNoAuth({
        works_id: getQueryString('works_id')
      }).then(res => {
        let data = res.data

        if (!data.error_info) {
          this.works_id = data.works_id
          this.isPreview = true
          let tempElements =  JSON.parse(data.elements)
          // 临时兼容处理
          tempCompatible(tempElements)
          data.elements = JSON.stringify(tempElements)
          let newData = {
            works: data,
            elements: JSON.parse(data.elements),
            events: JSON.parse(data.events),
            pages: JSON.parse(data.pages)
          }
          if (data.share_image && window.CMS_CONFIG.ALIYUN_DOMAIN.indexOf('http') < 0 && data.share_image.indexOf(window.CMS_CONFIG.PCFSTORE_IP) > -1){
              const indexHttp = data.share_image.indexOf('http://')
              const indexHttps = data.share_image.indexOf('https://')
              let str = ''
              if (indexHttp > -1) {
                str = data.share_image.substring(indexHttp + 7, data.share_image.length)
              }
              if (indexHttps > -1) {
                str = data.share_image.substring(indexHttps + 8, data.share_image.length)
              }
              const index1 = str.indexOf('\/')
              const str1 = str.substr(index1 + 1, data.share_image.length)
              data.share_image = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
          }
          this.share_info = { // 作品分享信息
            share_title: data.share_title,
            share_content: data.share_content,
            share_image: data.share_image !== undefined ? data.share_image : data.share_img_url,
            share_url: data.share_url_type === 'page_url' || data.share_url_type === ' ' || !data.share_url_type ? delQueStr(window.location.href, 'pageId') : data.share_page_url
          }
          if (data.works_property) {
            let works_property = JSON.parse(data.works_property)
            if (works_property.works_subtitle) {
              document.title = data.works_subtitle
            } else {
              document.title = data.works_title
            }
          } else {
            document.title = data.works_title
          }
          this.initRuntime().then(async () => {
            // this.loading = true
            if (pageId) {
              const index = JSON.parse(data.pages).findIndex(item => { return item.uuid == pageId })
              this.updateViewport(JSON.parse(data.pages)[index].style.width)
            } else {
              this.updateViewport(JSON.parse(data.pages)[0].style.width)
            }
            await this.resolveData(newData, pageId)
            this.loading = true
          })
        }
      }).catch((e) => {
        console.warn(e)
      }).finally(() => {
        this.loading_toast.clear()
      })
    },
    // 获取发布作品信息
    getPublicWorksContent(pageId) {
      // 获取作品基本信息
      getMobileWorksContent({
        serial_guid: this.works_link_id
      }).then(res => {
        let data = res.data
        // this.share_info = { // 作品分享信息
        //   share_title: data.share_title,
        //   share_content: data.share_content,
        //   share_image: data.share_image !== undefined ? data.share_image : data.share_img_url,
        //   share_url: data.share_url_type === 'page_url' || data.share_url_type === ' ' || !data.share_url_type ? delQueStr(window.location.href, 'pageId') : data.share_page_url
        // }
        if (!data.error_info) {
          this.works_id = data.works_id
          this.works_title = data.works_title
          this.publish_date_time = data.publish_date_time
          this.isPreview = false
          this.link_status = data.link_status
          this.expired_flag = data.expired_flag
          this.works_code = data.works_code
          if (data.works_property) {
            let works_property = JSON.parse(data.works_property)
            if (works_property.works_subtitle) {
              document.title = data.works_subtitle
            } else {
              document.title = data.works_title
            }
          } else {
            document.title = data.works_title
          }
          //视频组件历史数据添加默认值
          let tempElements = JSON.parse(data.elements)
          // 临时兼容处理
          tempCompatible(tempElements)
          data.elements = JSON.stringify(tempElements)
          // 链接停用
          if (data.link_status == '0') {
          } else if (data.expired_flag == '1') {
            // 页面过期
            this.link_status = '0'
            if (data.hint_title || data.hint_content) {
              this.showNoViewMsg(data.hint_title, data.hint_content)
            }
          } else {
            // 登录信息处理
            this.getUserInfoFunc(data)
            // 其他 若在有效期内容渲染内容
            let newData = {
              works: data,
              elements: JSON.parse(data.elements),
              events: JSON.parse(data.events),
              pages: JSON.parse(data.pages)
            }
            // if (data.share_image && data.share_image !== '') {
              // if (data.share_image.indexOf('aliyun') > -1) {

              // } else {
              //   const index = data.share_image.lastIndexOf('\/')
              //   const str = data.share_image.substring(index + 1, data.share_image.length)
              //   data.share_image = `${baseOMSUrl}/${str}`
              // }
            // }
            if (data.share_image && window.CMS_CONFIG.ALIYUN_DOMAIN.indexOf('http') < 0 && data.share_image.indexOf(window.CMS_CONFIG.PCFSTORE_IP) > -1){
                const indexHttp = data.share_image.indexOf('http://')
                const indexHttps = data.share_image.indexOf('https://')
                let str = ''
                if (indexHttp > -1) {
                  str = data.share_image.substring(indexHttp + 7, data.share_image.length)
                }
                if (indexHttps > -1) {
                  str = data.share_image.substring(indexHttps + 8, data.share_image.length)
                }
                const index1 = str.indexOf('\/')
                const str1 = str.substr(index1 + 1, data.share_image.length)
                data.share_image = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
            }
            console.log('data.share_image', data.share_image)
            this.share_info = { // 作品分享信息
              share_title: data.share_title,
              share_content: data.share_content,
              share_image: data.share_image !== undefined ? data.share_image : data.share_img_url,
              share_url: data.share_url_type === 'page_url' || data.share_url_type === ' ' || !data.share_url_type ? delQueStr(window.location.href, 'pageId') : data.share_page_url
            }
            this.initRuntime().then(async () => {
              // this.loading = true
              this.postTrackWorksVisit()
              if (pageId) {
                const index = JSON.parse(data.pages).findIndex(item => { return item.uuid == pageId })
                this.updateViewport(JSON.parse(data.pages)[index].style.width)
              } else {
                this.updateViewport(JSON.parse(data.pages)[0].style.width)
              }
              await this.resolveData(newData, pageId)
              this.loading = true
              let url = window.location.href.split('#')[0].toString()
              // 内容登录信息验证
              // if (this.act_preview != 'true') {
              //   this.checkUserLoginStatusOldFunc(data, this.works_link_id)
              // }

              // 微信分享
              if (window.CMS_CONFIG.SHOW_SHARE && window.CMS_CONFIG.SHOW_SHARE == 'true') {
                this.wxShareFunc(url, data)
              }
            })

            // 新版埋点
            if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
              this.trackInitFunc(newData, pageId)
            }
            // 老版埋点
            // if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true') {
            //   this.SAFunc(data, pageId)
            // }
          }
        }
      }).catch((e) => {
        console.warn(e)
      }).finally(() => {
        this.loading_toast.clear()
      })
    },
    // 样式信息格式化
    async resolveData(data, pageId) {
      if (pageId) {
        const pageIndex = data.pages.findIndex(item => { return item.uuid == pageId })
        this.allPages = data.pages
        this.pages = data.pages[pageIndex]
        this.elements = data.elements[pageId]
        this.events = data.events[pageId]
        this.works = data.works
      } else {
        this.allPages = data.pages
        this.pages = data.pages[0]
        this.elements = data.elements[data.pages[0].uuid]
        this.events = data.events[data.pages[0].uuid]
        this.works = data.works
      }
      let background = this.pages['style']['background_image']
      this.stylePage = {
        width: this.pages.style.width + 'px',
        height: this.pages.style.height + 'px',
        'background-color': this.pages['style']['background_color']
      }
      if (background) {
        if (/preview\s*=\s*true/.test(location.search)) {
          Object.assign(this.stylePage, {
            'background-image': `url(${background})`,
            'background-repeat': 'no-repeat',
            'background-color': `${this.pages['style']['background_color']}`,
            'background-size': '100% auto',
            height: this.pages.style.height + 'px'
          })
        } else {
          if (background.indexOf('aliyun') > -1) {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              const indexHttp = background.indexOf('http://')
              const indexHttps = background.indexOf('https://')
              if (indexHttp > -1) {
                const str = background.substring(indexHttp + 7, background.length)
                const index1 = str.indexOf('\/')
                const str1 = str.substr(index1 + 1, background.length)
                background = `${window.CMS_CONFIG.ALIYUN_DOMAIN}/${str1}`
                Object.assign(this.stylePage, {
                  'background-image': `url(${background})`,
                  'background-repeat': 'no-repeat',
                  'background-color': `${this.pages['style']['background_color']}`,
                  'background-size': '100% auto',
                  height: this.pages.style.height + 'px'
                })
              }
              if (indexHttps > -1) {
                const str = background.substring(indexHttps + 8, background.length)
                const index1 = str.indexOf('\/')
                const str1 = str.substr(index1 + 1, background.length)
                background = `${window.CMS_CONFIG.ALIYUN_DOMAIN}/${str1}`
                Object.assign(this.stylePage, {
                  'background-image': `url(${background})`,
                  'background-repeat': 'no-repeat',
                  'background-color': `${this.pages['style']['background_color']}`,
                  'background-size': '100% auto',
                  height: this.pages.style.height + 'px'
                })
              }
            } else if (window.CMS_CONFIG.USE_ALIYUN_PROXY && window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true') {
              const index = background.indexOf('outin')
              const str = background.substring(index, background.length)
              background = `${window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS}/${str}`
              Object.assign(this.stylePage, {
                'background-image': `url(${background})`,
                'background-repeat': 'no-repeat',
                'background-color': `${this.pages['style']['background_color']}`,
                'background-size': '100% auto',
                height: this.pages.style.height + 'px'
              })
            } else {
              const index = background.indexOf('outin')
              const str = background.substring(index, background.length)
              background = `https://${str}`
              Object.assign(this.stylePage, {
                'background-image': `url(${background})`,
                'background-repeat': 'no-repeat',
                'background-color': `${this.pages['style']['background_color']}`,
                'background-size': '100% auto',
                height: this.pages.style.height + 'px'
              })
            }
          } else if (background.indexOf(window.CMS_CONFIG.PCFSTORE_IP) > -1) {
            const indexHttp = background.indexOf('http://')
            const indexHttps = background.indexOf('https://')
            if (indexHttp > -1) {
              const str = background.substring(indexHttp + 7, background.length)
              const index1 = str.indexOf('\/')
              const str1 = str.substr(index1 + 1, background.length)
              background = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
              Object.assign(this.stylePage, {
                'background-image': `url(${background})`,
                'background-repeat': 'no-repeat',
                'background-color': `${this.pages['style']['background_color']}`,
                'background-size': '100% auto',
                height: this.pages.style.height + 'px'
              })
            }
            if (indexHttps > -1) {
              const str = background.substring(indexHttps + 8, background.length)
              const index1 = str.indexOf('\/')
              const str1 = str.substr(index1 + 1, background.length)
              background = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
              Object.assign(this.stylePage, {
                'background-image': `url(${background})`,
                'background-repeat': 'no-repeat',
                'background-color': `${this.pages['style']['background_color']}`,
                'background-size': '100% auto',
                height: this.pages.style.height + 'px'
              })
            }
          } else if (background.indexOf('file_guid') > -1) {
            const index = background.lastIndexOf('\/')
            const str = background.substring(index + 1, background.length)
            background = `${baseOMSUrl}/${str}`
            Object.assign(this.stylePage, {
              'background-image': `url(${background})`,
              'background-repeat': 'no-repeat',
              'background-color': `${this.pages['style']['background_color']}`,
              'background-size': '100% auto',
              height: this.pages.style.height + 'px'
            })
          } else {
            Object.assign(this.stylePage, {
              'background-image': `url(${background})`,
              'background-repeat': 'no-repeat',
              'background-color': `${this.pages['style']['background_color']}`,
              'background-size': '100% auto',
              height: this.pages.style.height + 'px'
            })
          }
        }
      } else {
        this.stylePage['background-color'] = this.pages.style['background_color']
      }
    },
    // 作品停留时间统计
    postTrackWorksStayTime() {
      if (this.serial_guid && this.link_status == '1') {
        postTrackWorksStayTime({
          serial_guid: this.serial_guid,
          view_stay_time: 5
        }).then()
      }
    },
    // 作品访问情况统计
    postTrackWorksVisit() {
      // 判断外部id是否存在，如果不存在使用mpc置换id
      const viewer_id = this.mpc_user_info.outer_uid != '' ? this.mpc_user_info.outer_uid : this.mpc_user_info.user_id
      postTrackWorksVisit({
        works_link_id: this.works_link_id,
        viewer_id: viewer_id
      }).then(res => {
        let data = res.data
        this.serial_guid = data.serial_guid
        if (this.serial_guid && this.link_status == '1') {
          StayTimer.init(this.serial_guid)
        }
      })
    },
    // 用户信息获取处理
    getUserInfoFunc(data) {
      const needUserInfo = data.need_user_info || true
      // 区分预览用户还是真实用户
      if (this.isPreview) {
        // 预览判断是否需要用户信息
        if (needUserInfo) {
          // 拼接跳转链接
          getLoginUrlFunc('', window.CMS_CONFIG.LOGIN_PREVIEW_URL)
        }
      } else {
        // 活动组件登录 (需要进一步明确对卡券组件是否要分客户)
        this.checkUserLoginStatusFunc(data)
      }
      
    },
    // 新版登录验证
    checkUserLoginStatusFunc(data) {
      const userInfo = {
        ctoken: this.mpc_user_info.ctoken,
        userId: this.mpc_user_info.user_id,
        outerUid: this.mpc_user_info.outer_uid
      }
      setPermanentDataFunc('userInfo', JSON.stringify(userInfo))
      if (data.works_login_flag == '1') {
        // 获取要求登录类型，默认手机号登录
        const login_req = data.works_login_user_type ? data.works_login_user_type : 'mobileLogin'
        // 验证是否登录
        checkUserInfo(login_req, true).then((isLogin) => {
          if (isLogin) {
            postCheckMpcLoginStatusExt({
              ctoken: userInfo.ctoken,
              user_id: userInfo.userId
            }).then((res) => {
              console.log(res, 'postCheckMpcLoginStatusExt');
              if (!res.data.mlogin_status) {
                this.$toast({duration: 0, message: '登录验证出错，请退出重试', icon: 'close'})
              }
            }).catch((err) => {
              this.$toast({duration: 0, message: err.data.error_info, icon: 'close'})
            })
          }
        })
      }
    },
    // 老版登录验证
    checkUserLoginStatusOldFunc(data, works_link_id) {
      // let zzuid = getQueryString('zzuid')
      // let zztoken = getQueryString('zztoken')
      const cmsUserInfo = {
        ctoken: this.outer_token,
        userId: this.outer_uid
      }
      setPermanentDataFunc('cmsUserInfo', JSON.stringify(cmsUserInfo))
      if (data.works_login_flag == '1' && window.CMS_CONFIG.CUSTOMER_CODE == '12836') {
        if (cmsUserInfo.ctoken !== '' && cmsUserInfo.userId !== '') {
          getMobileCheckUserAuth({
            user_id: cmsUserInfo.userId,
            access_token: cmsUserInfo.ctoken
          }).then(res => {
            let data = res.data
            if (data.error_info) {
              this.$toast(data.error_info)
              // window.location.href = `${window.CMS_CONFIG.LOGIN_URL}/#/?&callbackUrl=${encodeURIComponent(`${window.location.origin}${window.location.pathname}?works_link_id=${works_link_id}`)}&regCallbackUrl=${encodeURIComponent(`${window.location.origin}${window.location.pathname}?works_link_id=${works_link_id}`)}&regSource=${window.CMS_CONFIG.REG_SOURCE_CODE}`
            }
          }).catch((e) => {
            console.warn(e)
            this.$toast('登录验证失败')
            // window.location.href = `${window.CMS_CONFIG.LOGIN_URL}/#/?&callbackUrl=${encodeURIComponent(`${window.location.origin}${window.location.pathname}?works_link_id=${works_link_id}`)}&regCallbackUrl=${encodeURIComponent(`${window.location.origin}${window.location.pathname}?works_link_id=${works_link_id}`)}&regSource=${window.CMS_CONFIG.REG_SOURCE_CODE}`
          })
        } else {
          this.$toast('登录验证失败')
          // window.location.href = `${window.CMS_CONFIG.LOGIN_URL}/#/?&callbackUrl=${encodeURIComponent(`${window.location.origin}${window.location.pathname}?works_link_id=${works_link_id}`)}&regCallbackUrl=${encodeURIComponent(`${window.location.origin}${window.location.pathname}?works_link_id=${works_link_id}`)}&regSource=${window.CMS_CONFIG.REG_SOURCE_CODE}`
        }
      }
    },
    // 分享
    wxShareFunc(url, data) {
      getMobileAuthorityInfo({ url }).then(res => {
        wx.config({
          debug: false,
          appId: res.data.app_id,
          timestamp: res.data.timestamp,
          nonceStr: res.data.nonce_str,
          signature: res.data.signature,
          jsApiList: ['updateTimelineShareData', 'updateAppMessageShareData', 'onMenuShareAppMessage', 'onMenuShareTimeline']
        })
        wx.onMenuShareAppMessage({
          title: data.share_title && data.share_title !== ' ' ? data.share_title : data.works_title, // 分享标题
          desc: data.share_content && data.share_content !== ' ' ? data.share_content : '我发现了一个超酷炫的H5，快来看看吧。', // 分享描述
          link: window.location.href, // 分享链接
          type: 'link', // 分享类型,music、video或link，不填默认为link
          imgUrl: data.share_image // 分享图标
        })

        wx.onMenuShareTimeline({
          title: data.share_title && data.share_title !== ' ' ? data.share_title : data.works_title, // 分享标题
          desc: data.share_content && data.share_content !== ' ' ? data.share_content : '我发现了一个超酷炫的H5，快来看看吧。', // 分享描述
          link: window.location.href, // 分享链接
          type: 'link', // 分享类型,music、video或link，不填默认为link
          imgUrl: data.share_image // 分享图标
        })
      })
    },
    // 埋点
    // SAFunc(data, pageId) {
    //   sensors.init({
    //     server_url: window.CMS_CONFIG.SA_SERVER_URL,
    //     is_track_single_page: true, // 单页面配置，默认开启，若页面中有锚点设计，需要将该配置删除，否则触发锚点会多触发 $pageview 事件
    //     use_client_time: true,
    //     send_type: 'beacon',
    //     show_log: true,
    //     heatmap: {
    //       // 是否开启点击图，default 表示开启，自动采集 $WebClick 事件，可以设置 'not_collect' 表示关闭。
    //       clickmap: 'default',
    //       // 是否开启触达图，not_collect 表示关闭，不会自动采集 $WebStay 事件，可以设置 'default' 表示开启。
    //       scroll_notice_map: 'default',
    //       collect_tags: {
    //         div: {
    //           max_level: 3 // 默认是 1，即只支持叶子 div。可配置范围是 [1, 2, 3],非该范围配置值，会被当作 1 处理。
    //         },
    //         li: true,
    //         img: true,
    //         svg: true
    //         // ... 其他标签
    //       }
    //     },
    //     app_js_bridge: true
    //   })
    //   sensors.quick('autoTrack') // 用于采集 $pageview 事件。
    //   let pageIndex = 0
    //   if (pageId) {
    //     pageIndex = JSON.parse(data.pages).findIndex(item => { return item.uuid == pageId })
    //   }
    //   const journey_id = getQueryString('journey_id')
    //   sensors.track('OperactPage_view_hs', {
    //     platform_type: 'H5',
    //     business_type: `营销平台_运营活动_${data.works_title}_${data.publish_date_time}`,
    //     from_channel: window.CMS_CONFIG.SA_FROM_CHANNEL,
    //     work_id: data.works_id,
    //     page_id: JSON.parse(data.pages)[pageIndex].uuid,
    //     page_title: JSON.parse(data.pages)[pageIndex].name,
    //     page_url: window.location.href,
    //     is_Operactmainpage: pageIndex == 0 ? 1 : 0,
    //     journey_id: journey_id
    //   })
    // },
    // 埋点初始化
    trackInitFunc(newData, pageId) {
      // 存储作品相关信息, 提供埋点上报参数
      const journey_id = getQueryString('journey_id') || ''
      let pageIndex = 0
      if (pageId) {
        pageIndex = newData.pages.findIndex(item => { return item.uuid == pageId })
      }
      const trackData = {
        platform_type: 'H5',
        business_type: `营销平台_运营活动_${this.works_title}_${this.publish_date_time}`,
        from_channel: window.CMS_CONFIG.SA_FROM_CHANNEL,
        work_id: this.works_id,
        works_title: this.works_title,
        publish_date_time: this.publish_date_time,
        page_id: newData.pages[pageIndex].uuid,
        page_title: newData.pages[pageIndex].name,
        page_url: window.location.href,
        is_Operactmainpage: pageIndex == 0 ? 1 : 0,
        journey_id: journey_id
      }
      setPermanentDataFunc('trackData', JSON.stringify(trackData))
      // 埋点初始化
      trackLoadFunc(window.CMS_CONFIG.SA_SERVER_URL)
      setTimeout(() => {
        trackFunc('OperactPage_view_hs')
      }, 1500)
    }
  },
  destroyed() {
    this.runtimeContext.destroy()
  },
  beforeDestroy() {
    // 删除定时器-定时保存浏览时间
    // if (this.timer) {
    //   clearInterval(this.timer)
    // }
  }
}
</script>
<style scoped lang="scss">
body {
  box-sizing: border-box;
}
.phone-wrap {
  overflow-x: hidden;
  overflow-y: hidden;
  position: relative;
}
/deep/ * {
  box-sizing: border-box;
}
/* 滚动条 */
/deep/::-webkit-scrollbar {
  width: 7px;
  height: 7px;
  background-color: transparent;
}
/deep/::-webkit-scrollbar-track {
  /* -webkit-box-shadow: none; */
  -webkit-box-shadow: none;
  box-shadow: none;
}
/deep/::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: rgba(123, 129, 144, 0.6);
}
/deep/::-webkit-scrollbar-thumb:hover {
  cursor: pointer;
}
/deep/::-webkit-scrollbar-corner {
  background: transparent;
}
</style>
