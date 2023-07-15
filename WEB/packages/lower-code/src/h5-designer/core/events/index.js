
import Emitter from './Emitter'
import systemSign from './systemSign'

export default Emitter.mixin({
  get systemSign () {
    return systemSign
  }
})
