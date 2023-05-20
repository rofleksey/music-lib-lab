<template>
  <q-page class="full-width row items-start q-gutter-lg content-start justify-center" padding>
    <q-card
      class="song-card"
      v-for="song in songs"
      :key="song.id"
      @click="playerStore.play(song.title, song.audioLink)">
      <q-img
        :src="song.thumbLink"
        :ratio="1"
      />
      <q-card-section>
        <div class="text-subtitle2 ellipsis">{{ song.title }}</div>
        <q-rating
          v-model="song.rating"
          size="xs"
          icon="star_border"
          icon-selected="star"
          icon-half="star_half"
          :max="5"/>
      </q-card-section>
    </q-card>
    <div class="flex-break"></div>
    <q-pagination
      v-if="maxPages > 0"
      v-model="page"
      color="purple"
      :max="maxPages"
      :max-pages="6"
      :disable="dataLoading"
      boundary-numbers
      direction-links
    />
  </q-page>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import {Song} from 'src/lib/api-types';
import {BASE_URL, fetchSongs} from 'src/lib/get-api';
import {showError} from 'src/lib/util';
import {useQuasar} from 'quasar';
import {useRouter} from 'vue-router';
import {usePlayerStore} from 'stores/player-store';

const quasar = useQuasar();
const router = useRouter();

const playerStore = usePlayerStore();

const dataLoading = ref(false);
const songs = ref<Song[]>([]);
const page = ref(1);
const maxPages = ref(1);

watch(page, refreshData);

function refreshData() {
  dataLoading.value = true;
  fetchSongs(page.value - 1)
    .then((newData) => {
      console.log(newData);
      songs.value = newData.data.map((song) => ({
        ...song,
        thumbLink: `${BASE_URL}${song.thumbLink}`,
        audioLink: `${BASE_URL}${song.audioLink}`,
        rating: song.rating / 20,
      }));
      maxPages.value = Math.max(1, newData.maxPages)
    })
    .catch((e) => {
      showError('Failed to fetch songs', e);
    })
    .finally(() => {
      dataLoading.value = false;
    });
}

onMounted(() => {
  refreshData();
})
</script>

<style lang="sass" scoped>
.song-card
  width: 200px
  cursor: pointer
  transition: opacity 0.3s ease

  &:hover
    opacity: 0.6

.flex-break
  flex: 1 0 100% !important
</style>
