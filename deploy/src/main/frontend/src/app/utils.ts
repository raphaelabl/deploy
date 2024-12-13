import { Injectable } from '@angular/core';

export class PersistenceUtils {
  static isIdPersisted(id: number): boolean {
    return id !== null &&  id !== undefined && id !== 0;
  }

  static isEntityPersisted(entity: any): boolean {
    if(entity && 'id' in entity){
      return this.isIdPersisted(entity.id);
    }
    return false;
  }

  static isNullOrUndefined(entity: any): boolean {
    return entity === null || entity === undefined;
  }

}
