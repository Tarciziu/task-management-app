export const pathConstants = {
  home: '/',
  notFound: '/404',

  login: 'login',
  signUp: 'sign-up',
  tasksOverview: 'overview',
  settings: 'settings',
  addEditTasks: 'tasks',
};

export const defaultPath = `/${pathConstants.tasksOverview}`;

export interface NavLink {
  id: number;
  routerLink?: string;
  matIcon: string;
  labelKey: string;
  shouldShow: () => boolean;
  onClick?: () => void;
}

export const allNavLinks: NavLink[] = [
  {
    id: 0,
    routerLink: '/' + pathConstants.tasksOverview,
    matIcon: 'task',
    labelKey: 'header.nav.tasksOverview',
    shouldShow: () => true,
  },
  {
    id: 1,
    routerLink: '/' + pathConstants.settings,
    matIcon: 'settings',
    labelKey: 'header.nav.settings',
    shouldShow: () => true,
  },
  {
    id: 2,
    matIcon: 'logout',
    labelKey: 'header.nav.logout',
    shouldShow: () => true,
  },
];

export const navLinkLogoutIndex = allNavLinks.length - 1;
