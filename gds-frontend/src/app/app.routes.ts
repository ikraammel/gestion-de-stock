import { Routes } from '@angular/router';
import { PageLogin } from './page-login/page-login';
import { PageInscription } from './page-inscription/page-inscription';
import { PageDashboard } from './page-dashboard/page-dashboard';
import { PagesStatistiques } from './pages-statistiques/pages-statistiques';
import { PageArticle } from './articles/page-article/page-article';
import { NouvelArticle } from './articles/nouvel-article/nouvel-article';
import { MvmtStock } from './mvmt-stock/mvmt-stock';
import { PageClient } from './client/page-client/page-client';
import { PageFournisseurs } from './fournisseurs/page-fournisseurs/page-fournisseurs';
import { NouveauCltFrs } from './nouveau-clt-frs/nouveau-clt-frs';
import { DetailCmdCltFrs } from './detail-cmd-clt-frs/detail-cmd-clt-frs';
import { PageCmdCltFrs } from './page-cmd-clt-frs/page-cmd-clt-frs';
import { NouvelleCommandeCltFrs } from './nouvelle-commande-clt-frs/nouvelle-commande-clt-frs';
import { PageCategories } from './categories/page-categories/page-categories';
import { NouvelleCategorie } from './categories/nouvelle-categorie/nouvelle-categorie';
import { PageUtilisateurs } from './utilisateur/page-utilisateurs/page-utilisateurs';
import { NouvelUtilisateur } from './utilisateur/nouvel-utilisateur/nouvel-utilisateur';
import { PageProfil } from './profil/page-profil/page-profil';
import { ChangerMotDePasse } from './profil/changer-mot-de-passe/changer-mot-de-passe';
import { ApplicationGuard } from './services/guard/application-guard';

export const routes: Routes = [
    {
    path: 'login',
    component: PageLogin
    },{
        path: 'register',
        component: PageInscription
    },
    {
        path:'',
        component: PageDashboard,
        children:[
            {
                path: 'statistiques',
                component: PagesStatistiques,
                canActivate: [ApplicationGuard]
            },{
                path: 'articles',
                component: PageArticle,
                canActivate: [ApplicationGuard]
            },{
                path: 'nouvel-article',
                component: NouvelArticle,
                canActivate: [ApplicationGuard]
            },{
                path: 'nouvel-article/:articleId',
                component: NouvelArticle,
                canActivate: [ApplicationGuard]
            },{
                path: 'mouvement-stock',
                component: MvmtStock,
                canActivate: [ApplicationGuard]
            },{
                path: 'clients',
                component: PageClient,
                canActivate: [ApplicationGuard]
            },{
                path: 'fournisseurs',
                component: PageFournisseurs,
                canActivate: [ApplicationGuard]
            },{
                path: 'nouveauclient',
                component: NouveauCltFrs,
                data:{
                    origin:'client'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'nouveauclient/:id',
                component: NouveauCltFrs,
                data:{
                    origin:'client'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'nouveaufournisseur',
                component: NouveauCltFrs,
                data:{
                    origin:'fournisseur'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'nouveaufournisseur/:id',
                component: NouveauCltFrs,
                data:{
                    origin:'fournisseur'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'commandeclient',
                component: PageCmdCltFrs,
                data:{
                    origin:'client'
                }
            },{
                path: 'commandefournisseur',
                component: PageCmdCltFrs,
                data:{
                    origin:'fournisseur'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'nouvelle-commande-client',
                component: NouvelleCommandeCltFrs,
                data:{
                    origin:'client'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'nouvelle-commande-fournisseur',
                component: NouvelleCommandeCltFrs,
                data:{
                    origin:'fournisseur'
                },
                canActivate: [ApplicationGuard]
            },{
                path: 'categories',
                component: PageCategories,
                canActivate: [ApplicationGuard]
            },{
                path: 'nouvelle-categorie',
                component: NouvelleCategorie,
                canActivate: [ApplicationGuard]
            },
            {
                path: 'nouvelle-categorie/:categoryId',
                component: NouvelleCategorie,
                canActivate: [ApplicationGuard]
            },{
                path: 'utilisateurs',
                component: PageUtilisateurs,
                canActivate: [ApplicationGuard]
            },{
                path: 'nouvel-utilisateur',
                component: NouvelUtilisateur,
                canActivate: [ApplicationGuard]
            },{
                path: 'profil',
                component: PageProfil,
                canActivate: [ApplicationGuard]
            },{
                path: 'changer-mot-de-passe',
                component: ChangerMotDePasse,
                canActivate: [ApplicationGuard]
            }
        ]
    }
];
