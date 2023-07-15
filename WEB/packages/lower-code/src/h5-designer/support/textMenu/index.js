import ColorSelect from '@Components/ColorSelect'
import { cloneDeep } from 'lodash'
import './index.scss'

const btnList = [
  {
    title: '加粗',
    icon: 'icon-jiacu',
    label: 'font-weight',
    value: 'bold'
  },
  {
    title: '倾斜',
    icon: 'icon-qingxie-',
    label: 'font-style',
    value: 'italic'
  },
  {
    title: '下划线',
    icon: 'icon-ziyuan',
    label: 'text-decoration',
    value: 'underline'
  },
  {
    title: '删除线',
    icon: 'icon-strikethrough',
    label: 'text-decoration',
    value: 'line-through'
  }
]

export default {
  computed: {
  },
  data: {
  },
  props: {
    property: {
      type: Object,
      default: () => {}
    },
    context: {
      type: Object,
      default: () => {}
    },
    uuid: {
      type: String,
      default: () => ''
    }
  },
  methods: {
    changeSize(type, event) {
      event.preventDefault()
      event.stopPropagation()
      let num = cloneDeep(this.property['font-size'])
      if (type == 'add') {
        num = Number(num) + 1
      } else {
        num = Number(num) - 1
      }
      this.updateFunc('font-size', num)
    },
    updateFunc(name, value) {
      const data = {
        name: name,
        value: value,
        uuid: this.uuid
      }
      this.$emit('updateText', data)
    }
  },
  render (h) {
    return (
      <div class="top-bar"
        onClick={(e) => {
          e.preventDefault()
          e.stopPropagation()
        }}>
        <div class="font-menu">
          {
            <div class="font-item font-size-box">
              {
                <div onClick={(e) => this.changeSize('add', e)}>
                  <h-icon name="android-add" class="icon-btn"></h-icon>
                </div>
              }
              {
                <div
                  onClick={(e) => {
                    e.preventDefault()
                    e.stopPropagation()
                  }}
                >
                  {
                    h('h-typefield', {
                      props: {
                        integerNum: 9,
                        suffixNum: 0,
                        nonNegative: true,
                        setNull: true,
                        type: 'money',
                        min: 12,
                        value: this.property['font-size']
                      },
                      class: 'input-box',
                      on: {
                        'on-blur': (event) => {
                          let num = event.target.value
                          if (num && Number(num) < 12) {
                            num = 12
                          }
                          console.log(num)
                          this.updateFunc('font-size', num)
                        }
                      }
                    })
                  }
                </div>
              }
              {
                <div onClick={(e) => this.changeSize('sub', e)}>
                  <h-icon name="android-remove" class="icon-btn" ></h-icon>
                </div>
              }
            </div>
          }
          {
            <div
              class="font-style-box"
              onClick={(e) => {
                e.preventDefault()
                e.stopPropagation()
              }}
            >
              {
                btnList.map(item => (
                  <div class={ ['font-style-item', this.property[item.label] === item.value ? 'font-style-item-active' : ''] } key={item.value}>
                    <span
                      class={ ['iconfont', item.icon] }
                      title={ item.title }
                      onClick={() => {
                        if (this.property[item.label] === item.value) {
                          this.updateFunc(item.label, '')
                        } else {
                          this.updateFunc(item.label, item.value)
                        }
                      }}
                    ></span>
                  </div>
                ))
              }
            </div>
          }
          {
            <div
              onClick={(e) => {
                e.preventDefault()
                e.stopPropagation()
              }}
            >
              <ColorSelect
                class="font-color-box"
                style={{ borderColor: this.property['color'] }}
                selectedColor={ this.property['color'] }
                emptyHeight="26"
                emptyWidth="26"
                placement="bottom"
                onUpdateColor={(e) => {
                  this.updateFunc('color', e)
                }}
              />
            </div>
          }
        </div>
      </div>
    )
  }
}
