import axios from 'axios';
import {Album, Artist, PageResponse, Song} from 'src/lib/api-types';

axios.defaults.timeout = 10000;

export const BASE_URL = import.meta.env.VITE_BASE_URL

export async function fetchSongs(page: number): Promise<PageResponse<Song>> {
  const {data}: { data: PageResponse<Song> } = await axios.get(
    `${BASE_URL}/api/song`,
    {
      params: {
        page
      },
      withCredentials: true,
    }
  );
  return data;
}

export async function fetchAlbums(page: number): Promise<PageResponse<Album>> {
  const {data}: { data: PageResponse<Album> } = await axios.get(
    `${BASE_URL}/api/album`,
    {
      params: {
        page
      },
      withCredentials: true,
    }
  );
  return data;
}

export async function fetchArtists(page: number): Promise<PageResponse<Artist>> {
  const {data}: { data: PageResponse<Artist> } = await axios.get(
    `${BASE_URL}/api/artist`,
    {
      params: {
        page
      },
      withCredentials: true,
    }
  );
  return data;
}

export async function fetchAlbum(id: number): Promise<Album> {
  const {data}: { data: Album } = await axios.get(
    `${BASE_URL}/api/album/${id}`,
    {
      withCredentials: true,
    }
  );
  return data;
}

export async function fetchArtist(id: number): Promise<Artist> {
  const {data}: { data: Artist } = await axios.get(
    `${BASE_URL}/api/artist/${id}`,
    {
      withCredentials: true,
    }
  );
  return data;
}
