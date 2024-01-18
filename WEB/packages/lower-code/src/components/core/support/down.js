import { generateAsync } from 'jszip'
import {picCache} from './imgUrl'
import jszip from 'jszip'

const parseText = {
  valid(element) {
    return element.name == 'lbp-text-tinymce'
  },
  beforeParse(_, obj) {
    if (!obj.text) {
      obj.text = {
        name:'文字',
        value:[],
        label: ['文字颜色', '字体', '字体大小', '文字', '宽', '高', '距离顶部', '距离左边']
      }
    } 
  },
  parse(element, obj) {
    let {pluginProps, commonStyle} = element
    this.beforeParse(element, obj)
    let props = {
      fontColor: pluginProps.fontColor,
      fontFamily: pluginProps.fontFamily,
      fontSize: pluginProps.fontSize,
      text: pluginProps.text.split(/<[^>]\/?>/).join(),
      width: commonStyle.width,
      height:commonStyle.height,
      top:commonStyle.top,
      left:commonStyle.left
    }
    obj.text.value.push(props)
  }
}

const parsePic = {
  valid(element) {
    return element.name == 'lbp-picture'
  },
  beforeParse(_, obj) {
    if (!obj.pic) {
      obj.pic = {
        name:'图片',
        value: [],
        label: ['图片链接', 'x-旋转', 'y-旋转', '透明度', '宽', '高', '距离顶部', '距离左边']
      }
    } 
  },
  parse(element, obj) {
    let {pluginProps, commonStyle} = element
    this.beforeParse(element, obj)
    let props = {
      imgSrc: picCache.get(pluginProps.imgSrc) || pluginProps.imgSrc,
      xRate: pluginProps.xRate,
      yRate: pluginProps.yRate,
      opacity: pluginProps.opacity,
      width:commonStyle.width,
      height:commonStyle.height,
      top:commonStyle.top,
      left:commonStyle.left
    }
    obj.pic.value.push(props)
  }
}

const parseWork = {
  parse(work, obj) {
      obj.work = {
          name: '作品',
          label: ['作品宽','作品高'],
          value: [
            {
              width: obj.width,
              height: obj.height
            }
          ]
      }
      
  }
}

export const parse = (work, obj = {}) => {
  parseWork.parse(work, obj)
  let handles = [parseText,parsePic]
  let list = work.pages[0].elements
  for (let i=0; i <list.length; i++) {
    handles.some((handle) => {
      if (handle.valid(list[i])) {
        handle.parse(list[i], obj)
        return true
      }
    })
  }
  return obj
}


export const async generateZip = (work, obj) => {
  
}