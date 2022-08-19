import axios from 'axios';
import { PATTERN } from '@/constants';
import { Console } from 'console';
import { INTERVAL_CONNECTOME_CHECK_UPDATE, TYPE_VERTEX } from '@/shared/constants/ds-constants';
import { NetworkVertex } from '../../shared/model/network-vertex-model';
import { NetworkEdge } from '../../shared/model/network-edge-model';
import * as d3 from 'd3';
import * as d3ScaleChromatic from 'd3-scale-chromatic';
import { ConnectomeNetworkStorable, connectomeNetworkStore } from '@/shared/config/store/connectome-network-store';
import { Store } from 'vuex';

const baseConnectomeApiUrl = '/api/connectome';
const baseToolboxApiUrl = '/api/toolbox';

export default class ConnectomeService {
  constructor(private store: Store<ConnectomeNetworkStorable>) {
    this.initConnectome();
  }

  public getConnectomeByLogin(login: any) {
    const param = '?login='.concat(login);
    return axios.get('/api/connectome-feed/getByUserId' + param);
  }

  // public queryParamPhone(param) {
  //   if (!param) {
  //     return '';
  //   }
  //   return param.replaceAll(PATTERN.REPLACE_PHONE_REGEX, '');
  // }

  public getConnectomeNetworkMap(id: string) {
    if (!id) {
      return '';
    }
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseConnectomeApiUrl + '/network/get/' + id)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  public getConnectomeUpdatedNetworkMap(id: string) {
    if (!id) {
      return '';
    }
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseConnectomeApiUrl + '/network/updated/get/' + id)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  public getConnectomeMap(id: string) {
    if (!id) {
      return '';
    }

    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseConnectomeApiUrl + '/get/' + id)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  public getConnectomeUpdatedMap(id: string) {
    if (!id) {
      return '';
    }

    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseConnectomeApiUrl + '/updated/get/' + id)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  public postKeywordAnalysis(requestId: string, language: string, textToAnalyze: string) {
    if (!requestId) {
      console.log('!requestId');
      return '';
    }
    if (!language) {
      console.log('!language', language);
      return '';
    }
    if (!textToAnalyze) {
      console.log('!textToAnalyze');
      return '';
    }
    const formData = new FormData();
    formData.append('request_id', requestId);
    formData.append('text', textToAnalyze);
    formData.append('language', language);
    console.log('request', formData);
    return axios.post(baseToolboxApiUrl + '/' + language + '/keywords', { request_id: requestId, text: textToAnalyze });
  }

  public postKeyword_entity_disambiguation(requestId: string, language: string, textToAnalyze: string) {
    if (!requestId) {
      console.log('!requestId');
      return '';
    }
    if (!language) {
      console.log('!language', language);
      return '';
    }
    if (!textToAnalyze) {
      console.log('!textToAnalyze');
      return '';
    }
    const formData = new FormData();
    formData.append('request_id', requestId);
    formData.append('text', textToAnalyze);
    formData.append('language', language);
    console.log('request', formData);
    return axios.post(baseToolboxApiUrl + '/' + language + '/entity_disambiguation', { request_id: requestId, text: textToAnalyze });
  }

  public getEntityInfo(title: string, language: string) {
    if (!title) {
      return '';
    }
    if (!language) {
      return '';
    }

    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseConnectomeApiUrl + '/node/details/get?title=' + title + '&language=' + language)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }

  //update the connectome map data

  private initConnectome() {}

  public getEntityDetails(label: string, srcLang: string, trgLang: string) {
    if (!label) {
      return '';
    }
    if (!srcLang) {
      return '';
    }
    if (!trgLang) {
      return '';
    }
    console.log('getEntityDetails', label, srcLang, trgLang);

    ///entity/{srclang}/details/get
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseConnectomeApiUrl + '/entity/' + srcLang + '/details/get?label=' + label)
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
  }
}
