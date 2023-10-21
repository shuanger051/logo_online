<template>
  <a-form
    class="form-detail"
    :label-col="{ span: 6 }"
    :wrapper-col="{ span: 18 }"
  >
    <a-row>
      <a-col :span="12">
        <a-form-item label="商铺名称">{{ detail.shopName }}</a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="商铺地址">
          {{ detail.address }}{{ detail.addressDetail }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="营业年限">
          {{ detail.bizYears | dictMap(DictBizYears) }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="行业类型">
          {{ detail.industryType | dictMap(DictIndustryType) }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="商铺属性">
          {{ detail.shopsType | dictMap(DictShopsType) }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="是否老店">
          {{ detail.isOldShops | dictMap(DictBoolean) }}
        </a-form-item>
      </a-col>
      <!-- 店招信息 -->
      <a-divider orientation="left">店招信息</a-divider>
      <a-col :span="12">
        <a-form-item label="店招名称">{{ detail.logoName }}</a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="店招数量">{{ detail.logoNum || 0 }}</a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="店招宽度"
          >{{ detail.logoWidth || 0 }}米</a-form-item
        >
      </a-col>
      <a-col :span="12">
        <a-form-item label="店招高度"
          >{{ detail.logoHeight || 0 }}米</a-form-item
        >
      </a-col>
      <a-col :span="12">
        <a-form-item label="店招材质">{{ detail.material }}</a-form-item>
      </a-col>

      <!-- 经办人信息 -->
      <a-divider orientation="left">经办人信息</a-divider>
      <a-col :span="12">
        <a-form-item label="姓名">{{ detail.handledByName }}</a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="手机号">{{ detail.handledByPhone }}</a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="身份证号">{{ detail.handledByIdCard }}</a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>
<script>
import store from "@/store";
import { mapDictObject } from "@/store/helpers";
export default {
  props: {
    // 详情
    detail: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    DictBoolean() {
      return {
        0: "否",
        1: "是",
      };
    },
    // 店铺属性
    DictShopsType() {
      return mapDictObject("shopsType")(store.state);
    },
    // 行业类型
    DictIndustryType() {
      return mapDictObject("industryType")(store.state);
    },
    // 营业年限
    DictBizYears() {
      return mapDictObject("bizYears")(store.state);
    },
  },
  filters: {
    dictMap(val, dict) {
      return dict[val];
    },
  },
};
</script>
<style lang="less" scoped>
.form-detail {
  :deep(.ant-form-item) {
    margin-bottom: 16px;
    &-label {
      line-height: 1.8em;
    }
    &-control {
      line-height: 1.8em;
    }
  }
  :deep(.ant-divider) {
    &::before {
      width: 0%;
    }
    &::after {
      width: 100%;
    }
    &-inner-text {
      font-weight: normal;
      font-size: 14px;
      color: #999;
    }
  }
}
</style>
