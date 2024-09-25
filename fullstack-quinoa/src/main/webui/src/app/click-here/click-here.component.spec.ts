import { ComponentFixture, TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { ClickHereComponent } from './click-here.component';

describe('ClickHereComponent', () => {
  let component: ClickHereComponent;
  let fixture: ComponentFixture<ClickHereComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
    imports: [ClickHereComponent],
    providers: [provideHttpClient()]
})
    .compileComponents();

    fixture = TestBed.createComponent(ClickHereComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
