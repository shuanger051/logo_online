import Vue, { reactive } from "vue";
import { Popover } from "vant";
// 表单说明
Vue.directive("explain", {
  bind(el, binding, vnode) {
    const { name } = vnode.componentInstance; // 字段名
    const { fieldExplain } = window.miniAppConfig;
    const elLabel = el.querySelector(".van-field__label");
    const props = reactive({
      value: false,
      placement: "right-start",
      trigger: "click",
      theme: "dark",
    });
    // 判断是否配置了提示信息
    if (name && fieldExplain[name]) {
      let popover = new (Vue.extend({
        render(h) {
          return h(
            Popover,
            {
              props,
              on: {
                input: (val) => (props.value = val),
              },
            },
            [
              // 提示文案展示
              h(
                "div",
                {
                  style: {
                    width: "180px",
                    padding: "6px 12px",
                    display: "inline-block",
                    lineHeight: "1.8em",
                  },
                },
                [fieldExplain[name]]
              ),
              // 提示图标
              h("van-icon", {
                props: { name: "question-o" },
                slot: "reference",
              }),
            ]
          );
        },
      }))();
      // 提示信息挂载
      const elTip = document.createElement("span");
      elLabel.appendChild(elTip);
      popover.$mount(elTip);
    }
  },
});
