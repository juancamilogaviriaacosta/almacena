import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'semicolonBreak'
})
export class SemicolonBreakPipe implements PipeTransform {
  transform(value: string): string {
    return value ? value.replace(/;/g, '<br>') : '';
  }
}