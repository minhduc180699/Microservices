import _ from 'lodash';

export function separateArr(callbackFn, xs) {
  return xs.reduce(
    ([T, F], x, i, arr) => {
      if (callbackFn(x, i, arr) === false) return [T, [...F, x]];
      else return [[...T, x], F];
    },
    [[], []]
  );
}

export function randomEleInArray(array, ...ele): string {
  let arrRandom;
  if (ele) {
    const eleArray = [...ele];
    arrRandom = _.difference(array, eleArray);
  } else {
    arrRandom = array;
  }

  return arrRandom[Math.floor(Math.random() * arrRandom.length)];
}

export function removeEleDuplicateInArray(array: any[]): any[] {
  const s = new Set(array);
  const it = s.values();
  return Array.from(it);
}
