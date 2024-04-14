<template>
  <div class="page-wrap">
    <a-row>
      <a-col :span="12">
        <a-list :dataSource="list" :bordered="false">
          <template #header>材质类型</template>
          <template #renderItem="item">
            <a-list-item>
              <router-link :to="`/material/detail?name=${item.key}`">
                <a-list-item-meta :title="item.name">
                  <i
                    slot="avatar"
                    :class="`iconfont icon-${item.icon}`"
                    :style="{ color: item.iconColor }"
                  ></i>
                </a-list-item-meta>
              </router-link>
            </a-list-item>
          </template>
        </a-list>
      </a-col>
    </a-row>
  </div>
</template>
<script>
export default {
  data() {
    const list = window.pageContentJson.texture;
    return {
      list,
    };
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  max-width: 1000px;
  margin: 0 auto;
  border-radius: 4px;
  padding: 12px 24px;
  margin-top: 24px;
  border-radius: 4px;
  background-color: #fff;
  // background-color: @gray-2;
  min-height: 100%;
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    // background-image: url("../../assets/app-footer-bg.png");
    background-repeat: no-repeat;
    background-position: bottom center;
    background-size: 115% auto;
    background-attachment: fixed;
    filter: opacity(40%);
  }
  :deep(.ant-list) {
    margin-bottom: 12px;
    border-radius: 8px;
    overflow: hidden;
    &-header {
      position: relative;
      line-height: 24px;
      font-size: 18px;
      font-weight: 700;
      border-bottom-color: transparent;
      &::before {
        content: "";
        display: inline-block;
        margin-right: 8px;
        transform: translateY(2px);
        width: 4px;
        height: 16px;
        background-color: #2f63f1;
      }
    }
    &-item {
      font-size: 14px;
      padding-top: 8px;
      padding-bottom: 8px;
      border-bottom-color: transparent;
      &-meta-avatar {
        margin-right: 10px;
      }
    }
    &__content {
      padding: 12px 0;
    }
  }
}
</style>
