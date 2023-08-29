<template>
  <div class="page-wrap">
    <!-- 商户备案信息 -->
    <van-form @submit="onSubmit">
      <van-panel title="商户信息">
        <van-field
          required
          label="商户姓名"
          v-model="formData.merchantName"
          placeholder="请输入"
        />
        <field-picker
          required
          label="性别"
          placeholder="请选择"
          v-model="formData.gender"
          :columns="DictGenderArr"
        />
        <field-picker
          required
          label="营业状态"
          placeholder="请选择"
          v-model="formData.merchantStatus"
          :columns="DictMerchantStatusArr"
        />
        <van-field
          required
          label="联系电话"
          v-model="formData.phone"
          placeholder="请输入"
        />
        <van-field
          required
          label="身份证号"
          v-model="formData.idCard"
          placeholder="请输入"
        />
        <van-field
          label="备注"
          type="textarea"
          rows="3"
          v-model="formData.remark"
          placeholder="请输入"
        />
      </van-panel>
      <submit-bar>
        <van-button block type="primary">提交</van-button>
      </submit-bar>
    </van-form>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { shopService } from "@/apis";
import { mapDictOptions } from "@/store/helpers";
export default {
  data() {
    return {
      formData: {},
    };
  },
  computed: {
    ...mapState({
      // 用户信息
      userInfo: (state) => state.user.profiles,
      // 性别
      DictGenderArr: mapDictOptions("gender"),
      // 商户状态
      DictMerchantStatusArr: mapDictOptions("merchantStatus"),
    }),
  },
  created() {
    this.queryMerchantInfo();
    // 查询字典项
    this.$store.dispatch("cache/queryDictByKey", {
      keys: ["gender", "merchantStatus"],
    });
  },
  methods: {
    onSubmit() {
      const { formData } = this;
      // 存在id则为修改
      if (formData.id) this.doUpdate(formData);
      // 其他为新增
      else this.doAdd(formData);
    },
    // 新增商户信息
    doAdd(payload) {
      shopService
        .saveMerchantAPI(payload)
        // 新增成功
        .then((res) => {
          this.$toast.success({
            message: "保存成功",
            onClose: () => {
              this.$router.push({ path: "/" });
            },
          });
        })
        .catch((err) => {
          console.log(err);
          this.$toast.fail("保存失败");
        });
    },
    // 修改商户信息
    doUpdate(payload) {
      shopService
        .updateMerchantAPI(payload)
        // 修改成功
        .then((res) => {
          this.$toast.success({
            message: "保存成功",
            onClose: () => {
              this.$router.push({ path: "/" });
            },
          });
        })
        .catch((err) => {
          console.log(err);
          this.$toast.fail("保存失败");
        });
    },
    // 查询商户信息
    queryMerchantInfo() {
      const { customerName } = this.userInfo;
      shopService
        .getCustomerInfoByUserNameAPI({ customerName })
        // 获取商户信息
        .then((res) => {
          const { merchant } = res.data;
          // 添加商户信息
          this.formData = merchant || {};
        });
    },
  },
};
</script>
<style lang="less" scoped>
.page-wrap {
  background-color: @gray-2;
  padding: 12px 0 60px;
}
</style>
