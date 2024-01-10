import { mapState, mapActions } from "vuex";
import Shape from "core/support/shape";
let moveFlag = 0


const createEvent = (obj) => {
  let { el, name, handle } = obj;
  let event = name;

  el = el || document;
  el.addEventListener(event, handle);
  return () => {
    el.removeEventListener(event, handle);
  };
};

export default {
  props: ["elements", "handleClickElementProp", "handleClickCanvasProp"],
  data: () => ({
    pos: {
      left: 0,
      top: 0
    }
  }),
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
      this.setElementPosition(pos);
    },
    handleRotation(angle) {
      this.editingElement.commonStyle.angle = angle;
    },
    handlePointMove(pos, point) {
      this.setElementPosition(pos);
    },
    mousedown(e) {
      if (moveFlag == 0) {

        let sX = this.pos.left - e.changedTouches[0].clientX;
        let sY = this.pos.top - e.changedTouches[0].clientY;

        let move = (moveEvent) => {
          // !#zh 移动的时候，不需要向后代元素传递事件，只需要单纯的移动就OK
          moveEvent.stopPropagation();
          let currX = moveEvent.changedTouches[0].clientX;
          let currY = moveEvent.changedTouches[0].clientY;
          this.pos.top = currY + sY;
          this.pos.left = currX + sX;
        };
  
        let up = (moveEvent) => {
          removeMoveEvent();
          removeUpEvent();
        };
        let removeMoveEvent = createEvent({ handle: move, name: "mousemove" });
        let removeUpEvent = createEvent({ handle: up, name: "mouseup" });
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
          style = {{left: this.pos.left + 'px', top: this.pos.top+'px' }}
 
          onClick = {(e) => {
            if (!e.target.classList.contains('element-on-edit-canvas')) {
              this.setEditingElement()
            }
          }}
        >
          <div
            style={{ height: "100%", background: this.work.backgroundColor}}
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
                  element: element
                }
              };
              return (
                <Shape
                  onDelete={() => this.elementManager({ type: "delete" })}
                  style={element.getStyle({ position: "absolute" })}
                  defaultPosition={element.commonStyle} // {top, left}
                  element={element}
                  active={this.editingElement === element}
    
                  handleMousedownProp = {(e) => {
                    this.setEditingElement(element)
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
