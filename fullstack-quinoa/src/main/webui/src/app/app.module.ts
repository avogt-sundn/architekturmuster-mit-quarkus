import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { provideHttpClient } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ClickHereComponent } from './click-here/click-here.component';

@NgModule({
    declarations: [
        AppComponent,
        ClickHereComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule
    ],
    providers: [provideHttpClient()
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
