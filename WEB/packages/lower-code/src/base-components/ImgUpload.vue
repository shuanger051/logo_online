<template>
  <div class="img-upload-container">
    <div class="img-upload-wrap" :class="{'has-img': hasImg}">
      <div class="blank-img-wrap" v-if="!hasImg">
        <img class="blank-icon" :src="boxImg" alt="">
        <div class="upload-btn">选择图片</div>
        <div class="description">{{description}}</div>
      </div>
      <div class="img-wrap" v-if="hasImg">
        <img class="img" :src="imgSrc || localImgSrc" :alt="alt" @error="loadErrorImg">
      </div>
      <div class="img-reupload" v-show="hasImg">重新选择</div>
      <div class="img-del" v-show="hasImg" @click="delImg"><h-icon name="android-delete"></h-icon></div>
      <input class="file-upload" type="file" ref="imgFile" @click="resetValue($event)" @change="uploadImg($event)" :accept="acceptImg" />
    </div>
    <div class="img-name">{{name}}</div>
  </div>
</template>

<script>
import { uploadFilePlt, postMaterialFile } from '@Apis/common'
import { apiPost, baseOMSUrl } from '@Utils/request'

import errorImg from '@Assets/images/upload-error.png'
import boxImg from '@Assets/images/box.png'
import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
// eslint-disable-next-line no-unused-vars
import * as upload from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
window.OSS = OSS

export default {
  name: 'ImgUpload',
  props: {
    value: {
      type: String,
      default: () => ''
    },
    name: String,
    alt: String,
    size: {
      type: Number || String,
      default: () => 1024 // kb
    },
    description: String,
    accept: {
      type: Array,
      default: () => ['png']
    },
    isShare: 'false',
    type: {
      type: String,
      default: 'img'
    }
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
  data() {
    return {
      imgSrc: '',
      localImgSrc: '',
      useAliyun: window.CMS_CONFIG.USE_ALIYUN && window.CMS_CONFIG.USE_ALIYUN == 'true' && (window.CMS_CONFIG.ALIYUN_TYPE == 'all' || window.CMS_CONFIG.ALIYUN_TYPE == 'image'),
      aliregion: window.CMS_CONFIG.Ali_region,
      aliuserId: window.CMS_CONFIG.Ali_userId,
      uploader: null
    }
  },
  // created() {
  //   this.setValue(this.value)
  // },
  watch: {
    value: {
      handler(newVal) {
        this.setValue(newVal)
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
        this.imgSrc = val
      } else {
        this.imgSrc = ''
      }
    },
    resetValue() {
      this.$refs.imgFile.value = ''
    },
    delImg() {
      this.$refs.imgFile.value = ''
      this.setValue()
      this.$emit('input', '')
    },
    validImg(localSrc) {
      return new Promise(resolve => {
        const img = new Image()
        img.src = localSrc
        img.onload = function() {
          if (this.width > 100 || this.height > 100 || this.width !== this.height) {
            resolve(false)
          } else {
            resolve(true)
          }
        }
      })
    },
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
          this.$hMessage.info(`上传失败，图片大小不能超过${this.size / 1024}MB`)
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
        if (this.useAliyun) {
          // 在阿里云情况下，背景图片还是走本地存储
          // if (this.type == 'background') {
          //   uploadFilePlt({
          //     file: file
          //   })
          //     .then(res => {
          //       if (res.data.error_info) {
          //         this.$hMessage.error(res.data.error_info)
          //         return
          //       }
          //       if (res.data.file_guid) {
          //         let obj = res.data
          //         this.localImgSrc = localSrc
          //         Object.assign(obj, { file_type: 1 })
          //         postMaterialFile(obj).then(res => {
          //         })
          //         if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
          //           this.$emit('input', window.CMS_CONFIG.FSTORE_IP + '/' + res.data.file_group + '/' + res.data.file_path)
          //         } else {
          //           this.$emit('input', baseOMSUrl + '/previewFilePlt?file_guid=' + res.data.file_guid)
          //         }
          //       }
          //     })
          //     .catch(err => err)
          //     .finally(() => {
          //       e.target.value = ''
          //     })
          // } else {
          //   if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
          //     if (!this.uploader) {
          //       this.uploader = this.createUploader()
          //     }
          //     this.imgUploadChange(file)
          //   }
          // }
          if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
            if (!this.uploader) {
              this.uploader = this.createUploader()
            }
            this.imgUploadChange(file)
          }
        } else {
          uploadFilePlt({
            file: file
          })
            .then(res => {
              if (res.data.error_info) {
                this.$hMessage.error(res.data.error_info)
                return
              }
              if (res.data.file_guid) {
                let obj = res.data
                this.localImgSrc = localSrc
                Object.assign(obj, { file_type: 1 })
                postMaterialFile(obj).then(res => {
                })
                if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
                  this.$emit('input', window.CMS_CONFIG.FSTORE_IP + '/' + res.data.file_group + '/' + res.data.file_path)
                } else {
                  this.$emit('input', baseOMSUrl + '/previewFilePlt?file_guid=' + res.data.file_guid)
                }
              }
            })
            .catch(err => err)
            .finally(() => {
              e.target.value = ''
            })
        }
      }
    },
    imgUploadChange(file) {
      const userData = '{"Vod":{}}'
      this.uploader.addFile(file, null, null, null, userData)
      setTimeout(() => {
        this.uploader.startUpload()
      }, 100)
    },
    /**
     * 创建一个Ali云上传对象
     * 使用 UploadAuth 上传方式
     */
    createUploader() {
      let self = this
      let uploader = new AliyunUpload.Vod({
        timeout: 60000,
        partSize: 1048576,
        parallel: 5,
        retryCount: 3,
        retryDuration: 2,
        region: self.aliregion,
        userId: self.aliuserId,
        // 添加文件成功
        addFileSuccess: function (uploadInfo) {
        },
        // 开始上传
        onUploadstarted: async function (uploadInfo) {
          let uploadTitile = uploadInfo.file.name.substring(0, uploadInfo.file.name.indexOf('.'))
          let image_extion = uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1)
          const createUrl = window.CMS_CONFIG.CCM_URL + '/public/getImageUploadPassport'
          const res = await apiPost({title: uploadTitile, image_ext: image_extion}, createUrl)
          const uploadAuth = res.data.upload_auth
          const uploadAddress = res.data.upload_address
          const image_id = res.data.image_id
          uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, image_id)
        },
        // 文件上传成功
        onUploadSucceed: function (uploadInfo) {
          let obj = {
            'file_path': uploadInfo.object,
            'file_name': uploadInfo.file.name,
            'file_guid': uploadInfo.videoId,
            'file_size': uploadInfo.file.size,
            'file_extension': uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1),
            'file_type': 1
          }
          let localSrc = window.URL.createObjectURL(uploadInfo.file)
          let imgUrl = ''
          if (window.CMS_CONFIG.USE_ALIYUN_PROXY && window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true') {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              imgUrl = window.CMS_CONFIG.ALIYUN_DOMAIN + '/' + uploadInfo.object
            } else {
              imgUrl = window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS + '/' + uploadInfo.bucket + '.' + uploadInfo.endpoint.substring(8) + '/' + uploadInfo.object
            }
          } else {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              imgUrl = window.CMS_CONFIG.ALIYUN_DOMAIN + '/' + uploadInfo.object
            } else {
              imgUrl = 'https://' + uploadInfo.bucket + '.' + uploadInfo.endpoint.substring(8) + '/' + uploadInfo.object
            }
          }
          self.localImgSrc = localSrc
          postMaterialFile(obj).then(res => {
          })
          self.$emit('input', imgUrl)
        },
        // 文件上传失败
        onUploadFailed: function (uploadInfo, code, message) {
          self.$hMessage.error(message)
        },
        // 取消文件上传
        onUploadCanceled: function (uploadInfo, code, message) {
        },
        // 上传凭证超时
        onUploadTokenExpired: async function (uploadInfo) {
          const refreshUrl = window.CMS_CONFIG.CCM_URL + '/public/refreshVodUploadPassport'
          const res = await apiPost({video_id: uploadInfo.videoId}, refreshUrl)
          const uploadAuth = res.data.upload_auth
          uploader.resumeUploadWithAuth(uploadAuth)
        },
        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function (uploadInfo, totalSize, progress) {
        },
        // 全部文件上传结束,
        onUploadEnd: function (uploadInfo) {
        }
      })
      return uploader
    },
    // 唤起上传弹框
    uploadFunc() {
      this.$refs.imgFile.click()
    }
  },
  created() {
    this.boxImg = boxImg
    this.setValue(this.value)
  }
}
</script>

