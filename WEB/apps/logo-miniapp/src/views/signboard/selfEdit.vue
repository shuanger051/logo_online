<template>
  <div class="edit-select">
    <van-panel
      title="请选择"
      desc="点击需要处理的项目"
    >
    </van-panel>
    <div class="content">
      <div
        class="line-desing"
        @click="
          $router.push(`/signboard/intelligenceDesign?shopId=${$route.query.shopId}`)
        "
      >
        <span>智能设计</span>
        <icon-fa icon="game-icons:artificial-intelligence" color="#82b6f8" width="80%" />
      </div>

      <div
        class="line-desing"
        @click="
          $router.push(`/signboard/attribute?shopId=${$route.query.shopId}`)
        "
      >
        <span>自主设计</span>
        <icon-fa icon="iconoir:design-pencil" color="#e98c49" width="80%" />
      </div>
    </div>
  </div>
</template>
<script>

export default {
};
</script>
<style lang="less" scoped>
.content {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  .line-desing {
    width: 80%;
    margin: 0 auto;
    display: flex;
    position: relative;
    align-items: center;
    height: 100px;
    width: 170px;
    border-radius: 10px;
    background-color: #efefed;
    > span {
      position: absolute;
      right: 10px;
      top: 10px;
    }
  }
}
</style>
