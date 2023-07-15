import {dispatch} from './controller'

export const addNativeEvent = (el, event, handler) => {
  let off = () => el.removeEventListener(event, handler, false)
  el.addEventListener(event, handler, false)

  return off
}

export const nativeEventAdder = (store = []) => {
  return {
    add(...args) {
      let off = addNativeEvent(...args)
      store.push(off)
    },
    off: dispatch(store)
  }
}
