<template>
  <h1>这是首页</h1>
</template>
<script>
import { accountService } from "@/services";
export default {
  created() {
    accountService.queryCustomerByIdAPI().then((res) => {
      console.log(res);
    });
  },
};
</script>
