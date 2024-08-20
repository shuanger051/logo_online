<template>
  <div class="page-wrap">
    <!-- 店招牌类型 -->
    <van-panel title="店招牌类型">
      <van-checkbox-group v-model="formData.material">
        <van-row>
          <van-col v-for="item in attrs.material" :key="item.value" :span="12">
            <van-checkbox :name="item.value">{{ item.label }}</van-checkbox>
          </van-col>
        </van-row>
      </van-checkbox-group>
    </van-panel>
    <van-panel title="风格" class="style-panel">
      <van-row>
        <van-col :span="12">
          <span>立面颜色</span
          ><span @click="lmpicker = true" class="btn">{{ attrs.lmcolor }}</span>

          <van-popup
            v-model="lmpicker"
            position="bottom"
            :style="{ height: '30%' }"
          >
            <van-picker
              show-toolbar
              :columns="lmcolorLists"
              @cancel="lmpicker = false"
              v-model="attrs.lmcolor"
              @confirm="(v) => (onConfirm('lmcolor', v), (lmpicker = false))"
            >
          <template #option="option">
            <div class="lmpicker">
             <div>{{option}}</div>
             <span  :style="{ backgroundColor: getColorByName(option) }"></span>
            </div>
          </template>
          </van-picker>
          </van-popup>
        </van-col>

        <van-col :span="12">
          <span>招牌背景色</span>
          <span
            :style="{ backgroundColor: attrs.zpcolor }"
            class="colorButton"
            size="small"
            @click="zppicker = true"
          ></span>

          <van-dialog v-model="zppicker" show-confirm-button>
            <compact
              :value="attrs.zpcolor"
              @input="resolveColor"
              :palette="zpcolorLists"
            ></compact>
          </van-dialog>
        </van-col>

        <van-col :span="12">
          <span>主要字体</span>
          <span @click="fontpicker = true" class="btn">{{ attrs.font }}</span>

          <van-popup
            v-model="fontpicker"
            position="bottom"
            :style="{ height: '30%' }"
          >
            <van-picker
              show-toolbar
              :columns="font"
              @cancel="fontpicker = false"
              @confirm="(v) => (onConfirm('font', v), (fontpicker = false))"
            />
          </van-popup>
        </van-col>

        <van-col :span="12">
          <span>店招长宽比</span>
          <span @click="whratiopicker = true" class="btn">{{
            attrs.whratio
          }}</span>

          <van-popup
            v-model="whratiopicker"
            position="bottom"
            :style="{ height: '30%' }"
          >
            <van-picker
              show-toolbar
              :columns="whratio"
              @cancel="whratiopicker = false"
              @confirm="
                (v) => (onConfirm('whratio', v), (whratiopicker = false))
              "
            />
          </van-popup>
        </van-col>

        <van-col :span="12">
          <span>所在楼层</span>
          <span @click="floorpicker = true" class="btn">{{ attrs.floor }}</span>

          <van-popup
            v-model="floorpicker"
            position="bottom"
            :style="{ height: '30%' }"
          >
            <van-picker
              show-toolbar
              :columns="floor"
              @cancel="floorpicker = false"
              @confirm="(v) => (onConfirm('floor', v), (floorpicker = false))"
            />
          </van-popup>
        </van-col>
      </van-row>
    </van-panel>
    <submit-bar>
      <van-button block type="primary" @click="onNext">下一步</van-button>
    </submit-bar>
  </div>
</template>
<script>
import SubmitBar from "../../components/SubmitBar.vue";
import { Notify } from "vant";
import fonts from "core/styles/fontMap";
import { appGetItemsByDictKeyInDB } from "core/api";
import { Compact } from "vue-color";

