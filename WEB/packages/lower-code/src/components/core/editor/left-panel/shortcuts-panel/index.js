import ShortcutButton from './shortcut-button'
import UsageTip from './usage-tip'
import LoadNpmPlugins from './load-npm-plugins.vue'
import dragMixin from 'core/mixins/drag'
import loadPluginsMixin from 'core/plugins/index'
import { mapActions } from 'vuex'

export default {
  mixins: [ dragMixin, loadPluginsMixin],
  data: () => ({
    npmPackages: []
  }),
  methods: {
    ...mapActions('editor', [
      'elementManager',
      'pageManager',
      'saveWork',
      'setEditingPage'
    ]),
    ...mapActions('loading', {
      updateLoading: 'update'
    }),
    /**
     * !#zh 点击插件，copy 其基础数据到组件树（中间画布）
     * #!en click the plugin shortcut, create new Element with the plugin's meta data
     * pluginInfo {Object}: 插件列表中的基础数据, {name}=pluginInfo
     *
     * elementShortcutConfig: PluginListItem = {
      name: String,
      shortcutProps: {}
     }
     */
    clone (elementShortcutConfig) {
      this.elementManager({
        type: 'add',
        value: elementShortcutConfig
      })
    }

  },
  /**
   * #!zh: 在左侧或顶部导航上显示可用的组件快捷方式，用户点击/拖拽之后，即可将其添加到中间画布上
   * #!en: render shortcust at the sidebar or the header.
   * if user click/drag the shortcut, the related plugin will be added to the canvas
   */
  render (h) {
    // return this.renderShortCutsPanel(this.groups)
    return (
      <div style={{ background: '#fff', padding: '12px', paddingLeft: 0, width:'300px', height:'100%' }}>
      <a-row gutter={10} style="max-height: calc(100vh - 150px);overflow: auto;margin:0;">
        <UsageTip />
        {
          [].concat(this.pluginsList, this.npmPackages)
            .filter(plugin => plugin.visible)
            .map(plugin => (
              <a-col span={12} style={{ marginTop: '10px' }}>
                <ShortcutButton
                  clickFn={this.clone.bind(this, plugin)}
                  mousedownFn={this.handleDragStartFromMixin.bind(this, plugin)}
                  // title={plugin.title}
                  title={plugin.i18nTitle[this.currentLang] || plugin.title}
                  faIcon={plugin.icon}
                  disabled={plugin.disabled}
                />
              </a-col>
            ))
        }
      </a-row>
      </div>
    )
  }
}
