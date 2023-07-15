/**
 * 页面停留时长统计
 * 每5秒请求一次，进入后台后不再统计
 * 最多停留24小时，超过24小时，重新获取ID统计
 */

import { postTrackWorksStayTime } from '../api'

let u = navigator.userAgent
const isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)

const MAX_STAY_TIME = 24 * 60 * 60 * 100 // 24小时
const interval = 5000

const StayTimer = {
  init(serial_guid) {
    this.serial_guid = serial_guid
    this.initTime = Date.now()
    this.start()
    document.addEventListener('visibilitychange', () => {
      if (document.visibilityState === 'visible') {
        // 页面激活时
        this.start()
      } else {
        // 页面隐藏时
        this.destory()
      }
    })
    if (isIOS) {
      // 页面隐藏时，iOS需要监听pagehide事件
      window.addEventListener('pagehide', () => {
        this.destory()
      }, false)
    }
  },
  get now() {
    return Date.now()
  },
  start() {
    this.timer = setInterval(() => {
      if (this.now - this.initTime < MAX_STAY_TIME) {
        postTrackWorksStayTime({
          serial_guid: this.serial_guid,
          view_stay_time: interval
        })
      } else {
        window.location.reload()
      }
    }, interval)
  },
  destory() {
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
  }
}

export default StayTimer