<style lang="scss" scoped>
.img-upload-container {
  width: 334px;
  position: relative;
  margin-bottom: 12px;

  .img-name {
    text-align: center;
    color: #333;
    font-size: 12px;
    margin-top: 4px;
  }
}
.img-upload-wrap {
  position: relative;
  width: 334px;
  height: 160px;
  border: 1px dashed #ddd;
  border-radius: 2px;
  background-color: #f7f7f7;

  &.has-img {
    border: 0;

    .file-upload {
      height: 40px;
      left: 0;
      bottom: 0;
    }
  }

  .blank-img-wrap {
    position: relative;
    padding: 30px 8px 0;

    .blank-icon {
      display: block;
      width: 30px;
      height: 30px;
      margin: 0 auto;
    }

    .upload-btn {
      font-size: 14px;
      line-height: 14px;
      color: #333;
      text-align: center;
      margin: 10px 0;
    }

    .description {
      color: #999;
      text-align: center;
      line-height: 14px;
    }
  }

  .file-upload {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 334px;
    height: 160px;
    z-index: 100;
    opacity: 0;
    line-height: 0px;
    cursor: pointer;
  }

  .img-wrap {
    width: 334px;
    height: 160px;
    display: flex;
    justify-content: center;
    align-items: center;
    background: #f7f7f7;
    border-radius: 2px;

    .img {
      display: block;
      max-width: 334px;
      max-height: 160px;
    }
  }

  .img-reupload {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 40px;
    line-height: 40px;
    text-align: center;
    color: #fff;
    font-size: 14px;
    background-color: rgba(0, 0, 0, 0.3);
  }

  .img-del {
    position: absolute;
    right: 0;
    top: 0;
    width: 40px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    color: #fff;
    cursor: pointer;
    font-size: 14px;
    background-color: rgba(0, 0, 0, 0.3);
  }
}
</style>
