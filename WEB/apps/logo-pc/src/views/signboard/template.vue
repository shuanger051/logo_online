<template>
  <div class="page-wrap">
    <!-- 店招模版选择 -->
    <van-list
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="queryTemplate"
    >
      <div
        v-for="item in list"
        :key="item.id"
        @click="go(item.id)"
        class="logo-item"
      >
        <van-image
          v-if="item.url"
          width="100%"
          height="100px"
          fit="contain"
          :src="item.url"
        />
        <van-empty v-else description="" />
      </div>
    </van-list>
    <!-- 翻页 -->
    <suspend-page
      :total="page.total"
      :size="page.size"
      :current="page.current"
      @change="onPageChange"
    />
  </div>
</template>
<script>
import { signboardService } from "@/services";
import { resolveImgUrl } from "core/support/imgUrl";
import SuspendPage from "@/components/SuspendPage";

export default {
  components: { SuspendPage },
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      // 查询条件
      params: {},
      page: {
        size: 30,
        total: 0,
        current: 0,
      },
    };
  },
  created() {
    // 保存查询条件
    this.params = _.pick(this.$route.query, ["styles", "material"]);
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
        `/editSignboard/${id}?shopId=${this.$route.query.shopId}`
      );
    },
    // 翻页
    onPageChange(page) {
      this.queryTemplate(page);
    },
    // 模版查询
    queryTemplate(page) {
      const { styles, material } = this.params;
      const { size, current } = this.page;
      let pageNum = current + 1;
      // 是否存在页码参数
      if (page?.current) pageNum = page.current;
      this.loading = true;
      signboardService
        .queryTemplateListPageAPI({
          pageNum,
          pageSize: size,
          style: styles,
          material,
        })
        .then((res) => {
          const { list, total } = res.data;
          // 返回顶部
          const elPage = document.getElementById("page-container");
          elPage.scrollTo(0, 0);
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
  .logo-item {
    margin-bottom: 10px;
  }
}
</style>
