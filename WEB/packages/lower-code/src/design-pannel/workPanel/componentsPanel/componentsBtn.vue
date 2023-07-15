<template>
  <div>
    <div class="component-wrap" @click="handleClick" draggable
      @dragstart="onDragStart($event, componentName, componentId, name, componentType)"
      @dragend="onDragEnd($event, componentName)">
      <div class="icon-wrap">
        <img v-if="componentIcon" :src="require('@Root/assets/images/'+componentIcon+'.svg')" width="32" height="32">
        <h-icon v-else name="document-file icon-document-file" size="32"></h-icon>
      </div>
      <div class="component-name">{{ name }}</div>
    </div>
    <!-- <div
      v-else
      class="business-component-wrap"
      @click="handleClick"
      draggable
      @dragstart="onDragStart($event, componentName, componentId, name, componentType)"
      @dragend="onDragEnd($event, componentName)"
    >
      <div class="icon-wrap">
        <img v-if="componentIcon" :src="require('@Root/assets/images/'+componentIcon+'.svg')" width="78" height="78">
        <h-icon v-else name="document-file icon-document-file" size="78"></h-icon>
      </div>
      <div class="component-name">{{ name }}</div>
    </div> -->
  </div>
</template>

<script>
export default {
  props: {
    componentIcon: {
      type: String
    },
    componentName: {
      required: true,
      type: String
    },
    componentId: {
      required: true,
      type: String
    },
    name: {
      required: true,
      type: String
    },
    handleClick: {
      required: false,
      type: Function,
      default: () => { }
    },
    groupType: {
      type: String,
      default: () => ''
    },
    componentType: {
      type: String,
      default: () => ''
    }
  },
  methods: {
    onDragStart(e, component_name, component_id, component_show_name, component_type) {
      e.dataTransfer.setData('component_name', component_name)
      e.dataTransfer.setData('component_id', component_id)
      e.dataTransfer.setData('component_show_name', component_show_name)
      e.dataTransfer.setData('component_type', component_type)
    },
    onDragEnd(name, e) {
    }
  }
}
</script>

<style lang="scss" scoped>
.component-wrap {
  width: 80px;
  height: 80px;
  text-align: center;
  cursor: move;
  box-sizing: border-box;
  /* border: 1px solid #eee; */
  &:hover {
    background: #eff5ff;
  }
  .icon-wrap {
    padding-top: 15px;
    margin-top: 5px;
  }
  .component-name {
    color: #666666;
    font-size: 12px;
  }
}
.business-component-wrap {
  width: 110px;
  height: 110px;
  text-align: center;
  cursor: move;
  box-sizing: border-box;
  /* border: 1px solid #eee; */
  &:hover {
    background: #eff5ff;
  }
  .icon-wrap {
    padding-top: 15px;
    margin-top: 5px;
  }
  .component-name {
    color: #666666;
    font-size: 12px;
  }
}
</style>
