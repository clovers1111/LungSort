import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectFile } from './select-file';

describe('SelectFile', () => {
  let component: SelectFile;
  let fixture: ComponentFixture<SelectFile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectFile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectFile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
