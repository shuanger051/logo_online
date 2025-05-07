import config from '@/config'
import {ADMIN} from '@/config/default'
import route from '@/router/config'

export default {
  namespaced: true,
  state: {
    isMobile: false,
    animates: ADMIN.animates,
    palettes: ADMIN.palettes,
    pageMinHeight: 0,
    menuData: [],
    activatedFirst: undefined,
    ...config,
  },
  getters: {
    menuData(state, getters, rootState) {
      
      return state.menuData
    }
  },
  mutations: {
    setDevice (state, isMobile) {
      state.isMobile = isMobile
    },
    setTheme (state, theme) {
      state.theme = theme
    },
    setLayout (state, layout) {
      state.layout = layout
    },
    setMultiPage (state, multiPage) {
      state.multiPage = multiPage
    },
    setAnimate (state, animate) {
      state.animate = animate
    },
    setWeekMode(state, weekMode) {
      state.weekMode = weekMode
    },
    setFixedHeader(state, fixedHeader) {
      state.fixedHeader = fixedHeader
    },
    setFixedSideBar(state, fixedSideBar) {
      state.fixedSideBar = fixedSideBar
    },
    setLang(state, lang) {
      state.lang = lang
    },
    setHideSetting(state, hideSetting) {
      state.hideSetting = hideSetting
    },
    correctPageMinHeight(state, minHeight) {
      state.pageMinHeight += minHeight
    },
    setMenuData(state, menuData) {
      state.menuData = menuData
    },
    setAsyncRoutes(state, asyncRoutes) {
      state.asyncRoutes = asyncRoutes
    },
    setPageWidth(state, pageWidth) {
      state.pageWidth = pageWidth
    },
    setActivatedFirst(state, activatedFirst) {
      state.activatedFirst = activatedFirst
    },
    setFixedTabs(state, fixedTabs) {
      state.fixedTabs = fixedTabs
    },
    setCustomTitle(state, {path, title}) {
      if (title) {
        const obj = state.customTitles.find(item => item.path === path)
        if (obj) {
          obj.title = title
        } else {
          state.customTitles.push({path, title})
        }
        sessionStorage.setItem(process.env.VUE_APP_TBAS_TITLES_KEY, JSON.stringify(state.customTitles))
      }
    }
  }
}
