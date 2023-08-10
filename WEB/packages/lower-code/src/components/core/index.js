import { mapState, mapActions, mapMutations } from "vuex";
import FixedTools from "core/editor/fixed-tools/index";
import EditorRightPanel from "core/editor/right-panel/index";
import EditorCanvas from "core/editor/canvas/index";
import EditorLeftPanel from "core/editor/left-panel/index";
import AdjustLineV from "core/support/adjust-line/vertical";
import store from "core/store/index";
import "@editor/locales";
import "core/support/index.js";
import "core/styles/index.scss";
import "core/styles/scrollbar.scss";
import "animate.css";
import "@editor/plugins/index";

function AdjustHoc(WrappedComponent) {
  return {
    props: WrappedComponent.props,
    data: () => ({
      show: true,
    }),
    computed: {
      displayStyle() {
        return {
          display: this.show ? "block" : "none",
        };
      },
      iconType() {
        return `vertical-${this.show ? "right" : "left"}`;
      },
    },
    render(h) {
      return (
        <div class="collapse-indicator-wrapper">
          <WrappedComponent
            attrs={this.$attrs}
            props={this.$props}
            on={this.$listeners}
            scopedSlots={this.$scopedSlots}
            class="component-wrapper"
            style={this.displayStyle}
          />
          <div class="indicator-wrapper">
            <span
              class="indicator"
              onClick={() => {
                this.show = !this.show;
              }}
            >
              <a-icon type={this.iconType} />
            </span>
          </div>
        </div>
      );
    },
  };
}

const AdjustLeftPanel = AdjustHoc(EditorLeftPanel);

const CoreEditor = {
  name: "CoreEditor",
  store,
  props: {
    workId: {
      type: [Number, String],
    },
    local: {
      type: String,
      default: "CN",
    },
  },
  data: () => ({
    previewDialogVisible: false,
    propsPanelWidth: 320,
  }),
  computed: {
    ...mapState("editor", {
      work: (state) => state.work,
    }),
    ...mapState('loading', [
      'saveWork_tip',
      'saveWork_loading'
    ]),
    ...mapState("dialog", [
      "editScript_dialog",
      "viewScript_dialog",
      "createScript_dialog",
      "allScriptList_dialog",
    ]),
  },

  methods: {
    ...mapActions("editor", ["fetchWork", 'setEditingPage']),
    ...mapMutations("dialog", ["updateDialog"]),
  },
  render(h) {
    return (
      <a-spin spinning = {this.saveWork_loading} tip={this.saveWork_tip}>
        <a-layout>
          <a-layout style={{paddingTop: '20px'}}>
            <AdjustLeftPanel />
            <EditorCanvas />
            <AdjustLineV
              onLineMove={(offset) => {
                this.propsPanelWidth += offset;
              }}
            />
            <FixedTools />
            <EditorRightPanel width={this.propsPanelWidth} />
          </a-layout>
      </a-layout>
      </a-spin>
    );
  },
  created() {
    this.setEditingPage()
    if (this.$route.params.id) {
      this.fetchWork(this.$route.params.id);
    } else {
      // this.$message.error("no work id!");
    }
  },
};

export default CoreEditor;
