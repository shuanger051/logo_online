<template>
  <div class="edit-select">
    <van-panel title="请选择" desc="点击选择设计模式"> </van-panel>
    <div class="content">
      <div>
        <div
          class="line-desing"
          @click="
            $router.push({ name: 'intelligenceDesign', query: $route.query })
          "
        >
          <span>智能设计</span>
          <icon-fa
            icon="game-icons:artificial-intelligence"
            color="#82b6f8"
            width="80%"
          />
        </div>
        <p>只需输入店名，无需设计，一键生成店招</p>
      </div>
      <div>
        <div
          class="line-desing"
          @click="$router.push({ name: 'attribute', query: $route.query })"
        >
          <span>自主设计</span>
          <icon-fa icon="iconoir:design-pencil" color="#e98c49" width="80%" />
        </div>
        <p>按自己想法设计美观，有吸引力的店招，需多步操作</p>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  created(){
    console.log(111)
  }
};
</script>
<style lang="less" scoped>
.content {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  padding: 0 10px;
  p {
    font-size: 12px;
    color: #5c5c5c;
    padding: 0 10px;
  }
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
      font-size: 14px;
      font-weight: bold;
    }
  }
}
</style>
