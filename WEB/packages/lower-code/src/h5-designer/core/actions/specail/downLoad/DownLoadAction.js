import { trackFunc } from '@Utils/appSDK'
import BaseAction from '../BaseAction'
import { laterfn } from '@h5Designer/utils'

/**
 * 下载action
 */
let u = navigator.userAgent
let isWeixin = u.toLowerCase().indexOf('micromessenger') !== -1
let isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1
let isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
let laterTime = 6000

const jump = (params, type) => {
  if (params.jumpUrl !== '' || params.downloadUrl !== '') {
    if (window.self == window.top) {
      if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true' && params.element) {
        const { page, element, worksInfo, pageIndex } = params
        let page_icon_section = ''
        const journey_id = getQueryString('journey_id')
        switch (element.name) {
          case 'hs-cms-button':
            page_icon_section = '按钮'
            break
          case 'hs-cms-text':
            page_icon_section = '文本'
            break
          case 'hs-cms-video':
            page_icon_section = '视频'
            break
          case 'hs-cms-image':
            page_icon_section = '图片'
            break
          case 'hs-cms-hotspot':
            page_icon_section = '热区'
            break
          case 'hs-cms-carousel':
            page_icon_section = '轮播图'
            break
          case 'hs-cms-audio':
            page_icon_section = '音频'
            break
        }
        // sensors.track('Operactmainpage_icon_click_hs', {
        //   platform_type: 'H5',
        //   business_type: `营销平台_运营活动_${worksInfo.works_title}_${worksInfo.publish_date_time}`,
        //   from_channel: window.CMS_CONFIG.SA_FROM_CHANNEL,
        //   work_id: worksInfo.works_id,
        //   page_id: page.uuid,
        //   page_title: page.name,
        //   page_icon_section,
        //   page_icon_name: element.element_name,
        //   is_Operactmainpage: pageIndex == 0 ? 1 : 0,
        //   To_url: params.downloadUrl,
        //   journey_id: journey_id
        // })
        trackFunc('Operactmainpage_icon_click_hs', {
          To_url: params.downloadUrl,
          page_icon_section,
          page_icon_name: element.element_name
        })
      }
      if (type === 'jump') {
        if (params.jumpUrl !== '') {
          window.location.href = params.jumpUrl
        } else {
          return false
        }
      } else {
        window.location.href = params.downloadUrl
      }
    }
  }
}

const hidden = () => {
  let hidden =
    window.document.hidden ||
    window.document.mozHidden ||
    window.document.msHidden ||
    window.document.webkitHidden

  if (typeof hidden === 'undefined' || hidden === false) {
    return true
  }
}

const getQueryString = (name) => {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  var r = decodeURI(window.location.search.substr(1)).match(reg)
  if (r != null) return unescape(r[2])
  return null
}

export default class DownLoadAction extends BaseAction {
  bind() { }

  unbind() {
    if (this.__later) {
      //  当later存在时，销毁延时器
      this.__later.cancel()
      this.__later = null
    }
  }

  jumpOrDownLoad(params) {
    // 跳转应用
    jump(params, 'jump')
    // 等待，下载应用
    this.__later = laterfn(() => {
      if (hidden()) {
        jump(params, 'dowload')
      }
    }, laterTime)
  }

  exec(done) {
    // 当不是生产环境时跳出
    if (!window.__cmsProduction) {
      return done()
    }
    // 微信环境
    if (isWeixin) {
      alert('请在浏览器上打开')
    } else {
      // android端
      if (isAndroid) {
        this.jumpOrDownLoad({
          jumpUrl: this.params.android_jump_url,
          downloadUrl: this.params.android_download_url,
          ...this.params
        })
      }
      if (isIOS) {
        this.jumpOrDownLoad({
          jumpUrl: this.params.ios_jump_url,
          downloadUrl: this.params.ios_download_url,
          ...this.params
        })
      }
    }

    if (this.__later) {
      this.__later.then(() => {
        this.__later = null
        // 当完成时解除action锁定
        done()
      })
    }
  }
}
