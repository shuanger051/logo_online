import ePhoneShape from '../model/e-phone-shape'
import { resolvePreviewElementStyle, resolvePreviewShapeStyle } from '../utils'
import { cloneDeep } from 'lodash'
import { baseOMSUrl } from '@Utils/request'

export default {
  props: [
    'page',
    'elements',
    'events',
    'works',
    'works_id',
    'runtimeContext',
    'works_title',
    'isPreview',
    'allPages',
    'viewType',
    'publish_date_time',
    'works_code',
    'act_preview', // 活动使用勿删
    'mission_ids' // 活动使用勿删
  ],
  components: { ePhoneShape },
  data() {
    return {
      loading: false,
    }
  },
  created() {
    this.events.forEach((item) => {
      if (item.result && item.result.params && item.result.params.worksInfo) {
        item.result.params.worksInfo.works_id = this.works_id
        item.result.params.worksInfo.works_title = this.works_title
        item.result.params.worksInfo.publish_date_time = this.publish_date_time
      }
      if (item.result && item.result.value && item.result.value == 'skipPage') {
        if (item.result.params && item.result.params.out_url !== '') {
          const index = this.allPages.findIndex((j) => {
            return j.uuid == item.result.params.out_url
          })
          if (index == -1) {
            item.result.params.out_url = ''
          }
        }
      }
    })
    this.runtimeContext.mode.resolveEvents(this.events)
  },
  render() {
    let { runtimeContext } = this
    let { mode } = runtimeContext
    return (
      <div class="phone-page" style="height: calc(100vh); overflow-x:hidden;overflow-y: auto">
        {this.elements.map((element, index) => {
          let { uuid, name, style, property, extra } = element
          let styleTransfer = cloneDeep(style)
          styleTransfer['position'] = 'static'
          styleTransfer['border-width'] = 0
          styleTransfer['border-radius'] = 0
          styleTransfer['overflow'] = 'hidden'
          style['z-index'] = index + 1
          style['background-size'] = 'cover'
          if (/preview\s*=\s*true/.test(location.search)) {
          } else {
            for (let i in property) {
              if (typeof property[i] == 'string') {
                if (property[i].indexOf('file_guid') > -1) {
                  const index = property[i].lastIndexOf('/')
                  const str = property[i].substring(index + 1, property[i].length)
                  property[i] = `${baseOMSUrl}/${str}`
                }
                if (property[i].indexOf(window.CMS_CONFIG.PCFSTORE_IP) > -1) {
                  const indexHttp = property[i].indexOf('http://')
                  const indexHttps = property[i].indexOf('https://')
                  if (indexHttp > -1) {
                    const str = property[i].substring(indexHttp + 7, property[i].length)
                    const index1 = str.indexOf('/')
                    const str1 = str.substr(index1 + 1, property[i].length)
                    property[i] = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
                  }
                  if (indexHttps > -1) {
                    const str = property[i].substring(indexHttps + 8, property[i].length)
                    const index1 = str.indexOf('/')
                    const str1 = str.substr(index1 + 1, property[i].length)
                    property[i] = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
                  }
                }
                if (property[i].indexOf('aliyun') > -1) {
                  if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
                    const indexHttp = property[i].indexOf('http://')
                    const indexHttps = property[i].indexOf('https://')
                    if (indexHttp > -1) {
                      const str = property[i].substring(indexHttp + 7, property[i].length)
                      const index1 = str.indexOf('/')
                      const str1 = str.substr(index1 + 1, property[i].length)
                      property[i] = `${window.CMS_CONFIG.ALIYUN_DOMAIN}/${str1}`
                    }
                    if (indexHttps > -1) {
                      const str = property[i].substring(indexHttps + 8, property[i].length)
                      const index1 = str.indexOf('/')
                      const str1 = str.substr(index1 + 1, property[i].length)
                      property[i] = `${window.CMS_CONFIG.ALIYUN_DOMAIN}/${str1}`
                    }
                  } else if (window.CMS_CONFIG.USE_ALIYUN_PROXY && window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true') {
                    const index = property[i].indexOf('outin')
                    const str = property[i].substring(index, property[i].length)
                    property[i] = `${window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS}/${str}`
                  } else {
                    const index = property[i].indexOf('outin')
                    const str = property[i].substring(index, property[i].length)
                    property[i] = `https://${str}`
                  }
                }
              }
            }
            if (property.imgList && property.imgList.length) {
              property.imgList.forEach((item) => {
                if (typeof item.src == 'string') {
                  if (item.src.indexOf('file_guid') > -1) {
                    const index = item.src.lastIndexOf('/')
                    const str = item.src.substring(index + 1, item.src.length)
                    item.src = `${baseOMSUrl}/${str}`
                  }
                  if (item.src.indexOf(window.CMS_CONFIG.PCFSTORE_IP) > -1) {
                    const indexHttp = item.src.indexOf('http://')
                    const indexHttps = item.src.indexOf('https://')
                    if (indexHttp > -1) {
                      const str = item.src.substring(indexHttp + 7, item.src.length)
                      const index1 = str.indexOf('/')
                      const str1 = str.substr(index1 + 1, item.src.length)
                      item.src = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
                    }
                    if (indexHttps > -1) {
                      const str = item.src.substring(indexHttps + 8, item.src.length)
                      const index1 = str.indexOf('/')
                      const str1 = str.substr(index1 + 1, item.src.length)
                      item.src = `${window.CMS_CONFIG.H5FSTORE_IP}/${str1}`
                    }
                  }
                  if (item.src.indexOf('aliyun') > -1) {
                    if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
                      const indexHttp = item.src.indexOf('http://')
                      const indexHttps = item.src.indexOf('https://')
                      if (indexHttp > -1) {
                        const str = item.src.substring(indexHttp + 7, item.src.length)
                        const index1 = str.indexOf('/')
                        const str1 = str.substr(index1 + 1, item.src.length)
                        item.src = `${window.CMS_CONFIG.ALIYUN_DOMAIN}/${str1}`
                      }
                      if (indexHttps > -1) {
                        const str = item.src.substring(indexHttps + 8, item.src.length)
                        const index1 = str.indexOf('/')
                        const str1 = str.substr(index1 + 1, item.src.length)
                        item.src = `${window.CMS_CONFIG.ALIYUN_DOMAIN}/${str1}`
                      }
                    } else if (
                      window.CMS_CONFIG.USE_ALIYUN_PROXY &&
                      window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true'
                    ) {
                      const index = item.src.indexOf('outin')
                      const str = item.src.substring(index, item.src.length)
                      item.src = `${window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS}/${str}`
                    } else {
                      const index = item.src.indexOf('outin')
                      const str = item.src.substring(index, item.src.length)
                      item.src = `https://${str}`
                    }
                  }
                }
              })
            }
          }
          let Ctr = runtimeContext.getComponent(uuid, name, mode.resolveComponentMixIn(uuid))
          return (
            <e-phone-shape extra={extra} style={resolvePreviewShapeStyle(style)} key={uuid} id={uuid}>
              {h(Ctr, {
                props: {
                  style: resolvePreviewElementStyle(styleTransfer),
                  property, //: Object.assign(runtimeContext.configGetter.getConfig(name).defaultProperty, property), // 保留历史组件属性数据
                  context: {
                    ...mode.getCurrentModeApi(),
                  },
                  allElements: this.elements,
                  works_id: this.works_id,
                  isPreview: this.isPreview,
                  uuid,
                  viewType: this.viewType,
                  works_code: this.works_code,
                  act_preview: this.act_preview,
                  mission_ids: this.mission_ids
                },
                style: resolvePreviewElementStyle(style),
              })}
            </e-phone-shape>
          )
        })}
      </div>
    )
  },
}
