import axios from 'axios';
import { appendParamToRequest } from '@/util/ds-util';

export default class PublicService {
  public getWeatherInfo(ipAddress: string, language: string, units?, locWeather?) {
    const mapKeyValue = new Map();
    mapKeyValue.set('ip-address', ipAddress);
    mapKeyValue.set('language', language);
    if (units) mapKeyValue.set('units', units);
    if (locWeather) mapKeyValue.set('locWeather', locWeather);
    return axios.get('api/public/getWeather'.concat(appendParamToRequest(mapKeyValue)));
  }

  public getLocationByIp(ipAddress: string) {
    return axios.get('api/public/getLocationByIp/' + ipAddress);
  }

  public getTemperatureMinMax(ipAddress: string, language: string) {
    const mapKeyValue = new Map();
    mapKeyValue.set('ip-address', ipAddress);
    mapKeyValue.set('language', language);
    return axios.get('api/public/getTemperatureMinMax'.concat(appendParamToRequest(mapKeyValue)));
  }

  previewInfoByUrl(url: string, connectomeId?: string) {
    return axios.get('api/public/previewByUrl?url='.concat(url).concat('&connectomeId=').concat(connectomeId));
  }

  getFakeDataNetworkChart() {
    return axios.get('api/public/getFakeDataNetworkChart');
  }

  getIpFromClient() {
    return new Promise(resolve => {
      $.getJSON('https://api.ipify.org/?format=json', data => {
        resolve(data.ip);
      });
    });
  }

  public getCountryCode() {
    return axios.get('api/public/getCountryCode');
  }
}
