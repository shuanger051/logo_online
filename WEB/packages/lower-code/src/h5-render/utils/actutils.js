import {
  getWidgetListByWorksCodeExt,
  getWidgetInfoExt,
  getWidgetAwardConfigExt,
  updateRegistDrawEventExt,
  getMyRemainingDrawNumExt,
  getWidgetJoinFrequencyConfigExt,
  getWidgetJoinPermissionsConfigExt,
  getUserJoinPermissionsExt,
  postUserEnterBehaviorExt,
  getRemainPointsNumExt,
  getRemainCoinNumExt,
  getUserGuessDataExt,
  postTakeInvitationOfCodeExt,
  postCurrSessionsTicketExt,
  postSessionsDrawExt,
  getMyRemainingDrawNumOfSessionsExt
} from '@h5Render/api/actH5'
import store from '@h5Render/store/h5State'
import { Toast } from 'vant'
import 'vant/lib/toast/style'
import { getPermanentDataFunc, getLoginUrlFunc } from '@h5Render/utils/index'
import { isEmpty } from 'lodash'
import { loginFunc } from '@Utils/appSDK'

/**
 * 查询已发布的组件信息
 * @param data 作品code
 * @return
 */
export function getWidgetListByWorksCodeFunc(data) {
  return new Promise((resolve) => {
    getWidgetListByWorksCodeExt(data).then((res) => {
      const list = res.data.widget_list || []
      resolve(list)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 查询组件基本信息(别删，刮刮乐还要用)
 * @param data widget_uuid
 * @return
 */
export function getWidgetInfoFunc(data, actTips) {
  return new Promise((resolve) => {
    getWidgetInfoExt(data).then((res) => {
      resolve(res.data)
      if ((res.data.open_status != '1' || res.data.period_flag != '1') && actTips) {
        tipsShow(actTips, 'act_not_during')
      }
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 查询奖励配置
 * @param data widget_uuid
 * @return
 */
export function getWidgetAwardConfigFunc(data) {
  return new Promise((resolve) => {
    getWidgetAwardConfigExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 调用抽奖接口
 * @param data widget_uuid, ctoken, user_id
 * @return
 */
export function updateRegistDrawEventFunc(data, actTips) {
  return new Promise((resolve) => {
    updateRegistDrawEventExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data) {
        if (err.data.error_no == '200149') {
          getWidgetJoinFrequencyConfigFunc(data.widget_uuid)
        } else if (err.data.error_no == '200141') {
          getWidgetJoinPermissionsConfigFunc(data.widget_uuid)
        } else if (err.data.error_no == '200178' || err.data.error_no == '200188' || err.data.error_no == '200189') {
          tipsShow(actTips, 'act_not_during')
        } else {
          Toast(err.data.error_info)
        }
      }
      resolve('fail')
    })
  })
}

/**
 * 获取组件参与频率配置(别删，刮刮乐还要用)
 * @param widgetUuid widget_uuid
 * @return
 */
export function getWidgetJoinFrequencyConfigFunc(widgetUuid) {
  const data = {
    widget_uuid: widgetUuid
  }
  getWidgetJoinFrequencyConfigExt(data).then((res) => {
    if (res.data.frequency_prompt_config.prompt_type == '1') {
      window.location.href = res.data.frequency_prompt_config.page_link
    } else {
      tipsShow(res.data.frequency_prompt_config.prompt_content, 'act_no_times')
    }
  }).catch((err) => {
    if (err.data && err.data.error_no && err.data.error_no != 0) {
      Toast(err.data.error_info)
    }
  })
}

/**
 * 获取组件参与权限配置
 * @param String widgetUuid
 * @return
 */
export function getWidgetJoinPermissionsConfigFunc(widgetUuid) {
  const data = {
    widget_uuid: widgetUuid
  }
  getWidgetJoinPermissionsConfigExt(data).then((res) => {
    tipsShow(res.data.prompt_content)
  }).catch((err) => {
    if (err.data && err.data.error_no && err.data.error_no != 0) {
      Toast(err.data.error_info)
    }
  })
}

/**
 * 活动未开始提示
 * @param tips 提示信息
 * @return
 */
export function tipsShow(tips, trackName) {
  const tipsInfo = {
    showTips: true,
    tipsType: 'tips',
    tipsTitle: '',
    tipsText: tips,
    trackName: trackName
  }
  store.commit('changeTipsState', tipsInfo)
}

/**
 * 用户是否有权限参与抽奖
 * @param data widget_uuid, ctoken, user_id
 * @param first 是否为第一次
 * @param actTips
 * @param loginReq
 * @return
 */
export function getUserJoinPermissionsFunc(data, first = false, actTips, loginReq = 'mobileLogin') {
  return new Promise((resolve, reject) => {
    checkUserInfo(loginReq).then((isLogin) => {
      if (isLogin) {
        // 登录信息存在
        getUserJoinPermissionsExt(data).then((res) => {
          resolve(res.data)
        }).catch(err => {
          if (!first && err.data) {
            if (err.data.error_no == '200149') {
              getWidgetJoinFrequencyConfigFunc(data.widget_uuid)
            } else if (err.data.error_no == '200141') {
              getWidgetJoinPermissionsConfigFunc(data.widget_uuid)
            } else if (err.data.error_no == '200178' || err.data.error_no == '200188' || err.data.error_no == '200189') {
              tipsShow(actTips, 'act_not_during')
            } else {
              Toast(err.data.error_info)
            }
          }
          reject('fail')
        })
      }
    })
  })
}

/**
 * 用户进入页面上报接口
 * @param data widget_uuid, ctoken, user_id
 * @return
 */
export function postUserEnterBehaviorFunc(data) {
  return new Promise((resolve) => {
    postUserEnterBehaviorExt(data).then(() => {
      resolve('success')
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 获取抽奖次数
 * @param data widget_uuid, ctoken, user_id
 * @param type 0：可用次数  1：累计次数
 * @return
 */
export function getCommonDrawNumFunc(data, type) {
  return new Promise((resolve) => {
    getMyRemainingDrawNumFunc(data).then((res) => {
      let obj = {
        remaining_num: res.remaining_num
      }
      if (type === '1') {
        obj.remaining_num = res.cumulative_num
      }
      resolve(obj)
    })
  })
}

/**
 * 抽奖可用次数获取
 * @param data widget_uuid, ctoken, user_id
 * @return
 */
export function getMyRemainingDrawNumFunc(data) {
  return new Promise((resolve) => {
    getMyRemainingDrawNumExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 获取抽奖次数(红包雨专用)
 * @param data widget_uuid, ctoken, user_id
 * @param type 0：可用次数  1：累计次数
 * @return
 */
export function getCommonDrawNumOfSessionsFunc(data, type) {
  return new Promise((resolve) => {
    getMyRemainingDrawNumOfSessionsFunc(data).then((res) => {
      let obj = {
        remaining_num: res.remaining_num
      }
      if (type === '1') {
        obj.remaining_num = res.cumulative_num
      }
      resolve(obj)
    })
  })
}

/**
 * 可用抽奖次数获取（红包雨专用）
 * @param data widget_uuid, ctoken, user_id
 * @return
 */
export function getMyRemainingDrawNumOfSessionsFunc(data) {
  return new Promise((resolve) => {
    getMyRemainingDrawNumOfSessionsExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 获取剩余积分
 * @param data widget_uuid, ctoken, user_id
 * @return
 */
export function getRemainPointsNumFunc(data) {
  return new Promise((resolve) => {
    getRemainPointsNumExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 获取剩余活动币
 * @param data widget_uuid, ctoken, user_id
 * @return
 */
export function getRemainCoinNumFunc(data) {
  return new Promise((resolve) => {
    getRemainCoinNumExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 我的
 * @param { Object } data
 * @returns Promise
 */
export function getUserGuessDataExtFunc(data) {
  return new Promise((resolve, reject) => {
    getUserGuessDataExt(data).then(res => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        reject(err.data)
        Toast(err.data.error_info)
      }
    })
  })
}

/**
 * 接受邀请
 * @param data widget_uuid, ctoken, user_id, inviter_code
 * @return
 */
export function postTakeInvitationOfCodeFunc(data, actTips) {
  return new Promise((resolve) => {
    postTakeInvitationOfCodeExt(data).then((res) => {
      const info = {
        data: res.data || {},
        type: 'success'
      }
      resolve(info)
    }).catch((err) => {
      if (err.data) {
        if (err.data.error_no == '200151' || err.data.error_no == '200190' || err.data.error_no == '200340' || err.data.error_no == '200341') {
          const error_info = err.data.error_info ? JSON.parse(err.data.error_info) : {}
          const info = {
            data: error_info,
            type: 'fail'
          }
          resolve(info)
        } else {
          if (err.data.error_no == '200149') {
            getWidgetJoinFrequencyConfigFunc(data.widget_uuid)
          } else if (err.data.error_no == '200141') {
            getWidgetJoinPermissionsConfigFunc(data.widget_uuid)
          } else if (err.data.error_no == '200178' || err.data.error_no == '200188' || err.data.error_no == '200189') {
            tipsShow(actTips, 'act_not_during')
          } else {
            Toast(err.data.error_info)
          }
          resolve('fail')
        }
      } else {
        resolve('fail')
      }
    })
  })
}

/**
 * 点击权限判断
 * @param data
 * @param actTips
 * @param loginReq
 */
export function clickJudgePermission(data, actTips, loginReq = 'mobileLogin') {
  return new Promise((resolve, reject) => {
    getWidgetInfoExt(data).then((res) => {
      if (res.data.open_status != '1' || res.data.period_flag != '1') {
        // 不在活动时间内
        tipsShow(actTips, 'act_not_during')
      } else {
        checkUserInfo(loginReq).then((isLogin) => {
          if (isLogin) {
            // 登录信息存在
            getUserJoinPermissionsExt(data).then((res) => {
              resolve(res.data)
            }).catch(err => {
              if (err.data) {
                if (err.data.error_no == '200149') {
                  getWidgetJoinFrequencyConfigFunc(data.widget_uuid)
                } else if (err.data.error_no == '200141') {
                  getWidgetJoinPermissionsConfigFunc(data.widget_uuid)
                } else if (err.data.error_no == '200178' || err.data.error_no == '200188' || err.data.error_no == '200189') {
                  tipsShow(actTips, 'act_not_during')
                } else {
                  Toast(err.data.error_info)
                }
              }
              reject('fail')
            })
          }
        })
      }
    }).catch((err) => {
      if (err.data && err.data.error_no && err.data.error_no != 0) {
        Toast(err.data.error_info)
      }
      reject('fail')
    })
  })
}

/**
 * 申请当前场次票据
 * @param data widget_uuid, ctoken, user_id
 * @param actTips 活动未开始提示文案
 * @return
 */
export function postCurrSessionsTicketFunc(data) {
  return new Promise((resolve, reject) => {
    postCurrSessionsTicketExt(data).then((res) => {
      resolve(res.data)
    }).catch(err => {
      if (err.data) {
        Toast(err.data.error_info)
      }
      reject('fail')
    })
  })
}

/**
 * 场次抽奖
 * @param data widget_uuid, ctoken, user_id, ticket, get_red_count
 * @return
 */
export function postSessionsDrawFunc(data) {
  return new Promise((resolve, reject) => {
    postSessionsDrawExt(data).then((res) => {
      resolve(res.data)
    }).catch((err) => {
      if (err.data) {
        if (err.data.error_no == '200149') {
          getWidgetJoinFrequencyConfigFunc(data.widget_uuid)
        } else if (err.data.error_no == '200141') {
          getWidgetJoinPermissionsConfigFunc(data.widget_uuid)
        } else {
          Toast(err.data.error_info)
        }
      }
      reject('fail')
    })
  })
}

/**
 * 判断用户信息是否存在、是否符合要求
 * @param data widget_uuid, ctoken, user_id, ticket, get_red_count
 * @return
 */
export function checkUserInfo(loginReq, isHasTransferPage = true) {
  return new Promise((resolve) => {
    // 通过sessionStorage活动登录信息
    getPermanentDataFunc('mpcUserInfo', true).then(res => {
      let valid = false
      const mpcUserInfo = res ? JSON.parse(res) : {}
      const loginType = mpcUserInfo.support_login_modes || '' // 中转页存储登录模式
      if (loginType.indexOf(loginReq) > -1) {
        // 当前登录模式匹配
        const userInfo = getPermanentDataFunc('userInfo') ? JSON.parse(getPermanentDataFunc('userInfo')) : {}
        if (isEmpty(userInfo.ctoken) || isEmpty(userInfo.userId)) {
          // 登录信息不存在
          if (isHasTransferPage) {
            getLoginUrlFunc(loginReq, window.CMS_CONFIG.LOGIN_URL)
          } else {
            loginFunc()
          }
        } else {
          valid = true
          resolve(valid)
        }
      } else {
        // 当前登录模式不匹配
        if (isHasTransferPage) {
          getLoginUrlFunc(loginReq, window.CMS_CONFIG.LOGIN_URL)
        } else {
          loginFunc()
        }
      }
      resolve(valid)
     })
  })
}
