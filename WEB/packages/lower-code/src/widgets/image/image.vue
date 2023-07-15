<template>
  <div class="img-box">
    <img
      :src="propertyProxy.src || defaultImg"
      alt=""
      width="100%"
      height="100%"
      @dblclick="showUploadImg">
    <img-upload
      ref="imgUploadRef"
      v-if="showupload"
      class="img-upload"
      v-model="propertyProxy.src"
      :size="5120"
      :accept="['png', 'jpg','jpeg', 'gif']"
    ></img-upload>
  </div>
</template>

<script>
import defaultImg from '@Root/assets/images/defaultImg.png'
import ImgUpload from '@Components/ImgUpload'

export default {
  props: ['context', 'property', 'style', 'active','selectedElement', 'selectedPage'],
  components: {
    ImgUpload
  },
  computed: {
    propertyProxy() {
      return new Proxy(this.property, {
        get: (target, name) => {
          return this.property[name] || ''
        },
        set: (target, name, value) => {
          if (value == '') {
            this.updateFunc(375, 185, name, value)
          } else {
            const that = this
            const img = new Image()
            img.src = value
            img.onload = function() {
              if (this.width > 375 || this.width == 375) {
                const scale = 375 / this.width
                const newHeight = this.height * scale
                that.updateFunc(375, newHeight, name, value)
                that.updateStyleFunc(375, newHeight)
              } else {
                that.updateFunc(this.width, this.height, name, value)
                that.updateStyleFunc(this.width, this.height)
              }
            }
          }
          return true
        }
      })
    }
  },
  data () {
    return {
      defaultImg: defaultImg,
      showupload: false,
      id: '',
      pageId: ''
    }
  },
  created() {
    this.id = this.selectedElement // 记录下当前的元素的id
    this.pageId = this.selectedPage // 记录下当前页面的id

  },
  watch: {
    active: {
      handler(val) {
        // 本组建选中取消
        if (val && this.showupload) {
          this.showupload = false
        }
      },
      deep: true
    }
  },
  methods: {
    // 更新图片
    updateFunc(width, height, name, value) {
      this.showupload = false
      let { updateElementProperty } = this.context
      updateElementProperty({
        width: width,
        height: height,
        [name]: value
      })
    },
    // 更新图片样式
    updateStyleFunc(width, height) {
      let { updateElementStyle } = this.context
      updateElementStyle({
        width: width,
        height: height,
        eleId: this.id,
        pageId: this.pageId
      })
    },
    // 点击上传图片
    showUploadImg() {
      if (this.context && this.context.mode === 'edit') {
        this.showupload = true
        this.$nextTick(() => {
          this.$refs.imgUploadRef.uploadFunc()
        })
      }
    }
  }
}
</script>
<style scoped lang="scss">
.img-box {
  position: relative;
}

.img-upload {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;

  // 默认样式修改
  /deep/ .img-upload-wrap {
    width: 100%;
    height: 100%;
  }

  /deep/ .img-upload-wrap .img-del {
    display: none;
  }

  /deep/ .img-upload-wrap.has-img .file-upload {
    width: 100%;
    height: 100%;
  }
}
</style>
