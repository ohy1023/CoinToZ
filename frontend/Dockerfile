# Build stage
FROM node:18.18.0-alpine AS build-stage

WORKDIR /app

COPY package*.json ./
RUN apk add --no-cache python3 make g++ \
    && npm install

COPY . ./
RUN npm run build

# Production stage
FROM nginx:stable-alpine AS production-stage

RUN rm /etc/nginx/conf.d/default.conf
COPY ./docker/nginx.conf /etc/nginx/conf.d/nginx.conf
COPY --from=build-stage /app/build /usr/share/nginx/html

EXPOSE 80
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]
