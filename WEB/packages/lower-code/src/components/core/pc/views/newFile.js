import { mapActions } from "vuex";

export default (await import('vue')).defineComponent({
methods: {
...mapActions("editor", ["fetchWork", "setEditingPage", "mCreateCover", 'setEditingElement']),
},
components: {
toolBar,
editPanel,
propsPanel
},
});
