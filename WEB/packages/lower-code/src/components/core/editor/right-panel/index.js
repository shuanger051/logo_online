import RenderPropsEditor from './props'
import RenderAnimationEditor from './animation'
import RenderActionEditor from './action'
import RenderBackgroundEditor from './background'
export default {
  name: 'ElementPropsEditor',
  props: {
    width: {
      type: Number,
      default: 320
    }
  },
  data: () => ({
    activeTabKey: '作品'
  }),
  methods: {
    /**
     * #!zh: 设置 页面图tab 作为 active tab
     * #!en: set background(bg) tab as active tab
     */
    setActiveTab (activeTabKey) {
      this.activeTabKey = activeTabKey
    }
  },
  render (h) {
    return (
      <a-layout-sider
        width={this.width}
        data-set-width={this.width}
        theme='light'
        style={{ background: '#fff', padding: '0 6px 0 6px' }}
      >
        <a-tabs
          style="height: 100%;"
          tabBarGutter={10}
          defaultActiveKey={this.activeTabKey}
          activeKey={this.activeTabKey}
          onChange={this.setActiveTab}
        >
          <a-tab-pane key="属性"><span slot="tab">{this.$t('editor.editPanel.tab.prop')}</span><RenderPropsEditor/></a-tab-pane>
          <a-tab-pane label="作品" key='作品' tab={this.$t('editor.editPanel.tab.page')}>{ this.activeTabKey === '作品' && <RenderBackgroundEditor/> }</a-tab-pane>
        </a-tabs>
      </a-layout-sider>
    )
  },
  created () {
    window.EditorApp.$on('setEditingElement', ({ name }) => {
      this.setActiveTab(name === 'lbp-background' ? '页面' : '属性')
    })
  }
}
