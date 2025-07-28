import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Salle } from 'src/app/models/Salle';
import { Injectable } from '@angular/core';
import { FileHandle } from '../models/file-handle';

@Injectable({
  providedIn: 'root'
})
export class ImageProcessingService {

  constructor(private sanitizer:DomSanitizer) { }

  public createImages(salle: Salle){
  const salleImages: any[] =  salle.salleImages;
  const salleImagesToFileHandle: FileHandle[] = [];
  for(let i=0 ; i< salleImages.length; i++)
      {
       const imageFileData =  salleImages[i] ; 
       const imageBlob = this.dataURItoBlob(imageFileData.picByte,imageFileData.type)
       const  imageFile = new  File([imageBlob], imageFileData.name, {type: imageFileData.type})
       const finalFileHandele : FileHandle = {
         file : imageFile,
         url: this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(imageFile))
       };
       salleImagesToFileHandle.push(finalFileHandele)
      }
      salle.salleImages = salleImagesToFileHandle;
      return salle;
  }

  public dataURItoBlob(picBytes: string, imageType: string){

   const byteString =  window.atob(picBytes);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer)
     for(let i=0 ; i< byteString.length; i++)
     {
     int8Array[i] = byteString.charCodeAt(i)
     }
     const blob = new Blob([int8Array],{type: imageType});
     return blob;
  }

  
}
