const { glob } = require("glob");
const pinyin = require("pinyin");
const fs = require('fs');
const ejs = require('ejs')

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
      <% for (let ext of item.exts) {%>
      src: url('./font/<%= item.alias+'.'+ext %>');
      <% } %>
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

const getFontList = async () => {
  let fontList = [];
  const pyMap = new Map();
  const aliasMap = new Map();
  const results = await glob("./font/*.{ttf,otf}", {
    stat: true,
    withFileTypes: true,
  });
  results.forEach((path) => {
    const pathLists = path.name.split(".")
    const alias = pathLists.slice(0,-1).join('.')
    const ext = pathLists.slice(-1)[0]
    const py = pinyin(alias, pyConfig).join("").toLocaleLowerCase();
    let item;
    let cacheAlias
    if ((cacheAlias=pyMap.get(py)) && cacheAlias !== alias) {
      throw new Error(`same name: ${cacheAlias}, ${alias}`);
    }
    if (item = aliasMap.get(alias)) {
      item.exts.push(ext)
    } else {
      item = {
        exts: [ext],
        alias: alias,
        value: py
      }
      fontList.push(item)
      aliasMap.set(alias, item)
      pyMap.set(py, alias)
    }
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
  fs.writeFileSync('./font.scss',cssStr)
  fs.writeFileSync('./fontMap.js',jsStr)

}

const main = async() => {
  const fontList = await getFontList()
  createFile({fontList})

}

main()