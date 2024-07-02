# 任性推 - 专属消息推送服务

> [Surprised]🎉
>
> 由于TX的限制措施，此前任性推依赖的 [mirai](https://github.com/mamoe/mirai/) 社区已经几乎停滞。
>
> 2024.7.2，任性推 v1.0.0 正式发布。该版本弃用了此前对 [mirai](https://github.com/mamoe/mirai/)
> 的依赖，采用了新的底层框架：[NapCatQQ](https://github.com/NapNeko/NapCatQQ)。
>

欢迎使用任性推。

## 任性推是什么

任性推是一个消息推送机器人平台。用来通知自己消息的免费服务，目前支持 QQ、Telegram 两个平台。通过调用 API
接口，实现机器人给你发送消息的功能。通俗易懂点就是有什么重要通知需要及时知道，可以让任性推第一时间给你发消息。

## 为什么开发任性推

有很多客户端都可以实现这个功能，比如 Slack。有很多成熟的消息推送服务，我为什么还要开发一个呢？因为它们总是给得太多太复杂，我需要的只是一个简单消息通知而已。
因为总没有很简单的办法达到我要推送消息到 QQ 的目的，于是任性推诞生了！

- **只有一个接口（在使用时）：** 每次调用接口，我们都要去读很长的 API 说明文档，看得头昏眼花。所以任性推只有一个接口，只要会输入网址，就能实现调用。
- **一分钟完工：** 注册账号是很麻烦的事情，特别还要手机短信验证，更是令人心烦。
- **免安装客户端：** 安装客户端对很多用户来说是一种痛苦，所以使用 QQ 这一常用的通讯工具来进行消息推送。

## 使用场景

任性推是一个有着 GET/POST 请求的接口，可编程性 QQ 消息推送服务，所以你可以创造出各种玩法来，例如推送火车抢票通知、博客评论通知等。

## 如何使用

### 安装并运行

**注意：本项目要求最低 JDK 版本为 17**

1. 自行编译或下载预编译发布版本。ps：Releases
   页版本可能不是最新，最新预编译版本可在 [Actions](https://github.com/FlyRenxing/RenxingPush/actions)
   中的 `Java CI with Maven` 工作流程中的 `Artifacts` 工件列表内下载。
2. 创建 Mysql 数据库，例如：
    ```sql
    CREATE DATABASE renxingpush;
    ```
3. 搭建NapCatQQ服务，详见 [NapCatQQ](https://github.com/NapNeko/NapCatQQ)
4. 重命名 `application.yml.example` 为 `application.yml`，并修改数据库信息部分和qq服务的 Websocket 连接。
5. 运行。
6. 阅读 [API 文档](https://flyrenxing.github.io/RenxingPush/) 或自行查看源码进行使用。下面是 Quick Start：
    1. 注册接口 POST `/user/register`
    2. 登录接口 POST `/user/login`，记录下登录接口返回的 `cipher`
    3. 发送消息接口 POST `/msg/send/你的cipher`

若登录出现问题请参阅  [API 文档](https://flyrenxing.github.io/RenxingPush/index.html#_1_1_1_请求登录qq机器人)

### 发送消息

#### 请求方式

POST，Content-Type：application/json。具体内容请参考相关文档或百度查询。

#### JSON 格式对象:

```json
{
  "content": "消息内容",
  "meta": {
    "type": "qq/qq_group/telegram",
    "data": "qq号/qq群号/telegramID"
  }
}
```

#### QQ 消息内容增强

消息内容增强支持 QQ 表情，群 @，图片。详见 [NapCatQQ 消息格式支持情况](https://napneko.github.io/zh-CN/develop/msg)
及 [CQ 码文档](https://docs.go-cqhttp.org/cqcode/#cq-%E7%A0%81-cq-code)

- **表情用法：**
    - 直接使用 Emoji 表情：🙂
    - QQ 原生表情：`[CQ:face,id=1]`
        - 例：content="你好啊[CQ:face,id=1]这是一个撇嘴表情。"
        - 更多表情 ID [点击这里](https://github.com/richardchien/coolq-http-api/wiki/表情-CQ-码-ID-表)
          查看（不全）
        - 注意：如果使用了不存在的表情 ID，那么本次推送的整条消息可能将不会被发送出去。

- **群 @ 用法：** @个人 `[CQ:at,qq=114514]`、 @全体 `[CQ:at,qq=all]`
    - 例：content=你好[CQ:at,qq=114514]，我@了你。
    - 注意：如果你@的 QQ 号不在群内或者非管理员使用了@全体，那么本次推送的整条消息将不会被发送出去，@语法仅在推送群消息时有效。

- **图片用法：**`[CQ:image,file=可访问的图片url地址]`
    - 例：content="你好，这是百度的 logo 图片[CQ:image,file=https://baidu.com/tupian.png]"
    - 注意：图片格式包括 [gif,png,bmp,jpg]

### 触发关键词回调功能

在群里或者私聊中，机器人接收到的聊天消息包含您设置的聊天关键词，则机器人会向您设置的触发关键词回调 URL 发送一个 POST 请求，
您可以在接收到该 POST 请求后做任何业务逻辑。聊天关键词和触发关键词回调 URL 可以在控制台设置。

**注意：请将回调页的 HTTP 状态码设置为 200，否则将视为回调失败，机器人会进行 3 次重试操作**

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

### 一切开发旨在学习，请勿用于任何违反法律法规的用途

- 任性推是完全免费且开放源代码的软件，仅供学习和娱乐用途使用。
- 任性推不会通过任何方式强制收取费用，或对使用者提出物质条件。
- 任性推由整个开源社区维护，并不是属于某个个体的作品，所有贡献者都享有其作品的著作权。

### 许可证

```
Copyright (C) 2021-2024 Renxing Technologies and contributors.

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

> NapCatQQ 是基于 PC NTQQ 本体实现一套无头 Bot 框架。
>
> 名字寓意 瞌睡猫QQ，像睡着了一样在后台低占用运行的无需GUI界面的NTQQ。

> mirai 是一个在全平台下运行，提供 QQ Android 协议支持的高效率机器人库。

特别感谢 [NapCatQQ](https://github.com/NapNeko/NapCatQQ)，本项目由 [NapCatQQ](https://github.com/NapNeko/NapCatQQ) 提供
QQ 服务底层支持。

特别感谢 [mirai](https://github.com/mamoe/mirai/)，在本项目 v1.0.0 之前，由 [mirai](https://github.com/mamoe/mirai/) 提供
QQ 服务底层支持。

