import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-click-here',
    templateUrl: './click-here.component.html',
    styleUrl: './click-here.component.css'
})

export class ClickHereComponent {
    private http = inject(HttpClient);
    greeting = "(Bitte Knopf drücken)";

    click() {
        this.getWelcome()
            .subscribe(arg => { this.greeting = arg.title; console.log("empfangen:" + this.greeting) })
    }
    getWelcome(): Observable<Antwort> {

        console.log("getWelcome")
        return this.http.get<Antwort>(`http://localhost:8080/hello`);
    }

}


export interface Antwort {
    title: string;
}
