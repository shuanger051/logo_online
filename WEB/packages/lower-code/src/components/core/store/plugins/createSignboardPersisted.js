import createPersistedState from "vuex-persistedstate";
const mutationKey = ["setElementCommonStyle", "elementManager", "clearsignboardCache", 'updateCache'];

function debounce(fn, delay) {
  let timer = null;
  delay || (delay = 2000);
  return function () {
    let context = this,
      args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, delay);
  };
}

function filter(fn, handler) {
  return function () {
    if (fn.apply(null, arguments)) {
      handler.apply(null, arguments);
    }
  };
}

const createSignboardPersisted = (options) => {
  let key = options.key;
  let storage = options.storage || localStorage;
  return createPersistedState({
    key,
    paths: ["editor.signboardCache"],
    subscriber(store) {
      return (_) => {
        store.subscribe(
          filter(
            (mutation) => {
              return mutationKey.some((item) => {
                return (mutation.type || "").includes(item);
              });
            },
            debounce((mutation, state) => {
              try {
                if (mutation.type.includes('clearsignboardCache')) {
                  store.commit(
                    "editor/updatesignboardCache",
                    null
                  );
                  storage.removeItem(key);
                  return 
                }
                if (state.editor.work) {
                  let workStr = JSON.stringify({
                    editor: {
                      signboardCache: state.editor.work,
                    },
                  });
                  store.commit(
                    "editor/updatesignboardCache",
                    JSON.parse(JSON.stringify(state.editor.work))
                  );
                  storage.setItem(key, workStr);
                }
              } catch (e) {
                console.log(e);
              }
            })
          )
        );
      };
    },
  });
};
export default createSignboardPersisted;
