<template>
  <q-page class="full-width row items-start q-gutter-lg content-start justify-center" padding>
    <q-toolbar class="bg-purple text-white shadow-2 rounded-borders">
      <q-btn flat :label="album?.title ?? ''"/>
      <q-rating
        :model-value="album?.rating / 20 ?? 5"
        size="xs"
        icon="star_border"
        icon-selected="star"
        icon-half="star_half"
        :max="5"/>
    </q-toolbar>
    <q-card
      class="album-card"
      v-for="song in album?.songs"
      :key="song.id"
      @click="playerStore.play(song.title, song.audioLink)">
      <q-img
        :src="song.thumbLink"
        :ratio="1"
      />
      <q-card-section>
        <div class="text-subtitle2 ellipsis">{{ song.title }}</div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {Album} from 'src/lib/api-types';
import {BASE_URL, fetchAlbum} from 'src/lib/get-api';
import {showError} from 'src/lib/util';
import {useQuasar} from 'quasar';
import {useRoute, useRouter} from 'vue-router';
import {usePlayerStore} from 'stores/player-store';

const quasar = useQuasar();
const router = useRouter();
const route = useRoute();

const playerStore = usePlayerStore();

const albumId = computed(() => Number(route.query.id));

const dataLoading = ref(false);
const album = ref<Album>();

function refreshData() {
  dataLoading.value = true;
  fetchAlbum(albumId.value)
    .then((newData) => {
      album.value = {
        ...newData,
        thumbLink: `${BASE_URL}${newData.thumbLink}`,
        songs: newData.songs.map((song) => ({
          ...song,
          thumbLink: `${BASE_URL}${song.thumbLink}`,
          audioLink: `${BASE_URL}${song.audioLink}`,
          rating: song.rating / 20,
        }))
      }
    })
    .catch((e) => {
      showError('Failed to fetch album', e);
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
.album-card
  width: 200px
  cursor: pointer
  transition: opacity 0.3s ease

  &:hover
    opacity: 0.6

.flex-break
  flex: 1 0 100% !important
</style>
