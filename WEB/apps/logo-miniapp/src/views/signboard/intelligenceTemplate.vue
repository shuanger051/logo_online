<template>
  <div class="page-wrap">
    <!-- 店招模版选择 -->
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="queryTemplate"
    >
      <div v-for="item in list" :key="item.id" @click="go(item.id, item)">
        <div class="preview-wrap">
          <preview :elements = "item.elements" :style = item.style />
      </div>
      </div>
    </van-list>
  </div>
</template>
<script>
import { signboardService } from "@/apis";
import Element  from 'core/models/element'
import preview from 'core/editor/canvas/preview'
import store from "core/store/mobileIndex";
import { mapActions } from "vuex";

export default {
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      page: {
        size: 30,
        current: 0,
      },
    };
  },
  store,
  components: {
    preview
  },
  methods: {
    ...mapActions("editor", ["setCurrentWorkData"]),

    resolveElement(lists) {
      let name = this.$route.query.name
      return lists.map((item) => {
        const ret = {
          id: item.id,
          style: {}
        };
        try {
          const { domItem } = item;
          const data = JSON.parse(domItem);
          const lists = data.pages[0].elements
          const textElement = lists.find((item) => {
            return item.name == 'lbp-text-tinymce' && item.pluginProps.isShopName
          })
          if (textElement) {
            textElement.pluginProps.text = textElement.pluginProps.text.replace(/(<.*?>)?[^<]*/,(a, b) => {
              return b ? b + name : name
            })
          }
          ret.elements = lists.map((item) => {
            return new Element(item)
          })
          ret.data = data
          let r = this.calcRate(data.height,data.width)
          ret.style = {
            width: data.width + 'px',
            margin: '0 auto',
            overflow: 'hidden',
            height: data.height + 'px',
            transform: `scale(${r})`,
            transformOrigin: "left top",
          }
        } catch (e) {
          ret.element = null
        }
        return ret;
      });
    },
    calcRate(height, width) {
      //TODO
      let cw = document.documentElement.clientWidth
      let r = (200/height)*(document.documentElement.clientWidth/375)
      if (r*width >cw){
         r = width/cw
      }
      return width/cw
    },
    go(id, item) {
      this.setCurrentWorkData(item.data)
      this.$router.push(
        `/editSignboard/${id}?shopId=${this.$route.query.shopId}&hasWork=1`
      );
    },
    // 模版查询
    queryTemplate() {
      const { list: oldArr, page } = this;
      const { size, current } = page;
      const pageNum = current + 1;
      this.loading = true;
      signboardService
        .querySimpleTemplateByRandAPI({
          pageNum,
          pageSize: size,
        })
        .then((res) => {
          page.current = pageNum;
          const { list } = res.data;
          this.list = oldArr.concat(this.resolveElement(list));
          this.finished = list.length < size;
        })
        .finally(() => (this.loading = false));
    },
  },
};
</script>
<style lang="scss" scoped>
.preview-wrap {
  width: 100%;
  overflow: hidden;
}
</style>