export default {
  components: { SubmitBar, compact: Compact },
  data() {
    let style = window.pageContentJson.style;

    return {
      formData: {},
      lmpicker: false,
      zppicker: false,
      whratiopicker: false,
      fontpicker: false,
      floorpicker: false,
      // 属性列表
      attrs: {
        styles: [],
        material: [],
        streetType: [],
        lmcolor: style.lmcolor[0].name,
        zpcolor: null,
        whratio: style.whratio[0],
        floor: style.floor[0],
        font: fonts[0].value,
      },
    };
  },
  computed: {
    zpcolorLists() {
      let style = window.pageContentJson.style;
      let lmcolor = this.attrs.lmcolor;
      let ret = [];
      style.lmcolor.some((v) => {
        if (v.name == lmcolor) {
          ret = v.rgb;
          return true;
        }
      });
      return ret;
    },
  },
  watch: {
    "attrs.lmcolor": {
      handler(v) {
        this.attrs.zpcolor = this.zpcolorLists[0]
      },
      immediate: true,
    },
  },
  created() {
    let style = window.pageContentJson.style;

    this.lmcolorLists = style.lmcolor.map((v) => {
      return v.name;
    });
    this.whratio = style.whratio;
    this.floor = style.floor;
    this.font = fonts.map((v) => {
      return v.label;
    });
    appGetItemsByDictKeyInDB({ dictKey: "style" }).then(({ data }) => {
      this.attrs.styles = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
    appGetItemsByDictKeyInDB({ dictKey: "material" }).then(({ data }) => {
      this.attrs.material = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
    appGetItemsByDictKeyInDB({ dictKey: "streetType" }).then(({ data }) => {
      this.attrs.streetType = data.map((item) => {
        return {
          value: item.itemKey,
          label: item.itemValue,
        };
      });
    });
  },
  methods: {
    onNext() {
      const { formData } = this;
      let style = window.pageContentJson.style;
      let query = Object.assign({}, this.$route.query);
      Object.keys(formData).forEach((key) => {
        query[key] = formData[key].join(",");
      });
      style.lmcolor.some((v) => {
        if (v.name == this.attrs.lmcolor && v.code) {
          query.styles = v.code;
          return true;
        }
      });
      query.lttpt = this.attrs.floor == style.floor[0] ? 0 : 1;
      this.$router.push({ path: "/signboard/template", query });
    },
    getColorByName(option) {
      let v = {
        '1': 'rgb(196, 203, 205)',
        '2': 'rgb(227, 223, 215)',
        '3': 'rgb(112, 103, 96)',
        '4': 'rgb(75, 82, 89)',
      }
      let c = window.pageContentJson.style.lmcolor.filter((v) => {
        return v.name == option
      })
     return v[c[0].code]
    },
    onConfirm(key, v) {
      if (key == "floor" && v == window.pageContentJson.style.floor[1]) {
        Notify({ type: "warning", message: '选择"' + v + '"只能用立体字模版' });
      }
      this.attrs[key] = v;
    },
    resolveColor(m = {}) {
      this.attrs.zpcolor = m.hex;
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  box-sizing: border-box;
  padding: 24px 12px 64px;
  background-color: @gray-2;
  height: 100%;
  .btn {
    height: 32px;
    width: 60px;
    margin-left: 5px;
    border: 1px solid #2f63f1;
    color: #2f63f1;
    font-size: 12px;
    text-align: center;
    line-height: 32px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .colorButton {
    width: 40px;
    height: 40px;
    border: 1px solid #646566;
    margin-left: 5px;
    display: inline-block;
  }
  .vc-compact {
    box-shadow: none;
    padding: 10px 10px;
    width: 100%;
  }
  :deep(.vc-compact-color-item) {
    width: 25px;
    height: 25px;
  }
  :deep(.van-dialog__confirm) {
    color: #2f63f1;
  }
  .van-cell__value {
    overflow: visible;
  }
  .lmpicker {
    display: flex;
    span {
      width: 20px;
      height: 20px;
      border: 1px solid #646566;
      margin-left: 5px;
    }
  }
  .colorTitle {
    color: #646566;
  }
  .style-panel .van-col {
    display: flex;
    align-items: center;
  }

  :deep(.van-panel) {
    margin-bottom: 12px;
    border-radius: 8px;
    overflow: hidden;
    &__header {
      position: relative;
      line-height: 24px;
      font-size: 16px;
      &::before {
        content: "";
        display: inline-block;
        margin-right: 8px;
        transform: translateY(5px);
        width: 4px;
        height: 14px;
        background-color: @blue;
      }
    }
    &__content {
      padding: 12px 0;
      .van-col {
        box-sizing: border-box;
        padding: 12px 24px;
      }
      .van-radio {
        font-size: 14px;
      }
    }
  }
}
</style>
