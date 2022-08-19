import Vue from 'vue';
import Component from 'vue-class-component';
Component.registerHooks([
  'beforeRouteEnter',
  'beforeRouteLeave',
  'beforeRouteUpdate', // for vue-router 2.2+
]);
import Router from 'vue-router';

const Home = () => import('@/core/home/home.vue');
const DsFeed = () => import('@/core/home/feed/ds-feed.vue');
const DsFeedDetail = () => import('@/core/home/detail/ds-feed-detail.vue');
const DsPeopleDetail = () => import('@/core/home/detail/ds-people-detail.vue');
const DsPeople = () => import('@/core/home/people/ds-people.vue');
const DsSignals = () => import('@/core/home/signals/ds-signals.vue');
const DsIntelligence = () => import('@/core/home/intelligence/ds-intelligence.vue');
const Error = () => import('@/core/error/error.vue');
const qrCodeCheck = () => import('@/qrCode/qrCodeCheck.vue');
const Notifications = () => import('@/core/notification/notifications/notifications.vue');
import account from '@/router/account';
import admin from '@/router/admin';
import entities from '@/router/entities';
import pages from '@/router/pages';
import { Authority } from '@/shared/security/authority';

Vue.use(Router);

// prettier-ignore
export default new Router({
  mode: 'history',
  scrollBehavior (to, from, savedPosition) {
    // if (to.hash) {
    //   return new Promise(resolve => {
    //     setTimeout(() => {
    //       resolve({ selector: to.hash });
    //     }, 2000);
    //   });
    // }
    return { x: 0, y: 0 }
  },
  routes: [
    {
      path: '/',
      // name: 'Home',
      component: Home,
      children: [
        { path: '/', redirect: 'feed' },
        {
          name: 'Feed',
          path: 'feed',
          component: DsFeed
        },
        {
          name: 'People',
          path: 'people',
          component: DsPeople,
          meta: { authorities: [Authority.USER] }
        },
        {
          name: 'Signals',
          path: 'signals',
          component: DsSignals,
          meta: { authorities: [Authority.USER] }
        },
        {
          name: 'Intelligence',
          path: 'intelligence',
          component: DsIntelligence,
          meta: { authorities: [Authority.USER] }
        },
        {
          name: 'Detail',
          path: 'detail-feed/:connectomeId/:feedId',
          component: DsFeedDetail,
          props: true,
          meta: { authorities: [Authority.USER] }
        },
        {
          name: 'People Detail',
          path: 'peopleDetail',
          component: DsPeopleDetail,
          props: true,
          meta: { authorities: [Authority.USER] }
        },
        {
          name: 'Notifications',
          path: 'notifications',
          component: Notifications,
          meta: { authorities: [Authority.USER] }
        }
      ]
    },
    {
      path: '/forbidden',
      name: 'Forbidden',
      component: Error,
      meta: { error403: true }
    },
    {
      path: '/not-found',
      name: 'NotFound',
      component: Error,
      meta: { error404: true }
    },
    {
      path: '/qrCodeCheck',
      name: 'QrCodeCheck',
      component: qrCodeCheck,
    },
    ...account,
    ...admin,
    ...entities,
    ...pages
  ]
});
