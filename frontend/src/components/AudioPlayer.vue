<template>
  <div :class="{player: true, hasSource: !!source}">
    <div>
      {{ title }}
    </div>
    <div
      class="slider seeker"
      @mousemove.stop="onMouseMove"
      @mousedown.stop="onMouseMove"
      ref="sliderRef">
      <div class="handle" :style="{width: `${progress * 100}%`}"/>
    </div>
    <audio ref="audioRef" :src="source" preload="auto">
    </audio>
    <q-btn
      flat
      dense
      round
      @click="togglePlayback"
      :icon="playing ? 'pause' : 'play_arrow'"
    />
    <div>
      {{ maxDuration ? `${formatDuration(curTime * 1000)} / ${formatDuration(maxDuration * 1000)}` : '' }}
    </div>
  </div>
</template>

<script setup lang="ts">
import {clamp} from 'lodash';
import {useQuasar} from 'quasar';
import {computed, onMounted, ref, watch} from 'vue';
import {usePlayerStore} from 'stores/player-store';
import formatDuration from 'format-duration';

const quasar = useQuasar();

const playerStore = usePlayerStore();
const source = computed(() => playerStore.src);
const title = computed(() => playerStore.title);
const playing = ref(false);

watch(source, () => {
  audioRef.value?.load();
  audioRef.value!.addEventListener('canplay', () => {
    maxDuration.value = audioRef.value?.duration ?? 0;
    audioRef.value!.play();
    playing.value = true;
  }, {
    once: true,
  });
  console.log(`new source: ${source.value}`);
})

const sliderRef = ref<any>();
const audioRef = ref<HTMLAudioElement>();

const progress = ref(0);
const curTime = ref(0);
const maxDuration = ref(0);

function seekTo(timestamp: number) {
  if (!audioRef.value) {
    return
  }
  audioRef.value.currentTime = timestamp;
  updateProgress();
}

function togglePlayback() {
  if (playing.value) {
    audioRef.value?.pause();
  } else {
    audioRef.value?.play();
  }
  playing.value = !playing.value;
}

function onMouseMove(e: MouseEvent) {
  if (e.buttons !== 1 && e.buttons !== 3) {
    return
  }
  const bounds = (e.target as HTMLDivElement).getBoundingClientRect();
  const maxDx = (sliderRef.value as HTMLDivElement).clientWidth;
  const clientX = e.clientX;
  const dx = clamp(clientX - bounds.left, 0, maxDx);
  const factor = dx / maxDx;
  console.log(dx, factor, maxDuration.value)
  seekTo(factor * maxDuration.value);
}

function updateProgress() {
  curTime.value = audioRef.value?.currentTime ?? 0;
  progress.value = curTime.value / maxDuration.value;
  console.log(progress.value)
}

onMounted(() => {
  setInterval(() => {
    updateProgress();
  }, 500)
})
</script>

<style lang="sass" scoped>
.player
  display: none
  width: 100%
  flex-direction: row
  justify-content: center
  align-items: center

  &.hasSource
    display: flex

audio
  display: none

.slider
  position: relative
  padding: 1.5em 0
  box-sizing: initial
  background: hsla(0, 0%, 100%, 0.2)
  background-clip: content-box
  height: 2px
  cursor: pointer
  margin: 0 16px
  flex-grow: 1

  &:not(.dragging) .handle
    transition: width .15s

  > .handle
    position: relative
    height: 2px
    max-width: 100%
    background: #7e57c2
    z-index: 1

  .handle:before
    content: ""
    position: absolute
    height: 16px
    width: 16px
    background: hsla(0, 0%, 100%, 0.2)
    border-radius: 50%
    right: -8px
    top: -7px
    transition: background .15s

  &:active .handle:before
    background: hsla(0, 0%, 100%, 0.5)

  .handle:after
    content: ""
    position: absolute
    height: 4px
    width: 4px
    background: #fff
    border-radius: 50%
    right: -2px
    top: -1px
</style>
