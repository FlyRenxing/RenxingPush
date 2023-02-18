# 任性推 - 专属消息推送服务

欢迎使用任性推。

## 任性推是什么

任性推是一个消息推送机器人平台。用来通知自己消息的免费服务，目前支持QQ、Telegram两个平台。通过调用API接口，实现机器人给你发送消息的功能。通俗易懂点就是有什么重要通知需要及时知道，可以让任性推第一时间给你发消息。

## 为什么开发任性推

有很多的客户端都可以做到这个功能，比如Slack。有很多成熟的消息推送服务，我为什么还要开发一个呢？因为它们总是给得太多太复杂，我需要的只是一个简单消息通知而已。
因为总没有很简单的办法达到我要推送消息到QQ的目的，于是任性推诞生了！

- **只有一个接口（在使用时）：** 每次调用接口，我们都要去读N长的API说明文档，看得头昏眼花。所以任性推只有一个接口，只要会输入网址，就能实现调用。
- **一分钟完工：** 注册账号是很麻烦的事情，特别还要手机短信验证，更是令大家心烦。
- **免安装客户端：** 安装客户端对很多用户来说是一种痛苦，所以用大家很常用的QQ来作为消息推送渠道。

## 使用场景

任性推是一个有着GET/POST请求的接口，可编程性QQ消息推送服务，所以你可以创造出各种玩法来，例如推送火车抢票通知、推送博客评论通知等。

## 如何使用

### 安装并运行

**注意：本项目要求最低JDK版本为17**

1. 自行编译或下载预编译发布版本。ps：Releases页版本可能不是最新，最新预编译版本可在[Actions](https://github.com/FlyRenxing/RenxingPush/actions)
中的`Java CI with Maven`工作流程中的`Artifacts`工件列表内下载。
2. 创建Mysql数据库。
3. 重命名`application.yml.example`为`application.yml`，并修改数据库信息部分。
4. 注册个QQ小号，添加到数据库里。
5. 运行。
6. 阅读 [API文档](https://flyrenxing.github.io/RenxingPush/) 或自行查看源码进行使用。下面是Quick Start：
    1. 注册接口 post `/user/register`
    2. 登录接口 post `/user/login`，记录下登录接口返回的`cipher`
    3. 发送消息接口post `/msg/send/你的cipher`

若登录出现问题请参阅 [mirai登录文档](https://docs.mirai.mamoe.net/Bots.html#_2-%E7%99%BB%E5%BD%95)
及[API文档](https://flyrenxing.github.io/RenxingPush/index.html#_1_1_1_请求登录qq机器人)管理员相关的三个接口：

1. 请求登录qq机器人
2. 获取验证码url
3. 提交qq机器人登录验证码ticket

### 发送消息

#### 请求方式

POST，Content-Type：application/json。不懂？不懂百度学去

#### JSON格式对象:

```json
{
  "content": "消息内容",
  "meta": {
    "type": "qq/qq_group/telegram",
    "data": "qq号/qq群号/telegramID"
  }
}
```

#### QQ消息内容增强

QQ消息内容支持 QQ表情，群@，图片。 详见[Mirai文档-Mirai码](https://docs.mirai.mamoe.net/Messages.html#mirai-码)

- **表情用法：**
    - 直接使用Emoji表情：🙂
    - QQ原生表情：`[mirai:face:id]`
        - 例：content="你好啊[mirai:face:1]
          这是一个撇嘴表情。更多表情ID [点击这里](https://github.com/richardchien/coolq-http-api/wiki/表情-CQ-码-ID-表)
          查看(不全)
        - 注意：如果使用了不存在的表情ID，那么本次推送的整条消息将不会被发送出去

- **群@用法：** @个人 `[mirai:at:$target]`、 @全体 `[mirai:atall]`
    - 例：content=你好[mirai:at:1277489864]，我@了你。
    - 注意：如果你@的QQ号不在群内或者非管理员使用了@全体，那么本次推送的整条消息将不会被发送出去，@语法仅在推送群消息时有效。

- **图片用法：**`[mirai:image:可访问的图片url地址]`
    - 例：content="你好，这是百度的logo图片[mirai:image:https://baidu.com/tupian.png]"
    - 注意：图片格式包括[gif,png,bmp,jpg]

### 触发关键词回调功能

在群里或者私聊中，机器人接收到的聊天消息包含您设置的聊天关键词，则机器人会向您设置的触发关键词回调URL发送一个POST请求，
您可以在接收到该POST请求后做任何业务逻辑。聊天关键词和触发关键词回调URL可以在控制台设置。

**注意：请将回调页的HTTP状态码设置为200，否则将视为回调失败，机器人会进行3次重试操作**

请求方式：POST，Content-Type=application/json

Body：

```json
{
  "id": "触发关键词ID",
  "uid": "站内所属用户ID",
  "appType": "app类型，如：qq",
  "keyword": "包含关键词的完整消息",
  "callbackURL": "回调URL",
  "sender": "发送人号码",
  "group": "触发消息如在群内则为群号码，否则为空",
  "reply": "控制台设置的是否回复",
  "response": "如reply为true，则response为回复内容"
}
```

## 声明

### 一切开发旨在学习，请勿用于非法用途

- 任性推是完全免费且开放源代码的软件，仅供学习和娱乐用途使用
- 任性推不会通过任何方式强制收取费用，或对使用者提出物质条件
- 任性推由整个开源社区维护，并不是属于某个个体的作品，所有贡献者都享有其作品的著作权。

### 许可证

```
Copyright (C) 2021-2022 Renxing Technologies and contributors.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```

`任性推` 采用 `AGPLv3` 协议开源。为了整个社区的良性发展，我们**强烈建议**您做到以下几点：

- **间接接触（包括但不限于使用 `Http API` 或 跨进程技术）到 `任性推` 的软件使用 `AGPLv3` 开源**
- **不鼓励，不支持一切商业使用**

鉴于项目的特殊性，开发团队可能在任何时间**停止更新**或**删除项目**。

## 鸣谢

> mirai 是一个在全平台下运行，提供 QQ Android 协议支持的高效率机器人库

特别感谢 [mirai](https://github.com/mamoe/mirai/) ,本项目由 [mirai](https://github.com/mamoe/mirai/) 提供底层支持。
