<template>
  <q-layout view="hHh lpR fFf" class="bg-grey-10">
    <q-header elevated class="bg-grey-10 text-white q-py-xs" height-hint="58">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          @click="toggleLeftDrawer"
          aria-label="Menu"
          icon="menu"
        />

        <q-btn flat no-caps no-wrap class="q-ml-xs" v-if="$q.screen.gt.xs" @click="router.push('/')">
          <q-avatar size="28px">
            <img :src="Logo" alt="logo"/>
          </q-avatar>
          <q-toolbar-title shrink class="text-weight-bold">
            Flow
          </q-toolbar-title>
        </q-btn>

        <q-space/>

        <div class="audio-player-container row no-wrap self-center">
          <AudioPlayer/>
        </div>

        <q-space/>

        <div class="q-gutter-sm row items-center no-wrap">
          <q-btn round dense flat color="grey-13" icon="notifications">
            <q-tooltip>Notifications</q-tooltip>
          </q-btn>
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      class="bg-grey-10"
      :width="240"
    >
      <q-scroll-area class="fit bg-gray-10">
        <q-list padding>
          <q-item
            v-for="link in userLinks"
            :key="link.text"
            @click="router.push(link.page)"
            v-ripple
            clickable>
            <q-item-section avatar>
              <q-icon color="gray-12" :name="link.icon"/>
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.text }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view v-slot="{ Component }">
        <component :is="Component"></component>
      </router-view>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';
import {useQuasar} from 'quasar';
import Logo from './assets/logo.jpg'
import {ref} from 'vue';
import AudioPlayer from 'components/AudioPlayer.vue';

const quasar = useQuasar();
const router = useRouter();

interface LinkItem {
  icon: string;
  text: string;
  page: string;
}

const userLinks = ref<LinkItem[]>([
  {icon: 'home', text: 'Home', page: '/'},
  {icon: 'collections', text: 'Albums', page: '/albums'},
  {icon: 'people', text: 'Artists', page: '/artists'},
]);

const leftDrawerOpen = ref(false)
const search = ref('')

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>

<style lang="sass">
.YL
  &__toolbar-input-container
    min-width: 100px
    width: 55%

  &__toolbar-input-btn
    border-radius: 0
    border-style: solid
    border-width: 1px 1px 1px 0
    border-color: rgba(0, 0, 0, .24)
    max-width: 60px
    width: 100%

  &__drawer-footer-link
    color: inherit
    text-decoration: none
    font-weight: 500
    font-size: .75rem

    &:hover
      color: #FFF

.audio-player-container
  width: 25%
  min-width: 200px
</style>
