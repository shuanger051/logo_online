<template>
  
  <div class="page-wrap">
    <a-divider orientation="left">店招牌类型</a-divider>
    <a-checkbox-group
      v-model="formData.material"
      name="material"
      :options="attrs.material"
    >
    </a-checkbox-group>
    <a-divider orientation="left">风格</a-divider>
    <a-row type="flex" style="margin-bottom: 20px;">
      <a-col flex="auto" class="flex" style="flex-grow: 0; margin-right: 200px;">
        <span class="label">立面颜色:</span>
        <a-select v-model="attrs.lmcolor">
          <a-select-option v-for="item in lmcolorLists" :value="item.code">
           <div class="lmpicker">
             <div>{{item.name}}</div>
             <span  :style="{ backgroundColor: getColorByName(item.code) }"></span>
            </div>
          </a-select-option>
        </a-select>
      </a-col>
      <a-col flex="auto" class="flex" style="flex-grow: 0;">
          <span class="label">招牌背景色:</span>
          <span
            :style="{ backgroundColor: attrs.zpcolor }"
            class="colorButton"
            size="small"
            @click="zppicker = true"
          ></span>

          <a-modal v-model="zppicker" cancelText="取消" okText="确定" @ok="zppicker=false">
            <compact
              :value="attrs.zpcolor"
              @input="resolveColor"
              :palette="zpcolorLists"
            ></compact>
          </a-modal>
      </a-col>
    </a-row>
    <a-row type="flex" style="margin-bottom: 20px;">
      <a-col flex="auto" class="flex" style="flex-grow: 0;margin-right: 200px;">
        <span class="label">主要字体:</span>
        <a-select v-model="attrs.font">
          <a-select-option v-for="item in fontsLists" :value="item.value">
           <span>{{ item.label }}</span>
          </a-select-option>
        </a-select>
      </a-col>
      <a-col flex="auto" class="flex" style="flex-grow: 0;">
      <span class="label">店招长宽比:</span>
        <a-select v-model="attrs.whratio">
          <a-select-option v-for="item in whratioLists" :value="item">
           <span>{{ item }}</span>
          </a-select-option>
        </a-select>
        </a-col>
      </a-row>


      <a-row type="flex"  style="margin-bottom: 20px;">
      <a-col flex="auto" class="flex" style="flex-grow: 0;margin-right: 200px;">
        <span class="label">所在楼层:</span>
        <a-select v-model="attrs.floor" @change = "changeFloor">
          <a-select-option v-for="item in floorLists" :value="item">
           <span>{{ item }}</span>
          </a-select-option>
        </a-select>
      </a-col>
      </a-row>
    <div class="action-bar">
      <a-button type="primary" @click="onNext">下一步</a-button>
    </div>
  </div>
</template>
<script>
import { appGetItemsByDictKeyInDB } from "core/api";
import { Compact } from "vue-color";
import fonts from "core/styles/fontMap";

export default {
  data() {
    let style = window.pageContentJson.style;

    return {
      zppicker: false,
      formData: {},
      // 属性列表
      attrs: {
        styles: [],
        material: [],
        streetType: [],
        lmcolor: style.lmcolor[0].code,
        zpcolor: null,
        font: fonts[0].value,
        whratio: style.whratio[0],
        floor: style.floor[0]
      },
    };
  },
  components: { compact: Compact },

  created() {
    let style = window.pageContentJson.style;
    this.lmcolorLists = style.lmcolor.map((v) => {
      return v;
    });
    this.fontsLists = fonts
    this.floorLists = style.floor;

    this.whratioLists = style.whratio;
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
  },
  computed: {
    zpcolorLists() {
      let style = window.pageContentJson.style;
      let lmcolor = this.attrs.lmcolor;
      let ret = [];
      style.lmcolor.some((v) => {
        if (v.code == lmcolor) {
          ret = v.rgb;
          return true;
        }
      });
      return ret;
    },
  },
  watch: {
    "zpcolorLists": {
      handler(v) {
        this.attrs.zpcolor = this.zpcolorLists[0]
      },
      immediate: true,
    },
  },
  methods: {
    getColorByName(c) {
      let v = {
        '1': 'rgb(196, 203, 205)',
        '2': 'rgb(227, 223, 215)',
        '3': 'rgb(112, 103, 96)',
        '4': 'rgb(75, 82, 89)',
      }
      
     return v[c]
    },
    resolveColor(m = {}) {
      this.attrs.zpcolor = m.hex;
    },
    changeFloor(v) {
      if (v == window.pageContentJson.style.floor[1]) {

        this.$notification.warning({
          message: '选择"' + v + '"只能用立体字模版',
        });
      }
    },
    onNext() {
      const { formData } = this;
      let query = Object.assign({
        lttpt: this.attrs.floor == window.pageContentJson.style.floor[0] ? 0 : 1,
        styles: this.attrs.lmcolor
      }, this.$route.query);
      Object.keys(formData).forEach((key) => {
        query[key] = formData[key].join(",");
      });
      
      this.$router.push({ path: "/signboard/template", query });
    },
  },
};
</script>
<style lang="less" scoped>
  .lmpicker {
    display: flex;
    align-items: center;
    padding: 0 5px;
    span {
      width: 20px;
      height: 20px;
      border: 1px solid #646566;
      margin-left: 5px;
    }
  }
  .ant-select {width: 130px;}
  .label {width: 80px;}
  .flex {
    display: flex;
    align-items: center;
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
.page-wrap {
  padding: 12px 24px 60px;
  max-width: 1000px;
  margin: 0 auto;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
  :deep(.ant-checkbox-group) {
    margin-bottom: 12px;
    &-item > span {
      font-size: 15px;
    }
  }
  :deep(.ant-divider) {
    &-inner-text {
      font-size: 15px;
      color: #444;
    }
  }
  .action-bar {
    margin-top: 24px;
  }
}
</style>
