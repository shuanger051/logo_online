/**
 * https://github.com/ly525/luban-h5/issues/138
 */
import axios from 'axios'
import ImageItem from 'core/support/image-gallery/components/image-item.js'
import Uploader from 'core/support/image-gallery/components/uploader.js'
import {getMaterialListByPage} from 'core/api'

export default {
  data: () => ({
    items: [],
    cachedItems: [],
    loading: false,
    total: 30,
    page: 1,
    pageSize: 30
  }),
  methods: {
    uploadSuccess ({ file, fileList }) {
      const response = file.response.length && file.response[0]
      this.items = [{ name: response.name, url: response.url.replace('http://localhost:1337', '') }, ...this.items]
      this.total++
    },
    beforeUpload (file) {
      this.items.unshift({
        loading: true
      })
      return file
    },
    searchFiles () {
      getMaterialListByPage({
        fileType:1
      })
        .then(res => {
          
          this.items = res.data.list
          this.total = res.data.list.length
          this.cachedItems = []
        })
    }
  },
  render (h) {
    return (
      <div>
        <a-spin tip="Loading..." spinning={this.loading}>
          <a-card>
            <Uploader
              slot="extra"
              beforeUpload={file => this.beforeUpload(file)}
              uploadSuccess={info => this.uploadSuccess(info)}
            />
            <a-list
              pagination={{
                showSizeChanger: true,
                total: this.total,
                pageSize: this.pageSize,
                onChange: (page, pageSize) => {
                  this.page = page
                },
                onShowSizeChange: (currentPage, pageSize) => {
                  this.pageSize = pageSize
                }
              }}
              style="height: 400px; overflow: auto;"
              grid={{ gutter: 12, column: 3 }}
              dataSource={this.items}
              renderItem={(item, index) => (
                <a-list-item onClick={() => {
                  this.$emit('changeItem', item)
                }}>
                  <ImageItem item={item} />
                </a-list-item>
              )}
            >
            </a-list>
          </a-card>
        </a-spin>
      </div>
    )
  },
  mounted () {
    
    this.searchFiles()
    // demo code
  }
}
