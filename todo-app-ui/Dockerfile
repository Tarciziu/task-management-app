FROM node:20.4.0 as build-stage
LABEL stage=build-stage
WORKDIR /app
COPY package*.json /app/
RUN  npm pkg delete scripts.prepare
RUN npm ci --legacy-peer-deps
COPY ./ /app/
ARG configuration=production
RUN npm run build -- --output-path=./dist/out --configuration $configuration

FROM nginx:1.25
COPY --from=build-stage /app/dist/out/browser/ /usr/share/nginx/html
# Copy nginx conf files
COPY --from=build-stage /app/security-headers.conf /etc/nginx/security-headers.conf
COPY --from=build-stage /app/nginx.conf /etc/nginx/conf.d/default.conf
