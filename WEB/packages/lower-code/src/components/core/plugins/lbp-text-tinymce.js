// https://github.com/luban-h5-components/plugin-common-props
import PropTypes from "./propTypes";
import MobilePropTypes from "core/mobile/basicProps/mobile-plugin-props";
import font from "core/styles/fontMap";
const writingModeMap = [
  {
    label: "水平从左往右",
    value: "horizontal-tb",
  },
  {
    label: "垂直从上往下",
    value: "vertical-lr",
  },
];
export default {
  render(h) {
    const style = {
      fontFamily: this.fontFamily || "arial",
      position: "relative",
      color: `${this.fontColor} !important`,
      textDecoration: "none",
      // backgroundColor: this.backgroundColor || 'unset',
      // lineHeight: `${this.lineHeight}em`,
      border: `${this.borderWidth}px solid ${this.borderColor}`,
      borderRadius: `${this.borderRadius}px`,
      fontSize: `${this.fontSize}px` || "14px",
      writingMode: this.writingMode || "horizontal-tb",
      letterSpacing: `${this.letterSpacing || 0}px`,
      lineHeight: `${this.fontSize}px` || "14px",
    };
    return (
      <div
        style={style}
        domPropsInnerHTML={this.text}
        class="ql-editor ql-container"
      ></div>
    );
  },
  name: "lbp-text-tinymce",
  data() {
    return {
      innerText: this.text || "双击修改文字",
    };
  },
  mobileProps: {
    fontColor: MobilePropTypes.color({ label: "颜色", defaultValue: "#000" }),
    fontFamily: MobilePropTypes.select({
      defaultValue: "宋体",
      label: "字体",
      options: font,
    }),
    fontSize: MobilePropTypes.number({
      defaultValue: 14,
      label: "字体大小(px)",
      props: {
        inputWidth: "50px",
        buttonSize: "42px",
      },
    }),
    text: MobilePropTypes.string({
      label: "内容",
      defaultValue: "双击修改文字",
      visible: true,
      component: "tinymce-editor",
      hiddenLabel: true,
      props: {
        init: {
          language: "zh_CN", //注意大小写
          language_url: "/tinymac/CN.js",
          menubar: false,
          width: "90vw",
          height: "100px",
          elementpath: false,
          statusbar: false,
          toolbar: `formatselect | bold italic |`,
        },
      },
    }),
  },

  pcProps: {
    fontColor: PropTypes.color({
      label: "颜色",
      defaultValue: "#000",
      showLable: true,
    }),
    fontFamily: PropTypes.select({
      defaultValue: "宋体",
      label: "字体",
      options: font,
      showLable: true,
    }),
    fontSize: PropTypes.number({
      defaultValue: 14,
      label: "字体大小",
      options: font,
      showLable: true,
    }),
    text: MobilePropTypes.string({
      label: "内容",
      defaultValue: "双击修改文字",
      visible: true,
      component: "tinymce-editor",
      hiddenLabel: true,
      props: {
        init: {
          language: "zh_CN", //注意大小写
          language_url: "/tinymac/CN.js",
          menubar: false,
          width: "90vw",
          height: "100px",
          elementpath: false,
          statusbar: false,
          toolbar: `formatselect | bold italic |`,
        },
      },
    }),
  },

  props: {
    fontColor: PropTypes.color({ label: "颜色", defaultValue: "#000" }),
    fontFamily: PropTypes.select({
      defaultValue: "宋体",
      label: "字体",
      options: font,
    }),
    fontSize: PropTypes.number({
      defaultValue: 14,
      label: "字体大小(px)",
      options: font,
    }),
    letterSpacing: PropTypes.number({ defaultValue: 0, label: "字体间距(px)" }),
    writingMode: PropTypes.select({
      defaultValue: "horizontal-tb",
      label: "字体方向",
      options: writingModeMap,
    }),
    borderWidth: PropTypes.number({ label: "边框宽度(px)", defaultValue: 0 }),
    borderRadius: PropTypes.number({ label: "圆角(px)" }),
    borderColor: PropTypes.color({ label: "边框颜色" }),
    isShopName: PropTypes.boolean({ label: "店招名称" }),
    text: PropTypes.string({
      label: "内容",
      defaultValue: "双击修改文字",
      visible: true,
      hiddenLabel: true,
      component: "tinymce-editor",
      props: {
        init: {
          menubar: false,
          toolbar: `formatselect | bold italic backcolor | \
        alignleft aligncenter alignright alignjustify | \
        bullist numlist outdent indent`,
        },
      },
    }),
    editorMode: PropTypes.string({
      defaultValue: "preview", // 可选值: preview/edit
      label: "模式",
      visible: false,
    }),
    isEditingElement: PropTypes.boolean({
      defaultValue: false, // 可选值: preview/edit
      label: "是否当前元素",
      visible: false,
    }),
  },
  editorConfig: {},
};
