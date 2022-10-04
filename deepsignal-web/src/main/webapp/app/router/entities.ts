import { Authority } from '@/shared/security/authority';
import FrequentlyAskedQuestions from '@/entities/help/faq/faq/faq.vue';
import faqRoute from '@/entities/help/faq/faq.route';
import QnaComponent from '@/entities/help/question-and-answer/qna.vue';
import NoticeList from '@/entities/help/notice/noticeList/noticeList.vue';
import NoticeDetail from '@/entities/help/notice/noticeDetail/noticeDetail.vue';
import LearningSocial from '@/entities/connectome/learning-management/learningSocial/leaningSocial.vue';
import LearningWeb from '@/entities/connectome/learning-management/learningWeb/learningWeb.vue';

/* tslint:disable */
// prettier-ignore
const Upload = () => import('@/entities/upload/upload.vue');
const ConnectomeAnalyser = () => import('@/entities/connectome/toolbox/connectome-analyzer/connectome-analyzer.vue');
const KeywordAnalyser = () => import('@/entities/connectome/toolbox/keyword-analyzer/keyword-analyzer.vue');
const ConnectomeSearch = () => import('@/entities/connectome/search/search-connectome.vue');
const MetaSearch = () => import('@/entities/connectome/meta-search/meta-search.vue');
const LearningDoc = () => import('@/entities/connectome/learning-management/learningDoc/learningDoc.vue');
const MyAi = () => import('@/entities/my-ai/my-ai.vue');
const LearningCenter = () => import('@/entities/my-ai/learning-center/learning-center/learning-center.vue');
const ConnectomeMap = () => import('@/entities/my-ai/connectome/connectome-map-v2.vue');
const Map2dNetwork = () => import('@/entities/my-ai/connectome/map-2d-network/map-2d-network.vue');
const Map3dNetwork = () => import('@/entities/my-ai/connectome/map-3d-network/map-3d-network.vue');
const ConnectomeBuilder = () => import('@/entities/my-ai/connectome-builder/builder-map/builder-map.vue');
const MiniConnectomeMap = () => import('@/entities/my-ai/mini-connectome/mini-connectome-map.vue');
const MiniMap2dNetwork = () => import('@/entities/my-ai/mini-connectome/mini-map-2d-network/mini-map-2d-network.vue');
export default [
  {
    path: '/upload',
    name: 'Upload',
    component: Upload,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/myconnectome/toolbox/keywords',
    name: 'DsKeywordAnalyser',
    component: KeywordAnalyser,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/myconnectome/toolbox/analyzer',
    name: 'DsConnectomeAnalyser',
    component: ConnectomeAnalyser,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/myconnectome/search',
    name: 'DsConnectomeSearch',
    component: ConnectomeSearch,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/search',
    name: 'DsSearch',
    component: MetaSearch,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/faq',
    name: 'Frequently Asked Questions',
    component: FrequentlyAskedQuestions,
    children: [{ path: '/faq', redirect: 'privacy' }, ...faqRoute],
  },
  {
    path: '/qna',
    name: 'Question And Answer',
    component: QnaComponent,
  },
  {
    path: '/notice',
    name: 'Notice',
    component: NoticeList,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/notice/:id',
    name: 'Notice-Detail',
    component: NoticeDetail,
    meta: { authorities: [Authority.USER] },
  },
  // {
  //   path: '/myconnectome/learning-management',
  //   redirect: '/myconnectome/learning-management/learning-doc',
  // },
  {
    path: '/myconnectome/learning-management/learning-doc',
    name: 'Learning-Document',
    component: LearningDoc,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/myconnectome/learning-management/learning-social',
    name: 'Learning-Social',
    component: LearningSocial,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/myconnectome/learning-management/learning-web',
    name: 'Learning-Web',
    component: LearningWeb,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/my-ai',
    name: 'MyAi',
    component: MyAi,
    children: [
      {
        path: '/my-ai/connectome',
        name: 'Connectome',
        component: ConnectomeMap,
        children: [
          {
            path: '/my-ai/connectome/2dnetwork',
            name: '2DNetwork',
            component: Map2dNetwork,
          },
          {
            path: '/my-ai/connectome/3dnetwork',
            name: '3DNetwork',
            component: Map3dNetwork,
          },
          {
            path: '/my-ai/connectome/builder',
            name: 'Builder',
            component: ConnectomeBuilder,
          },
        ],
        redirect: to => {
          return '/my-ai/connectome/2dnetwork';
        },
      },
      {
        path: '/my-ai/learning-center',
        name: 'Learning-Center',
        component: LearningCenter,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: '/my-ai/mini-connectome',
        name: 'MiniConnectome',
        component: MiniConnectomeMap,
        children: [
          {
            path: '/my-ai/mini-connectome/mini-2dnetwork',
            name: 'Mini2DNetwork',
            component: MiniMap2dNetwork,
          },
        ],
        redirect: to => {
          return '/my-ai/mini-connectome/mini-2dnetwork';
        },
      },
    ],
    redirect: to => {
      return '/my-ai/learning-center';
    },
    meta: { authorities: [Authority.USER] },
  },
];
