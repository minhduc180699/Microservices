import { Authority } from '@/shared/security/authority';

const DsUserManagementComponent = () => import('@/admin/user-management/user-management.vue');
const DsUserManagementViewComponent = () => import('@/admin/user-management/user-management-view.vue');
const DsUserManagementEditComponent = () => import('@/admin/user-management/user-management-edit.vue');
const DsDocsComponent = () => import('@/admin/docs/docs.vue');
const DsConfigurationComponent = () => import('@/admin/configuration/configuration.vue');
const DsHealthComponent = () => import('@/admin/health/health.vue');
const DsLogsComponent = () => import('@/admin/logs/logs.vue');
const DsMetricsComponent = () => import('@/admin/metrics/metrics.vue');

export default [
  {
    path: '/admin/user-management',
    name: 'DsUser',
    component: DsUserManagementComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/user-management/new',
    name: 'DsUserCreate',
    component: DsUserManagementEditComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/user-management/:userId/edit',
    name: 'DsUserEdit',
    component: DsUserManagementEditComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/user-management/:userId/view',
    name: 'DsUserView',
    component: DsUserManagementViewComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/docs',
    name: 'DsDocsComponent',
    component: DsDocsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/health',
    name: 'DsHealthComponent',
    component: DsHealthComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/logs',
    name: 'DsLogsComponent',
    component: DsLogsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/metrics',
    name: 'DsMetricsComponent',
    component: DsMetricsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/configuration',
    name: 'DsConfigurationComponent',
    component: DsConfigurationComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
];
