import { mapState, mapActions } from "vuex";
import "./share-info.scss";
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

export default {
  computed: {
    ...mapState("editor", {
      work: (state) => state.work,
    }),
    previewUrl() {
      return `${window.location.origin}/works/preview/${this.work.id}?view_mode=preview`;
    },
    releaseUrl() {
      return `${window.location.origin}/works/preview/${this.work.id}`;
    },
    previewEngineDoc() {
      return "https://ly525.github.io/luban-h5/zh/getting-started/quick-start.html#_2-%E6%9E%84%E5%BB%BA%E9%A2%84%E8%A7%88%E6%89%80%E9%9C%80%E7%9A%84%E6%B8%B2%E6%9F%93%E5%BC%95%E6%93%8E";
    },
  },
  methods: {
    ...mapActions("editor", ["saveWork", "updateWork"]),
    /**
     * 修改标题、描述信息后自动保存
     * @param {Object} info { title/description: String }
     */
    autoSave(info) {
      this.updateWork(info);
      // this.debounceSave();
    },
  },
  mounted() {
    // 修改标题、描述信息后自动保存
    this.debounceSave = debounce(() => this.saveWork(), 2000);
  },
  render(h) {
    return (
      <div class="setting">
        <div class="info">
          <h4 class="label">设置作品信息</h4>
          <a-input
            class="input"
            style={{ "margin-bottom": "10px" }}
            value={this.work.title}
            onChange={(e) => this.autoSave({ title: e.target.value })}
            // onBlur={this.saveTitle}
            placeholder="请输入标题"
          ></a-input>
          <a-input
            class="input"
            value={this.work.description}
            onChange={(e) => this.autoSave({ description: e.target.value })}
            // v-model="description"
            // onBlur={this.saveDescription}
            placeholder="请输入描述"
            type="textarea"
          ></a-input>
          <el-color-picker
           show-alpha 
           value={this.work.backgroundColor} 
           onChange={(e) => {
            this.autoSave({ backgroundColor: e })
          }}
          ></el-color-picker>
        </div>
      </div>
    );
  },
};
