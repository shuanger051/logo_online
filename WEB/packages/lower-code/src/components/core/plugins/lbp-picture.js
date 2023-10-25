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
    const imgStyle = {
       filter: 'opacity('+(this.opacity == null ? 100: this.opacity)+'%)'
    }
    const {xRate, yRate} = this
    if (xRate && yRate) {
       style.alignItems = 'center';
       const {commonStyle} = this.element
       let wh = {}
       let hh
       if ((hh=commonStyle.width* yRate/xRate) <= commonStyle.height) {
          wh.w = commonStyle.width;
          wh.h = hh
       } else {
          wh.h = commonStyle.height;
          wh.w = commonStyle.height * xRate/yRate
       }
      return (
        <div style={style}>
            <img src={url} height={wh.h + 'px'} width={wh.w + 'px'} style={imgStyle} />
        </div>
      );
    }
    if (this.fillType == "none") {
      return <img src={url} alt="" width="100%" height="100%" style={imgStyle} />;
    } else if (this.fillType == "horizontally") {
      return (
        <div style={style}>
          <img src={url} height="100%" style={imgStyle} />
        </div>
      );
    } else {
      style.flexDirection = "column";
      return(
        <div style={style}>
          <img src={url} width="100%" style={imgStyle} />
        </div>
      );
    }
  },
  mobileProps: {
    imgSrc: MobilePropTypes.image(),
    xRate: MobilePropTypes.number({ defaultValue: 0, label: "x轴", showLable: true, props: {
      "inputWidth":"50px",
      "buttonSize": "42px",
      min: 0,

    }}),
    yRate: MobilePropTypes.number({ defaultValue: 0, label: "y轴",showLable: true, props: {
      "inputWidth":"50px",
      "buttonSize": "42px",
      min: 0,
    }}),
    opacity: MobilePropTypes.slider({
      defaultValue: 100,
      label: '透明度',
      showLable: true
    })
  },
  props: {
    imgSrc: PropTypes.image(),
    xRate: PropTypes.number({ defaultValue: 0, visible: false}),
    yRate: PropTypes.number({ defaultValue: 0, visible: false}),
    opacity: PropTypes.number({
      defaultValue: 100,
      label: '透明度',
      visible: false
    }),
    element: {
      type: Object,
      visible: false,
      default: () => {return {}}
    },
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
