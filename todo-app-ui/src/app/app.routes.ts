import { Routes } from '@angular/router';
import { pathConstants } from './shared/path.constants';
import { AuthGuard } from './shared/guards/auth.guard';
import { inject } from '@angular/core';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: pathConstants.login,
    loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
    canActivate: [() => inject(AuthGuard).canLoginActivate()],
  },
  {
    path: pathConstants.signUp,
    loadChildren: () =>
      import('./sign-up/sign-up.module').then(m => m.SignUpModule),
  },
  {
    path: pathConstants.tasksOverview,
    loadChildren: () =>
      import('./tasks-overview/tasks-overview.module').then(
        m => m.TasksOverviewModule
      ),
    canActivate: [() => inject(AuthGuard).canActivate()],
  },
  {
    path: pathConstants.settings,
    loadChildren: () =>
      import('./settings/settings.module').then(m => m.SettingsModule),
    canActivate: [() => inject(AuthGuard).canActivate()],
  },
  {
    path: pathConstants.addEditTasks,
    loadChildren: () =>
      import('./add-edit-task/add-edit-task.module').then(
        m => m.AddEditTaskModule
      ),
    canActivate: [() => inject(AuthGuard).canActivate()],
  },
];
