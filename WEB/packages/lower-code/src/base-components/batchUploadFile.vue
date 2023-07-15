<template>
  <div class="batch-upload-file-box" style="margin-top: 15px;">
    <h-spin size="large" fix v-if="spinShow"></h-spin>
    <input type="file" name="fileUpload" class="fileUpload" :class="{'error-border': !attachment.files.length}"
      :style="{'width': btnWidth + 'px'}" @click="resetValue($event)" @change="handleChange($event)" :accept="accept"
      ref="fileUpload" />
    <h-button type="ghost">
      <template v-if="!audioSrc">
        <h-icon name="upload1"></h-icon>
        <span>上传{{fileName}}</span>
      </template>
      <template v-else>
        <span>重新上传</span>
      </template>
    </h-button>
    <div class="h-select-dropdown form-drop error-customer" v-show="showUploadValid && !attachment.files.length">
      <div class="verify-tip verify-bottom">
        <div class="verify-tip-arrow"></div>
        <div class="verify-tip-inner" style="max-width: unset;">
        </div>
      </div>
    </div>
    <h-row style="margin-top: 15px;">
      <h-col :span="22">
        <audio :src="audioSrc" :autoplay="false" preload controls v-if="audioSrc" style="width: 100%;">
          您的浏览器不支持 音频 元素
        </audio>
      </h-col>
      <h-col :span="2">
        <h-icon v-if="audioSrc" name="t-b-delete" size="12" :color="delIndex === index ? '#F14C5D' : '#333'"
          style="cursor: pointer;vertical-align: -2px;margin-left: 10px;margin-top: 18px;position: absolute;"
          @mouseenter.native="delIndex = index" @mouseleave.native="delIndex = null" @on-click="deleteFile(index)" />
      </h-col>
    </h-row>
    <div v-for="(name, index) in attachment.names" :key="index" class="file-names">
      <!-- <h-icon name="accessory"
        :color="delIndex === index ? '#F14C5D' : '#333'"
        style="vertical-align: -2px;"></h-icon> -->
      <!-- <span class="ellipsis" :style="{color: delIndex === index ? '#F14C5D' : '#333'}">{{name}}</span> -->
    </div>
    <div>支持{{fileType}}格式，文件大小不超过{{fileSize}}MB</div>
  </div>
</template>

