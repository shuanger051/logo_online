import Vue from 'vue'

/**
 * 获取本地信息，兼容框架1.0，2.0
 */
let getStorageHandle = {
  operatorNo: (dft = ' ') => {
    let data = window.localStorage.getItem('user')
    let ret
    try {
      data = JSON.parse(data)
      ret = data.operator_no
    } catch (e) {}

    if (!ret) {
      ret = window.sessionStorage.getItem('operatorNo') || dft
    }
    return ret
  },
  currMenuCode: (dft = ' ') => {
    let data = window.localStorage.getItem('bizStorage')
    let ret
    try {
      data = JSON.parse(data)
      ret = data.activeMenuItem && data.activeMenuItem.id
    } catch (e) {}

    if (!ret) {
      ret = window.sessionStorage.getItem('currMenuCode') || dft
    }
    return ret
  },
  operatorCode: (dft = ' ') => {
    let data = window.localStorage.getItem('user')
    let ret
    try {
      data = JSON.parse(data)
      ret = data.operator_code
    } catch (e) {}

    if (!ret) {
      ret = (window.sessionStorage.getItem('operatorCode') || window.sessionStorage.getItem('operator_code')) || dft
    }
    return ret
  }
}

let getStorageInfo = (name, dft) => {
  if (getStorageHandle[name]) {
    return getStorageHandle[name](dft)
  } else {
    return ' '
  }
}


/**
 * 菜单控制，兼容1.0，2.0
*/
let tabControl = {
  maps: {
    delCurVisitedViews: ['delCurVisitedViews', 'del-custom-tab'],
    refreshCurPage: ['refreshCurPage']
  },
  context: null,
  _handle(alias, ...arg) {
    let {context, maps} = tabControl
    let arr = maps[alias]
    let name

    if (Vue.isHUI20) {
      name = arr[1]
      return Vue.Event.triggerFrame(name, ...arg)
    } else {
      name = arr[0]
      return context.$store.dispatch(name, ...arg)
    }
  },
  refreshCurPage(...arg) {
    let {context} = tabControl
    if (!Vue.isHUI20 && context.$route.meta.isKeepAlive) {
      context.$store.dispatch('refreshCurPage', ...arg)
    }
  },
  delCurVisitedViews(...arg) {
    let {_handle, context} = tabControl
    if (!Vue.isHUI20 && typeof arg[0] === 'function') {
      let fn = arg.shift()
      let view = context.$store.state.app.visitedViews.find(fn)
      arg.unshift(view)
    }
    return _handle('delCurVisitedViews', ...arg)
  }
}

function tabHandler (context, alias, ...arg) {
  if (tabControl[alias]) {
    tabControl.context = context
    return tabControl[alias](...arg)
  }
}

const logout = () => {
  try {
    if (window.__UCPStore) {
      window.__UCPStore.dispatch('Logout').then(() => {
        location.reload()
      })
    } else if (Vue.isHUI20) {
      Vue.Event.triggerFrame('logout')
    }
  } catch(e) {
    console.log("logout error:" + e)
  }
}

export {
  getStorageInfo,
  tabHandler,
  logout
}
