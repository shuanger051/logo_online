<template>
  <div class="page-wrap">
    <van-empty v-if="!detail.id" description="文章详情找不到了"></van-empty>
    <template v-else>
      <h2 class="title">{{ article.title }}</h2>
      <div class="attribute">
        <span>编辑：{{ article.author }}</span>
        <span>{{ updateTime | date }}</span>
      </div>
      <!-- 文章内容 -->
      <div class="content" v-html="article.content"></div>
      <!-- 分割线 -->
      <van-divider />
      <!-- 附件列表 -->
      <dl class="attachment" v-if="attachment.length">
        <dt>附件下载</dt>
        <dd v-for="item in attachment" :key="item.id">
          <a :href="item.urlPath">
            {{ item.filename }}
          </a>
        </dd>
      </dl>
    </template>
  </div>
</template>
<script>
import { articleService } from "@/apis";
import { mapState } from "vuex";
export default {
  data() {
    return {
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
      return list;
      // const downloadUrl =
      //   window.__baseUrl +
      //   "logo/app/downloadContentAttachment" +
      //   `?token=${this.token}`;
      // return list.map((item) => {
      // 拼装下载地址
      // item.downloadUrl = `${downloadUrl}&attachmentName=${item.attachmentName}&`;
      //   return item;
      // });
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
      return articleService
        .getContentByIDAPI({
          id: pid,
        })
        .then((res) => {
          console.log(res);
          this.detail = res.data;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  padding: 0 12px 12px;
  .title {
    line-height: 1.6em;
  }
  .attribute {
    color: @gray-5;
    font-size: 12px;
    line-height: 1.8em;
    margin-bottom: 12px;
    & > span:not(:last-child) {
      margin-right: 12px;
    }
  }
  .content {
    font-size: 14px;
    line-height: 1.6em;
    color: @gray-8;
  }
  .attachment {
    line-height: 2em;
    dt {
      font-size: 14px;
      color: @gray-6;
    }
    dd {
      margin-left: 0;
      a {
        color: @blue;
      }
    }
  }
}
</style>
