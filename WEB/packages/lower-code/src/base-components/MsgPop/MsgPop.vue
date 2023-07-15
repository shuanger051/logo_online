<template>
  <div class="alert-mask">
    <div class="alert-content">
      <p class="alert-title">{{ type }}</p>
      <p class="txt">{{ txt }}</p>
      <div class="btn-box" @click="callback(true)">
        {{option.buttonText || '是'}}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    callback: {
      type: Function
    }
  },
  data() {
    return {
      txt: '',
      type: '',
      option: {}
    };
  }
};
</script>

<style scoped>
/* @import '../../../'; */
.alert-mask{
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  z-index: 10;
  background: rgba(0, 0, 0, 0.5);
}

.alert-content{
  box-sizing: border-box;
  width: 280px;
  background: white;
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%,-50%);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  color:#333333;
}

.alert-title{
  text-align: center;
  font-size: 17px;
  font-weight: 500;
  padding: 24px 24px 0;
  word-wrap:break-word
}
.txt{
  flex: 1;
  min-height: 52px;
  max-height: 292px;
  overflow-y: auto;
  padding: 13px 24px;
  font-size: 14px;
  word-wrap:break-word
}
.btn-box{
  text-align: center;
  height: 45px;
  line-height: 45px;
  font-size: 15px;
  color: #4686F2;
  font-weight: 400;
  border-top: 1px solid #EBEBEB;
}
</style>
