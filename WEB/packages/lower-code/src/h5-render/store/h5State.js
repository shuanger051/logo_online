import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    curShowElement: [],
    isDraw: true,
    isScratchcardDraw: false,
    resetCoverPainting: false, // 重置刮刮乐的涂层
    updateObj: {
      isUpdate: false, // 是否更新数据
      widgetUuid: '', // 更新组件uuid
      widgetType: '', // 更新组件类型
    },
    prizeObj: {
      showPrizeAlert: false, // 显示隐藏
      type: '1', // 1领奖弹框 2卡号卡密 3手机号输入 4手机号确认 5展示兑换手机号
      prizeAlertName: '', // 奖品名称
      prizeAlertImage: '', // 奖品图片
      cardNo: '', // 卡号
      cardPwd: '', // 卡密
      shortUrl: '', // 短链接
      expireDate: '', // 有效期
      winBindMobile: '', // 绑定手机号
      worksCode: '', // 作品code
      winId: '' // 奖品记录id
    },
    tipsObj: {
      showTips: false, // 显示隐藏
      tipsType: 'tips', // tips 通用提示  rule 活动规则
      tipsTitle: '', // 标题
      tipsText: '', // 提示内容
      trackName: '' // 埋点名称类型
    },
    animationObj: {
      type: 'egg', // 类型
      showAnimation: false, // 动效显示隐藏
      openPrizeAlert: false, // 奖品弹框显示隐藏
      imgUrlObj: {}, // 动效图片
      widgetUuid: ''
    },
    myPrizeObj: {
      showMyPrize: false,
      tips: '', // 提示信息
      type: 'pop', // 类型
      styleObj: {}, // 弹框样式
      worksCode: '', // 作品code
      missionIds: '', // 任务id
      filterCategoryIds: [] // 过滤奖品
    },
    submitInviteCodeObj: {
      showSubmitAlert: false, // 显示隐藏
      type: '1', // 1提交, 2展示信息
      widgetUuid: '',
      styleObj: {} // 样式信息
    },
    redPacketRainObj: {
      type: 'rain',
      showRainAlert: false,
      awardList: [],
      imageList: [],
      alertCSS: {},
      getRedCount: 0,
      continueTimes: 10,
      accessValue: 1,
      widgetUuid: '',
      ticket: ''
    },
    hPreviewData: [],
    sysParamsH5: {
    } // 系统参数
  },
  getters: {},
  mutations: {
    // 更新奖品弹框信息
    changePrizeAlertState(state, payload) {
      state.prizeObj = payload
    },

    // 重置刮刮乐涂层
    resetCover(state, payload) {
      state.resetCoverPainting = payload
    },

    // 更新通用弹框信息
    changeTipsState(state, payload) {
      state.tipsObj = payload
    },

    // 更新动效弹框信息
    changeAnimationState(state, payload) {
      state.animationObj = payload
    },

    // 更新抽奖状态
    changeDrawState(state, payload) {
      state.isDraw = payload
    },

    // 更新刮刮乐抽奖状态
    changeScratchcardDraw(state, payload) {
      state.isScratchcardDraw = payload
    },

    // 更新次数
    changeUpdateState(state, payload) {
      state.updateObj = payload
    },

    // 显示隐藏我的奖品
    changeMyPrizeState(state, payload) {
      state.myPrizeObj = payload
    },

    // 显示输入邀请码弹框
    changeSubmitInviteCodeState(state, payload) {
      state.submitInviteCodeObj = payload
    },

    // 更新系统参数
    changeSysParamsH5State(state, payload) {
      state.sysParamsH5 = payload
    },

    // 显示红包雨
    changeRedPacketRainState(state, payload) {
      state.redPacketRainObj = payload
    },

    ADDCURELEMENT(state, payload) {
      if (Array.isArray(payload)) {
        for (let i = 0; i < payload.length; i++) {
          if (state.curShowElement.indexOf(payload[i]) === -1) {
            state.curShowElement.push(payload[i])
          }
        }
      } else {
        if (state.curShowElement.indexOf(payload) === -1) {
          state.curShowElement.push(payload)
        }
      }
    },

    DELETECURELEMENT(state, payload) {
      if (Array.isArray(payload)) {
        for (let i = 0; i < payload.length; i++) {
          let index = state.curShowElement.findIndex(payload[i])
          if (index) {
            state.curShowElement.splice(index, 1)
          }
        }
      } else {
        let index = state.curShowElement.findIndex(payload)
        if (index) {
          state.curShowElement.splice(index, 1)
        }
      }
    },

    CHANGECURELEMENT(state, payload) {
      if (Array.isArray(payload)) {
        state.curShowElement = payload
      } else {
        state.curShowElement.push(payload)
      }
    },
    // 改变领券组件预览数据
    changeCouponData(state, payload) {
      // console.log('payload====>', payload, payload.id)
      if (!payload.id) return
      let flag = false
      for (let i = 0; i < state.hPreviewData.length; i++) {
        // console.log('state.hPreviewData====', state.hPreviewData[i], payload.id !== state.hPreviewData[i].id, payload.id, state.hPreviewData[i].id)
        if (payload.id === state.hPreviewData[i].id) {
          flag = true
          break
        } else {
          flag = false
        }
      }
      if (!flag) {
        state.hPreviewData.push(payload)
      }
      // if (payload.id) {
      //   state.hPreviewData.push(payload)
      // }
      // state.hPreviewData = payload
    }
    // changePreviewData (state, payload) {
    //   console.log('修改previewdata', state, payload)
    // }
  },
  actions: {
    addCurElement({ commit }, param) {
      commit('ADDCURELEMENT', param)
    },
    deleteCurElement({ commit }, param) {
      commit('DELETECURELEMENT', param)
    },
    changeCurElement({ commit }, param) {
      commit('CHANGECURELEMENT', param)
    }
  }
})
