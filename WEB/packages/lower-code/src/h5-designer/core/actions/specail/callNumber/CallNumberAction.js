import BaseAction from '../BaseAction'
/**
 * 拨打电话action
 */

export default class CallNumberAction extends BaseAction {
  exec(done) {
    let { call_number } = this.params
    if (call_number !== '') {
      if (window.self == window.top) {
        window.location.href = 'tel:' + call_number
      }
    }
    done()
  }
}
