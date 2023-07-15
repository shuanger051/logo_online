<template>
  <div class="ele-upload-video">
    <!-- 上传组件 -->
    <div class="img-upload-wrap" v-if="!hasAudio">
      <input class="file-upload" type="file" ref="audioFile" @change="uploadAudio($event)" @click="resetValue($event)" :accept="accept" />
      <h-spin size="large" fix v-if="spinShow"></h-spin>
      <template v-else>
        <div class="el-upload__text">
          <img class="blank-icon" :src="boxImg" alt="">
          <div class="upload-btn">选择音频</div>
          <div class="el-upload__tip" slot="tip" v-if="showTip">
            <span
            >支持&nbsp;{{
                this.fileType ? this.fileType.join("/") : "音频"
              }}&nbsp;</span
            >格式文件
            <template v-if="fileSize">
              ，且文件大小不超过
              <span>{{ fileSize }}</span
              >&nbsp;MB
            </template>
          </div>
        </div>
      </template>
      <div class="img-reupload">
        <span @click="uploadFunc" style="cursor: pointer">本地上传</span>
        <MaterialSelectModal
          v-if="CMM_FLAG"
          ref="materialSelectModal"
          material_format="2"
          :omsGSV="baseOMSUrl"
          :cmmGSV="baseCMMUrl"
          v-model="remoteAudioData"
        >
          <span @click="remoteUpload" style="cursor: pointer">素材库</span>
        </MaterialSelectModal>
      </div>
    </div>
    <div v-if="hasAudio" class="audio-area">
      <h-row>
        <h-col :span="22">
          <audio :src="audioSrc" :autoplay="false" preload controls v-if="audioSrc" style="width: 100%;">
            您的浏览器不支持 音频 元素
          </audio>
        </h-col>
        <h-col :span="2" style="line-height: 58px">
          <h-icon
            v-if="audioSrc"
            class="del-btn"
            name="t-b-delete"
            size="12"
            @on-click="deleteFile"/>
        </h-col>
      </h-row>
    </div>
  </div>
</template>

<script>
import { uploadFilePlt, postMaterialFile } from '@Apis/common'
import { apiPost, baseOMSUrl } from '@Utils/request'
import boxImg from '@Root/assets/images/box.png'
import MaterialSelectModal from './materialSelectModal/MaterialSelectModal'
import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
// eslint-disable-next-line no-unused-vars
import * as upload from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
window.OSS = OSS
export default {
  name: 'AudioSelect',
  components: { MaterialSelectModal },
  props: {
    value: {
      type: String,
      default: () => ''
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    accept: {
      type: String,
      default: () => ''
    },
    fileType: {
      type: Array
    },
    fileSize: {
      type: Number
    }
  },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize)
    },
    hasAudio() {
      return !!this.audioSrc
    }
  },
  data () {
    return {
      CMM_FLAG: window.CMS_CONFIG.CMM_FLAG === 'true',
      spinShow: false,
      remoteAudioData: {},
      baseOMSUrl: window.CMS_CONFIG.OMS_URL,
      baseCMMUrl: window.CMS_CONFIG.CMM_URL,
      audioSrc: '',
      useAliyun: window.CMS_CONFIG.USE_ALIYUN && window.CMS_CONFIG.USE_ALIYUN == 'true' && (window.CMS_CONFIG.ALIYUN_TYPE == 'all' || window.CMS_CONFIG.ALIYUN_TYPE == 'video'),
      aliregion: window.CMS_CONFIG.Ali_region,
      aliuserId: window.CMS_CONFIG.Ali_userId
    }
  },
  created() {
    this.boxImg = boxImg
    this.audioSrc = this.value
  },
  watch: {
    remoteAudioData: {
      handler(newVal) {
        const fileObj = {
          fileUrl: newVal.materiel_path,
          fileName: newVal.materiel_name
        }
        this.$emit('fileObj', {fileObj: fileObj})
      },
      deep: true
    },
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
    // 直接唤起上传
    uploadFunc() {
      this.$refs.audioFile.click()
    },
    remoteUpload() {
      this.$refs.materialSelectModal.showModal()
    },
    resetValue() {
      this.$refs.audioFile.value = ''
    },
    handleUpload (file) {
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
              this.$emit('fileObj', {fileObj: fileObj})
            }
          })
          .catch(err => err)
          .finally(() => {
            this.$refs.audioFile.value = ''
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
            const res = await apiPost({video_id: uploadInfo.videoId}, refreshUrl)
            const uploadAuth = res.data.upload_auth
            const uploadAddress = res.data.upload_address
            const videoId = res.data.video_id
            uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, videoId)
          } else {
            let uploadTitile = uploadInfo.file.name.substring(0, uploadInfo.file.name.indexOf('.'))
            let uploadFileName = uploadInfo.file.name
            const createUrl = window.CMS_CONFIG.CCM_URL + '/public/getVodUploadPassport'
            const res = await apiPost({title: uploadTitile, file_name: uploadFileName}, createUrl)
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
          self.$emit('fileObj', {fileObj: fileObj})
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
          const res = await apiPost({video_id: uploadInfo.videoId}, refreshUrl)
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
    uploadAudio (e) {
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
          this.$refs.audioFile.value = ''
          return
        }
        if (this.fileSize && size > this.fileSize * 1024 * 1024) {
          this.$hMessage.info(`最大限制${this.fileSize}MB，请重新提交`)
          this.$refs.audioFile.value = ''
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
    deleteFile () {
      const fileObj = {
        fileUrl: '',
        fileName: ''
      }
      this.$emit('fileObj', {fileObj: fileObj})
    }
  }
}
</script>
<style lang='scss' scoped>
.ele-upload-video .el-upload__tip {
  line-height: 12px;
}
.blank-icon {
  width: 30px;
  height: 30px;
  margin-top: 30px;
}
.upload-btn {
  font-size: 14px;
}
.img-upload-wrap {
  position: relative;
  width: 334px;
  height: 160px;
  border: 1px dashed #ddd;
  border-radius: 2px;
  background-color: #f7f7f7;
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
    display: flex;
    justify-content: space-around;
    align-items: center;
  }
}
.description {
  color: #999;
  text-align: center;
  line-height: 14px;
}
.file-upload {
  display: none;
  //position: absolute;
  //left: 0;
  //bottom: 0;
  //width: 334px;
  //height: 160px;
  //z-index: 100;
  //opacity: 0;
  //line-height: 0px;
  //cursor: pointer;
}
.el-upload__text {
  text-align: center;
}
.audio-area {
  .del-btn {
    margin-left: 10px;
    cursor: pointer;
  }
  .del-btn:hover {
    color: #F14C5D;
  }
}
</style>
