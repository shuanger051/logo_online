<template>
  <div class="img-upload-container" :style="{flexDirection: direction}">
    <div class="img-upload-box" :style="{
        width: width ? `${width}px` : '100%',
        minWidth: width ? `${width}px` : '100%',
        height: height ? `${height}px` : '100%'
      }">
      <div v-if="hasImg" class="img-box">
        <img class="img-wrap" :src="imgSrc.indexOf('http') !== -1 ? imgSrc : ((imgUrl + imgSrc) || localImgSrc)" @error="loadErrorImg">
        <div class="btn-upload" :style="{
          width: !CMM_FLAG ? '100%' : '50%',
          height: buttonHeight + 'px'
        }">
          <h-poptip trigger="hover" :content="uploadText" v-if="uploadText">
            <img src="@Root/assets/images/goldeneggs/icon-upload.png" alt>
            <input class="file-upload" type="file" ref="imgFile" @change="uploadImg($event)" :accept="acceptImg" />
          </h-poptip>
          <div v-if="uploadText === ''" style="display: flex; align-items: center">
            <img src="@Root/assets/images/goldeneggs/icon-upload.png" alt>
            <input class="file-upload" type="file" ref="imgFile" @change="uploadImg($event)" :accept="acceptImg" />
          </div>
        </div>
        <div class="btn-material" :style="{height: buttonHeight + 'px'}" @click="onShowMaterial" v-if="materialable && CMM_FLAG">
          <MaterialSelectModal
            ref="materialSelectModal"
            :omsGSV="baseOMSUrl"
            :cmmGSV="baseCMMUrl"
            v-model="remoteImgData"
          >
<!--            <span @click="remoteUpload" style="cursor: pointer">素材库</span>-->
            <h-poptip trigger="hover" :content="materialText" v-if="materialText">
              <img src="@Root/assets/images/goldeneggs/icon-material.png" alt="">
            </h-poptip>
            <div v-if="materialText === ''" style="display: flex; align-items: center">
              <img src="@Root/assets/images/goldeneggs/icon-material.png" alt="">
            </div>
          </MaterialSelectModal>
        </div>
        <div v-if="closable" class="btn-del" :style="{width: `${closeWidth}px`, height: `${closeHeight}px`}"
          @click="onDelete">
          <h-icon name="android-close"></h-icon>
        </div>
        <div v-if="resetable" class="btn-del" :style="{width: `${closeWidth}px`, height: `${closeHeight}px`}"
             @click="onReset">
          <img style="width: 100%; max-width: 16px;" src="@Root/assets/images/goldeneggs/icon-reset.png" alt="">
        </div>
      </div>
      <div v-else class="img-box2">
        <img class="blank-icon" :src="boxImg" alt="">
        <div class="upload-btn">点击上传</div>
        <input class="file-upload" type="file" ref="imgFile" @change="uploadImg($event)" :accept="acceptImg" />
      </div>
    </div>
    <div :class="['img-upload-info', direction == 'column' ? 'img-upload-info-column' : '']">
      {{description}}
    </div>
  </div>
</template>

<script>
import { uploadFilePlt } from '@Apis/actRule'
import errorImg from '@Root/assets/images/upload-error.png'
import boxImg from '@Root/assets/images/box.png'
import { baseOMSUrl } from '@Utils/request'
import MaterialSelectModal from './materialSelectModal/MaterialSelectModal'

