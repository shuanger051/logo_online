<template>
  <div class="page-wrap">
    <a-list
      item-layout="vertical"
      :loading="loading"
      :data-source="list"
      :pagination="page"
      :locale="{
        emptyText: '暂未发布相关信息文章',
      }"
    >
      <!-- 文章列表不为空 -->
      <a-list-item slot="renderItem" slot-scope="item" :key="item.id">
        <template slot="actions">
          <span class="attr-item time">{{
            item.contentExt.releaseDate | date("YYYY-MM-DD hh:mm")
          }}</span>
          <span class="attr-item visitor"
            >阅读<i>{{ item.viewsDay }}</i></span
          >
        </template>
        <a-list-item-meta :description="item.contentExt.description">
          <router-link
            slot="title"
            :to="`/article/${item.channelId}/detail?pid=${item.id}`"
            >{{ item.contentExt.title }}</router-link
          >
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </div>
</template>
<script>
import { articleService } from "@/services";
import router from "@/router";
export default {
  data() {
    return {
      loading: false,
      finished: false,
      list: [],
      // 查询条件
      params: {},
      page: {
        pageSize: 30,
        total: 0,
        current: 0,
        onChange: (page) => this.queryArticleList(page),
      },
    };
  },
  created() {
    // 保存查询条件
    this.params = _.pick(this.$route.params, ["channelId"]);
    this.queryArticleList();
  },
  methods: {
    queryArticleList(current = 0, size) {
      const { page } = this;
      const { channelId } = this.params;
      let pageNum = current + 1;
      if (current) pageNum = current;
      if (size) page.pageSize = size;
      this.loading = true; // 标记加载中
      articleService
        .getContentByChannelIdAPI({
          channelId,
          pageNum,
          pageSize: page.pageSize,
        })
        .then((res) => {
          const { list = [], total = 0 } = _.get(res, "data", {});
          this.page.current = pageNum;
          this.page.total = total;
          this.list = list;
        })
        .finally(() => {
          this.loading = false
        })
        .catch((err) => {
          this.finished = true;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px;
  min-height: 100%;
  max-width: 1000px;
  margin: 0 auto;
  box-sizing: border-box;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
  // background-color: @text-color;
}
</style>
