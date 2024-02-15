import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CursuriComponent } from './sharepage/cursuri/cursuri.component';

const routes: Routes = [
  {path:'cursuri', component:CursuriComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
