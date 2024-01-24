<template>
  <div class="page-wrap">
    <!-- 加载中 -->
    <div class="loading" v-if="loading">
      <a-spin tip="文章加载中..." />
    </div>
    <!-- 不存在 -->
    <a-empty v-else-if="!detail.id" description="文章详情找不到了"></a-empty>
    <!-- 内容详情 -->
    <template v-else>
      <h2 class="title">{{ article.title }}</h2>
      <div class="attribute">
        <span>编辑：{{ article.author || "--" }}</span>
        <span>发布时间：{{ updateTime | date }}</span>
      </div>
      <!-- 文章内容 -->
      <div class="content" v-html="article.content"></div>
      <!-- 分割线 -->
      <a-divider />
      <!-- 附件列表 -->
      <!-- <dl class="attachment" v-if="attachment.length">
        <dt>附件下载</dt>
        <dd v-for="item in attachment" :key="item.id">
          <a :href="item.downloadUrl">
            {{ item.filename }}
          </a>
        </dd>
      </dl> -->
    </template>
  </div>
</template>
<script>
import { articleService } from "@/services";
import { mapState } from "vuex";
export default {
  data() {
    return {
      loading: true,
      detail: {},
    };
  },
  computed: {
    ...mapState({
      token: (state) => state.user.token,
    }),
    // 文章内容
    article() {
      return this.detail.contentExt || {};
    },
    // 附件
    attachment() {
      const { list = [] } = this.detail;
      const downloadUrl =
        window.__baseUrl +
        "logo/app/downloadContentAttachment" +
        `?token=${this.token}`;
      return list.map((item) => {
        // 拼装下载地址
        item.downloadUrl = `${downloadUrl}&attachmentName=${item.attachmentName}&`;
        return item;
      });
    },
    // 时间
    updateTime() {
      const { contentExt } = this.detail;
      return contentExt.updateTime || contentExt.createTime;
    },
  },
  created() {
    this.getDetail();
  },
  methods: {
    // 获取文章详情
    getDetail() {
      const { pid } = this.$route.query;
      this.loading = true;
      return articleService
        .getContentByIDAPI({
          id: pid,
        })
        .finally(() => {
          this.loading = false;
        })
        .then((res) => {
          this.detail = res.data;
        });
    },
  },
};
</script>
<style lang="less">
.page-wrap {
  .content img {
    max-width: 100% !important;
  }
}
</style>
<style lang="less" scoped>
.page-wrap {
  padding: 12px 24px;
  max-width: 1000px;
  margin: 0 auto;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
  .loading{
    text-align: center;
    padding: 120px 0;
  }
  // :deep(.ant-spin) {?
    // margin: 0 auto;
  // }
  .title {
    font-size: 22px;
    line-height: 1.6em;
  }
  .attribute {
    // color: @text-color;
    font-size: 14px;
    line-height: 1.8em;
    margin-bottom: 12px;
    & > span:not(:last-child) {
      margin-right: 12px;
    }
  }
  .content {
    font-size: 14px;
    line-height: 1.6em;
    // color: @text-color-secondary;
  }
  .attachment {
    line-height: 2em;
    dt {
      font-size: 14px;
      // color: @text-color-secondary;
    }
    dd {
      margin-left: 0;
      a {
        // color: @blue-6;
      }
    }
  }
}
</style>