<script>
import { uploadFilePlt, postMaterialFile } from '@Apis/common'
import { apiPost, baseOMSUrl } from '@Utils/request'
import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
// eslint-disable-next-line no-unused-vars
import * as upload from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
window.OSS = OSS
export default {
  name: 'batchUploadFile',
  props: {
    value: {
      type: String,
      default: () => ''
    },
    btnWidth: {
      type: String,
      default: '80'
    },
    showUploadValid: Boolean,
    accept: {
      type: String,
      default: '.txt'
    },
    maxSize: [Number, String],
    maxNum: [Number, String],
    uploadDesc: String,
    fileName: String,
    fileType: String,
    fileSize: Number
  },
  data() {
    return {
      spinShow: false,
      attachment: {
        files: [],
        names: []
      },
      delIndex: null,
      audioSrc: '',
      useAliyun: window.CMS_CONFIG.USE_ALIYUN && window.CMS_CONFIG.USE_ALIYUN == 'true' && (window.CMS_CONFIG.ALIYUN_TYPE == 'all' || window.CMS_CONFIG.ALIYUN_TYPE == 'video'),
      aliregion: window.CMS_CONFIG.Ali_region,
      aliuserId: window.CMS_CONFIG.Ali_userId
    }
  },
  created() {
    this.setValue(this.value)
  },
  watch: {
    value: {
      handler(newVal) {
        this.setValue(newVal)
      },
      deep: true
    }
  },
  methods: {
    setValue(val) {
      val = typeof val === 'string' ? val.trim() : ''
      if (val) {
        this.audioSrc = val
      } else {
        this.audioSrc = ''
      }
    },
    delImg() {
      this.setValue()
      this.$emit('input', '')
      this.$refs.fileUpload.value = ''
    },
    resetValue() {
      this.$refs.fileUpload.value = ''
    },
    handleUpload(file) {
      if (this.maxNum === 1 && this.attachment.names.length > 0) {
        this.attachment.files = []
        this.attachment.names = []
      }
      if (file) {
        this.attachment.files.push(file)
        this.attachment.names.push(file.name)
      }
      this.spinShow = true
      if (this.useAliyun) {
        if (window.CMS_CONFIG.Aliyun_type && window.CMS_CONFIG.Aliyun_type == 'ALIYUN_VOD') {
          if (!this.uploader) {
            this.uploader = this.createUploader()
          }
          this.videoUploadChange(file)
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
              Object.assign(obj, { file_type: 6 })
              postMaterialFile(obj).then(res => {
                this.$hMessage.success('上传成功')
                this.spinShow = false
              })
              let url = ''
              if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
                // this.$emit('fileUrl', window.CMS_CONFIG.FSTORE_IP + '/' + res.data.file_group + '/' + res.data.file_path)
                url = window.CMS_CONFIG.FSTORE_IP + '/' + res.data.file_group + '/' + res.data.file_path
              } else {
                // this.$emit('fileUrl', baseOMSUrl + '/previewFilePlt?file_guid=' + obj.file_guid)
                url = baseOMSUrl + '/previewFilePlt?file_guid=' + obj.file_guid
              }
              const fileObj = {
                fileUrl: url,
                fileName: obj.file_name
              }
              this.$emit('fileObj', { fileObj: fileObj })
            }
          })
          .catch(err => err)
          .finally(() => {
            this.$refs.fileUpload.value = ''
            this.delIndex = null
          })
      }
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
          if (uploadInfo.videoId) {
            // 如果videoId有值，根据videoId刷新上传凭证
            const refreshUrl = window.CMS_CONFIG.CCM_URL + '/public/refreshVodUploadPassport'
            const res = await apiPost({ video_id: uploadInfo.videoId }, refreshUrl)
            const uploadAuth = res.data.upload_auth
            const uploadAddress = res.data.upload_address
            const videoId = res.data.video_id
            uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, videoId)
          } else {
            let uploadTitile = uploadInfo.file.name.substring(0, uploadInfo.file.name.indexOf('.'))
            let uploadFileName = uploadInfo.file.name
            const createUrl = window.CMS_CONFIG.CCM_URL + '/public/getVodUploadPassport'
            const res = await apiPost({ title: uploadTitile, file_name: uploadFileName }, createUrl)
            const uploadAuth = res.data.upload_auth
            const uploadAddress = res.data.upload_address
            const videoId = res.data.video_id
            uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, videoId)
          }
        },
        // 文件上传成功
        onUploadSucceed: function (uploadInfo) {
          let obj = {
            'file_path': uploadInfo.object,
            'file_name': uploadInfo.file.name,
            'file_guid': uploadInfo.videoId,
            'file_size': uploadInfo.file.size,
            'file_extension': uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1),
            'file_type': 6
          }
          postMaterialFile(obj).then(res => {
            self.$hMessage.success('上传成功')
            self.spinShow = false
          })
          let url = ''
          if (window.CMS_CONFIG.USE_ALIYUN_PROXY && window.CMS_CONFIG.USE_ALIYUN_PROXY == 'true') {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              url = window.CMS_CONFIG.ALIYUN_DOMAIN + '/' + uploadInfo.object
            } else {
              url = window.CMS_CONFIG.ALIYUN_PROXY_ADDRESS + '/' + uploadInfo.bucket + '.' + uploadInfo.endpoint.substring(8) + '/' + uploadInfo.object
            }
          } else {
            if (window.CMS_CONFIG.ALIYUN_DOMAIN && window.CMS_CONFIG.ALIYUN_DOMAIN.length > 0) {
              url = window.CMS_CONFIG.ALIYUN_DOMAIN + '/' + uploadInfo.object
            } else {
              url = 'https://' + uploadInfo.bucket + '.' + uploadInfo.endpoint.substring(8) + '/' + uploadInfo.object
            }
          }
          // self.$emit('fileUrl', {fileUrl: url})
          const fileObj = {
            fileUrl: url,
            fileName: obj.file_name
          }
          self.$emit('fileObj', { fileObj: fileObj })
        },
        // 文件上传失败
        onUploadFailed: function (uploadInfo, code, message) {
          self.$hMessage.error(message)
          self.spinShow = false
        },
        // 取消文件上传
        onUploadCanceled: function (uploadInfo, code, message) {
        },
        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function (uploadInfo, totalSize, progress) {
        },
        // 上传凭证超时
        onUploadTokenExpired: async function (uploadInfo) {
          const refreshUrl = window.CMS_CONFIG.CCM_URL + '/public/refreshVodUploadPassport'
          const res = await apiPost({ video_id: uploadInfo.videoId }, refreshUrl)
          const uploadAuth = res.data.upload_auth
          uploader.resumeUploadWithAuth(uploadAuth)
        },
        // 全部文件上传结束,
        onUploadEnd: function (uploadInfo) {
        }
      })
      return uploader
    },
    videoUploadChange(file) {
      const userData = '{"Vod":{}}'
      this.uploader.addFile(file, null, null, null, userData)
      setTimeout(() => {
        this.uploader.startUpload()
      }, 100)
    },
    handleChange(e) {
      const file = e.target.files[0]
      // IE上传调取两次，第二次为null跳出
      if (!file) return
      const size = file && file.size
      const fileName = file && file.name
      if (size) {
        const extension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase()
        if (!extension || !this.accept.split(',').includes(extension)) {
          this.$hMessage.info({
            content: `仅支持上传${this.accept}格式的文件`,
            duration: 5
          })
          this.$refs.fileUpload.value = ''
          return
        }
        if (this.maxSize && size > this.maxSize * 1024) {
          this.$hMessage.info(`最大限制${this.maxSize}KB，请重新提交`)
          this.$refs.fileUpload.value = ''
          return
        }
        const file = e.target.files[0]
        this.handleUpload(file)
      } else {
        this.$hMessage.info({
          content: `不允许上传空文件`,
          duration: 5
        })
      }
    },
    deleteFile(index) {
      this.attachment.files.splice(index, 1)
      this.attachment.names.splice(index, 1)
      this.setValue()
      this.$emit('input', '')
    },
    reset() {
      this.attachment = {
        files: [],
        names: []
      }
    }
  }
}
</script>
<style lang='scss' scoped>
.batch-upload-file-box {
  position: relative;
  display: inline-block;
  width: 334px;
  .fileUpload {
    position: absolute;
    top: 0;
    left: 0;
    height: 32px;
    z-index: 2;
    opacity: 0;
    cursor: pointer;
    font-size: 0;
  }
  .file-name {
    width: 330px;
    display: inline-block;
    vertical-align: -10px;
  }
}
.batch-upload-file-box + .h-select-dropdown .verify-tip-inner {
  max-width: 200px !important;
}
.batch-upload-file-box {
  .error-border .h-input {
    border-color: #f5222d;
  }
}
</style>
