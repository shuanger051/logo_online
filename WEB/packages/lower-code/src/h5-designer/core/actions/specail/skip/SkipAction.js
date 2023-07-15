import BaseAction from '../BaseAction'
import { trackFunc } from '@Utils/appSDK'
/**
 * 跳转action
 */

// TODO: 当前为直接跳转,
// 后续可以对接router模块

export default class SkipAction extends BaseAction {
  exec(done) {
    let { out_url, page, element, worksInfo, pageIndex } = this.params
    if (out_url !== '') {
      if (window.self == window.top) {
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
          //   To_url: out_url,
          //   journey_id: journey_id
          // })
          trackFunc('Operactmainpage_icon_click_hs', {
            To_url: out_url,
            page_icon_section,
            page_icon_name: element.element_name
          })
        }
        window.location.href = out_url
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
}
