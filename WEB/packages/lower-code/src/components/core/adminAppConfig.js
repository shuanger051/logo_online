import createConfig from './Config'
import Vue from "vue";
import AsyncImage from 'core/plugins/AsyncImage'


let saveSucessJumpFn = () => {}
const initSaveSucessJump = (fn) => {
  saveSucessJumpFn = fn
}
Vue.component(AsyncImage.name, AsyncImage)


const extraObj = {}

export default createConfig(() => {
  return {
    instanceHandler: {
      handlerSaveSucessJump() {
        return saveSucessJumpFn()
      },
      getExtraObj() {
        return extraObj
      }
    },
    installHandler: {
      initSaveSucessJump,
      extraFn(obj) {
        Object.assign(extraObj, obj)
      }
    }
  }
})