export default {
  name: 'ImgUpload',
  props: {
    value: {
      type: String,
      default: () => ''
    }, // 传入值
    description: {
      type: String,
      default: () => ''
    }, // 描述
    accept: {
      type: Array,
      default: () => ['png']
    }, // 支持格式
    size: {
      type: Number || String,
      default: () => 500 // kb
    }, // 图片大小
    width: {
      type: Number || String,
      default: () => null // px
    }, // 宽
    height: {
      type: Number || String,
      default: () => null // px
    }, // 高
    direction: {
      type: String,
      default: () => 'row'
    }, // 图片组件结构
    uploadText: {
      type: String,
      default: () => '重新上传'
    }, // 上传按钮文案
    resetText: {
      type: String,
      default: () => ''
    }, // 重置按钮文案
    materialText: {
      type: String,
      default: () => '素材库'
    }, // 素材库文案
    resetable: {
      type: Boolean || String,
      default: () => true
    }, // 是否可重置
    materialable: {
      type: Boolean || String,
      default: () => true
    }, // 是否需要素材库
    closable: {
      type: Boolean || String,
      default: () => true
    }, // 是否可删除
    closeWidth: {
      type: Number || String,
      default: () => 16 // px
    }, // 关闭按钮宽
    closeHeight: {
      type: Number || String,
      default: () => 16 // px
    }, // 关闭按钮高
    isShare: {
      type: Boolean || String,
      default: () => false
    }, // 是否分享
    buttonHeight: {
      type: Number || String,
      default: () => 28
    } // 底部按钮高度
  },
  computed: {
    hasImg() {
      return !!this.imgSrc
    },
    acceptImg() {
      return this.accept.map(item => {
        return `image/${item}`
      }).join(',')
    }
  },
  components: {
    MaterialSelectModal
  },
  data() {
    return {
      CMM_FLAG: window.CMS_CONFIG.CMM_FLAG === 'true',
      remoteImgData: {},
      baseOMSUrl: window.CMS_CONFIG.OMS_URL,
      baseCMMUrl: window.CMS_CONFIG.CMM_URL,
      imgSrc: '', // 图片路径
      localImgSrc: '', // 图片路径
      imgUrl: '' // 图片域名
    }
  },
  created() {
    this.boxImg = boxImg
    this.setValue(this.value)
  },
  watch: {
    value: {
      handler(newVal) {
        this.setValue(newVal)
      },
      deep: true
    },
    remoteImgData: {
      handler(val) {
        if (val.materiel_path) {
          this.$emit('input', val.materiel_path)
        }
      },
      deep: true
    }
  },
  methods: {
    // 请求网络图片或者接口图片为空、错误时，使用默认图片
    loadErrorImg(event) {
      if (event.type == 'error') {
        event.target.src = errorImg
      }
    },
    setValue(val) {
      val = typeof val === 'string' ? val.trim() : ''
      if (val) {
        if (val.indexOf('previewFilePlt') > -1) {
          this.imgUrl = baseOMSUrl
        } else if (val.indexOf('group1') > -1) {
          this.imgUrl = window.CMS_CONFIG.IMAGE_URL || ''
        } else {
          this.imgUrl = ''
        }
        this.imgSrc = val
      } else {
        this.imgSrc = ''
      }
    },
    // 上传图片
    async uploadImg(e) {
      let file = e.target.files[0]
      let localSrc = window.URL.createObjectURL(file)
      let type = file.type
      // 兼容火狐、IE
      if (this.acceptImg.split(',').indexOf(type) === -1) {
        this.$hMessage.info(`仅支持${this.accept.join('，')}格式的图片`)
        this.$refs.imgFile.value = ''
        return
      }
      if (file && file.size > 0) {
        let maxSize = file.size
        if (maxSize > this.size * 1024) {
          this.$hMessage.info(`上传失败，图片大小不能超过${this.size}kb`)
          this.$refs.imgFile.value = ''
          return false
        }
        if (this.isShare) {
          const flag = await this.validImg(localSrc)
          if (!flag) {
            this.$hMessage.info('请上传一张100*100px的图片')
            this.$refs.imgFile.value = ''
            return false
          }
        }
        uploadFilePlt({
          file: file
        })
          .then(res => {
            if (res.data.error_info) {
              this.$hMessage.error(res.data.error_info)
              return
            }
            if (res.data.pic_url) {
              this.localImgSrc = localSrc
              this.$emit('input', res.data.pic_url)
            } else if (res.data.picUrl) {
              this.localImgSrc = localSrc
              this.$emit('input', res.data.picUrl)
            }
          })
          .catch(err => err)
          .finally(() => {
            e.target.value = ''
          })
      }
    },
    // 验证大小
    validImg(localSrc) {
      return new Promise(resolve => {
        const img = new Image()
        img.src = localSrc
        img.onload = function () {
          if (this.width > 100 || this.height > 100 || this.width !== this.height) {
            resolve(false)
          } else {
            resolve(true)
          }
        }
      })
    },
    // 打开素材库
    onShowMaterial() {
      this.$refs.materialSelectModal.showModal()
    },
    // 重置
    onReset() {
      this.$emit('onReset')
    },
    // 删除
    onDelete() {
      this.$emit('onDelete')
    }
  }
}
</script>

<style lang="scss" scoped>
.img-upload-container {
  display: flex;
  align-items: center;
}

.img-upload-box {
  border-radius: 2px;
  border: 1px solid #d9d9d9;

  .img-box,
  .img-box2 {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: #f7f7f7;
  }

  .img-box2 {
    flex-direction: column;
    background: #f7f7f7;
  }

  .img-wrap {
    max-width: 100%;
    max-height: 100%;
  }

  .btn-upload,
  .btn-material {
    position: absolute;
    bottom: 0;
    left: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 50%;
    height: 28px;
    color: #fff;
    font-size: 12px;
    background-color: rgba(0, 0, 0, 0.5);
    cursor: pointer;

    img {
      width: 16px;
      height: 16px;
    }
  }

  .file-upload {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
    line-height: 0px;
    cursor: pointer;
    z-index: 100;
  }

  .btn-material {
    right: 0;
    left: auto;
    border-left: 1px solid #d9d9d9;
  }

  .btn-del {
    position: absolute;
    top: 0;
    right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 50%;
    color: #fff;
    background-color: rgba(0, 0, 0, 0.5);
    cursor: pointer;
  }

  .blank-icon {
    display: block;
    width: 30px;
    height: 30px;
    margin: 0 auto;
  }

  .upload-btn {
    font-size: 12px;
    line-height: 20px;
    color: #333;
    text-align: center;
    margin: 8px 0 0;
  }
}

.img-upload-info {
  flex: 1;
  margin-left: 12px;
  font-size: 12px;
  color: #999999;
  line-height: 20px;
}

.img-upload-info-column {
  margin-left: 0;
  margin-top: 5px;
  text-align: center;
}

/deep/ .h-poptip-rel {
  display: flex;
  align-items: center;
} // 上传图标高度居中

/deep/ .material-select {
  display: flex;
  align-items: center;
} // 素材库高度居中

/deep/ .h-poptip-popper {
  min-width: 60px;
}

/deep/.h-poptip-popper {
  .h-poptip-content {
    .h-poptip-inner {
      background-color: rgba(51, 51, 51, 0.9);
      padding: 3px 14px !important;
      display: flex;
      align-items: center;
      justify-content: center;

      .h-poptip-body-content-inner {
        color: #fff !important;
      }
    }
  }
}

/deep/.h-poptip-arrow::after {
  border-top-color: rgba(51, 51, 51, 0.9) !important;
  color: #fff;
}
</style>
