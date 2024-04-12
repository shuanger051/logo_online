<template>
  <div class="page-wrap">
    <van-list v-model="loading" :finished="finished" @load="queryArticleList">
      <!-- 加载更多 -->
      <template slot="finished">
        <span v-if="list.length">-没有更多了-</span>
      </template>
      <!-- 文章列表为空 -->
      <van-empty v-if="list.length == 0 && !loading" description="暂未发布相关信息文章"> </van-empty>
      <!-- 文章列表不为空 -->
      <template v-else>
        <van-cell
          v-for="item in list"
          :to="`/article/${item.channelId}/detail?pid=${item.id}`"
          :key="item.id"
          :title="item.contentExt.title"
        >
          <template slot="label">
            <div class="description">{{ item.contentExt.description }}</div>
            <div class="attrs-bar">
              <span class="attr-item time">{{
                item.contentExt.releaseDate | date("YYYY-MM-DD hh:mm")
              }}</span>
              <span class="attr-item visitor"
                >阅读<i>{{ item.viewsDay }}</i></span
              >
            </div>
          </template>
        </van-cell>
      </template>
    </van-list>
  </div>
</template>
<script>
import { articleService } from "@/apis";
export default {
  data() {
    return {
      loading: false,
      finished: false,
      list: [],
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
    this.params = _.pick(this.$route.params, ["channelId"]);
  },
  methods: {
    queryArticleList() {
      const { channelId } = this.params;
      const { size, current } = this.page;
      let pageNum = current + 1;
      articleService
        .getContentByChannelIdAPI({ channelId })
        .then((res) => {
          const { list = [], total = 0 } = _.get(res, "data", {});
          this.finished = list.length < size;
          this.page.current = pageNum;
          this.page.total = total;
          this.list = list;
        })
        .finally(() => (this.loading = false))
        .catch((err) => {
          this.finished = true;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 0;
  min-height: 100%;
  box-sizing: border-box;
  background-color: @white;  
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("../../assets/app-footer-bg.png");
    background-repeat: no-repeat;
    background-position: bottom center;
    background-size: 115% auto;
    background-attachment: fixed;
    filter: opacity(40%);
  }
  :deep(.van-list) {
    .van-cell {
      &__value {
        line-height: 0;
      }
      .description {
        margin-bottom: 4px;
        line-height: 1.6em;
        max-height: 3.2em;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        overflow: hidden;
      }
      .attrs-bar {
        .attr-item {
          &:not(:last-child) {
            margin-right: 6px;
          }
        }
        .visitor {
          & > i {
            font-style: normal;
            display: inline-block;
            padding: 0 2px;
          }
        }
      }
    }
    .van-image {
      height: 64px;
    }
  }
}
</style>
