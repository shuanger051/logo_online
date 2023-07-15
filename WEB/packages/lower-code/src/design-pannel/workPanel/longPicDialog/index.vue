<template>
  <div>
    <h-msg-box :value="show" @on-close="closeHandler" class="preview-dialog">
      <div>
        <img :src="imgsrc" alt="" />
      </div>
      <div slot="footer">
        <h-button @click="closeHandler">取消</h-button>
      </div>
    </h-msg-box>
  </div>
</template>

<script>
import html2canvas from 'html2canvas'

export default {
  props: ['show'],
  data() {
    return {
      imgsrc: ''
    }
  },
  components: {
    preview
  },
  watch: {
    show: {
      handler(val) {
        if (val) {
          this.$nextTick(() => {
            let dom = document.querySelector('.preview-wrap') || document.querySelector('.preview-wrap1')
            this.drawLongPic(dom)
          })
        }
      },
      deep: true
    }
  },
  methods: {
    dataURItoBlob (dataURI) {
      var byteString = atob(dataURI.split(',')[1])
      var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]
      var ab = new ArrayBuffer(byteString.length)
      var ia = new Uint8Array(ab)
      for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i)
      }
      var blob = new Blob([ab], { type: mimeString })
      return blob
    },
    closeHandler() {
      this.$emit('update:show', false)
    },
    getPixelRatio(context) {
      var backingStore =
        context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio ||
        1
      return (window.devicePixelRatio || 1) / backingStore
    },
    drawLongPic(dom) {
      var shareContent = document.querySelector('.preview-panel')
      var width = shareContent.offsetWidth // 获取(原生）dom 宽度
      var height = shareContent.offsetHeight // 获取(原生）dom 高
      var offsetTop = shareContent.offsetTop
      let that = this
      let canvas = document.createElement('canvas') // 创建canvas 对象
      let context = canvas.getContext('2d')
      let scaleBy = this.getPixelRatio(context) // 获取像素密度
      canvas.width = width * scaleBy
      canvas.height = (height + offsetTop) * scaleBy
      context.scale(scaleBy, scaleBy)
      let opts = {
        async: true,
        allowTaint: true, // 允许加载跨域的图片
        tainttest: true, // 检测每张图片都已经加载完成
        scale: scaleBy, // 添加的scale 参数
        canvas: canvas, // 自定义 canvas
        logging: true, // 日志开关，发布的时候记得改成false
        width: width, // dom 原始宽度
        height: height // dom 原始高度
      }
      html2canvas(dom, opts).then(function(canvas) {
        let dataUrl = canvas.toDataURL('image/png', 0.6)
        that.imgsrc = dataUrl
      })
    }
  }
}
</script>
