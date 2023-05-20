import {defineStore} from 'pinia';
import {ref} from 'vue';

export const usePlayerStore = defineStore('player', () => {
  const src = ref<string>('');
  const title = ref<string>('');

  function play(newTitle: string, newSource: string) {
    title.value = newTitle;
    src.value = newSource;
  }

  return {title, src, play}
})
