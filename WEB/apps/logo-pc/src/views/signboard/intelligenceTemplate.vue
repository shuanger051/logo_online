<template>
  <div class="page-wrap">
    <!-- 店招模版选择 -->
    <a-list
      v-model="loading"
      :grid="{ gutter: 16, column: 3 }"
      :pagination="page"
      :data-source="list"
      :locale="{
        emptyText: '暂无相关店招模版',
      }"
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <div class="preview-wrap" style="height: 120px;"  @click="go(item.id, item)">
          <preview :elements="item.elements" :style="item.style" />
        </div>
      </a-list-item>
    </a-list>
  </div>
</template>
<script>
import { signboardService } from "@/services";
import Element from "core/models/element";
import preview from "core/editor/canvas/preview";
import store from "core/pc/store/index";
import { mapActions } from "vuex";
class BitSet {
  constructor(arg) {
    this.bits = [];
    this.repeat = 0;
  }
  add(n) {
    this.bits[n >> 5] |= 1 << (n & 31);
  }
  has(n) {
    let flag = !!(this.bits[n >> 5] & (1 << (n & 31)));
    if (flag) {
      this.repeat++;
    }
    return flag;
  }
}
export default {
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      page: {
        pageSize: 30,
        total: 0,
        current: 0,
        onChange: (page) => this.queryTemplate(page),
      },
    };
  },
  store,
  components: {
    preview,
  },
  methods: {
    ...mapActions("editor", ["setCurrentWorkData"]),

    resolveElement(lists) {
      let config = this.$route.query.tp;
      config = JSON.parse(decodeURIComponent(config));
      let clists = [];
      lists.forEach((item) => {
        if (!this._bitSet.has(item.id)) {
          clists.push(item);
          this._bitSet.add(item.id);
        }
      });
      return clists.map((item) => {
        const ret = {
          id: item.id,
          style: {},
        };
        try {
          const { domItem } = item;
          const data = JSON.parse(domItem);
          const lists = data.pages[0].elements;
          const textElement = lists.find((item) => {
            return (
              item.name == "lbp-text-tinymce" && item.pluginProps.isShopName
            );
          });
          if (textElement) {
            textElement.pluginProps.text = textElement.pluginProps.text.replace(
              /(<.*?>)?[^<]*/,
              (a, b) => {
                return b ? b + config.name : config.name;
              }
            );
            if (config.color) {
              textElement.pluginProps.fontColor = config.color;
            }
            if (config.font) {
              textElement.pluginProps.fontFamily = config.font;
            }
          }
          ret.elements = lists.map((item) => {
            return new Element(item);
          });
          ret.data = data;
          let r = this.calcRate(data.height, data.width);
          ret.style = {
            width: data.width + "px",
            margin: "0 auto",
            overflow: "hidden",
            height: data.height + "px",
            transform: `scale(${r})`,
            transformOrigin: "left top",
          };
          ret.height = 100 + "px!important";
        } catch (e) {
          ret.element = null;
        }
        return ret;
      });
    },
    calcRate(height, width) {
      //TODO
      return 300/width;
    },
    go(id, item) {
      this.setCurrentWorkData(item.data);
      this.$router.push(
        `/signboard/editSignboard/${id}?hasWork=1`
      );
    },
    // 模版查询
    queryTemplate(current, size) {
      const { page } = this;
      let pageNum = page.current + 1;
      // 是否存在页码参数
      if (current) pageNum = current;
      // 更新page size
      if (size) page.pageSize = size;
      this.loading = true;
      // 模拟翻页请求
      new Promise((resolve) => {
        if (this.tplArr.length) resolve();
        else
          signboardService
            .querySimpleTemplateByRandAPI({
              pageNum: 1,
              pageSize: 2000,
            })
            .then((res) => {
              const { list } = res.data;
              const resolveLists = this.resolveElement(list);
              this.tplArr = resolveLists;
              resolve();
            });
      })
        // 实现翻页
        .then(() => {
          const start = (pageNum - 1) * page.pageSize;
          this.list = this.tplArr.slice(start, start + page.pageSize);
          this.page.current = pageNum;
          this.page.total = this.tplArr.length;
        })
        .finally(() => (this.loading = false));
    },
  },
  created() {
      this.tplArr = [];
      this._bitSet = new BitSet();
      this.queryTemplate();
    },
};
</script>
<style lang="scss" scoped>
.preview-wrap {
  padding: 12px 24px;
  cursor: pointer;
  background-color: #fff;
}
.page-wrap {
  width: 1000px;
  padding: 12px 24px;
  margin-top: 24px;
  margin: 0 auto;
}
</style>
