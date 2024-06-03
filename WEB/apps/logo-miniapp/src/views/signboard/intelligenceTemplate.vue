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
        <div class="preview-wrap" :style="{height: item.height}">
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
import store from "core/mobile/store/index";
import { mapActions } from "vuex";
class BitSet {
	constructor(arg) {
		this.bits = [];
    this.repeat=0
	}
	add(n) {
		this.bits[n >> 5] |= 1 << (n & 31);
	}
	has(n) {
		let flag= !!(this.bits[n >> 5] & (1 << (n & 31)));
    if (flag) {
      this.repeat++
    }
    return flag
	}
}
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
      let clists = []
      lists.forEach(item => {
          if (!this._bitSet.has(item.id)) {
            clists.push(item)
            this._bitSet.add(item.id)
          }
      });
      return clists.map((item) => {
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
          ret.height = data.height*r + 'px!important'
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
        `/signboard/editSignboard/${id}?hasWork=1`
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
          const { list,total } = res.data;
          const resolveLists = this.resolveElement(list)
          this.list = oldArr.concat(resolveLists);
          this.finished = this.list.length + this._bitSet.repeat < total;
        })
        .finally(() => (this.loading = false));
    },
  },
  created() {
    this._bitSet = new BitSet()
  }
};
</script>
<style lang="scss" scoped>
.preview-wrap {
  width: 100%;
  overflow: hidden;
  margin-bottom: 10px;
}
</style>