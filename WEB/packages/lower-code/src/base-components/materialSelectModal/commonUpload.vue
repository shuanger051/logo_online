<template>
  <div class="common-upload">
    <h-upload
      style="display: inline-block"
      with-credentials
      :show-upload-list="false"
      :showErrorlist="false"
      :accept="accept"
      :format="format"
      :before-upload="beforeUpload"
      :on-progress="uploadProgress"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      :on-format-error="formatError"
      :headers="headers"
      :action="`${apiUrl}`">
      <h-button icon="upload1"  @click="openUploadFolder">上传文件</h-button>
    </h-upload>
    <div class="upload-result" v-if="uploadStatus === 'progress'">
      <span>
        <h-icon name="accessory"></h-icon>
        <span class="file-name" :title="progressObj.name">{{progressObj.name}}</span>
      </span>
      <div>
        <h-progress :percent="progressObj.percentage.toFixed(2)"></h-progress>
      </div>
    </div>
    <div class="upload-result" v-else-if="uploadStatus === 'success' || uploadList.length">
      <span>
        <h-icon name="accessory"></h-icon>
        <span class="file-name" :title="uploadList[0].file_name">{{uploadList[0].file_name}}</span>
        <span class="file-size-str">{{uploadList[0].file_size_str}}</span>
      </span>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import OSS from 'ucp-components/lib/utils/lib/aliyun-upload-sdk/lib/aliyun-oss-sdk-5.3.1.min.js'
import 'ucp-components/lib/utils/lib/aliyun-upload-sdk/aliyun-upload-sdk-1.5.0.min.js'
window.OSS = OSS

