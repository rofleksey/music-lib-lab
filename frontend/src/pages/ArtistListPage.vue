<template>
  <q-page class="full-width row items-start q-gutter-lg content-start justify-center" padding>
    <q-card
      class="artist-card"
      v-for="artist in artists"
      :key="artist.id"
      @click="router.push(`/artist?id=${artist.id}`)">
      <q-img
        :src="artist.thumbLink"
        :ratio="1"
      />
      <q-card-section>
        <div class="text-subtitle2 ellipsis">{{ artist.name }}</div>
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
import {Artist} from 'src/lib/api-types';
import {BASE_URL, fetchArtists} from 'src/lib/get-api';
import {showError} from 'src/lib/util';
import {useQuasar} from 'quasar';
import {useRouter} from 'vue-router';

const quasar = useQuasar();
const router = useRouter();

const dataLoading = ref(false);
const artists = ref<Artist[]>([]);
const page = ref(1);
const maxPages = ref(1);

watch(page, refreshData);

function refreshData() {
  dataLoading.value = true;
  fetchArtists(page.value - 1)
    .then((newData) => {
      console.log(newData);
      artists.value = newData.data.map((artist) => ({
        ...artist,
        thumbLink: `${BASE_URL}${artist.thumbLink}`,
      }));
      maxPages.value = Math.max(1, newData.maxPages)
    })
    .catch((e) => {
      showError('Failed to fetch artists', e);
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
.artist-card
  width: 200px
  cursor: pointer
  transition: opacity 0.3s ease

  &:hover
    opacity: 0.6

.flex-break
  flex: 1 0 100% !important
</style>
