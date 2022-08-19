import { Store } from 'vuex';
import { AccountStateStorable } from '@/shared/config/store/account-store';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import VueRouter from 'vue-router';
import * as SockJS from 'sockjs-client';
import { map } from 'rxjs';

const DESTINATION_TRACKER_NOTIFICATION = '/topic/notification';
const DESTINATION_TRACKER_UPDATE_FEED = '/topic/updateFeed';
const DESTINATION_TRACKER_UPDATE_CONNECTOME = '/topic/updateConnectome';
const DESTINATION_TRACKER_ISSUE_TRACKING = '/topic/signalTracking';

export default class WebsocketService {
  private rxStomp: RxStomp;
  private rxStompConfig: RxStompConfig;

  constructor(private router: VueRouter, private store: Store<AccountStateStorable>) {
    this.stomp = new RxStomp();
    // this.rxStompConfig = {
    //   connectHeaders: {
    //     login: 'deepsignal',
    //     passcode: 'deeppass22',
    //   }
    // };
    // this.stomp.configure(this.rxStompConfig);
    // this.router.afterEach(() => this.sendActivity());

    this.store.watch(
      (_state, getters) => getters.authenticated,
      (value, oldValue) => {
        if (value === oldValue) return;
        if (value) {
          return this.connect();
        }
        return this.disconnect();
      }
    );
  }

  get stomp() {
    return this.rxStomp;
  }

  set stomp(rxStomp) {
    this.rxStomp = rxStomp;
    this.rxStomp.configure({
      debug: (msg: string): void => {
        // console.log(new Date(), msg);
      },
    });
    // this.rxStomp.connected$.subscribe(() => {
    //   this.sendActivity();
    // });
  }

  private connect(): void {
    this.updateCredentials();
    return this.rxStomp.activate();
  }

  private disconnect(): Promise<void> {
    return this.rxStomp.deactivate();
  }

  private getAuthToken() {
    return localStorage.getItem('ds-authenticationToken') || sessionStorage.getItem('ds-authenticationToken');
  }

  private buildUrl(): string {
    // building absolute path so that websocket doesn't fail when deploying with a context path
    const loc = window.location;
    const baseHref = document.querySelector('base').getAttribute('href');
    const url = '//' + loc.host + baseHref + 'websocket/tracker';
    const authToken = this.getAuthToken();
    if (authToken) {
      return `${url}?access_token=${authToken}`;
    }
    return url;
  }

  private updateCredentials(): void {
    this.rxStomp.configure({
      webSocketFactory: () => {
        return new SockJS(this.buildUrl());
      },
    });
  }

  // private sendActivity(): void {
  //   this.rxStomp.publish({
  //     destination: DESTINATION_ACTIVITY,
  //     body: JSON.stringify({ page: this.router.currentRoute.fullPath }),
  //   });
  // }

  public subscribeNotification(observer) {
    return this.rxStomp
      .watch(DESTINATION_TRACKER_NOTIFICATION)
      .pipe(map(message => JSON.parse(message.body)))
      .subscribe(observer);
  }

  public subscribeUpdateFeed(observer) {
    return this.rxStomp
      .watch(DESTINATION_TRACKER_UPDATE_FEED)
      .pipe(map(message => JSON.parse(message.body)))
      .subscribe(observer);
  }

  public subscribeUpdateConnectome(observer) {
    return this.rxStomp
      .watch(DESTINATION_TRACKER_UPDATE_CONNECTOME)
      .pipe(map(message => JSON.parse(message.body)))
      .subscribe(observer);
  }

  public subscribeSignalTracking(observer) {
    return this.rxStomp
      .watch(DESTINATION_TRACKER_ISSUE_TRACKING)
      .pipe(map(message => JSON.parse(message.body)))
      .subscribe(observer);
  }
}
