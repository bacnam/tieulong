# syntax=docker/dockerfile:1
# Web portal + H5 game client (Client/, Lumen 10). Build context: repository root.

########## composer dependencies ##########
FROM composer:2 AS vendor
WORKDIR /app
COPY Client/composer.json Client/composer.lock ./
RUN composer install --no-dev --no-interaction --no-progress --no-scripts --no-autoloader \
        --prefer-dist --ignore-platform-reqs
COPY Client/ ./
RUN composer dump-autoload --no-dev --optimize

########## runtime ##########
FROM php:8.2-apache
RUN docker-php-ext-install pdo_mysql \
 && a2enmod rewrite headers \
 && sed -ri 's#DocumentRoot /var/www/html#DocumentRoot /var/www/html/public#' /etc/apache2/sites-available/000-default.conf \
 && printf '<Directory /var/www/html/public>\n    AllowOverride All\n    Require all granted\n</Directory>\n' > /etc/apache2/conf-available/tieulong.conf \
 && a2enconf tieulong
WORKDIR /var/www/html
COPY --from=vendor /app ./
RUN mkdir -p storage/logs storage/framework/views storage/framework/cache storage/app \
 && chown -R www-data:www-data storage

ENV TZ=Asia/Ho_Chi_Minh
EXPOSE 80
