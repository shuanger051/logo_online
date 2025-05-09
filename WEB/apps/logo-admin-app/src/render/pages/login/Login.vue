<template>
  <common-layout>
    <div class="top">
      <div class="header">
        <img alt="logo" class="logo" src="@/assets/img/logo3.png" />
        <span class="title">{{ systemName }}</span>
      </div>
      <div class="desc">杭州城管菜单式店招备案系统</div>
    </div>
    <div class="login">
      <a-form @submit="onSubmit" :form="form">
        <a-alert
          type="error"
          showIcon
          style="margin-bottom: 24px"
          :closable="true"
          :message="error"
          v-if="error"
          @close="onClose"
        />
        <a-form-item>
          <a-input
            autocomplete="autocomplete"
            size="large"
            placeholder="请输入key"
            v-decorator="[
              'userName',
              {
                rules: [
                  { required: true, message: '请输入账户名', whitespace: true },
                ],
              },
            ]"
          >
            <a-icon slot="prefix" type="user" />
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button
            :loading="logging"
            style="width: 100%; margin-top: 0px"
            size="large"
            htmlType="submit"
            type="primary"
            >登录</a-button
          >
        </a-form-item>
      </a-form>
    </div>
  </common-layout>
</template>

<script>
import CommonLayout from "@/layouts/CommonLayout";
import { mapMutations } from "vuex";
import { appService } from "@/services";
import { runPromiseInSequence } from "../../utils/util";

export default {
  name: "Login",
  components: { CommonLayout },
  data() {
    return {
      logging: false,
      error: "",
      form: this.$form.createForm(this),
    };
  },
  computed: {
    systemName() {
      return '店招备案';
    },
    // 图形验证码
    imgCode() {
      return (
        window.__baseUrl +
        "/logo/sys/img-code/kaptcha?uid=" +
        this.uid
      );
    },
  },
  methods: {
    ...mapMutations("account", ["setUser", "setPermissions", "setRoles"]),
    onSubmit(e) {
      e.preventDefault();
      this.onClose(); //关闭错误提示
      this.form.validateFields((err) => {
        if (!err) {
          this.logging = true;
          const userName = this.form.getFieldValue("userName");
          // 登录流程
          runPromiseInSequence([
            this.login,
          ])({ userName })
            .finally(() => {
              this.logging = false;
            })
            // 登录成功跳首页
            .then(() => {
              this.$router.push({ path: "/home" });
            })
            .catch((err) => {
              this.error = _.get(err, "msg", "未知错误");
            });
        }
      });
    },
    // 刷新图片验证码
    onRefreshImgCode() {
      return (this.uid = +new Date());
    },
    // 登录
    login(ctx) {
      return (
        appService
          .login(_.pick(ctx, ["userName"]))
          // 设置登录信息
          .then((res) => {
            console.log("登录成功", res);
            const { permissionList = [], ...user } = _.get(res, "data", {});
            this.setUser(user);
            // this.setPermissions(permissionList);
          })
      );
    },
    // 获取公钥
    getPublicKey(ctx) {
      return (
        appService
          .getPublicKey()
          // 写入秘钥
          .then((res) => {
            ctx.publicKey = _.get(res, "data.publicKey");
          })
      );
    },
    // 获取加密串
    getEncryptSign(ctx) {
      return (
        appService
          .encrypt({
            param: ctx.password,
            publicKey: ctx.publicKey,
          })
          // 写入加密密码串
          .then((res) => {
            ctx.password = _.get(res, "data.sign");
          })
      );
    },

    // afterLogin(res) {
    //   this.logging = false;
    //   const loginRes = res.data;
    //   if (loginRes.code >= 0) {
    //     const { user, permissions, roles } = loginRes.data;
    //     this.setUser(user);
    //     this.setPermissions(permissions);
    //     this.setRoles(roles);
    //     setAuthorization({
    //       token: loginRes.data.token,
    //       expireAt: new Date(loginRes.data.expireAt),
    //     });
    //     // 获取路由配置
    //     getRoutesConfig().then((result) => {
    //       const routesConfig = result.data.data;
    //       loadRoutes(routesConfig);
    //       this.$router.push("/demo");
    //       this.$message.success(loginRes.message, 3);
    //     });
    //   } else {
    //     this.error = loginRes.message;
    //   }
    // },
    onClose() {
      this.error = false;
    },
  },
};
</script>

<style lang="less" scoped>
.common-layout {
  .top {
    text-align: center;
    .header {
      height: 100px;
      line-height: 100px;
      a {
        text-decoration: none;
      }
      .logo {
        height: 60px;
        // border-radius: 4px;
        vertical-align: middle;
        margin-right: 16px;
      }
      .title {
        font-size: 33px;
        color: @title-color;
        font-family: "Myriad Pro", "Helvetica Neue", Arial, Helvetica,
          sans-serif;
        font-weight: 600;
        position: relative;
        top: 2px;
      }
    }
    .desc {
      font-size: 14px;
      color: @text-color-second;
      margin-top: 12px;
      margin-bottom: 40px;
    }
  }
  .login {
    width: 368px;
    margin: 0 auto;
    @media screen and (max-width: 576px) {
      width: 95%;
    }
    @media screen and (max-width: 320px) {
      .captcha-button {
        font-size: 14px;
      }
    }
    .icon {
      font-size: 24px;
      color: @text-color-second;
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: @primary-color;
      }
    }
  }
}
</style>
