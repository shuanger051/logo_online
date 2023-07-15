/**
 * 编辑模式
 */
export default class Edit {
  constructor(runtime) {
    this.runtime = runtime
  }
  /**
   * 编辑模式下提供二开的api
   * @returns
   */
  getCurrentModeApi() {
    return {
      mode: 'edit',
      eventController: this.runtime.eventController
    }
  }

  destroy() {}
}
