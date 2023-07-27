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

const styleMap = [
  { value: "1", label: "古典风" },
  { value: "2", label: "现代风" },
  { value: "3", label: "商务风" },
  { value: "4", label: "极简风" },
  { value: "5", label: "欧式风" },
  { value: "6", label: "美式风" },
  { value: "7", label: "原木风" },
  { value: "8", label: "工业风" },
  { value: "9", label: "田园风" },
];

export default {
  computed: {
    ...mapState("editor", {
      work: (state) => state.work,
      style: (state) => state.work.style.split(","),
    }),
    previewUrl() {
      return `${window.location.origin}/works/preview/${this.work.id}?view_mode=preview`;
    },
    releaseUrl() {
      return `${window.location.origin}/works/preview/${this.work.id}`;
    }
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
  },
  mounted() {
    // 修改标题、描述信息后自动保存
  },
  render(h) {
    window.aaaa = this;
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
                options={styleMap}
                mode="multiple"
                value={this.style}
                onChange={(e) => this.autoSave({ style: e.join(",") })}
                placeholder="请选择风格"
              ></a-select>
            </a-form-item>
            <a-form-item label="背景色">
              <el-color-picker
                show-alpha
                value={this.work.backgroundColor}
                style={{ "margin-bottom": "10px" }}
                onChange={(e) => this.autoSave({ backgroundColor: e })}
              ></el-color-picker>
            </a-form-item>
          </a-form>
        </div>
      </div>
    );
  },
};
