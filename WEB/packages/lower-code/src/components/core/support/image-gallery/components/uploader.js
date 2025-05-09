import { appUploadMaterialAttachmentOSS } from "core/api/";

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
    async upload(evt) {
        const {data} = await appUploadMaterialAttachmentOSS(evt.file);
       this.uploadSuccess({url: data.urlPath});
    },
    handleBeforeUpload (file) {
      return this.beforeUpload(file)
    },

  },
  render (h) {
    return (
      <a-upload
        name="file"
        showUploadList={false}
        customRequest={this.upload}
        beforeUpload={this.handleBeforeUpload}
       >
        {this.$slots.default &&  this.$slots.default}
      </a-upload>
    )
  },
  mounted () {
  }
}
