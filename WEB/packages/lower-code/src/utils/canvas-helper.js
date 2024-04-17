import html2canvas from 'html2canvas'

// https://stackoverflow.com/questions/12168909/blob-from-dataurl
function dataURItoBlob (dataURI) {
  // convert base64 to raw binary data held in a string
  // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
  var byteString = atob(dataURI.split(',')[1])

  // separate out the mime component
  var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

  // write the bytes of the string to an ArrayBuffer
  var ab = new ArrayBuffer(byteString.length)

  // create a view into the buffer
  var ia = new Uint8Array(ab)

  // set the bytes of the buffer to the correct values
  for (var i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i)
  }

  // write the ArrayBuffer to a blob, and you're done
  var blob = new Blob([ab], { type: mimeString })
  return blob
}

/**
 * 生成作品封面图(截图)
 * @param {String} selector
 * @param {文件名} fileName
 */
export function takeScreenshot ({
  selector = '#content_edit',
  fileName = `${+new Date()}.png`,
  type = 'file'
} = {}) {
  const el = document.querySelector(selector)
  return new Promise((resolve, reject) => {
    // html2canvas document: https://html2canvas.hertzen.com/configuration
    // allowTaint: Whether to allow cross-origin images to taint the canvas
    // if you use allowTaint: true, the cors image will taint the canvas, and canvas.toDataURL won't work
    // 会对canvas造成污染，导致 canvas.toDataURL 无效
    html2canvas(el, {backgroundColor: null}).then(canvas => {
      // https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/toDataURL
      const dataUrl = canvas.toDataURL('image/png', 0.4)
      const blob = dataURItoBlob(dataUrl)
      // const blob = new Blob([ab], { type: mimeString })
      const file = new window.File([blob], fileName, { type: 'image/png' })

      switch (type) {
        case 'canvas':
          resolve(canvas)
          break
        case 'blob':
          resolve(blob)
          break
        case 'dataUrl':
          resolve(dataUrl)
          break
        default:
          resolve(file)
          break
      }
    })
  })
}

/**
 *
 * @param {*} canvas
 * @param {*} name
 */
export function downLoadCanvas (canvas, name) {
  var a = document.createElement('a')
  a.href = canvas.toDataURL()
  a.download = name
  a.click()
}

/**
 * 下载海报
 */
export function downloadPoster (object = {}) {
  takeScreenshot(Object.assign({ type: 'canvas' }, object)).then(canvas => {
    downLoadCanvas(canvas, new Date())
  })
}

export function saveStills(str){
  let Url = str //图片路径，也可以传值进来
  var triggerEvent = "touchstart"; //指定下载方式
  var blob = new Blob([''], {
      type: 'application/octet-stream'
  }); //二进制大型对象blob
  var url = URL.createObjectURL(blob); //创建一个字符串路径空位
  var a = document.createElement('a'); //创建一个 a 标签
  a.href = Url; //把路径赋到a标签的href上
  a.download = Url.replace(/(.*\/)*([^.]+.*)/ig, "$2").split("?")[0];
  
  var e = new MouseEvent('click', (true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null));
  //派遣后，它将不再执行任何操作。执行保存到本地
  a.dispatchEvent(e);
  URL.revokeObjectURL(url);

}

export function convertImageToBase64(imgUrl, callback) {
  const image = new Image();
  image.crossOrigin='anonymous';
  image.onload = () => {
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    canvas.height = image.naturalHeight;
    canvas.width = image.naturalWidth;
    ctx.drawImage(image, 0, 0);
    const dataUrl = canvas.toDataURL();
    callback && callback(dataUrl)
  }
  image.src = imgUrl;
}