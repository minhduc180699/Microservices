import '../shared/model/global';

export async function getImageSizeByUrl(src) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () =>
      resolve({
        width: img.width,
        height: img.height,
      });
    img.onerror = reject;
    img.src = src;
  });
}

export function appendBaseParamRequest(page = 0, size = 10, sortBy = 'id', sortDirection = 'desc') {
  const mapReq = new Map();
  mapReq.set('page', page);
  mapReq.set('size', size);
  mapReq.set('sortBy', sortBy);
  mapReq.set('sortDirection', sortDirection);
  return appendParamToRequest(mapReq);
}

export function appendParamToRequest(mapParam: Map<string, any>, isContinue?): string {
  if (!mapParam) {
    return;
  }
  let paramUrl = isContinue ? '&' : '?';
  const keys = Array.from(mapParam.keys());
  for (let i = 0; i < keys.length; i++) {
    const key = keys[i];
    paramUrl = paramUrl + `${key}=` + mapParam.get(key);
    if (i < keys.length - 1) {
      paramUrl = paramUrl + '&';
    }
  }
  return paramUrl;
}

export function getExtensionFileByName(fileName: string) {
  if (!fileName) {
    return;
  }
  return fileName.split('.').pop();
}

/*
 * find all element duplicate in array
 * return: new array contain element duplicated
 * */
export function findDuplicates(arr) {
  if (!arr || arr.length == 0) {
    return;
  }
  const sorted_arr = arr.slice().sort(); // You can define the comparing function here.
  // JS by default uses a crappy string compare.
  const results = [];
  for (let i = 0; i < sorted_arr.length - 1; i++) {
    if (sorted_arr[i + 1] == sorted_arr[i]) {
      results.push(sorted_arr[i]);
    }
  }
  return results;
}

export function precise(x: any) {
  try {
    return Number.parseFloat(x).toPrecision(4);
  } catch (error) {
    return x;
  }
}

export function buildRequestParamBase(pageable) {
  return {
    page: pageable && pageable.page ? pageable.page : 0,
    size: pageable && pageable.size ? pageable.size : 10,
    sort: pageable && pageable.sort ? pageable.sort : ['createdDate,desc'],
  };
}

export function splitTagHtml(content): [] {
  if (!content) {
    return;
  }
  const div = document.createElement('div');
  div.innerHTML = content;

  div.querySelectorAll('span').forEach(span => {
    div.innerHTML = div.innerHTML.replace(span.outerHTML, '⠀' + span.outerHTML + '⠀'); // Invisible character U+2800
  });

  content = div.innerHTML.split('⠀'); // U+2800
  return content.filter(x => x);
}

export function extractContentFromTag(s) {
  const span = document.createElement('span');
  span.innerHTML = s;
  return span.textContent || span.innerText;
}

export function getExtensionFileBySearchType(searchType: string) {
  if (!searchType) {
    return;
  }
  const fileType = searchType.split(':').pop();
  return fileType;
}

export function onlyInLeft(leftValue, rightValue, keyCompare: string[]) {
  const res = [];

  for (let i = 0; i < leftValue.length; i++) {
    let j = 0;
    let isSame = false;
    while (j < rightValue.length) {
      let m = 0;
      for (let n = 0; n < keyCompare.length; n++) {
        if (rightValue[j][keyCompare[n]] == leftValue[i][keyCompare[n]]) {
          m++;
        }
      }
      if (m == keyCompare.length) {
        isSame = true;
        break;
      }
      j++;
    }
    if (!isSame) res.push(leftValue[i]);
  }
  return res;
}

export function timeDifference(current, previous) {
  const msPerMinute = 60 * 1000;
  const msPerHour = msPerMinute * 60;
  const msPerDay = msPerHour * 24;
  const msPerMonth = msPerDay * 30;
  const msPerYear = msPerDay * 365;

  const elapsed = current - previous;

  if (elapsed < msPerMinute) {
    return Math.round(elapsed / 1000) + ' seconds ago';
  } else if (elapsed < msPerHour) {
    return Math.round(elapsed / msPerMinute) + ' minutes ago';
  } else if (elapsed < msPerDay) {
    return Math.round(elapsed / msPerHour) + ' hours ago';
  } else if (elapsed < msPerMonth) {
    return Math.round(elapsed / msPerDay) + ' days ago';
  } else if (elapsed < msPerYear) {
    return Math.round(elapsed / msPerMonth) + ' months ago';
  } else {
    return Math.round(elapsed / msPerYear) + ' years ago';
  }
}

export function getDomainFromUrl(url) {
  try {
    const domain = new URL(url);
    return domain.hostname;
  } catch {
    return '';
  }
}
