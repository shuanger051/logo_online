import "./gallery.scss";
import { resolveImgUrl } from "core/support/imgUrl";
import Uploader from "core/support/image-gallery/components/uploader.js";

export default {
  name: "lbs-image-gallery",
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    value: {
      type: String,
      default: "",
    },
  },
  data: () => ({
    tabs: [
      {
        value: "personal",
        label: "我的图库",
      },
    ],
    activeTab: "personal",
    innerVisible: false,
    pixabayList: [],
  }),
  computed: {},
  watch: {
    visible(value) {
      this.innerVisible = value;
    },
  },
  methods: {
    showGallery() {
      this.innerVisible = true;
    },
    handleClose() {
      this.innerVisible = false;
    },
    changeTab({ key }) {
      this.activeTab = key;
    },
    handleSelectImage(item) {
      this.handleClose();
      this.$emit("change", item.url);
    },
    handlerUploadImg() {},
    renderDefaultActivator() {
      const activatorWithoutImg = (
        <Uploader uploadSuccess={this.handleSelectImage} style='display: block'>
          <div
            class="default-activator cursor-pointer empty-bg-activator"
            onClick={this.showGallery}
          >
            <a-icon type="plus" />
          </div>
        </Uploader>
      );
      const activatorWithImg = (
        <div>
          <div class="default-activator cursor-pointer ">
            <img
              src={resolveImgUrl(this.value, true)}
              width="50%"
              style={{ margin: "auto" }}
            />
          </div>
          <div class="flex-space-between" style="margin-top: 8px;">
            <Uploader uploadSuccess={this.handleSelectImage}>
              <a-button size="small">更换</a-button>
            </Uploader>
            <a-button
              size="small"
              onClick={(e) => {
                e.stopPropagation();
                this.handleSelectImage({ url: "" });
              }}
            >
              移除
            </a-button>
          </div>
        </div>
      );
      return this.value ? activatorWithImg : activatorWithoutImg;
      // return (this.value ? activatorWithImg : activatorWithoutImg)
    },
  },
  render(h) {
    return (
      <div>
        <a-input
          value={this.value}
          onChange={(e) => {
            this.$emit("change", e); // #309
          }}
          placeholder="输入图片链接/上传1"
        ></a-input>
        {this.renderDefaultActivator()}
      </div>
    );
  },
};
