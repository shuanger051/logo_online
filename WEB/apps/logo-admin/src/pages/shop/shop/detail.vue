<template>
  <a-form layout="inline">
    <a-form-item label="商铺名称">{{ detail.shopName }}</a-form-item>
    <a-form-item label="商铺地址">{{ detail.address }}</a-form-item>
    <a-form-item label="营业年限">{{ detail.bizYears }}</a-form-item>
    <a-form-item label="行业类型">{{ detail.industryType }}</a-form-item>
    <a-form-item label="商铺属性">{{ detail.shopsType }}</a-form-item>
    <a-form-item label="测试">{{ detail.shopName }}</a-form-item>
    <a-form-item label="测试">{{ detail.shopName }}</a-form-item>
    <a-form-item label="测试">{{ detail.shopName }}</a-form-item>
    <a-form-item label="测试">{{ detail.shopName }}</a-form-item>
  </a-form>
</template>
<script>
export default {
  props: {
    // 详情
    detail: {
      type: Object,
      default: () => ({}),
    },
  },
};
</script>
