<template>
  <div class="intelligence-design">
    <van-panel
      title="智能设计"
      desc="智能设计是系统依据您的店铺类型、位置等信息，采用人工智能技术自动设计生成店铺店招，您可以在输入您的店铺名称后，方便快捷的选择系统生成的店铺店招。"
    >
    </van-panel>
    <van-form @submit="onSubmit">
      <van-field
        v-model="name"
        name="店招名称"
        label="店招名称"
        placeholder="店招名称"
        :rules="[{ required: true, message: '请填写店招名称' }]"
      />
      <van-field
        readonly
        clickable
        label="字体"
        :value="fontFamily"
        placeholder="字体"
        @click="showPicker = true"
      />

      <van-popup v-model="showPicker" position="bottom">
        <van-picker
          show-toolbar
          :columns="columns"
          @cancel="showPicker = false"
          @confirm="onConfirm"
        />
      </van-popup>

      <van-cell-group>
        <van-cell  title="颜色" class="colorTitle">
          <span :style="{'backgroundColor':color}" class="colorButton" size="small"  @click="showColor = true"></span>
        </van-cell>
      </van-cell-group>
      
      <van-dialog v-model="showColor" show-confirm-button>

        <compact :value="fontColor" @input="resolveColor"></compact>
      </van-dialog>

      <div style="margin: 16px; margin-top: 30px;">
        <van-button round block type="info" native-type="submit"
          >确定</van-button
        >
      </div>
    </van-form>
  </div>
</template>
<script>
import fonts from "core/styles/fontMap";
import { Compact } from "vue-color";
export default {
  data() {
    return {
      name: "",
      showPicker: false,
      showColor: false,
      fontLabel: "宋体",
      fontColor: { r: 0, g: 0, b: 0, a:1 },
    };
  },
  components: {
    compact: Compact,
  },
  computed: {
    color() {
      let rgb = ['r','g','b','a'].map((v) => {
        return this.fontColor[v]
      })
      return `rgba(${rgb.join(',')})` 
    },
    fontFamily() {
      return this.filterFonts(this.fontLabel, false).label;
    },
  },
  methods: {
    onSubmit() {
      let tp = {
        name: this.name,
        font: this.fontLabel,
        color: this.color
      }
      this.$router.push({
        name: "intelligenceTemplate",
        query: {
          ...this.$route.query,
          tp: encodeURIComponent(JSON.stringify(tp)),
        },
      });
    },
    filterFonts(val, labelOrValue = true) {
      const item = fonts.find((column) => {
        return column[labelOrValue ? "label" : "value"] == val;
      });
      return item;
    },
    onConfirm(value) {
      this.fontLabel = this.filterFonts(value).value;
      this.showPicker = false;
    },
    resolveColor(v) {
      this.fontColor = v.rgba
    }
  },
  created() {
    this.columns = fonts.map((item) => {
      return item.label;
    });
  },
};
</script>
<style lang="less" scoped>
.intelligence-design :deep(.vc-compact) {
  padding:10px 10px;
  width: 100%;
  .vc-compact-color-item {
    width: 25px;
    height: 25px;
  }
}
.intelligence-design {
  :deep(.van-dialog__confirm) {
    color: #2f63f1;
  }
  .van-cell__value {
    overflow:visible;
  }
  .colorButton {
    position: relative;
    z-index: 111;
    display:inline-block;
    left: -210px;
    width: 30px;
    height: 30px;
  }
  .colorTitle {
    color:#646566
  }
}
</style>
