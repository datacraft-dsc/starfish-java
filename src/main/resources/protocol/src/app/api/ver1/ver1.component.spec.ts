import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Ver1Component } from './ver1.component';

describe('Ver1Component', () => {
  let component: Ver1Component;
  let fixture: ComponentFixture<Ver1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Ver1Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Ver1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
