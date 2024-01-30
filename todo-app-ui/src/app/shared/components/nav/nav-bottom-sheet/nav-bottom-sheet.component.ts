import { Component, OnInit } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import {
  allNavLinks,
  NavLink,
  navLinkLogoutIndex,
} from '../../../path.constants';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { Logout } from '../../../redux/auth/auth.actions';
import { Store } from '@ngxs/store';

@Component({
  selector: 'app-nav-bottom-sheet',
  standalone: true,
  imports: [
    MatListModule,
    MatButtonModule,
    RouterLinkActive,
    TranslateModule,
    RouterLink,
  ],
  templateUrl: './nav-bottom-sheet.component.html',
  styleUrl: './nav-bottom-sheet.component.scss',
})
export class NavBottomSheetComponent implements OnInit {
  allNavLinksWithActions = allNavLinks;

  constructor(
    private _bottomSheetRef: MatBottomSheetRef<NavBottomSheetComponent>,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.allNavLinksWithActions = allNavLinks.map((value: NavLink) => {
      if (value.id === navLinkLogoutIndex) {
        value.onClick = () => this.store.dispatch(new Logout());
      }

      return value;
    });
  }

  openLink($event: MouseEvent) {
    this._bottomSheetRef.dismiss();
    $event.preventDefault();
  }
}
