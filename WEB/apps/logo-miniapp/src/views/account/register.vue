<template>
  <div class="page-wrap">
    <!-- 注册页 -->
    <van-form @submit="onSubmit">
      <van-field
        label="姓名"
        placeholder="请输入"
        v-model="formData.nickName"
      />
      <van-field
        label="证件号码"
        placeholder="请输入"
        v-model="formData.idCard"
      />
      <van-field label="性别" placeholder="请输入" v-model="formData.gender" />
      <van-field
        label="手机号"
        placeholder="请输入"
        v-model="formData.mobile"
      />
      <van-field
        label="密码"
        placeholder="请输入"
        v-model="formData.password"
      />
      <!-- 提交栏 -->
      <submit-bar>
        <van-button block type="primary">确认并注册</van-button>
      </submit-bar>
    </van-form>
  </div>
</template>
<script>
// import { rsaEncrypt } from "@/utils/crypto";
import { accountService } from "@/apis";
// import { runPromiseInSequence } from "../../../../logo-admin/src/utils/util";
export default {
  data() {
    return {
      formData: {},
    };
  },
  methods: {
    onSubmit() {
      const { formData } = this;
      accountService
        .registerCustomerAPI(
          Object.assign(
            {
              customerName: formData.mobile,
            },
            formData
          )
        )
        .then(() => {
          this.$toast.success("注册成功");
          this.$router.push("/");
        })
        .catch((err) => {
          console.log(err);
          this.$toast.fail("注册失败");
        });
      // runPromiseInSequence([
      //   // 加密
      //   (ctx) =>
      //     rsaEncrypt(formData.password).then((sign) => (ctx.password = sign)),
      //   // 注册
      //   (ctx) =>
      // accountService.registerCustomerAPI(
      //   Object.assign({}, formData, {
      //     customerName: formData.mobile,
      //     password: ctx.password,
      //   })
      // ),
      // ])()
      //   .then((res) => {
      //     console.log(res);
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //     this.$toast.fail("注册失败");
      //   });
    },
  },
};
</script>
