# Stage 1: Build Angular App
FROM node:18 as node
WORKDIR /app
COPY ./package*.json /app/
RUN npm install
COPY . .
RUN npm run build --prod

# Stage 2: Serve Angular App with Nginx
FROM nginx:alpine
RUN ls /app/
RUN ls /app/dist
COPY --from=node /app/dist/deploy/src/main/frontend/ /usr/share/nginx/html
COPY nginx-custom.conf /etc/nginx/conf.d/default.conf
