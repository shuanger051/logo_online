<template>
  <div :name="source.id">
    <!--一级标签分类-->
    <div class="first-tag" @click="toggleShow(source)" :title="source.tagName">
      <i class="iconfont icon-ios-arrow-right" :style="source.show?'transform: rotate(90deg)':''"
         style="display:inline-block;"></i>
      {{ source.tagName }}
    </div>
    <div v-show="source.children&&source.show" class="second-layer">
                      <span v-for="value in source.children" :key="value.id">
                         <!--二级标签-->
                        <div v-show="value.children">
                          <p class="tag-second-group">{{ value.tagName }}</p>
                          <!--三级标签-->
                          <span v-for="val in value.children" :key="val.id" :value="val" :label="val"
                                @click="chooseBtn(val)"
                                class="h-checkbtn-wrapper"
                                :class="{'h-checkbtn-wrapper-selected':hasChooseTagList.find(cho=>cho.id==val.id)}"
                                style="margin-right: 8px; margin-bottom: 12px;">
                            <div class="h-checkbtn-inner tag-name" :title="val.tagName">{{val.tagName}}</div>
                            <div class="h-checkbtn-icon"
                                 :style="{display:hasChooseTagList.find(cho=>cho.id==val.id)?'block':'none'}"><i
                              class="h-icon iconfont icon-right h-icon-right"></i></div>
                          </span>
                        </div>
                          <span v-show="!value.children" :value="value" :label="value" @click="chooseBtn(value)"
                                class="h-checkbtn-wrapper"
                                :class="{'h-checkbtn-wrapper-selected':hasChooseTagList.find(cho=>cho.id==value.id)}"
                                style="margin-right: 8px; margin-bottom: 12px;">
                            <div class="h-checkbtn-inner tag-name" :title="value.tagName">{{value.tagName}}</div>
                            <div class="h-checkbtn-icon"
                                 :style="{display:hasChooseTagList.find(cho=>cho.id==value.id)?'block':'none'}"><i
                              class="h-icon iconfont icon-right h-icon-right"></i></div>
                          </span>
                      </span>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  props: {
    source: {
      type: Object,
      default() {
        return {}
      }
    },
    index: {
      type: Number,
      default: 0
    },
    hasChooseTagList: {
      type: Array,
      default() {
        return []
      }
    },
    chooseBtn: {
      type: Function
    }
  },
  data() {
    return {}
  },
  methods: {
    toggleShow(item) {
      item.show = !item.show
    }
  },
  watch: {}
}
</script>
<style lang="scss" scoped>
  .first-tag {
    width: 100%;
    height: 38px;
    color: #666;
    background: #f7f7f7;
    line-height: 38px;
    font-weight: 700;
    padding-left: 18px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .chooseBtn {
    background: pink;
  }

  .second-tag {
    height: 0;
    transition: height 0.2s ease;
  }

  .second-layer {
    padding: 16px 18px;
    .tag-second-group {
      margin-bottom: 8px;
    }
    .tag-name {
      max-width: 188px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
</style>
