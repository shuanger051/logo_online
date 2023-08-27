<template>
  <div>
    <!-- 店招图  -->
    <van-cell v-if="shopLogo.logoUrl">
      <van-image
        :src="shopLogo.logoUrl"
        :alt="shopLogo.logoName"
        :title="shopLogo.logoName"
      />
    </van-cell>
    <!-- 商铺信息 -->
    <van-cell
      title="行业类型"
      :value="detail.industryType | dict(DictIndustryType)"
    ></van-cell>
    <van-cell
      title="营业年限"
      :value="detail.bizYears | dict(DictBizYears)"
    ></van-cell>
    <van-cell
      title="店铺属性"
      :value="detail.shopsType | dict(DictShopsType)"
    ></van-cell>
    <van-cell title="商铺地址" :value="detail.address"></van-cell>
    <van-cell title="经办人" :value="detail.handledByName"></van-cell>
    <van-cell title="备注" :value="detail.remark"></van-cell>
    <van-cell title="">
      <!-- 已备案 -->
      <van-button
        v-if="detail.isFilings == '2'"
        disabled
        plain
        icon="lock"
        size="small"
        >已备案</van-button
      >
      <!-- 备案中 -->
      <van-button
        v-else-if="detail.isFilings == '1'"
        disabled
        plain
        size="small"
        icon="underway"
        >备案中</van-button
      >
      <!-- 未备案 -->
      <template v-else>
        <van-button plain size="small" :to="`/shop/detail?shopId=${detail.id}`"
          >信息变更</van-button
        >
        <van-button
          plain
          size="small"
          :to="`/signboard/editSelect?shopId=${detail.id}`"
          >店招设计</van-button
        >
      </template>
    </van-cell>
  </div>
</template>
<script>
import { shopService } from "@/apis";
import { mapState } from "vuex";
import { mapDictObject } from "@/store/helpers";
import { resolveImgUrl } from "core/support/imgUrl";
export default {
  props: {
    detail: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      shopLogo: {},
    };
  },
  computed: {
    ...mapState({
      // 行业类别
      DictIndustryType: mapDictObject("industryType"),
      // 营业念想
      DictBizYears: mapDictObject("bizYears"),
      // 商铺属性
      DictShopsType: mapDictObject("shopsType"),
    }),
  },
  created() {
    const { id, isFilings } = this.detail;
    // 如果备案中或已备案
    if (["1", "2"].includes(isFilings)) {
      this.getShopLogo(id);
    }
  },
  methods: {
    // 获取店招
    getShopLogo(shopsId) {
      shopService
        .getLogoInfoByShopsIdAPI({ shopsId })
        // 更新店招图
        .then((res) => {
          const data = _.get(res, "data", {});
          data.logoUrl = resolveImgUrl(data.urlPath);
          this.shopLogo = data;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.van-cell {
  .van-button {
    &:not(:last-child) {
      margin-right: 8px;
    }
  }
}
</style>
