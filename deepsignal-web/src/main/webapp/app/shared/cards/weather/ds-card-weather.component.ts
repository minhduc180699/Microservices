import { UserSetting } from '@/shared/model/usersetting.model';
import { Component, Vue, Inject, Prop, Watch } from 'vue-property-decorator';
import { CARD_SIZE, DATA_TYPE_CARDS } from '@/shared/constants/feed-constants';
import PublicService from '@/service/public.service';
import { LOCAL_STORAGE_CONSTANTS, SESSION_STORAGE_CONSTANTS, TEMPERATURE_UNIT } from '@/shared/constants/ds-constants';
import moment from 'moment';
import { ResponseCode, Setting } from '@/shared/constants/ds-constants';
import { UserSettingService } from '@/service/usersetting.service';
import { UserSettingModel } from '@/shared/cards/popup-setting/popup-user-setting.model';

@Component({
  computed: {
    getSetting() {
      return this.$store.getters['userSettingStore/timeSetting'];
    },
  },
  watch: {
    getSetting(value) {
      this.changeSettingWeather();
    },
  },
})
export default class DsCardWeather extends Vue {
  // @Prop(CardModel) readonly item: CardModel | undefined;
  @Prop(Object) readonly currentSetting: UserSetting | undefined;
  item: any;
  @Inject('publicService')
  private publicService: () => PublicService;
  @Inject('userSettingService')
  private userSettingService: () => UserSettingService;
  public maxTemperature = {
    weather: [{ main: '' }],
    main: {},
  };
  public minTemperature = {
    weather: [{ main: '' }],
    main: {},
  };
  private today = {};

  ipClient;
  language;
  private temperatureUnit;
  private location;

  data(): any {
    return {
      item: this.item,
    };
  }

  // @Watch('currentSetting')
  // detectNewSetting() {
  //   this.getWeatherBaseInfo(this.ipClient, this.language);
  // }

  created(): void {
    this.ipClient = this.getIpFromCache();
    this.language = this.$i18n.locale;
    const data = this.getWeatherInCache();
    if (!data) {
      this.getWeatherByIp();
    } else {
      this.processResponse(data);
    }
  }

  getWeatherByIp() {
    if (this.ipClient) {
      this.getWeatherBaseInfo(this.ipClient, this.language);
    } else {
      this.publicService()
        .getIpFromClient()
        .then((res: string) => {
          this.ipClient = res;
          this.getWeatherBaseInfo(res, this.language);
        });
    }
  }

  getIpFromCache(): string {
    return localStorage.getItem(LOCAL_STORAGE_CONSTANTS.IP);
  }

  cacheResponseWeather(res) {
    if (res) {
      const data = {
        lastUpdated: new Date(),
        ...res,
      };
      sessionStorage.setItem(SESSION_STORAGE_CONSTANTS.WEATHER, JSON.stringify(data));
    }
  }

  getWeatherInCache() {
    if (sessionStorage.getItem(SESSION_STORAGE_CONSTANTS.WEATHER)) {
      const weather = JSON.parse(sessionStorage.getItem(SESSION_STORAGE_CONSTANTS.WEATHER));
      const now = moment(new Date());
      const weatherLastUpdated = moment(weather.lastUpdated);
      // if last updated > now is 2 hours, call API to update data weather
      if (now.diff(weatherLastUpdated, 'hours', true) > 2) {
        return;
      } else {
        return weather;
      }
    }
  }

  getWeatherBaseInfo(ip, language) {
    const dataTransfer = this.$store.getters['userSettingStore/userSetting'];
    if (dataTransfer) {
      this.temperatureUnit = dataTransfer.weather.temperatureUnit;
      if (dataTransfer.weather.alwaysDetectLocation == false && dataTransfer.locWeather) {
        this.location = dataTransfer.locWeather;
      }
    } else {
      this.temperatureUnit = TEMPERATURE_UNIT.C;
    }
    this.publicService()
      .getWeatherInfo(ip, language, this.temperatureUnit, this.location)
      .then(data => {
        this.cacheResponseWeather(data.data);
        this.processResponse(data.data);
      });
  }

  processResponse(res) {
    if (res) {
      this.item = { ...this.item, ...res };
      this.item.dataTemplate = 'wheather';
      this.item.name = 'Weather';
      this.item.dataSize = CARD_SIZE._1_1;
      this.item.dataType = DATA_TYPE_CARDS[0]._01;
      this.today = JSON.parse(res.daily)[0];
    }
  }

  openPopUpUserSetting() {
    this.$bvModal.show('modal-member-setting');
  }

  hideWeatherCard() {
    this.$root.$emit('hide-weather-card');
  }

  changeSettingWeather() {
    const userSetting = this.$store.getters['userSettingStore/userSetting'];
    if (userSetting.weather.temperatureUnit != this.temperatureUnit) {
      this.temperatureUnit = userSetting.weather.temperatureUnit;
      this.getWeatherBaseInfo(this.ipClient, this.language);
    }
  }
}
