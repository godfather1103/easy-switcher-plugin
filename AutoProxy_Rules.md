## Basic

```
example.com
```

- 匹配：http://www.example.com/foo
- 匹配：http://www.google.com/search?q=www.example.com
- 不匹配：https://www.example.com/

用于表明字符串 example.com 为 URL 关键词。任何含关键词的 http 连接（不包括 https）皆会使用代理。

----

```
||example.com
```

- 匹配：http://example.com/foo
- 匹配：https://subdomain.example.com/bar
- 不匹配：http://www.google.com/search?q=example.com

匹配整个域名（含子域名）（不论是 http 还是 https），一般用于该站点的 IP 被封锁的情况。

----

```
@@||example.com
```

这种规则的优先级最高，表示所有匹配 ||example.com 规则的网址均 禁止 代理。一般用于特殊情况，比如禁止对国内的网站误用代理。

## Others

```
|https://ssl.example.com
```

这种规则匹配的是所有以 https://ssl.example.com 开头的地址。一般用于某些 IP 的 HTTPS 访问被定点封锁的情况。

---

```
|http://example.com
```

这种规则匹配的是所有以 http://example.com 开头的地址。一般用于某些域名较短的网站，例如短网址服务，可以防止出现慢规则。

---

```
/^https?:\/\/[^\/]+example\.com/
```

正则表达式，匹配所有以 http:// 或 https:// 开头，且主域为 example.com 的网址。

---

```
!Comment
```

注释，以英文感叹号开头，解释说明，不起实际作用。