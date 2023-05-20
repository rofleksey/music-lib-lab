<template>
  <q-page class="full-width q-gutter-lg" padding>
    <q-toolbar class="bg-purple text-white shadow-2 rounded-borders">
      <q-btn flat :label="artist?.name ?? ''"/>
      <q-btn flat :label="`${artist?.albums?.length ?? 0} albums`"/>
      <q-btn flat :label="`${artist?.songs?.length ?? 0} songs`"/>
    </q-toolbar>
    <div class="text-h6">
      Albums
    </div>
    <div class="row q-gutter-md">
      <q-card
        class="entry-card"
        v-for="album in artist?.albums"
        :key="album.id"
        @click="router.push(`/album?id=${album.id}`)">
        <q-img
          :src="album.thumbLink"
          :ratio="1"
        />
        <q-card-section>
          <div class="text-subtitle2 ellipsis">{{ album.title }}</div>
          <q-rating
            v-model="album.rating"
            size="xs"
            icon="star_border"
            icon-selected="star"
            icon-half="star_half"
            :max="5"/>
        </q-card-section>
      </q-card>
    </div>
    <div class="text-h6">
      Songs
    </div>
    <div class="row q-gutter-md">
      <q-card
        class="entry-card"
        v-for="song in artist?.songs"
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
    </div>
  </q-page>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {Artist} from 'src/lib/api-types';
import {BASE_URL, fetchArtist} from 'src/lib/get-api';
import {showError} from 'src/lib/util';
import {useQuasar} from 'quasar';
import {useRoute, useRouter} from 'vue-router';
import {usePlayerStore} from 'stores/player-store';

const quasar = useQuasar();
const router = useRouter();
const route = useRoute();

const playerStore = usePlayerStore();

const artistId = computed(() => Number(route.query.id));

const dataLoading = ref(false);
const artist = ref<Artist>();

function refreshData() {
  dataLoading.value = true;
  fetchArtist(artistId.value)
    .then((newData) => {
      artist.value = {
        ...newData,
        thumbLink: `${BASE_URL}${newData.thumbLink}`,
        songs: newData.songs.map((song) => ({
          ...song,
          thumbLink: `${BASE_URL}${song.thumbLink}`,
          audioLink: `${BASE_URL}${song.audioLink}`,
          rating: song.rating / 20,
        })),
        albums: newData.albums.map((album) => ({
          ...album,
          thumbLink: `${BASE_URL}${album.thumbLink}`,
          rating: album.rating / 20,
        }))
      }
    })
    .catch((e) => {
      showError('Failed to fetch artist', e);
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
.entry-card
  width: 200px
  cursor: pointer
  transition: opacity 0.3s ease

  &:hover
    opacity: 0.6

.flex-break
  flex: 1 0 100% !important
</style>
