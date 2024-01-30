export interface PagingRequest {
  page: number;
  pageSize: number;
}

export interface PagingMetadata {
  totalPages: number;
  totalItems: number;
}

export interface PagingResponse<T> {
  metadata: PagingMetadata;
  data: T[];
}
