import BaseAction from '../BaseAction'
// import sensors from 'sa-sdk-javascript'
import { trackFunc, shareFunc } from '@Utils/appSDK'
import { delQueStr } from '@Utils/utils'
import { baseOMSUrl } from '@Utils/request'
/**
 * 跳转action
 */

// TODO: 当前为直接跳转,
// 后续可以对接router模块
export default class ShareEventAction extends BaseAction {
  exec(done) {
    // let { page_uuid, page, element, worksInfo, pageIndex, out_url } = this.params
    console.log('this.params',this.params)
    let share_info = {
      share_title: this.params.share_event_title,
      share_content: this.params.share_event_content,
      share_image:this.params.share_event_img_url,
      share_url:this.params.share_event_page_url === 'page_url' || this.params.share_event_page_url === ' ' || !this.params.share_event_page_url ? delQueStr(window.location.href, 'pageId') : this.params.share_event_page_url
      // share_url:this.params.share_event_page_url
    }
    // if (share_info.share_image && share_info.share_image.indexOf('aliyun') < 0) {
    //     const index = share_info.share_image.lastIndexOf('\/')
    //     const str = share_info.share_image.substring(index + 1, share_info.share_image.length)
    //     share_info.share_image = `${baseOMSUrl}/${str}`
    // }
    if (share_info.share_image && window.CMS_CONFIG.ALIYUN_DOMAIN.indexOf('http') < 0 && share_info.share_image.indexOf(window.CMS_CONFIG.PCFSTORE_IP) > -1){
        const indexHttp = share_info.share_image.indexOf('http://')
        const indexHttps = share_info.share_image.indexOf('https://')
        let str = ''
        if (indexHttp > -1) {
          str = share_info.share_image.substring(indexHttp + 7, share_info.share_image.length)
        }
        if (indexHttps > -1) {
          str = share_info.share_image.substring(indexHttps + 8, share_info.share_image.length)
        }
        const index1 = str.indexOf('\/')
        const str1 = str.substr(index1 + 1, share_info.share_image.length)
        share_info.share_image = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
    }
    console.log('share_info',share_info)
    shareFunc(share_info)
    done()
  }
}