import Vue from 'vue'
import engine from './engine'
import { Dialog, Toast, Tab, Tabs, Popup, List, Icon, Checkbox, CheckboxGroup, Radio, RadioGroup, Field } from 'vant'
import { ismPaaSOS, nativeReady } from '@winner-fed/mpaas-jsapi'
import '../../styles/libs/animate.min.css'
import 'vant/lib/dialog/style'
import 'vant/lib/toast/style'
import 'vant/lib/tab/style'
import 'vant/lib/tabs/style'
import 'vant/lib/list/style'
import 'vant/lib/checkbox/style'
import 'vant/lib/checkbox-group/style'
import 'vant/lib/radio/style'
import 'vant/lib/radio-group/style'
import 'vant/lib/icon/style'
import 'vant/lib/field/style'
import VueClipboard from 'vue-clipboard2'
import MsgPop from '@Components/MsgPop'
/*
* 安卓4.4.3一下的手机还是不支持Promise的,需要引入npm install babel-polyfill和npm install babel-runtime，在入口文件上加上即可
* import 'babel-polyfill';
*/
// import 'babel-polyfill'
// 过滤器
import './filters'

// 全局注册
Vue.use(Dialog)
Vue.use(Toast)
Vue.use(Tab)
Vue.use(Tabs)
Vue.use(Popup)
Vue.use(List)
Vue.use(VueClipboard)
Vue.use(MsgPop)
Vue.use(Radio)
Vue.use(RadioGroup)
Vue.use(Checkbox)
Vue.use(CheckboxGroup)
Vue.use(Icon)
Vue.use(Field)

if (ismPaaSOS()) {
  nativeReady(() => {
    new Vue({
      render(h) {
        return h(engine)
      },
    }).$mount('#app')
  })
} else {
  new Vue({
    render(h) {
      return h(engine)
    },
  }).$mount('#app')
}
