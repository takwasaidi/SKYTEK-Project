import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
// Dans ton component Angular, par exemple :
scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

subscribeNewsletter() {
  // Implémente ici ta logique d’abonnement newsletter
  alert('Merci pour votre abonnement !');
}

}