export default {
  name: 'commonUpload',
  props: {
    value: {
      type: Array,
      default() {
        return []
      }
    },
    systemConfigInfo: {
      required: true,
      type: Object,
      default() {
        return {}
      }
    },
    // 接口前缀
    omsGSV: {
      required: false,
      type: String,
      default() {
        return ''
      }
    },
    cmmGSV: {
      required: false,
      type: String,
      default() {
        return ''
      }
    },
    // 单个文件最大尺寸
    maxSize: {
      type: Number,
      default: Math.pow(1024, 3) * 5
    },
    material_types: {
      required: false,
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    let crsf = window.sessionStorage.getItem('omc_crsf')
    return {
      apiUrl: '/fileUpload/upload',
      headers: {csrfcheck: crsf || ''},
      uploadStatus: '',
      progressObj: {},
      uploadList: this.value || [],
      // 阿里云上传对象
      uploader: null,
      canUploadFiles: []
    }
  },
  computed: {
    type_arr() {
      return this.systemConfigInfo.type_arr
    },
    format() {
      if (this.material_types.length) {
        return this.material_types
      } else {
        let typeArr = []
        let arr = this.systemConfigInfo.material_types.split(';')
        arr.forEach((item, index) => {
          let temp = item.split(':')[1].split(',')
          typeArr = typeArr.concat(temp)
        })
        return typeArr
      }
    },
    accept() {
      let acceptList = this.format.map(i => {
        return '.' + i
      })
      console.log(acceptList.join(','))
      return acceptList.join(',')
    }
  },
  methods: {
    getImgWH(file) {
      return new Promise((resolve, reject) => {
        if (typeof file !== 'string') {
          const reader = new FileReader()
          reader.readAsDataURL(file)
          reader.onload = function (readRes) {
            let img = new Image()
            img.src = readRes.target.result
            img.onload = (e) => {
              resolve({
                width: e.path[0].width,
                height: e.path[0].height
              })
            }
            img.onerror = (e) => {
              rejrct({
                width: '',
                height: ''
              })
            }
          }
        } else {
          let img = new Image()
          img.src = file
          img.onload = (e) => {
            resolve({
              width: e.path[0].width,
              height: e.path[0].height
            })
          }
          img.onerror = (e) => {
            rejrct({
              width: '',
              height: ''
            })
          }
        }
      })
    },
    openUploadFolder() {
      this.canUploadFiles = []
    },
    // 上传
    async beforeUpload(file) {
      this.canUploadFiles.push(file)
      if (file.size === 0) {
        this.$hMessage.error('不允许上传空素材')
        return Promise.reject()
      }
      if (file.size > this.maxSize) {
        this.$hMessage.error('素材大小超过10M')
        return Promise.reject()
      }
      let arr = file.name.split('.')
      let suffix = arr[arr.length - 1]
      if (!this.format.includes(suffix.toLowerCase())) {
        this.$hMessage.error(`素材格式错误,仅支持${this.format}`)
        return Promise.reject()
      }
      let type = ''
      for (let i = 0; i < this.type_arr.length; i++) {
        if (this.type_arr[i].type.includes(suffix.toLowerCase())) {
          type = this.type_arr[i].name
          break
        }
      }
      if (type === '图片') {
        const wh = await this.getImgWH(file)
        file.width = wh.width
        file.height = wh.height
        if (this.systemConfigInfo.image_storage_server === 'aliVod') {
          // Ali云上传
          this.uploader = this.createUploader(type)
          this.uploadChange(file)
          return Promise.reject()
        }
        if (this.systemConfigInfo.image_storage_server === 'oms') {
          this.apiUrl = `${this.omsGSV}/uploadFilePlt`
        }
        if (this.systemConfigInfo.image_storage_server === 'custom') {
          this.apiUrl = `${this.cmmGSV}/fileUpload/upload`
        }
      } else if (type === '视频' || type === '音频') {
        if (this.systemConfigInfo.video_storage_server === 'aliVod') {
          // Ali云上传
          this.uploader = this.createUploader(type)
          this.uploadChange(file)
          return Promise.reject()
        }
        if (this.systemConfigInfo.video_storage_server === 'oms') {
          this.apiUrl = `${this.omsGSV}/uploadFilePlt`
        }
        if (this.systemConfigInfo.video_storage_server === 'custom') {
          this.apiUrl = `${this.cmmGSV}/fileUpload/upload`
        }
      } else if (type === '文档') {
        if (this.systemConfigInfo.doc_storage_server === 'oms') {
          this.apiUrl = `${this.omsGSV}/uploadFilePlt`
        }
        if (this.systemConfigInfo.doc_storage_server === 'custom') {
          this.apiUrl = `${this.cmmGSV}/fileUpload/upload`
        }
      } else {
        if (this.systemConfigInfo.others_storage_server === 'oms') {
          this.apiUrl = `${this.omsGSV}/uploadFilePlt`
        }
        if (this.systemConfigInfo.others_storage_server === 'custom') {
          this.apiUrl = `${this.cmmGSV}/fileUpload/upload`
        }
      }
    },
    uploadProgress(event, file, fileList) {
      this.uploadStatus = 'progress'
      this.progressObj = file
    },
    uploadSuccess(response, file, fileList) {
      console.log(response, 'response')
      const curFile = this.canUploadFiles.filter(item => item.name === response.file_name)[0]
      if (!response.error_info) {
        this.uploadStatus = 'success'
        response.width = curFile.width
        response.height = curFile.height
        this.uploadList = [response]
        this.uploadList[0].file_type = this.checkType(response.file_extension)
        if (response.file_group) {
          const file_group = response.file_group.trim()
          if (file_group) {
            this.uploadList[0].file_path = file_group + '/' + response.file_path
          } else {
            this.uploadList[0].file_path = response.file_path
          }
        }
        this.$hMessage.success('上传成功')
        this.$emit('input', this.uploadList)
      } else {
        this.$hMessage.error(response.error_info)
      }
    },
    uploadError(error, response, file) {
      this.$hMessage.error(response.error_info)
      console.log(error)
    },
    formatError(file, fileList) {
      this.$hMessage.error(`素材格式错误,仅支持${this.format}`)
    },
    checkType(type) {
      let result = this.systemConfigInfo.type_arr.find(item => item.type.includes(type.toLowerCase()))
      if (result) {
        return result.id
      } else {
        return '99'
      }
    },
    /**
     * 创建一个Ali云上传对象
     * 使用 UploadAuth 上传方式
     */
    createUploader(type) {
      let self = this
      let uploader = new AliyunUpload.Vod({
        userId: self.systemConfigInfo.aliVod_userId,
        region: self.systemConfigInfo.aliVod_regionId,
        partSize: 1048576,
        parallel: 5,
        retryCount: 3,
        retryDuration: 2,
        // 开始上传
        onUploadstarted: async function (uploadInfo) {
          if (type === '图片') {
            let uploadTitile = uploadInfo.file.name.substring(0, uploadInfo.file.name.indexOf('.'))
            let image_extion = uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1)
            const createUrl = `${self.cmmGSV}/public/getImageUploadPassport?title=${uploadTitile}&image_ext=${image_extion}`
            axios.get(createUrl).then(res => {
              const uploadAuth = res.data.data.upload_auth
              const uploadAddress = res.data.data.upload_address
              const image_id = res.data.data.image_id
              uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, image_id)
            })
          }
          if (type === '视频' || type === '音频') {
            if (uploadInfo.videoId) {
              // 如果videoId有值，根据videoId刷新上传凭证
              const refreshUrl = `${self.cmmGSV}/public/refreshVodUploadPassport?video_id=${uploadInfo.videoId}`
              axios.get(refreshUrl).then(res => {
                const uploadAuth = res.data.data.upload_auth
                const uploadAddress = res.data.data.upload_address
                const videoId = res.data.data.video_id
                uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, videoId)
              })
            } else {
              let uploadTitile = uploadInfo.file.name.substring(0, uploadInfo.file.name.indexOf('.'))
              let uploadFileName = uploadInfo.file.name
              const createUrl = `${self.cmmGSV}/public/getVodUploadPassport?title=${uploadTitile}&fileName=${uploadFileName}`
              axios.get(createUrl).then(res => {
                const uploadAuth = res.data.data.upload_auth
                const uploadAddress = res.data.data.upload_address
                const videoId = res.data.data.video_id
                uploader.setUploadAuthAndAddress(uploadInfo, uploadAuth, uploadAddress, videoId)
              })
            }
          }
        },
        // 文件上传成功
        onUploadSucceed: function (uploadInfo) {
          console.log(uploadInfo, '成功')
          let obj = {
            'width': uploadInfo.file.width,
            'height': uploadInfo.file.height,
            'file_path': self.systemConfigInfo.aliVod_domainName + '/' + uploadInfo.object,
            'file_name': uploadInfo.file.name,
            'file_guid': uploadInfo.videoId,
            'file_size': uploadInfo.file.size,
            'file_size_str': self.bytesToSize(parseInt(uploadInfo.file.size)),
            'file_extension': uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1),
            'file_type': self.checkType(uploadInfo.file.name.substring(uploadInfo.file.name.lastIndexOf('.') + 1))
          }
          console.log(obj)
          self.uploadStatus = 'success'
          self.uploadList = [obj]
          self.$hMessage.success('上传成功')
          self.$emit('input', [obj])
        },
        // 文件上传失败
        onUploadFailed: function (uploadInfo, code, message) {
          self.$hMessage.error('上传失败')
          console.log('文件上传失败', uploadInfo, code, message)
        },
        // 取消文件上传
        onUploadCanceled: function (uploadInfo, code, message) {
          console.log('文件已取消上传')
        },
        // 文件上传进度，单位：字节, 可以在这个函数中拿到上传进度并显示在页面上
        onUploadProgress: function (uploadInfo, totalSize, progress) {
          self.uploadStatus = 'progress'
          self.progressObj = {
            name: uploadInfo.file.name,
            percentage: Math.ceil(progress * 100)
          }
        },
        // 上传凭证超时
        onUploadTokenExpired: function (uploadInfo) {
          const refreshUrl = `${self.cmmGSV}/public/refreshVodUploadPassport?video_id=${uploadInfo.videoId}`
          axios.get(refreshUrl).then(res => {
            let uploadAuth = res.data.data.upload_auth
            uploader.resumeUploadWithAuth(uploadAuth)
          })
          console.log('文件超时')
        },
        // 全部文件上传结束,
        onUploadEnd: function (uploadInfo) {
          console.log('文件上传结束')
        }
      })
      return uploader
    },
    uploadChange(file) {
      let userData = '{"Vod":{}}'
      this.uploader.addFile(file, null, null, null, userData)
      setTimeout(() => {
        this.uploader.startUpload()
      }, 100)
    },
    bytesToSize(bytes) {
      if (bytes < 1024) {
        return bytes + 'B'
      }
      if (bytes >= 1024 && bytes < 1024 * 1024) {
        let K = (bytes / 1024).toFixed(2)
        return K + 'KB'
      }
      if (bytes >= 1024 * 1024 && bytes < 1024 * 1024 * 1024) {
        let M = (bytes / 1024 / 1024).toFixed(2)
        return M + 'MB'
      }
      if (bytes >= 1024 * 1024 * 1024 && bytes < 1024 * 1024 * 1024 * 1024) {
        let G = (bytes / 1024 / 1024 / 1024).toFixed(2)
        return G + 'GB'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.common-upload {
  .upload-result {
    margin-top: 8px;
    /deep/ .h-progress-outer {
      margin-right: -60px;
      padding-right: 60px;
    }
    .file-name {
      display: inline-block;
      margin-left: 4px;
      max-width: calc(100% - 120px);
      overflow: hidden;
      text-overflow:ellipsis;
      white-space: nowrap;
      vertical-align: -8px;
    }
    .file-size-str {
      margin-left: 20px;
      color: #999;
    }
  }
}
</style>
