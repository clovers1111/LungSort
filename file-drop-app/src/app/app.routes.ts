import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FileDrop } from './components/file-drop/file-drop';
import { SelectFile } from './components/select-file/select-file';

export const routes: Routes = [
    { path: '', component: FileDrop },
    { path: 'select-file', component: SelectFile },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule { }