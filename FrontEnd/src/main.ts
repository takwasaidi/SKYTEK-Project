
// src/polyfills.ts
(window as any).global = window;
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { register } from 'swiper/element/bundle';

register(); // À faire UNE seule fois


platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
