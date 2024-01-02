<template>
  <a-dropdown>
    <div class="header-avatar" style="cursor: pointer">
      <a-avatar class="avatar" size="small" shape="circle" :src="user.avatar" />
      <span class="name">{{ user.userName }}</span>
    </div>
    <a-menu :class="['avatar-menu']" slot="overlay">
      <a-menu-item @click="onChangePwd({ user })">
        <a-icon style="margin-right: 8px" type="form" />
        <span>修改密码</span>
      </a-menu-item>
      <a-menu-item @click="logout">
        <a-icon style="margin-right: 8px" type="poweroff" />
        <span>退出登录</span>
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import { appService } from "@/services";
import { createModalEvent } from "@/hooks/useTable";
import PassWordChange from "./PassWordChange.vue";

export default {
  name: "HeaderAvatar",
  computed: {
    ...mapGetters("account", ["user"]),
  },
  setup() {
    const onChangePwd = createModalEvent(PassWordChange, {
      title: "密码修改",
    });
    return {
      onChangePwd,
    };
  },
  methods: {
    ...mapMutations("account", ["setUser", "setPermissions", "setRoles"]),
    logout() {
      appService.logout();
      this.setUser();
      this.setPermissions();
      this.$router.push("/login");
    },
  },
};
</script>

<style lang="less">
.header-avatar {
  display: inline-flex;
  .avatar,
  .name {
    align-self: center;
  }
  .avatar {
    margin-right: 8px;
  }
  .name {
    font-weight: 500;
  }
}
.avatar-menu {
  width: 150px;
}
</style>
