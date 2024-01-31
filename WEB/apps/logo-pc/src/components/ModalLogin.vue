<template>
  <a-modal
    v-model="show"
    title="登录"
    okText="登录"
    cancelText="取消"
    @ok="onSubmit"
  >
    <a-form-model
      ref="loginForm"
      :model="formData"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }"
      :rules="rules"
    >
      <a-form-model-item
        required
        name="customerName"
        label="手机号"
        prop="customerName"
      >
        <a-input
          v-model="formData.customerName"
          placeholder="请输入"
          max-length="11"
        />
      </a-form-model-item>
      <a-form-model-item required name="password" label="密码" prop="password">
        <a-input
          v-model="formData.password"
          placeholder="请输入"
          type="password"
        />
      </a-form-model-item>
    </a-form-model>
  </a-modal>
</template>
<script>
import store from "@/store";
import eventBus from "@/core/eventBus";
import { commonService, accountService } from "@/services";
import { runPromiseInSequence } from "@/utils/util";
export default {
  data() {
    return {
      show: false,
      loginType: "1", // 1-本地，2-浙里办授权登录
      formData: {
        customerName: "18888888888",
        password: "Abc123456",
      },
    };
  },
  computed: {
    rules() {
      return {
        customerName: [{ required: true, message: "请输入" }],
        password: [{ required: true, message: "请输入" }],
      };
    },
  },
  created() {
    this.qlygAuthLogin();
    // 未登录提示
    eventBus.$on("login", () => {
      this.qlygAuthLogin();
    });
  },
  methods: {
    // 权利阳光登录
    qlygAuthLogin() {
      const timestamp = +new Date();
      runPromiseInSequence([
        // 获取秘钥
        this.getPublicKey,
        // 加密
        this.encrypt,
        // 登录
        this.getTokenTimestampAPI
      ])({
        password: timestamp,
      });
    },
    // 权利阳光登录
    getTokenTimestampAPI(ctx) {
      return accountService
        .getTokenTimestampAPI({ sign: ctx.password })
        .then((res) => {
          const { token } = res.data;
          store.commit("user/setToken", token);
        })
        .catch((err) => {
          this.$toast.fail("登录失败，请稍后再试");
        });
    },
    // event：登录
    onSubmit() {
      // this.$refs.loginForm.validate((valid) => {
      //   if (valid) {
      const { customerName, password } = this.formData;
      runPromiseInSequence([
        // 获取秘钥
        this.getPublicKey,
        // 加密
        this.encrypt,
        // 登录
        this.login,
      ])({
        customerName,
        password,
      })
        // 失败提示
        .catch((err) => {
          console.log(err);
          this.$toast.fail("登录失败：" + err.msg);
        });
      //   }
      // });
    },
    // 登录
    login(ctx) {
      return accountService
        .getTokenAPI({
          customerName: ctx.customerName,
          password: ctx.password,
        })
        .then((res) => {
          this.show = false;
          const { token, customerInfo } = res.data;
          store.commit("user/setToken", token);
          store.commit("user/setUserInfo", customerInfo);
        })
        .catch((err) => {
          this.$toast.fail("登录失败，请稍后再试");
        });
    },
    // 获取公钥
    getPublicKey(ctx) {
      return commonService
        .getPublicKey()
        .then((res) => (ctx.publicKey = res.data.publicKey));
    },
    // 加密
    encrypt(ctx) {
      return commonService
        .encrypt({
          param: ctx.password,
          publicKey: ctx.publicKey,
        })
        .then((res) => {
          ctx.password = res.data.sign;
        });
    },
  },
};
</script>
<style lang="less" scoped>
.popup-login {
  width: 300px;
  border-radius: 6px;
  :deep(.van-form) {
    padding: 24px 12px 12px;
    .van-field:last-child {
      margin-top: 12px;
    }
  }
}
</style>
