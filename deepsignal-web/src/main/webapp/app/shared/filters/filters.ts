import Vue from 'vue';
import dayjs from 'dayjs';
import { FILE_TYPE } from '@/shared/constants/ds-constants';
import { getExtensionFileByName } from '@/util/ds-util';
import moment from 'moment';

export const DATE_FORMAT = 'YYYY-MM-DD';
export const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm';

export const DATE_TIME_LONG_FORMAT = 'YYYY-MM-DDTHH:mm';

export function initFilters() {
  Vue.filter('formatDate', (value, format) => {
    if (value) {
      const regex = new RegExp('ago');
      if (regex.test(value)) return value;
      if (format) {
        return dayjs(value).format(format);
      } else {
        return dayjs(value).format(DATE_TIME_FORMAT);
      }
    }
    return '';
  });
  Vue.filter('dateAgo', (value, format) => {
    if (value) {
      return moment(value).fromNow();
    }
    return '';
  });
  Vue.filter('formatMillis', value => {
    if (value) {
      return dayjs(value).format(DATE_TIME_FORMAT);
    }
    return '';
  });
  Vue.filter('duration', value => {
    if (value) {
      const formatted = dayjs.duration(value).humanize();
      if (formatted) {
        return formatted;
      }
      return value;
    }
    return '';
  });
  Vue.filter('iconWeather', value => {
    if (value) {
      // This url to load icon. Docs: https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
      return 'http://openweathermap.org/img/wn/' + value + '@2x.png';
    }
    return '';
  });
  Vue.filter('fileSize', bytes => {
    if (bytes) {
      const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
      if (bytes == 0) return '0 Byte';
      const i = Math.floor(Math.log(bytes) / Math.log(1024));
      return Math.round(bytes / Math.pow(1024, i)) + ' ' + sizes[i];
    }
    return '';
  });
  Vue.filter('fileExtension', value => {
    if (value) {
      let iconUrl;
      const extension = getExtensionFileByName(value);
      if (FILE_TYPE.EXCEL.some(v => extension.includes(v))) {
        iconUrl = 'file-excel-fill.svg';
      } else if (FILE_TYPE.COMPRESSED.some(v => extension.includes(v))) {
        iconUrl = 'file-zip-fill.svg';
      } else if (FILE_TYPE.PPT.some(v => extension.includes(v))) {
        iconUrl = 'file-ppt-fill.svg';
      } else if (FILE_TYPE.PDF.some(v => extension.includes(v))) {
        iconUrl = 'file-pdf-fill.svg';
      } else if (FILE_TYPE.DOC.some(v => extension.includes(v))) {
        iconUrl = 'file-word-fill.svg';
      } else {
        iconUrl = 'file-text.svg';
      }
      return 'content/images/common/file/' + iconUrl;
    }
    return 'content/images/common/file/file.svg';
  });
  Vue.filter('lmTo', (value, numOfLimit) => {
    if (!value || !numOfLimit || isNaN(Number(numOfLimit)) || value.length < numOfLimit) {
      return value;
    }
    return value.substr(0, numOfLimit).concat('...');
  });
  // split text with space
  Vue.filter('lmTo2', (value, numOfLimit?) => {
    if (!value || isNaN(Number(numOfLimit)) || value.length < numOfLimit) {
      return value;
    }
    if (value.includes(' ')) {
      const values = value.split(' ');
      if (values.length < numOfLimit) {
        return value;
      }
      const arrRest = values.splice(0, numOfLimit);
      return arrRest.join(' ').concat('...');
    }
    return value;
  });

  Vue.filter('onlyTime', (value, format) => {
    if (value) {
      const date = new Date(value);
      let hours = date.getHours();
      let minutes = date.getMinutes();
      const ampm = hours >= 12 ? 'PM' : 'AM';
      hours = hours % 12;
      hours = hours ? hours : 12; // the hour '0' should be '12'
      minutes = minutes < 10 ? 0 + minutes : minutes;
      const strTime = hours + ':' + minutes + ' ' + ampm;
      return strTime;
    }
    return '';
  });

  Vue.filter('onlyDate', (value, format) => {
    if (value) {
      const date = new Date(value);
      const options = { year: 'numeric', month: 'long', day: 'numeric' } as Intl.DateTimeFormatOptions;
      return date.toLocaleDateString('en-us', options);
    }
    return '';
  });
}
