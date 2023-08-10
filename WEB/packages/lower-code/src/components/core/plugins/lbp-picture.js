import PropTypes from "@luban-h5/plugin-common-props";
import { resolveImgUrl } from "core/support/imgUrl";
import MobilePropTypes from 'core/mobile/basicProps/mobile-plugin-props'
import placeholderImg from "./lbp-picture-placeholder.png"; // issue #34
export default {
  name: "lbp-picture",
  render() {
    const url = this.getUrl(this.imgSrc || placeholderImg);
    const style = {
      width: "100%",
      height: "100%",
      display: "flex",
      overflow: "hidden",
      justifyContent: "center",
    };
    if (this.fillType == "none") {
      return <img src={url} alt="" srcset="" width="100%" height="100%" />;
    } else if (this.fillType == "horizontally") {
      return (
        <div style={style}>
          <img src={url} height="100%" />
        </div>
      );
    } else {
      style.flexDirection = "column";
      return(
        <div style={style}>
          <img src={url} width="100%" />
        </div>
      );
    }
  },
  mobileProps: {
    imgSrc: MobilePropTypes.image()
  },
  props: {
    imgSrc: PropTypes.image(),
    fillType: {
      type: String,
      default: "vertical-center",
      editor: {
        type: "a-select",
        label: "填充方式",
        props: {
          options: [
            { label: "原始", value: "none" },
            { label: "水平居中", value: "vertical-center" },
            { label: "垂直居中", value: "horizontally" },
          ],
        },
      },
    },
  },
  data: () => ({
    placeholderImg,
  }),
  methods: {
    getUrl(url) {
      return resolveImgUrl(url);
    },
  },
};
