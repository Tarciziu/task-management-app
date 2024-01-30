import { Component, OnDestroy } from '@angular/core';
import {
  combineLatest,
  filter,
  Observable,
  ObservableInput,
  Subject,
  Subscription,
  takeUntil,
} from 'rxjs';

@Component({
  selector: 'app-base-component',
  template: '',
})
export class BaseComponent implements OnDestroy {
  public unsubscribe$: Subject<void> = new Subject<void>();

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  protected subscribeToDefined<T>(
    observable$: Observable<T>,
    subscribeCallback: (result: T) => void,
    errorCallback?: (error: unknown) => void
  ): Subscription {
    return observable$
      .pipe(
        filter(value => value !== null && value !== undefined),
        takeUntil(this.unsubscribe$)
      )
      .subscribe({
        next: subscribeCallback,
        error: errorCallback,
      });
  }

  protected subscribeToDefinedWithoutCancel<T>(
    observable$: Observable<T>,
    subscribeCallback: (result: T) => void,
    errorCallback?: (error: unknown) => void
  ): Subscription {
    return observable$
      .pipe(filter(value => value !== null && value !== undefined))
      .subscribe({
        next: subscribeCallback,
        error: errorCallback,
      });
  }

  protected subscribeToTrue(
    observable$: Observable<boolean>,
    subscribeCallback: (result: boolean) => void,
    errorCallback?: (error: unknown) => void
  ): Subscription {
    return observable$
      .pipe(
        filter(value => value),
        takeUntil(this.unsubscribe$)
      )
      .subscribe({
        next: subscribeCallback,
        error: errorCallback,
      });
  }

  protected subscribeToCombinedLatest<T extends ObservableInput<any>>(
    observables$: T[],
    subscribeCallback: (result: any[]) => void,
    errorCallback?: (error: any) => void
  ): Subscription {
    return combineLatest(observables$)
      .pipe(
        filter((values: any[]) =>
          values.every(value => value !== null && value !== undefined)
        ),
        takeUntil(this.unsubscribe$)
      )
      .subscribe({
        next: subscribeCallback,
        error: errorCallback,
      });
  }
}
