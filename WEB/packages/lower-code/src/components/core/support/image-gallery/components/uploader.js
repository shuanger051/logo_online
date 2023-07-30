import {saveMaterial} from 'core/api'
export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    handleClose: {
      type: Function,
      default: () => {}
    },
    uploadSuccess: {
      type: Function,
      default: () => {}
    },
    beforeUpload: {
      type: Function,
      default: (file) => file
    }
  },
  computed: {
  },
  data: () => ({
    loading: false
  }),
  methods: {
    handleBeforeUpload (file) {
      return this.beforeUpload(file)
    },
    async saveMaterial(info) {
      return saveMaterial({
        fileName: info.attachmentName,
        filePath: info.attachmentPath,
        name: info.fileName,
        fileType: '1'
      })
    },
    async handleChange(info){
      this.loading = true
      const status = info.file.status

      if (status !== 'uploading') {
      }
      if (status === 'done' && info.file?.response?.code == '0') {
        this.loading = false
        await this.saveMaterial(info.file.response.data)
        this.uploadSuccess(info)
        this.$message.success(`${info.file.name} 上传成功.`)
      } else if (status === 'error') {
        this.$message.error(`${info.file.name} 上传失败.`)
      }
    }
  },
  render (h) {
    return (
      <a-upload
        name="file"
        action="/api/logo/attachment/uploadMaterialAttachment"
        beforeUpload={this.handleBeforeUpload}
        onChange={this.handleChange}>
        <slot>
          <a-button>
            <a-icon type="upload" /> Click to Upload
          </a-button>
        </slot>
      </a-upload>
    )
  },
  mounted () {
  }
}
