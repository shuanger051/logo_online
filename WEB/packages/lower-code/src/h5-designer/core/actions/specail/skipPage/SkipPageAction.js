/*
 * @Author: your name
 * @Date: 2021-11-11 17:17:15
 * @LastEditTime: 2021-11-12 16:44:54
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \前端埋点\UCP-FRAME\src\biz\cms\src\editor\core\actions\specail\skipPage\SkipPageAction.js
 */
import BaseAction from '../BaseAction'
// import sensors from 'sa-sdk-javascript'
import { trackFunc } from '@Utils/appSDK'
/**
 * 跳转action
 */

// TODO: 当前为直接跳转,
// 后续可以对接router模块

export default class SkipPageAction extends BaseAction {
  exec(done) {
    let { page_uuid, page, element, worksInfo, pageIndex, out_url } = this.params
    if (out_url == '') {
      return false
    }
    if (page_uuid !== '') {
      const pageId = this.getQueryString('pageId')
      const type = this.getQueryString('type')
      if (type && type == 'previewTem') {
        return false
      }
      if (pageId !== null && page_uuid == pageId) {
        return
      }
      if (window.location.href.indexOf('?') > -1) {
        if (pageId) {
          const newUrl = this.replaceParamVal('pageId', page_uuid)
          if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true' && this.params.element) {
            let page_icon_section = ''
            const journey_id = this.getQueryString('journey_id')
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
            //   To_url: newUrl,
            //   journey_id: journey_id
            // })
            trackFunc('Operactmainpage_icon_click_hs', {
              To_url: newUrl,
              page_icon_section,
              page_icon_name: element.element_name
            })
          }
          window.location.href = newUrl
        } else {
          if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true' && this.params.element) {
            let page_icon_section = ''
            const journey_id = this.getQueryString('journey_id')
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
            //   To_url: `${window.location.href}&pageId=${page_uuid}`,
            //   journey_id: journey_id
            // })
            trackFunc('Operactmainpage_icon_click_hs', {
              To_url: `${window.location.href}&pageId=${page_uuid}`,
              page_icon_section,
              page_icon_name: element.element_name
            })
          }
          window.location.href = `${window.location.href}&pageId=${page_uuid}`
        }
      } else {
        if (window.CMS_CONFIG.NEED_SA && window.CMS_CONFIG.NEED_SA == 'true' && this.params.element) {
          let page_icon_section = ''
          const journey_id = this.getQueryString('journey_id')
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
          //   To_url: `${window.location.href}?pageId=${page_uuid}`,
          //   journey_id: journey_id
          // })
          trackFunc('Operactmainpage_icon_click_hs', {
            To_url: `${window.location.href}?pageId=${page_uuid}`,
            page_icon_section,
            page_icon_name: element.element_name
          })
        }
        window.location.href = `${window.location.href}?pageId=${page_uuid}`
      }
    }
    done()
  }

  getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
    var r = decodeURI(window.location.search.substr(1)).match(reg)
    if (r != null) return unescape(r[2])
    return null
  }

  replaceParamVal(paramName, replaceWith) {
    var oUrl = window.location.href
    // eslint-disable-next-line no-eval
    var re = eval('/(' + paramName + '=)([^&]*)/gi')
    let nUrl = ''
    if (re.test(oUrl)) {
      nUrl = oUrl.replace(re, paramName + '=' + replaceWith)
    } else {
      if (oUrl.indexOf('?') > 0) {
        nUrl = oUrl + '&' + paramName + '=' + replaceWith
      } else {
        nUrl = oUrl + '?' + paramName + '=' + replaceWith
      }
    }
    return nUrl
  }
}
