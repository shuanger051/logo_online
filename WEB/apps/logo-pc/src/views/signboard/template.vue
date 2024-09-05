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
        <async-image
          @click.native="go(item.id)"
          width="100%"
          height="100px"
          :style="{ objectFit: 'contain' }"
          :src="item.url"
        />
      </a-list-item>
    </a-list>
  </div>
</template>
<script>
import { signboardService } from "@/services";
import { resolveImgUrl } from "core/support/imgUrl";
import _ from "lodash";
// 所有模板数据
let tplArr = [];
export default {
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      // 查询条件
      params: {},
      page: {
        pageSize: 30,
        total: 0,
        current: 0,
        onChange: (page) => this.queryTemplate(page),
      },
    };
  },
  created() {
    // 保存查询条件
    this.params = _.pick(this.$route.query, ["styles", "material"]);
    if (this.$route.query.lttpt == 1) {
      this.params.id = 591;
    }
    tplArr = [];
    console.log("tplArr", tplArr);
    this.queryTemplate();
  },
  methods: {
    resolveList(lists) {
      return (
        lists
          .map((item) => {
            const ret = {
              id: item.id,
            };
            try {
              const { domItem } = item;
              const data = JSON.parse(domItem);
              ret.url = resolveImgUrl(data.cover_image_url, true);
            } catch (e) {
              console.log(e);
              ret.url = null;
            }
            return ret;
          })
          // 过滤url为空的数据
          .filter((item) => item.url)
      );
    },
    go(tplId) {
      this.$router.push(
        `/signboard/editSignboard/${tplId}?styles=${this.$route.query.styles}`
      );
    },
    // 根据条件过滤
    doFilter(list, condition) {
      // let streetType = this.$route.query.streetType;
      // if (streetType) {
      //   list = list.filter((item) => {
      //     if (!item.streetType || item.streetType == streetType) {
      //       return true;
      //     }
      //   });
      // }
      return list.filter((item) => {
        return Object.keys(condition).every((key) => {
          const val = condition[key] || "";
          // 传条件则过滤
          if (typeof val == 'number') {
            return val === item[key]
          }
          if (val) {
            const arr = val.split(",");
            return arr.some((v) => item[key].split(",").includes(v));
          }
          return true;
        });
      });
    },
    // 模版查询
    queryTemplate(current, size) {
      const { page } = this;
      const { styles, material, id } = this.params;
      let pageNum = page.current + 1;
      // 是否存在页码参数
      if (current) pageNum = current;
      // 更新page size
      if (size) page.pageSize = size;
      this.loading = true;
      // 模拟翻页请求
      new Promise((resolve) => {
        if (tplArr.length) resolve();
        else
          signboardService
            .queryTemplateListPageAPI({
              pageNum: 1,
              pageSize: 2000,
            })
            .then((res) => {
              const list = _.get(res, "data.list", []);
              // 对数据做过滤
              tplArr = this.doFilter(list, { style: styles, material, id });
              resolve();
            });
      })
        .finally(() => (this.loading = false))
        // 实现翻页
        .then(() => {
          console.log("tplArr-query", tplArr);
          const start = (pageNum - 1) * page.pageSize;
          const list = tplArr.slice(start, start + page.pageSize);
          this.list = this.resolveList(list);
          this.page.current = pageNum;
          this.page.total = tplArr.length;
        });
    },
  },
};
</script>
<style scoped lang="scss">
.page-wrap {
  max-width: 1000px;
  margin: 0 auto;
  border-radius: 4px;
  padding: 12px 24px;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
  .logo-item {
    margin-bottom: 10px;
  }
}
</style>
