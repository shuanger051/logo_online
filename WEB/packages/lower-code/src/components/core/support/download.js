import {picCache} from './imgUrl'
import * as XLSX from 'xlsx'
import {appUploadExcelBase64APIOSS} from "core/api"


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
      text: pluginProps.text.split(/<[^>]*\/?>|[\n\t]/).filter((t) =>!!t).join(' || '),
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
              width: work.width,
              height: work.height
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


export const createXLSL = (work, config={type: 'base64'}) => {
  const json = parse(work);
  const workbook = XLSX.utils.book_new();

  Object.entries(json).forEach(([key, item]) => {
    const worksheet = XLSX.utils.json_to_sheet(item.value);
    XLSX.utils.book_append_sheet(workbook, worksheet, item.name);
    XLSX.utils.sheet_add_aoa(worksheet, [item.label], {
      origin: "A1",
    }); 
  })
  if (config.type == 'base64') {
    return XLSX.write(workbook, {bookType:'xlsx', type: "base64" })
  } else {
    return XLSX.writeFile(workbook, '店招.xlsx')
  }
}


export const downLoadXLSL = async (work) => {
  if (typeof ZWJSBridge == 'undefined') {
    return createXLSL(work, {type: 'file'})
  } else {
    let base64 = createXLSL(work);
    base64 = 'data:application/vnd.ms-excel;base64,' + base64
    const info = await appUploadExcelBase64APIOSS({
      base64
    })
    return download(info.data.urlPath, '店招.xlsx')
  }
}


export const download = async (url, name) => {
  if (typeof ZWJSBridge == 'undefined') {
    var a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    return true
  } else {
    return ZWJSBridge.saveImage({
      url: url
    })
  }
}