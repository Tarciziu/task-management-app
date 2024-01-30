import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../base/base.component';
import {
  allNavLinks,
  NavLink,
  navLinkLogoutIndex,
  pathConstants,
} from '../../path.constants';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { BreakpointObserver, BreakpointState } from '@angular/cdk/layout';
import { map } from 'rxjs';
import {
  MatBottomSheet,
  MatBottomSheetModule,
} from '@angular/material/bottom-sheet';
import { NavBottomSheetComponent } from './nav-bottom-sheet/nav-bottom-sheet.component';
import { MatListModule } from '@angular/material/list';
import { Store } from '@ngxs/store';
import { Logout } from '../../redux/auth/auth.actions';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    TranslateModule,

    // material modules
    MatTooltipModule,
    MatToolbarModule,
    MatButtonModule,
    MatBottomSheetModule,
    MatListModule,
  ],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.scss',
})
export class NavComponent extends BaseComponent implements OnInit {
  // check this: https://material.angular.io/cdk/layout/overview
  private readonly XSMALL_MEDIA_QUERY = '(max-width: 599px)';

  isXSmallDevice!: boolean;
  allNavLinksWithActions = allNavLinks;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private _bottomSheet: MatBottomSheet,
    private store: Store
  ) {
    super();
  }

  ngOnInit(): void {
    this.breakpointObserver
      .observe([this.XSMALL_MEDIA_QUERY])
      .pipe(map((result: BreakpointState) => result.matches))
      .subscribe(value => (this.isXSmallDevice = value));

    this.allNavLinksWithActions = allNavLinks.map((value: NavLink) => {
      if (value.id === navLinkLogoutIndex) {
        value.onClick = () => this.store.dispatch(new Logout());
      }

      return value;
    });
  }

  onShowMenuClicked() {
    this._bottomSheet.open(NavBottomSheetComponent, {
      panelClass: 'rounded-bottom-sheet',
    });
  }

  protected readonly pathConstants = pathConstants;
}
