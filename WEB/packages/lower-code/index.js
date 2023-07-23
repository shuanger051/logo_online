class editorConfig {
  initI18n (I18n) {
    this.i18nInstance = I18n
  }
  get i18n () {
    return this.i18nInstance
  }
}
export default new editorConfig()