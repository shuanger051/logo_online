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
        <img
          v-if="item.url"
          @click="go(item.id)"
          width="100%"
          height="100px"
          fit="contain"
          :src="item.url"
        />
      </a-list-item>
    </a-list>
  </div>
</template>
<script>
import { signboardService } from "@/services";
import { resolveImgUrl } from "core/support/imgUrl";

export default {
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      // 查询条件
      params: {},
      page: {
        pageSize: "30",
        total: 0,
        current: 0,
        onChange: (page) => this.queryTemplate(page),
      },
    };
  },
  created() {
    // 保存查询条件
    this.params = _.pick(this.$route.query, ["styles", "material"]);
    this.queryTemplate();
  },
  methods: {
    resolveList(lists) {
      return lists.map((item) => {
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
      });
    },
    go(id) {
      this.$router.push(
        `/signboard/editSignboard/${id}?shopId=${this.$route.query.shopId}`
      );
    },
    // 模版查询
    queryTemplate(current, size) {
      const { page } = this;
      const { styles, material } = this.params;
      let pageNum = page.current + 1;
      // 是否存在页码参数
      if (current) pageNum = current;
      // 更新page size
      if (size) page.pageSize = size;
      this.loading = true;
      signboardService
        .queryTemplateListPageAPI({
          pageNum,
          pageSize: page.pageSize,
          style: styles,
          material,
        })
        .then((res) => {
          const { list, total } = res.data;
          // 返回顶部
          this.page.current = pageNum;
          this.page.total = total;
          this.list = this.resolveList(list);
          this.finished = list.length < size;
        })
        .finally(() => (this.loading = false));
    },
  },
};
</script>
<style scoped lang="scss">
.page-wrap {
  max-width: 1000px;
  margin: 0 auto;
  .logo-item {
    margin-bottom: 10px;
  }
}
</style>
