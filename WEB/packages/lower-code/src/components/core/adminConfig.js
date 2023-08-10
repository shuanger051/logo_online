import createConfig from './Config'



let saveSucessJumpFn = () => {}
const initSaveSucessJump = (fn) => {
  saveSucessJumpFn = fn
}

export default createConfig(() => {
  return {
    instanceHandler: {
      handlerSaveSucessJump() {
        return saveSucessJumpFn()
      }
    },
    installHandler: {
      initSaveSucessJump
    }
  }
})