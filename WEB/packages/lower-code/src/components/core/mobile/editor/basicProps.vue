<template>
  <div>
    <div v-for="(item, index) in formItems">
        <component
          :key="'props-'+ index"
          :is = "item.type"
          v-model="formModel[item.propsName]"
          :bind="item.props"
        ></component>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['formItems', 'formModel'],
    
  }
</script>