# OSI 7层模型和TCP/IP 4层模型



- OSI: open system interconnection 开放式系统互联参考模型
- OSI 和TCP/IP 的对应关系和协议

![img](https://pic1.zhimg.com/80/v2-2d62ba265be486cb94ab531912aa3b9c_720w.jpg)

OSI模型各层的基本作用

![img](https://pic2.zhimg.com/80/v2-436927a69a3574532059a78623d3095d_720w.jpg)

- OSI模型的详解（但是对会话层、表示层、应用层合并为TCP/IP的应用层）
- 大纲

![img](https://pic4.zhimg.com/80/v2-5ce0438c1e2c59840286b124fc70dd67_720w.jpg)



- 物理层

![img](https://pic1.zhimg.com/80/v2-343093645638ea0839b71db5eba1f7c0_720w.jpg)

- 数据链路层

![img](https://pic1.zhimg.com/80/v2-fb8534d86e40986e43449de6c35ebd14_720w.jpg)

- 网络层

![img](https://pic4.zhimg.com/80/v2-991572825990575d273f653a78bcc5e7_720w.jpg)

- 传输层

![img](https://pic2.zhimg.com/80/v2-31bff54e0720487afe37e5f3f282d231_720w.jpg)

- 应用层

![img](https://pic2.zhimg.com/80/v2-741e4cd7f95897d6a61bd219e208f1c1_720w.jpg)

- 以下为补充内容，可以帮助理解



- pc连网的设置详解

![img](https://pic4.zhimg.com/80/v2-b09a3718e0501f053b6ed418b087211b_720w.jpg)

- 数据链路层数据包（以太网数据包）格式，除了应用层没有头部，其他都有

![img](https://pic2.zhimg.com/80/v2-3c8ab7e3f330238821adedea31b9c321_720w.jpg)

- 由于以太网数据包的数据部分，最大长度为1500字节，当IP包过大时，会分割下来，但是每个分割包的头部都一样

![img](https://pic1.zhimg.com/80/v2-5ce2810c5f0ed99ad92d7d3a43cc652c_720w.jpg)

数据包在传送时的封装和解封装如下所示

![img](https://pic3.zhimg.com/80/v2-80430dbb37a1e42315a77e30448b34b2_720w.jpg)

- 参考文献：

- - [计算机网络漫谈：OSI七层模型与TCP/IP四层（参考）模型](https://link.zhihu.com/?target=http%3A//www.jianshu.com/p/c793a279f698)
  - [OSI七层协议模型、TCP/IP四层模型学习笔记](https://link.zhihu.com/?target=https%3A//www.cnblogs.com/Robin-YB/p/6668762.html)