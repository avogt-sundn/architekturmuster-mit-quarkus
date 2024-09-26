import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-click-here',
    styleUrl: './click-here.component.css',
    standalone: true,
    template:
        `
        <div>
        <p> Willkommen, {{ greeting }} </p>
                <button (click)="click()" >Knopf</button>
        <div>
            `
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
