import { mapState, mapActions } from "vuex";
import "./share-info.scss";
import { getDictById, getMaterialByID } from "core/api";
const debounce = function debounce(func, wait) {
  let timerId = 0;
  return function (...args) {
    if (timerId) {
      clearTimeout(timerId);
    }
    timerId = setTimeout(() => {
      func.apply(this, args);
    }, wait);
  };
};

const tempType = [
  { value: "1", label: "简单模板" },
  { value: "0", label: "复杂模板" },
];

const isTopArr = [
  { value: "0", label: "否" },
  { value: "1", label: "是" },
];

export default {
  computed: {
    ...mapState("editor", {
      work: (state) => state.work,
      style: (state) => state.work.style.split(","),
      material: (state) => state.work.material.split(","),
      streetType: (state) => state.work.streetType.split(","),
      isSimpleTpl: (state) => state.work.isSimpleTpl,
    }),
    previewUrl() {
      return `${window.location.origin}/works/preview/${this.work.id}?view_mode=preview`;
    },
    releaseUrl() {
      return `${window.location.origin}/works/preview/${this.work.id}`;
    },
  },
  data() {
    return {
      styleMap: [],
      materialMap: [],
      streetTypeMap: [],
      loading: false,
    };
  },
  methods: {
    ...mapActions("editor", ["updateWork"]),
    /**
     * 修改标题、描述信息后自动保存
     * @param {Object} info { title/description: String }
     */
    autoSave(info) {
      this.updateWork(info);
    },
    beforeUpload () {
      this.loading = true
    },
    async handleChange(info){
      const status = info.file.status

      if (status === 'done' && info.file?.response?.code == '0') {
        this.loading = false
        this.$message.success(`模板图片上传成功.`)
        // start
        let url = info.file.response.data.urlPath
        if (/img-save-dir/.test(url)) {
          url = 'https://img-save-dir.oss-cn-hangzhou.aliyuncs.com' + url.split('img-save-dir')[1]
        }
        this.autoSave({cover_image_url: url})
        // end
        //this.autoSave({cover_image_url: info.file.response.data.urlPath})

      } else if (status === 'error') {
        this.$message.error(`模板图片上传失败.`)
      }
    }
  },
  mounted() {
    // 修改标题、描述信息后自动保存
    getDictById({ dictKey: "style" }).then(({ data }) => {
      this.styleMap = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
    getDictById({ dictKey: "material" }).then(({ data }) => {
      this.materialMap = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
    getDictById({ dictKey: "streetType" }).then(({ data }) => {
      this.streetTypeMap = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
  },
  render(h) {
    return (
      <div class="setting">
        <div class="info">
          <a-form label-col={{ span: 6 }} wrapper-col={{ span: 16, offset: 1 }}>
            <a-form-item label="标题">
              <a-input
                class="input"
                value={this.work.title}
                onChange={(e) => this.autoSave({ title: e.target.value })}
                // onBlur={this.saveTitle}
                placeholder="请输入标题"
              ></a-input>
            </a-form-item>
            <a-form-item label="风格">
              <a-select
                class="input"
                options={this.styleMap}
                mode="multiple"
                value={this.style}
                onChange={(e) => this.autoSave({ style: e.join(",") })}
                placeholder="请选择风格"
              ></a-select>
            </a-form-item>
            <a-form-item label="材质">
              <a-select
                class="input"
                options={this.materialMap}
                mode="multiple"
                value={this.material}
                onChange={(e) => this.autoSave({ material: e.join(",") })}
                placeholder="请选择材质"
              ></a-select>
            </a-form-item>
            <a-form-item label="街道类型">
              <a-select
                class="input"
                options={this.streetTypeMap}
                mode="multiple"
                value={this.streetType}
                onChange={(e) => this.autoSave({ streetType: e.join(",") })}
                placeholder="请选择街道类型"
              ></a-select>
            </a-form-item>
            <a-form-item label="模板类型">
              <a-select
                class="input"
                options={tempType}
                value={this.isSimpleTpl}
                onChange={(e) => this.autoSave({ isSimpleTpl: e })}
                placeholder="请选择模板类型"
              ></a-select>
            </a-form-item>
            <a-form-item label="是否置顶">
              <a-select
                class="input"
                options={isTopArr}
                value={this.work.isTop}
                onChange={(e) => this.autoSave({ isTop: e })}
                placeholder="请选择"
              ></a-select>
            </a-form-item>
            <a-form-item label="排序编号">
              <a-input
                class="input"
                type="number"
                value={this.work.sortNo}
                onChange={(e) => this.autoSave({ sortNo: e.target.value })}
                placeholder="请输入排序编号"
              ></a-input>
            </a-form-item>
            <a-form-item label="背景色">
              <el-color-picker
                show-alpha
                value={this.work.backgroundColor}
                style={{ "margin-bottom": "10px" }}
                onChange={(e) => this.autoSave({ backgroundColor: e })}
              ></el-color-picker>
            </a-form-item>
            <a-form-item label="模板图片">
              <a-upload
                class="avatar-uploader"
                show-upload-list={false}
                beforeUpload = {this.beforeUpload}
                list-type="picture-card"
                name="file"
                action={`${window.__baseUrl}/logo/attachment/uploadMaterialAttachmentOSS`}
                onChange={this.handleChange}
              >
                {this.work.cover_image_url ? (
                  <async-image  class="cover_image_url" src={this.work.cover_image_url} />
                ) : (
                  <div>
                    <a-icon type={this.loading ? "loading" : "plus"} />
                  </div>
                )}
              </a-upload>
            </a-form-item>
          </a-form>
        </div>
      </div>
    );
  },
};
