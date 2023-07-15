import MsgPop from './MsgPop';

export default {
  install(Vue) {
    function MsgPopFunc(txt, type="提示", option={buttonText:'我知道了'}) {
      return new Promise((resolve, reject) => {
        const MsgPopPlugin = Vue.extend(MsgPop);
        const $vm = new MsgPopPlugin({
          el: document.createElement('div')
        });
        $vm.txt = txt;
        $vm.type = type;
        $vm.option = option;
        $vm.callback = val => {
          document.body.removeChild($vm.$el);
          if (val) {
            resolve();
          } else {
            reject();
          }
        };
        document.body.appendChild($vm.$el);
      });
    }
    window.$MsgPop = MsgPopFunc;
  }
};
