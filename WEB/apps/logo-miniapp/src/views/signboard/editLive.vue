<template>
  <div style="height: 100%;">
    <div class="edit-live-header">
      <van-button type="primary" class="recover" @click="creatLivePic" >保存</van-button>
    </div>
    <div class="edit-live">
      <div id="edit-live__wrap">
        <shape
          :handle-element-move-prop="handleElementMove"
          :handle-mousedown-prop="noop"
          :handle-point-move-prop="handleElementMove"
          :handle-element-mouse-up-prop="noop"
          :handle-rotation-prop="handleRotationProp"
          :handle-point-mouse-up-prop="noop"
          :default-position="style"
          :active="active"
          :delIcon="false"
          :style="getStyle()"
          class="shape_wrap"
        >
          <div class="shape_content">
            <img :src="currentShopSign" width="100%" />
          </div>
        </shape>

        <van-image width="100%" :src="currentLivePic" />
      </div>
    </div>
  </div>
</template>
<script>
import store from "core/store/mobileIndex";
import { mapState } from "vuex";
import { resolveImgUrl } from "core/support/imgUrl";
import shape from "core/support/shape";
import { Toast } from "vant";
import { takeScreenshot} from "@editor/utils/canvas-helper.js";
import { Notify } from "vant";
import { ImagePreview } from 'vant';
const sleep = async (time) => {
  return new Promise((r) => {
    setTimeout(() => r(), time);
  });
};
export default {
  store,
  components: {
    shape,
  },
  data() {
    return {
      style: {
        left: 20,
        top: 20,
        width: 100,
        height: 50,
        angle: 0,
      },
      active: true,
    };
  },
  computed: {
    ...mapState("editor", {
      currentLivePic: (state) => state.mobile.currentLivePic,
      currentShopSign: (state) => resolveImgUrl(state.mobile.currentShopSign),
    }),
  },
  methods: {
    handleElementMove(pos) {
      this.style = {
        ...this.style,
        ...pos,
      };
    },
    async creatLivePic() {
      const toast = Toast.loading({
        message: "生成中...",
        forbidClick: true,
        duration: 0,
      });
       try {
        const url = await takeScreenshot({ selector: "#edit-live__wrap", type: 'dataUrl' });
        Notify({ type: "success", message: "创建成功" });
        await sleep(1000);
        ImagePreview([url]);
      } catch (e) {
        Notify({ type: "danger", message: "创建失败" });
      }
      toast.clear();
    },

    handleRotationProp(angle) {
      this.style.angle = angle;
    },
    getStyle() {
      const style = Object.assign({}, this.style);
      Object.entries(style).forEach(([key, val]) => {
        style[key] = val + "px";
      });
      style.transform = `rotate(${this.style.angle}deg)`;
      return style;
    },
    noop() {},
  },
};
</script>
<style lang="scss" scoped>
.edit-live {
  background-color: #9d9c9c;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: calc(100% - 45px);
}
#edit-live__wrap {
  position: relative;
}
.edit-live__content {
  position: absolute;
  z-index: 100;
  padding: 10px;
  &.active {
    border: 1px solid #fa7a36;
  }
  .icon-fa-scale {
    position: absolute;
    bottom: -25px;
    right: -22px;
  }
}
.shape_wrap {
  z-index: 100;
  position: absolute;
}
.shape_content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
}
.edit-live-header {
  height: 45px;
  overflow: hidden;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
