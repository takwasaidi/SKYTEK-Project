import { Component, OnInit } from '@angular/core';
import { ToastService } from 'src/app/services/toast-service.service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit {
  message: string | null = null;
  timeoutId: any;

  constructor(private toastService: ToastService) {}

  ngOnInit() {
    this.toastService.toastState.subscribe(msg => {
      this.message = msg;
      clearTimeout(this.timeoutId);
      this.timeoutId = setTimeout(() => this.message = null, 5000); // disparait après 5s
    });
  }
}