# Étape 1 : Build de l'application Angular
FROM node:18-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install -g @angular/cli && npm install
COPY . .
RUN ng build --configuration production

# Étape 2 : Serveur web Nginx pour héberger Angular
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]

