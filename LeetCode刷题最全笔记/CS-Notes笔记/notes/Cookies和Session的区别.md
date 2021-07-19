在面试中，研发岗位经常会考察Session和Cookies的区别。

==我们都知道HTTP服务是无状态的，这里的无状态体现在两点：==

- ==每一个HTTP请求是独立的，服务不能鉴别出两个请求是不是来自同一个用户。==
- ==Web服务没有在内存中保留请求的任何内容（只有磁盘的信息才能在请求之间共享）。==

无状态这个特性对于开发人员是不友好的，因为在实际开发中有时候需要跟踪用户。==Cookies和Session技术都是为了使**无状态**的HTTP成为**有状态**的==。

## Cookies

==Cookies是客户端浏览器保存在用户机器上一小段文本信息==。当浏览器第一次连接服务器时，服务器发出响应，==服务器的响应中有一个字段是Set-Cookie==，这个==字段就是Cookies==，每一个部分都是==name-value==对。之后每次用户的web浏览器与服务器交互时，它都会将==Cookies信息传递给web服务器==。只有==浏览器存储的、与请求的url中的域相关的cookie才会发送到服务器==。这意味着与http://www.example.com相关的cookie将不会发送到[http://www.exampledomain.com](http://www.exampledomain.com/)。==服务器得到Cookies就可以识别用户身份==。

Cookies的常见用途是==身份验证、购物车项目和服务器Session ID==。

举个例子，当我们使用浏览器登录某些网站时，可以选择记住密码，那么当你下次登录的时候就不需要手动输入用户名和密码，浏览器直接填充这些信息，极大方便了用户，这里就是将用户名和密码信息加密存储在Cookies中。

### **Cookies里面会包含什么：**

- ==名称和数据==
- ==域名==
- ==过期时间：浏览器可以删除旧的Cookies==。

## Session

Session可以定义为希望在用户与网站或web应用程序的交互过程中保持信息的==服务器端存储==。==服务器使用Cookies实现Session==。==Cookies里面会包含一个Session ID，Web应用程序将此Session ID与其内部数据库配对，并检索存储的变量以供请求的页面使用==。在每次请求时，服务器都会从会话 Cookie 中读取 SessionId，==如果服务端的数据和读取的 SessionId 相同，那么服务器就会发送响应给浏览器，允许用户登录。==

**Session和Cookies的区别：**

==Cookies保存在客户端，Session保存在服务器上==。

==Cookies可以用来跟踪会话，也可以保存用户的特点或者用户名密码之类的信息。Session用来跟踪会话。==

==Session的实现依赖于Cookies，经常会把Session ID 存放在Cookies中。==