import {RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('pages/HomePage.vue'),
  },

  {
    path: '/album',
    component: () => import('pages/AlbumPage.vue'),
  },

  {
    path: '/albums',
    component: () => import('pages/AlbumListPage.vue'),
  },

  {
    path: '/artist',
    component: () => import('pages/ArtistPage.vue'),
  },

  {
    path: '/artists',
    component: () => import('pages/ArtistListPage.vue'),
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
