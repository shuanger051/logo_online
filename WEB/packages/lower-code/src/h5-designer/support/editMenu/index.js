import { mapGetters, mapState } from 'vuex'
import './index.scss'

function isRegExp (value) {
  return value instanceof RegExp
}

// 垂直菜单
const contextmenuOptions = [
  {
    label: '复制',
    value: 'copy',
    hotkeyTooltip: 'Ctrl + C',
    elementBlackList: ['hs-cms-formExt-result', 'hs-cms-form-submit']
  },
  {
    label: '删除',
    value: 'delete',
    hotkeyTooltip: 'Delete',
    elementBlackList: ['hs-cms-formExt-result']
  }
  // {
  //   label: '置顶',
  //   value: 'move2Top'
  // },
  // {
  //   label: '置底',
  //   value: 'move2Bottom'
  // },
  // {
  //   label: '上移',
  //   value: 'addZindex'
  // },
  // {
  //   label: '下移',
  //   value: 'minusZindex'
  // }
]

export default {
  computed: {
    ...mapGetters('cms/editState', ['selectedElementData']),
    ...mapState('cms/editState', [
      'clipboard'
    ]),
    filteredOptions () {
      if (!this.selectedElementData) {
        if (this.clipboard.length > 0) {
          return [{
            label: '粘贴',
            value: 'paste',
            hotkeyTooltip: 'Ctrl + V'
          }]
        } else {
          return []
        }
      } else {
        const elementName = this.selectedElementData.name
        const filteredOptions = contextmenuOptions.filter(option => {
          const wl = option.elementWhiteList
          const bl = option.elementBlackList
          if (wl) {
            if (Array.isArray(wl)) return wl.includes(elementName)
            if (isRegExp(wl)) return wl.test(elementName)
          }
          if (bl) {
            if (Array.isArray(bl)) return !bl.includes(elementName)
            if (isRegExp(bl)) return !bl.test(elementName)
          }
          return true
        })
        return filteredOptions
      }
    }
  },
  props: {
    position: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    handleSelectMenu (selectedKeys) {
      this.$emit('select', { selectedKeys })
    }
  },
  render (h) {
    return (
      <div>
        {
        this.filteredOptions.length > 0 ?
        <h-card
          shadow
          class="editmenu"
        >
          <ul
            class="editmenu-content"
          >
            {
              this.filteredOptions.map(option => (
                <li
                  onClick={() => this.handleSelectMenu(option.value)}
                  key={option.value}
                  class="editmenu-content__item"
                >
                  <div>{option.label}</div>
                  <div>{option.hotkeyTooltip}</div>
                </li>
              ))
            }
          </ul>
        </h-card> : '' }
      </div>
    )
  }
}
