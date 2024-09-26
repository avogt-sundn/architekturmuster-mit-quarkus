import { Component } from '@angular/core';
import { ClickHereComponent } from './click-here/click-here.component';
import { RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrl: './app.component.css',
    standalone: true,
    imports: [ClickHereComponent, RouterOutlet]
})
export class AppComponent {
  title = 'my-app';
}
