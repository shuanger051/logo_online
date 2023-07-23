import Vue from "vue";
import VueI18n from "vue-i18n";
// default language
import enUSLang from "./lang/US";
import zhCNLang from "./lang/CN";
import editorConfig from "../../index";

Vue.use(VueI18n);

const messages = {
  US: {
    ...enUSLang,
  },
  CN: {
    ...zhCNLang,
  },
};
export const defaultLang = "CN";

let i18n;

if (editorConfig.i18n) {
  i18n = editorConfig.i18n;
  i18n.mergeLocaleMessage("US", enUSLang);
  i18n.mergeLocaleMessage("CN", zhCNLang);
} else {
  i18n = new VueI18n({
    locale: defaultLang,
    fallbackLocale: defaultLang,
    messages,
  });
}

export default i18n;

const loadedLanguages = [defaultLang];

function setI18nLanguage(lang) {
  i18n.locale = lang;
  document.querySelector("html").setAttribute("lang", lang);
  return lang;
}

export function loadLanguageAsync(lang = defaultLang) {
  return new Promise((resolve) => {
    if (i18n.locale !== lang) {
      if (!loadedLanguages.includes(lang)) {
        return import(
          /* webpackChunkName: "lang-[request]" */ `./lang/${lang}`
        ).then((msg) => {
          i18n.setLocaleMessage(lang, msg.default);
          loadedLanguages.push(lang);
          return setI18nLanguage(lang);
        });
      }
      return resolve(setI18nLanguage(lang));
    }
    return resolve(lang);
  });
}
