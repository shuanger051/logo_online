const { glob } = require("glob");
const path = require('path')
const fs = require('fs');
const ttf2woff2 = require('ttf2woff2');


const resolve = (str) => {
  return path.resolve(__dirname, str)
}
const ttfToWoff2 = async () => {
  const results = await glob(resolve("./font/*.ttf"), {
    stat: true,
    withFileTypes: true,
  });
  let len = results.length;
  results.forEach((path, i) => {
    const pathLists = path.name.split(".")
    const alias = pathLists.slice(0,-1).join('.')
    const input = fs.readFileSync(path.path + '/'+path.name);

    console.log('转换中：------------------')
    console.log(`总共:${len}个文件，当前：第${i+1}个`)
    try {
      if (require.resolve(`${path.path}/${alias}.woff2`)) {
        return
      }
    } catch(e) {

    }
    fs.writeFileSync(`${path.path}/${alias}.woff2`, ttf2woff2(input));
  })
}

ttfToWoff2()