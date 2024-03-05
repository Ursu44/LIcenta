import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CursuriComponent } from './sharepage/cursuri/cursuri.component';
import { CatalogComponent } from './sharepage/catalog/catalog.component';
import { ForgotPopupComponent } from './sharepage/forgot-popup/forgot-popup.component';

const routes: Routes = [
  {path:'cursuri', component:CursuriComponent},
  {path:'catalog', component:CatalogComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
