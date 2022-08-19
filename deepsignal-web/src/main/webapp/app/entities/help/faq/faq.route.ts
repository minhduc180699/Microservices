import FAQPrivacy from '@/entities/help/faq/faq-privacy/faq-privacy.vue';
import FaqLogin from '@/entities/help/faq/faq-login/faq-login.vue';
import FaqJoin from '@/entities/help/faq/faq-join/faq-join.vue';

export default [
  {
    path: '/faq/privacy',
    name: 'Frequently Asked Questions Privacy',
    component: FAQPrivacy,
  },
  {
    path: '/faq/login',
    name: 'Frequently Asked Questions Login',
    component: FaqLogin,
  },
  {
    path: '/faq/join',
    name: 'Frequently Asked Questions Join And Unsubscribe',
    component: FaqJoin,
  },
];
