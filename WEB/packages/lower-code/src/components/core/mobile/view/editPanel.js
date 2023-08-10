import { mapState, mapActions } from "vuex";
import Shape from "core/support/shape";

export default {
  props: ["elements", "handleClickElementProp", "handleClickCanvasProp"],
  data: () => ({}),
  computed: {
    ...mapState("editor", ["editingElement", "work"]),
  },
  methods: {
    ...mapActions("editor", [
      "setEditingElement",
      "setElementPosition",
      "setElementShape",
      "recordElementRect",
      "elementManager",
      "updateWork",
    ]),

    handleElementMove(pos) {
      // console.log(pos, 999)
      this.setElementPosition(pos);
    },
    handleRotation(angle) {
      this.editingElement.commonStyle.angle = angle;
    },
    handlePointMove(pos, point) {
      this.setElementPosition(pos);
    },
    handleClickCanvas(e) {
      if (!e.target.classList.contains("element-on-edit-canvas")) {
        this.setEditingElement();
      }
    },

    /**
     * TODO 封装 adjust editor scale 组件
     * scale: height/width
     * @param {MouseEvent} e
     */
    /**
     * #!zh: renderCanvas 渲染中间画布
     * elements
     * @param {*} h
     * @param {*} elements
     * @returns
     */
    renderCanvas(h, elements) {
      return (
        <div
          class="edit-panel-wrap"
          onClick={(e) => {
            this.handleClickCanvas(e);
          }}
        >
          <div
            style={{ height: "100%", background: this.work.backgroundColor }}
            id="content_edit"
          >
            {elements.map((element, index) => {
              const isEditingElement =
                this.editingElement &&
                this.editingElement.uuid === element.uuid;
              const data = {
                style: {
                  width: "100%",
                  height: "100%",
                },
                // 添加 class 的原因：与 handleClickCanvasProp 配合,
                // 当点击编辑画布上的其它区域（clickEvent.target.classList 不包含下面的 className）的时候，设置 editingElement=null
                class: "element-on-edit-canvas",
                props: {
                  ...element.getProps(), // #6 #3,
                  editorMode: "edit",
                  isEditingElement,
                }
              };
              return (
                <Shape
                  onDelete={() => this.elementManager({ type: "delete" })}
                  style={element.getStyle({ position: "absolute" })}
                  defaultPosition={element.commonStyle} // {top, left}
                  element={element}
                  active={this.editingElement === element}
                  handleMousedownProp={() => {
                    this.setEditingElement(element);
                  }}

                  handlePointMoveProp={this.handlePointMove}
                  handleElementMoveProp={this.handleElementMove}
                  handleRotationProp={this.handleRotation}
                  handleElementMouseUpProp={() => {
                    this.recordElementRect();
                  }}
                  handlePointMouseUpProp={() => {
                    this.recordElementRect();
                  }}
                >
                  {h(element.uuid, data)}
                </Shape>
              );
            })}
          </div>
        </div>
      );
    },
  },
  render(h) {

    return this.renderCanvas(h, this.elements);
  },
};
