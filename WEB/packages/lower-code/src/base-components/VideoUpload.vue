<template>
  <div class="ele-upload-video">
    <!-- 上传组件 -->
    <div class="img-upload-wrap" v-if="!hasVideo || alwaysUpload">
      <input class="file-upload" type="file" ref="videoFile" @change="uploadVideo($event)" @click="resetValue($event)" :accept="accept" />
      <h-table border="false" loading no-data-text="" loadingText="上传中, 请稍后" v-if="videoUploadPercent > 0" style="height: 100%"></h-table>
      <template v-else>
        <div class="el-upload__text">
          <img class="blank-icon" :src="boxImg" alt="">
          <div class="upload-btn">选择视频</div>
            <div class="el-upload__tip" slot="tip" v-if="showTip">
            <span
                >支持&nbsp;{{
                this.fileType ? this.fileType.join("/") : "视频"
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
    </div>

    <!-- 视频缩略 -->
    <vue-hover-mask v-if="hasVideo && !alwaysUpload">
      <video
        id="video"
        :autoplay="true"
        muted
        :src="videoSrc"
        :style="{
          width: width ? width + 'px' : '100%',
          height: height ? height + 'px' : 'auto'
        }"
      >
        您的浏览器不支持视频播放
      </video>
      <template v-slot:action>
        <span @click="handlePlayerVideo" class="ele-upload-video_mask__item">
          <i class="el-icon-zoom-in"></i>
        </span>
        <span @click="handleRemove" class="ele-upload-video_mask__item">
          <i class="el-icon-delete"></i>
        </span>
      </template>
    </vue-hover-mask>

    <!-- 弹窗播放 -->
    <h-msg-box width="560" v-model="isShowVideo" transfer class="msg-wrap" :footerHide="true">
      <video
        :autoplay="true"
        :src="videoSrc"
        controls
        class="video-box"
        v-if="isShowVideo"
      >
        您的浏览器不支持视频播放
      </video>
      <!-- <video-player  class="video-player vjs-custom-skin"
          ref="videoPlayer"
          :playsinline="true"
          :options="playerOptions"
        ></video-player> -->
         <!-- <iframe :src="videoSrc" frameborder='0'
          allow='autoplay;encrypted-media' allowfullscreen style='width:100%;height:500px;'>
         </iframe> -->
    </h-msg-box>
  </div>
</template>

<script>
import { uploadFilePlt, postMaterialFile } from '@Apis/common'
import { apiPost, baseOMSUrl } from '@Utils/request'
import VueHoverMask from 'vue-hover-mask'
import boxImg from '@Root/assets/images/box.png'
import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
// eslint-disable-next-line no-unused-vars
import * as upload from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
// import {cloneDeep} from 'lodash'
window.OSS = OSS
export default {
  name: 'EleUploadVideo',
  components: {
    VueHoverMask
  },
  props: {
    accept: {
      type: String,
      default: () => ''
    },
    // 值
    value: {
      type: String,
      default: () => ''
    },
    // 上传地址
    action: {
      type: String,
      required: true
    },
    // 响应处理函数
    responseFn: Function,
    // 文件大小限制(Mb)
    fileSize: {
      type: Number
    },
    // 显示宽度(默认auto，单位px)
    width: {
      type: Number
    },
    // 显示高度(默认auto)
    height: {
      type: Number
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    // 是否显示上传成功的提示
    isShowSuccessTip: {
      type: Boolean,
      default: true
    },
    // 文件类型
    fileType: {
      type: Array
    },
    // 设置上传的请求头部
    headers: Object,
    // 支持发送 cookie 凭证信息
    withCredentials: {
      type: Boolean,
      default: false
    },
    // 上传时附带的额外参数
    data: {
      type: Object
    },
    // 上传的文件字段名
    name: {
      type: String,
      default: 'file'
    },
    // 覆盖默认的上传行为，可以自定义上传的实现
    httpRequest: Function,
    // 接受上传的文件类型（thumbnail-mode 模式下此参数无效）
    // 删除前的操作
    beforeRemove: Function,
    // 上传长打开
    alwaysUpload: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    value: {
      handler(newVal) {
        this.setValue(newVal)
      },
      deep: true
    }
  },
  data () {
    return {
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], // 播放速度
        autoplay: false, // 如果true,浏览器准备好时开始回放。
        muted: false, // 默认情况下将会消除任何音频。
        loop: false, // 导致视频一结束就重新开始。
        preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [{
          type: '', // 这里的种类支持很多种：基本视频格式、直播、流媒体等，具体可以参看git网址项目
          src: '' // url地址
        }],
        poster: '', // 你的封面地址
        // width: document.documentElement.clientWidth, //播放器宽度
        notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true // 全屏按钮
        }
      },
      isShowVideo: false,
      videoUploadPercent: 0,
      attachment: {
        files: [],
        names: []
      },
      videoSrc: null,
      coverImgSrc: null,
      aliregion: window.CMS_CONFIG.Ali_region,
      aliuserId: window.CMS_CONFIG.Ali_userId,
      useAliyun: window.CMS_CONFIG.USE_ALIYUN && window.CMS_CONFIG.USE_ALIYUN == 'true' && (window.CMS_CONFIG.ALIYUN_TYPE == 'all' || window.CMS_CONFIG.ALIYUN_TYPE == 'video'),
      uploader: null,
      currentType: ''
    }
  },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize)
    },
    hasVideo() {
      return !!this.videoSrc
    }
  },
  created() {
    this.boxImg = boxImg
    this.videoSrc = this.value
  },
  methods: {
    delImg() {
      this.setValue()
      this.$emit('input', '')
    },
    setValue(val) {
      val = typeof val === 'string' ? val.trim() : ''
      if (val) {
        this.videoSrc = val
      } else {
        this.videoSrc = ''
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
          self.videoUploadPercent = 0
          let obj = {
            'file_path': uploadInfo.object,
            'file_name': uploadInfo.file.name,
            'file_guid': uploadInfo.videoId,
            'file_size': uploadInfo.file.size,
            'file_extension': uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1),
            'file_type': 5
          }
          postMaterialFile(obj).then(res => {
            self.$hMessage.success('上传成功')
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
          self.videoSrc = url
          self.playerOptions.sources[0] = {
            type: 'rmvb',
            src: self.videoSrc
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
          self.videoUploadPercent = 0
        },
        // 取消文件上传
        onUploadCanceled: function (uploadInfo, code, message) {
        },
        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function (uploadInfo, totalSize, progress) {
          if (progress > 0) {
            self.videoUploadPercent = Math.ceil(progress * 100)
          }
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
    handleUpload (file) {
      // 如果对接阿里云就调取阿里云sdk，反之则调取接口上传
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
        }).then(res => {
          if (res.data.error_info) {
            this.videoUploadPercent = 0
            this.$hMessage.error(res.data.error_info)
            return
          }
          if (res.data.file_guid) {
            this.videoUploadPercent = 0
            let obj = res.data
            Object.assign(obj, { file_type: 5 })
            postMaterialFile(obj).then(res => {
              this.$hMessage.success('上传成功')
            })
            let url = null
            if (window.CMS_CONFIG.STORE_TYPE && window.CMS_CONFIG.STORE_TYPE == 'fstore') {
              url = window.CMS_CONFIG.FSTORE_IP + '/' + obj.file_group + '/' + obj.file_path
            } else {
              url = baseOMSUrl + '/previewVideoFile?file_guid=' + obj.file_guid
            }
            this.videoSrc = url
            this.playerOptions.sources[0] = {
              type: 'rmvb',
              src: this.videoSrc
            }
            // this.$emit('fileUrl', {fileUrl: url})
            const fileObj = {
              fileUrl: url,
              fileName: obj.file_name
            }
            this.$emit('fileObj', {fileObj: fileObj})
          }
        }).catch(err => err).finally(() => {
        })
      }
    },
    uploadVideo(e) {
      let file = e.target.files[0]
      const fileName = file && file.name
      if (file && file.size > 0) {
        const extension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase()
        if (!extension || !this.accept.split(',').includes(extension)) {
          this.$hMessage.info({
            content: `仅支持${this.accept.join('，')}格式的视频`,
            duration: 5
          })
          this.$refs.videoFile.value = ''
          return
        }
        if ((fileName.length - extension.length) > 50) {
          this.$hMessage.info({
            content: `请修改文件名长度在50字以内`,
            duration: 5
          })
          this.$refs.videoFile.value = ''

          return
        }
        let maxSize = file.size
        if (maxSize > this.fileSize * 1024 * 1024) {
          this.$hMessage.info(`${this.name}最大限制${this.fileSize}MB，请重新提交`)
          e.target.value = ''
          return false
        }
        this.handleUploadProcess()
        this.handleUpload(file)
      }
    },
    // 上传进度
    handleUploadProcess() {
      this.videoUploadPercent = this.videoUploadPercent + 1
    },
    handleRemove() {
      this.videoSrc = null
      this.videoUploadPercent = 0
      this.$emit('coverImgSrc', '')
      this.delImg()
    },
    // 播放视频
    handlePlayerVideo() {
      this.isShowVideo = true
    },
    // 直接唤起上传
    uploadFunc() {
      this.$refs.videoFile.click()
    },
    // 重置input, 支持多次上传
    resetValue() {
      this.$refs.videoFile.value = ''
    }
  }
}
</script>

<style scoped lang="scss">
.ele-upload-video_mask__item {
  padding: 0 10px;
}
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
}
.description {
  color: #999;
  text-align: center;
  line-height: 14px;
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
.el-upload__text {
  text-align: center;
}
.msg-wrap {
  /deep/ .h-modal-body {
    display: flex;
    justify-content: center;
  }
}
.video-box {
  padding: 10px 0;
  width: 80%;
}
</style>
