import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent {
    title = 'example-ng';

    private http = inject(HttpClient);

    click() {
        this.getWelcome()
            .subscribe(arg => { this.title = arg.title; console.log("empfangen:" + this.title) })
    }
    getWelcome(): Observable<Antwort> {
        console.log("getWelcome")

        return this.http.get<Antwort>(`http://localhost:8080/hello`);
    }

}

export interface Antwort {
    title: string;
}
