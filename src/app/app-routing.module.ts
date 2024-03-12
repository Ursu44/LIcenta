import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CursuriComponent } from './sharepage/cursuri/cursuri.component';
import { CatalogComponent } from './sharepage/catalog/catalog.component';
import { ForgotPopupComponent } from './sharepage/forgot-popup/forgot-popup.component';
import { CoursesComponent } from './sharepage/courses/courses.component';

const routes: Routes = [
  {path:'cursuri', component:CoursesComponent},
  {path:'catalog', component:CatalogComponent},
  {path:'curs', component:CursuriComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
