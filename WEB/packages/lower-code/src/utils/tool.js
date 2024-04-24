

export  const later = (fn, time) => {
  setTimeout(() => {
    fn();
  }, time);
};

export const sleep = async (time) => {
  return new Promise((r) => {
    setTimeout(() => r(), time);
  });
};
