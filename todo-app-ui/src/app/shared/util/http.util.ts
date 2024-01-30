import { HttpParams } from '@angular/common/http';

export class HttpUtil {
  private constructor() {
    throw new Error('Utility class, cannot be instantiated');
  }

  public static buildUrlParams(filters?: object): HttpParams {
    let urlSearchParam: HttpParams = new HttpParams();

    if (filters) {
      Object.entries(filters).forEach(([key, val]) => {
        if (val !== null && val !== undefined) {
          urlSearchParam = urlSearchParam.set(key, val);
        }
      });
    }
    return urlSearchParam;
  }
}
