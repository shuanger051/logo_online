<template>
  <div class="self-edit">
    <div class="select-title">
      <h2>请选择</h2>
      <p>点击选择设计模式</p>
    </div>
    <a-divider>
    </a-divider>
    <div class="content">
      <div
        class="line-desing"
        @click="
          $router.push({ name: 'intelligenceDesign', query: $route.query })
        "
      >
        <span>智能设计</span>
        <icon-fa icon="game-icons:artificial-intelligence" color="#82b6f8" width="80%" />
        <p>只需输入店名，无需设计，一键生成店招</p>

      </div>

      <div
        class="line-desing"
        @click="
          $router.push({ name: 'attribute', query: $route.query })
        "
      >
        <span>自主设计</span>
        <icon-fa icon="iconoir:design-pencil" color="#e98c49" width="80%" />
        <p style="bottom: -60px">按自己想法设计美观，有吸引力的店招，需多步操作</p>

      </div>
    </div>
  </div>
</template>
<script>

export default {
};
</script>
<style lang="less" scoped>
.self-edit {
  width: 1000px;
  margin:  0 auto;
  padding: 0 20px
}
.select-title {
  padding-top: 20px;
}
.content {
  display: flex;
  justify-content:space-between;
  margin-top: 10px;
  .line-desing {
    cursor: pointer;
    display: flex;
    position: relative;
    align-items: center;
    height: 200px;
    width: 270px;
    border-radius: 10px;
    background-color: #efefed;
    > span {
      position: absolute;
      right: 10px;
      top: 10px;
      font-weight: bold;
    }
    > p {
      position: absolute;
      bottom: -50px;
    }
  }
}
</style>
