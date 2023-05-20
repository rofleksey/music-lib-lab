export interface Song {
  id: number;
  title: string;
  audioLink: string;
  thumbLink: string;
  rating: number;
  albumId: number;
  artistId: number;
}

export interface Album {
  id: number;
  title: string;
  thumbLink: string;
  rating: number;
  songs: Song[];
}

export interface Artist {
  id: number;
  name: string;
  thumbLink: string;
  rating: number;
  songs: Song[];
  albums: Album[];
}

export interface PageResponse<T> {
  data: T[];
  maxPages: number;
}
