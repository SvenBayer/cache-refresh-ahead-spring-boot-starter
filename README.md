[![Maven Central](https://maven-badges.herokuapp.com/maven-central/blog.svenbayer/cache-refresh-ahead-spring-boot-starter/badge.svg?style=plastic)](https://search.maven.org/search?q=g:blog.svenbayer%20AND%20a:cache-refresh-ahead-spring-boot-starter)
[![Javadocs](http://javadoc.io/badge/blog.svenbayer/cache-refresh-ahead-spring-boot-starter.svg?color=blue)](http://javadoc.io/doc/blog.svenbayer/cache-refresh-ahead-spring-boot-starter)
[![Project Stats](https://www.openhub.net/p/cache-refresh-ahead-spring-boot-starter/widgets/project_thin_badge?format=gif&ref=Thin+badge)](https://www.openhub.net/p/cache-refresh-ahead-spring-boot-starter)

[**\[Unit Tests** ![CircleCI](https://circleci.com/gh/SvenBayer/cache-refresh-ahead-spring-boot-starter/tree/master.svg?style=svg)](https://circleci.com/gh/SvenBayer/cache-refresh-ahead-spring-boot-starter/tree/master) 
[**\[Integration Tests** ![CircleCI](https://circleci.com/gh/SvenBayer/cache-refresh-ahead-samples/tree/master.svg?style=svg)](https://circleci.com/gh/SvenBayer/cache-refresh-ahead-samples/tree/master)

[**\[SonarQube\]**](https://sonarcloud.io/dashboard?id=blog.svenbayer%3Acache-refresh-ahead-spring-boot-starter)

# Cache Refresh Ahead Starter

A Cache Refresh-Ahead Spring Boot Starter for [Caffeine](https://github.com/ben-manes/caffeine) and [Redis](https://projects.spring.io/spring-data-redis/) cache!

This starter enables [Refresh-Ahead Caching](https://docs.oracle.com/cd/E15357_01/coh.360/e15723/cache_rtwtwbra.htm#COHDG5178). This enables clients to benefit from the speed of caching while the application keeps the cache closely up-to-date. The client will not feel any impact of the slow source behind the cache.

## Supported Cache Frameworks

Currently, only Caffeine and Redis Cache is supported.

## Usage

### Preprequisites

You have to add @EnableCaching to your project and define a CacheManager bean.

### Setup

Add the Spring Boot starter to your maven pom. Define the expiration of your cache values.

Define the cache refresh intervals in your *application.properties/yml*. You can either define a global interval, or specify an interval for each cache separately. For example, the first line specifies that the caches are reloaded every 5 seconds. The second line specifies that only the cache named **longrun** is being reloaded every 5 seconds.

```
cache.reload.ahead.refresh-ahead-interval=5s
cache.reload.ahead.refresh-ahead-interval-caches.longrun=5s
```

### More Information

Visit my blog at [https://svenbayer.blog](https://svenbayer.blog)
