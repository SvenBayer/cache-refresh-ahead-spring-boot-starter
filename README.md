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