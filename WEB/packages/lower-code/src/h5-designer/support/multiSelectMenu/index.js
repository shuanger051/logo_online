import { mapGetters, mapState, mapActions } from 'vuex'
import './index.scss'

export default {
  computed: {
    // ...mapGetters('cms/editState', ['selectedElementData']),
    ...mapGetters('cms/elements', ['selectedPageElements']),
    ...mapState('cms/pages', {
      pages: state => state.items
    }),
    ...mapState('cms/editState', [
      'clipboard',
      'selectedList',
      'selectedPage'
    ]),
    //获取编辑页面的高度，用于计算吸底元素的top
    pageHeight(){
      const index = this.pages.findIndex(item => { return item.uuid == this.selectedPage })
      if (index !== -1) {
        return this.pages[index].style.height
      }
    },
    multiSelectMenuPosition(){
      if(this.selectedList.length > 1){
        let selItem = this.selectedPageElements.filter(item => this.selectedList.indexOf(item.uuid) > -1)
        let top = Math.min.apply(Math,selItem.map(item => { return typeof item.style.top === 'number'?item.style.top : (this.pageHeight-item.style.height) })) - 32 - 10 //获取多选元素的top最高值
        let maxLeft = Math.max.apply(Math,selItem.map(item => { return item.style.left }))
        let minLeft = Math.min.apply(Math,selItem.map(item => { return item.style.left }))
        let left = (maxLeft - minLeft) / 2 + minLeft
        return { top, left }
      } else {
        return { top:0, left:0}
      }
    },
    selectedItemList(){
      return this.selectedPageElements.filter(item => this.selectedList.indexOf(item.uuid) > -1)
    },
    setPlaceBtnList(){
      return {
        horizontal:[//水平
          {
            type:'horizontal-left',
            name:'左对齐'
          },
          {
            type:'horizontal-center',
            name:'居中'
          },
          {
            type:'horizontal-right',
            name:'右对齐'
          }
        ],
        vertical:[//垂直,包含吸底元素显示禁用
          {
            type:this.selectedPageElements.findIndex(e => typeof e.style.top !== 'number')>-1?'vertical-top-disable':'vertical-top',
            name:'顶部对齐'
          },
          {
            type:this.selectedPageElements.findIndex(e => typeof e.style.top !== 'number')>-1?'vertical-center-disable':'vertical-center',
            name:'垂直居中'
          },
          {
            type:'vertical-bottom',
            name:'底部对齐'
          }
        ],
        spreadOut:[//分散
          {
            type:this.selectedList.length < 3 ? 'spreadOut-vertical-disable' : 'spreadOut-vertical',
            name:'垂直分布'
          },
          {
            type:this.selectedList.length < 3 ? 'spreadOut-horizontal-disable' : 'spreadOut-horizontal',
            name:'水平分布'
          }
        ]
      }
    },
  },
  // props: {
  //   position: {
  //     type: Array,
  //     default: () => []
  //   }
  // },
  methods: {
    ...mapActions('cms/elements', ['multiUpdateElementStyle', 'updateElementStyle']),
    handleSelectMenu(type){
      console.log('type',type,'selectedItemList',this.selectedItemList,'selectedList',this.selectedList)
      if(type.indexOf('disable') > -1) return
      //设selectedBox为被选中元素构成的虚拟框
      let selectedBox = {
        topLine:0, //虚拟框上边的top距离
        bottomLine:0, //虚拟框下边的top距离
        leftLine:0, //虚拟框左边的left距离
        rightLine:0, //虚拟框右边的left距离
        centerTop:0, //虚拟框垂直的中心点距离
        centerLeft:0 //虚拟框水平的中心点距离
      }
      selectedBox.topLine = Math.min.apply(Math,this.selectedItemList.map(item => { return typeof item.style.top === 'number'?item.style.top : (this.pageHeight - item.style.height) }))
      selectedBox.bottomLine = Math.max.apply(Math,this.selectedItemList.map(item => { return typeof item.style.top === 'number'?(item.style.top + item.style.height):this.pageHeight }))
      selectedBox.leftLine = Math.min.apply(Math,this.selectedItemList.map(item => { return item.style.left }))
      selectedBox.rightLine = Math.max.apply(Math,this.selectedItemList.map(item => { return item.style.left + item.style.width }))
      selectedBox.centerTop = selectedBox.topLine + (selectedBox.bottomLine - selectedBox.topLine) / 2
      selectedBox.centerLeft = selectedBox.leftLine + (selectedBox.rightLine - selectedBox.leftLine) / 2
      let objStyle = {}
      switch (type) {
        case 'horizontal-left':
          this.selectedItemList.forEach(e => {
            objStyle[e.uuid] = {left: selectedBox.leftLine}
          });
          this.multiUpdateElementStyle(objStyle)
          break;
        case 'horizontal-center':
          this.selectedItemList.forEach(e => {
            objStyle[e.uuid] = {left: selectedBox.centerLeft - e.style.width / 2}
          });
          this.multiUpdateElementStyle(objStyle)
          break;
        case 'horizontal-right':
          this.selectedItemList.forEach(e => {
            objStyle[e.uuid] = {left: selectedBox.rightLine - e.style.width}
          });
          this.multiUpdateElementStyle(objStyle)
          break;
        case 'vertical-top':
          this.selectedItemList.forEach(e => {
            objStyle[e.uuid] = {top: selectedBox.topLine}
          });
          this.multiUpdateElementStyle(objStyle)
          break;
        case 'vertical-center':
          this.selectedItemList.forEach(e => {
            objStyle[e.uuid] = {top: selectedBox.centerTop - e.style.height / 2}
          });
          this.multiUpdateElementStyle(objStyle)
          break;
        case 'vertical-bottom':
          this.selectedItemList.forEach(e => {
            //吸底元素无需调整
            if(typeof e.style.top === 'number'){
              objStyle[e.uuid] = {top: selectedBox.bottomLine - e.style.height}
            }
          });
          this.multiUpdateElementStyle(objStyle)
          break;
        case 'spreadOut-vertical':
          this.setSpreadOut(selectedBox, 'spreadOut-vertical')
          break;
        case 'spreadOut-horizontal':
          this.setSpreadOut(selectedBox, 'spreadOut-horizontal')
          break;
      }
    },
    // setElePosition(value, t){
    //   let objStyle = {}
    //   this.selectedList.forEach(e => {
    //     objStyle[e] = {[t]: value}
    //   });
    //   this.multiUpdateElementStyle(objStyle)
    // },
    setSpreadOut(selectedBox, t){
      if(t === 'spreadOut-vertical'){
        //盒子高度
        let selectedBoxHeight = selectedBox.bottomLine - selectedBox.topLine
        let _this = this
        //按照top排序
        let newSelectedItemList = JSON.parse(JSON.stringify(this.selectedItemList)).sort(function(a,b){
          let top1 = typeof a.style.top === 'number'? a.style.top : (_this.pageHeight - a.style.height)
          let top2 = typeof b.style.top === 'number'? b.style.top : (_this.pageHeight - b.style.height)
          if(top1 < top2){
            return -1
          }
          if(top1 > top2){
            return 1
          }
          if(top1 === top2){
            return 0
          }
        })
        console.log('newSelectedItemList',newSelectedItemList)
        //获取垂直方向上无需移动的两个端点元素
        // let topEle = this.newSelectedItemList.find( e => e.style.top === selectedBox.topLine)
        // let btmEle = this.newSelectedItemList.find( e => selectedBox.bottomLine === (e.style.top + e.style.height))
        let allEleHeight = this.selectedItemList.reduce((a,b) =>{
            a = a + b.style.height;
            return a;
        },0)
        console.log('allEleHeight',allEleHeight)
        let distance = (selectedBoxHeight  - allEleHeight)/(this.selectedItemList.length - 1)
        console.log('distance',distance)
        let obj = {}

        newSelectedItemList.forEach((e, index) => {
          if(index!==0 && index!==(newSelectedItemList.length - 1) && typeof e.style.top === 'number'){
            // console.log('newSelectedItemList[index-1].style.top + newSelectedItemList[index-1].style.height + distance',newSelectedItemList[index-1].style.top + newSelectedItemList[index-1].style.height + distance)
            // console.log('e.uuid',e.uuid)
            // this.updateElementStyle({ top: newSelectedItemList[index-1].style.top + newSelectedItemList[index-1].style.height + distance, eleId:e.uuid})
            obj[e.uuid] = {top: obj[newSelectedItemList[index-1].uuid].top + newSelectedItemList[index-1].style.height + distance}
          } else {
            if(typeof e.style.top === 'number'){
              obj[e.uuid] = {top: e.style.top}
            }
          }
        })
        this.multiUpdateElementStyle(obj)
      } else {
        //盒子宽度
        let selectedBoxWidth = selectedBox.rightLine - selectedBox.leftLine
        let newSelectedItemListHor = JSON.parse(JSON.stringify(this.selectedItemList)).sort(function(a,b){
          if(a.style.left < b.style.left){
            return -1
          }
          if(a.style.left > b.style.left){
            return 1
          }
          if(a.style.left === b.style.left){
            return 0
          }
        })

        let allEleWidth = this.selectedItemList.reduce((a,b) =>{
            a = a + b.style.width;
            return a;
        },0)
        console.log('allEleWidth',allEleWidth)
        let distanceHor = (selectedBoxWidth  - allEleWidth)/(this.selectedItemList.length - 1)
        console.log('distance',distanceHor)
        let obj = {}
        newSelectedItemListHor.forEach((e, index) => {
          if(index!==0 && index!==(newSelectedItemListHor.length - 1)){
            // console.log('newSelectedItemList[index-1].style.left + newSelectedItemList[index-1].style.width + distanceHor',newSelectedItemListHor[index-1].style.top + newSelectedItemListHor[index-1].style.height + distanceHor)
            // console.log('e.uuid',e.uuid)
            // this.updateElementStyle({ left: newSelectedItemListHor[index-1].style.left + newSelectedItemListHor[index-1].style.width + distanceHor, eleId:e.uuid})
            obj[e.uuid] = {left: obj[newSelectedItemListHor[index-1].uuid].left + newSelectedItemListHor[index-1].style.width + distanceHor}
          } else {
            obj[e.uuid] = {left:e.style.left}
          }
        })
        this.multiUpdateElementStyle(obj)
      }
    }
  },

  render (h) {
    let multiSelectMenuPosition = `position:absolute;top:${this.multiSelectMenuPosition.top}px;left:${this.multiSelectMenuPosition.left}px;zIndex:100`
    return (
          <div 
            class="multiSelectTool" 
            style={multiSelectMenuPosition}
            onClick={(e) => {
              e.preventDefault()
              e.stopPropagation()
            }}
            onmousedown={(e) => {
              e.preventDefault()
              e.stopPropagation()
            }}
          >
          {
            this.setPlaceBtnList.horizontal.map(
              option => {
                return (
                  <h-tooltip content={option.name} placement="top">
                    <div 
                      class={`btn`}
                      onClick={(e) => {e.stopPropagation();this.handleSelectMenu(option.type)}}
                      key={option.type}
                    >
                      <div class={`btnSvg ${option.type}`}></div>
                    </div>
                  </h-tooltip>
                  )
                }
            )
          }
          {
            this.setPlaceBtnList.vertical.map(
              option => {
                return (
                  <h-tooltip content={option.name} placement="top">
                    <div 
                      class={`${option.type.indexOf('disable') > -1 ? 'btn-disable' : 'btn'}`}
                      onClick={(e) => {e.stopPropagation();this.handleSelectMenu(option.type)}}
                    >
                      <div class={`btnSvg ${option.type}`}></div>
                    </div>
                  </h-tooltip>
              )}
            )
          }
          {
            this.setPlaceBtnList.spreadOut.map(
              option => {
                return (
                  <h-tooltip content={option.name} placement="top">
                    <div 
                      class={`${option.type.indexOf('disable') > -1 ? 'btn-disable' : 'btn'}`}
                      onClick={(e) => {e.stopPropagation();this.handleSelectMenu(option.type)}}
                    >
                      <div class={`btnSvg ${option.type}`}></div>
                    </div>
                  </h-tooltip>
              )}
            )
          }
      </div> 
    )
  }
}
