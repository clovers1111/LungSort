import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImgSelect } from './img-select';

describe('ImgSelect', () => {
  let component: ImgSelect;
  let fixture: ComponentFixture<ImgSelect>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImgSelect]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImgSelect);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
