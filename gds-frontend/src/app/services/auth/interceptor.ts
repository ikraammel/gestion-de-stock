import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoaderService } from '../../loader/service/loader-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Injecter le service loader
  const loaderService = inject(LoaderService);

  // 2. Afficher le loader au démarrage de la requête
  loaderService.show();

  const token = localStorage.getItem('accessToken');

  // Logique existante pour le token
  if (
    req.url.includes('/gestiondestock/v1/auth/register') ||
    req.url.includes('/gestiondestock/v1/auth/authenticate') ||
    req.url.includes('/gestiondestock/v1/entreprise')
  ) {
    // On doit quand même cacher le loader si on sort ici par un "return"
    // mais on va plutôt utiliser finalize sur le flux next(req)
  }

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // 3. Utiliser l'opérateur "finalize" pour cacher le loader
  // finalize s'exécute quand la requête se termine (Succès OU Erreur)
  return next(req).pipe(
    finalize(() => loaderService.hide())
  );
};