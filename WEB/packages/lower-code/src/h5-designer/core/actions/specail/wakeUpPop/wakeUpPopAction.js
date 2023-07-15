import BaseAction from '../BaseAction'
// import sensors from 'sa-sdk-javascript'
import { trackFunc } from '@Utils/appSDK'
/**
 * 跳转action
 */

// TODO: 当前为直接跳转,
// 后续可以对接router模块
export default class WakeUpPopAction extends BaseAction {
  exec(done) {
    // let { page_uuid, page, element, worksInfo, pageIndex, out_url } = this.params
    window.$MsgPop(this.params.wakeUpPop_content, this.params.wakeUpPop_title)
    done()
  }
}