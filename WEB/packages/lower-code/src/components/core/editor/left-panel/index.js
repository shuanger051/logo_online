import RenderShortcutsPanel from './shortcuts-panel/index'
import RenderPageManager from './page-manager/index'
import RenderPageTree from './page-tree/index'
import RenderPagePreferences from './preferences/index'
import RenderDataSourcePanel from 'core/editor/data-source/index.js'

export default {
  name: 'EditorLeftPanel',
  render (h) {
    return (
      <div>
      <RenderShortcutsPanel />
      </div>
    )
  }
}
