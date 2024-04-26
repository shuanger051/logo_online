const { glob } = require("glob");
const pinyin = require("pinyin");
const path = require('path')
const fs = require('fs');
const ejs = require('ejs')
const fontMap_back = require('./fontMap_bak')
const resolve = (str) => {
  return path.resolve(__dirname, str)
}

let backFontWoff = {}

const formatBackStr = () => {
  const backStr = fs.readFileSync('./font_bak1.scss', 'utf-8');
  const arry = backStr.match(/@font-face.*?{[\s\S]*?}/g);
  arry.forEach((str) => {
    let name = /font-family:\s*'(.*?)'/.exec(str)
    let woff = /url\('(.*?.woff2)'\)/.exec(str)
    if (name && woff) {
      backFontWoff[name[1]] = woff[1]
    }
  })
  console.log(backFontWoff)
}
formatBackStr()
const pyConfig = {
  style: 0,
  heteronym: false,
};
const systemFont = [
  {
    value: '宋体',
    alias: '宋体'
  },
  {
    value: '微软雅黑',
    alias: '微软雅黑'
  },
  {
    value: '黑体',
    alias: '黑体'
  },
  {
    value: 'arial',
    alias: 'arial'
  },
  {
    value: 'sans-serif',
    alias: 'sans-serif'
  },
]
const cssTemp = `
  <% for (let item of fontList) {%>
    @font-face {
      font-family: '<%= item.value %>';
      src:
        url('./font22/<%= item.value+'.ttf' %>') format('truetype')<%= item.woff ? ',' : ';'%>
        <%- item.woff ? "url('"+item.woff+"') format('woff2');" : ''%>
    }
  <% } %>
`;
const jsTemp = `
    const lists = [
      <% for (let item of fontList) {%>
      {
        label: '<%= item.alias%>',
        value: '<%= item.value%>'
      },<%}%>
    ]
    export default lists
`;

const fontMeta = {
  ttf: {
    priority: 1,
    format: 'truetype'
  },
  woff2: {
    priority: 2,
    format: 'woff2'
  }
}


var num = 0
const getFontList = async () => {
  let fontList = [];
  const pyMap = new Set();
  const results = await glob(resolve("./font/*.ttf"), {
    stat: true,
    withFileTypes: true,
  });
  results.forEach((path) => {
    const pathLists = path.name.split(".")
    const alias = pathLists.slice(0,-1).join('.')
    let py = pinyin(alias, pyConfig).join("").toLocaleLowerCase();
    let item;
    py = py.replace(/\s|\(|\)|（|）/g, '')
    py=py.replace(/[\u4e00-\u9fa5]/g,() => {
      return num++
  })
    if (pyMap.has(py)) {
      py += num++
    }

    fs.renameSync(`./font/${path.name}`, `./font/${py}.ttf`)
    

      item = {
        woff: backFontWoff[py],
        alias: alias,
        value: py,
      }
      fontList.push(item)
      pyMap.add(py)
    
  })

  return fontList;
};


const createFile = (data) => {
  const cssTpl = ejs.compile(cssTemp);
  const jsTpl = ejs.compile(jsTemp);
  const cssStr = cssTpl(data)
  const jsStr = jsTpl({
    fontList: [
      ...systemFont,
      ...data.fontList
    ]
  })
  fs.writeFileSync(resolve('./font.scss'),cssStr)
  fs.writeFileSync(resolve('./fontMap.js'),jsStr)

}

const main = async() => {
  const fontList = await getFontList()
  createFile({fontList})

}

main()
