import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './sharepage/navbar/navbar.component';
import { FooterComponent } from './sharepage/footer/footer.component';
import { HomeComponent } from './sharepage/home/home.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CursuriComponent } from './sharepage/cursuri/cursuri.component';
import { CatalogComponent } from './sharepage/catalog/catalog.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PopupComponent } from './sharepage/popup/popup.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ForgotPopupComponent } from './sharepage/forgot-popup/forgot-popup.component';
import { VerifyCodComponent } from './sharepage/verify-cod/verify-cod.component';
import { PassResetComponent } from './sharepage/pass-reset/pass-reset.component';
import { CoursesComponent } from './sharepage/courses/courses.component';
import { TestComponent } from './sharepage/test/test.component';
import {Chart} from 'chart.js';
import { GraficComponent } from './sharepage/grafic/grafic.component';
import { StatisticiComponent } from './sharepage/statistici/statistici.component';

@NgModule({ 
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    HomeComponent,
    CursuriComponent,
    CatalogComponent,
    PopupComponent,
    ForgotPopupComponent,
    VerifyCodComponent,
    PassResetComponent,
    CoursesComponent ,
    TestComponent,
    GraficComponent,
    StatisticiComponent 
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatDialogModule,
      ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { 
  constructo(){
    localStorage.clear();
  }

}
