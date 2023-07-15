const makerAsap = () => {
  let handler

  if (typeof setImmediate !== 'undefined') {
    handler = setImmediate
  } else if (typeof process !== 'undefined' && process.nextTick) {
    handler = process.nextTick
  } else {
    handler = (fn) => setTimeout(fn, 0)
  }
  return handler
}

const getListenerAsObject = (handler) => {
  let listener

  if (typeof handler === 'object') {
    listener = handler
    handler = listener.handler
  }
  listener = listener || {}
  listener.handler = handler

  if (!handler) {
    warn(`event listener must be a function`)
    return false
  }
  return listener
}

let asap = makerAsap()
let RegHook = /-hook/
// let RegFilter = /(-hook:)|(assign:)/
let noProd = process.env.NODE_ENV !== 'production'
let warn = (() => {
  if (noProd) {
    return (msg) => { console && console.error('[Event]: %s', msg) }
  } else {
    return () => {}
  }
})()
const makeHook = function(cb) {
  return function(event) {
    // Prevent infinite loops
    if (!RegHook.test(event)) {
      cb.apply(this, arguments)
    }
  }
}
const preHook = makeHook(function(event, ...args) {
  this.emit('pre-hook:' + event, ...args)
})
const offHook = makeHook(function(event, ...args) {
  this.emit('off-hook:' + event, ...args)
})
const afterHook = makeHook(function(event, ...args) {
  this.emit('after-hook:' + event, ...args)
})
const allHook = makeHook(function(event) {
  this.hasAll && this.emit('all-hook', event)
})
/**
 * Emitter
 */
class Emitter {
  static mixin(parent = {}) {
    let keys = Object.keys(parent)
    for (var i = 0; i < keys.length; i++) {
      let key = keys[i]
      let value = parent[key]

      if (!Emitter.prototype[key]) {
        Emitter.prototype[key] = value
      }
    }
    return Emitter
  }

  on(event, handler) {
    let listener = getListenerAsObject(handler)

    if (!listener) { return this }

    this._listeners = this._listeners || {};

    (
      this._listeners['$' + event] =
      this._listeners['$' + event] || []
    )
      .push(listener)

    return this
  }

  once(event, handler) {
    this.on(event, {
      handler,
      count: 1
    })
    return this
  }

  off(event, handler) {
    let listeners, listener
    let len = arguments.length

    this._listeners = this._listeners || {}

    if (len === 0) {
      this._listeners = {}
      return this
    }

    listeners = this._listeners['$' + event]

    if (!listeners) return this

    if (len === 1) {
      delete this._listeners['$' + event]
      return this
    } else {
      for (let i = 0; i < listeners.length; i++) {
        listener = listeners[i]
        if (handler === listener.handler ||
          handler === listener) {
          listeners.splice(i, 1)
          // 当删除事件时同步触发offhook
          offHook.call(this, event)
          break
        }
      }

      if (!listeners.length) {
        // TODO:
        // 删除事件时是否需要删除hook？
        delete this._listeners['$' + event]
      }
    }
    return this
  }

  emit(...args) {
    let event = args[0]
    let parmas = args.slice(1)
    let listener
    this._listeners = this._listeners || {}
    let listeners = this._listeners['$' + event]

    preHook.apply(this, args)

    if (listeners) {
      for (let i = 0, len = listeners.length; i < len; ++i) {
        listener = listeners[i]
        listener.value =
          listener.handler.apply(listener, parmas)

        if (
          (listener.count && !--listener.count) ||
          (listener.value && typeof listener.value == 'object' && listener.value.delete)
        ) {
          this.off(event, listener.handler)
          i--
        }
      }
    }

    afterHook.apply(this, args)

    allHook.call(this, event)
    return this
  }

  emitLatter(...args) {
    asap(() => this.emit.apply(this, args))
    return this
  }

  /**
   * 发送异步事件
   *
   * @return promise
   *
   * example:
   *
   * event.on("evt1", () => new Promise((res, rej) => res() ))
   * event.emitAsync("evt1").then(callback)
   */

  emitAsync(...args) {
    let promise = []
    let event = args[0]
    let listeners = this.listeners('$' + event)

    if (listeners.length) {
      this.emit(...args)

      listeners.forEach((listener) => {
        promise.push(listener.value)
      })

      return Promise.all(promise)
    }

    return Promise.reject(new Error('No current type event'))
  }

  after(event, ...args) {
    this.on.apply(
      this,
      [`after-hook:${event}`].concat(args)
    )
  }
  before(event, ...args) {
    this.on.apply(
      this,
      [`pre-hook:${event}`].concat(args)
    )
  }

  allEvents(handler) {
    this.hasAll = true
    this.on('all-hook', handler)
  }

  broadcast(...args) {
    let {children} = this

    this.emit.apply(this, args)

    if (Array.isArray(children)) {
      for (var i = 0, len = children.length; i < len; i++) {
        let child = children[i]
        if (child && typeof child.broadcast === 'function') {
          child.broadcast.apply(child, args)
        }
      }
    }
    return this
  }

  listeners(event) {
    this._listeners = this._listeners || {}
    return this._listeners['$' + event] || []
  }

  hasListeners(event) {
    return !!this._listeners(event).length
  }
}

export default Emitter
