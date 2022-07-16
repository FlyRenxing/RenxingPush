# 任性推-Docs

Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
v2022-07-16 23:36:06|2022-07-16 23:36:06|auto|@12774|Created by smart-doc

## 消息处理

### 推送消息

**URL:** http://{{server}}/msg/send/{cipher}

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json; charset=utf-8

**Description:** 推送消息

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
cipher|string|个人密钥|true|-

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
content|string|消息内容|true|-
meta|object|消息元数据|false|-
└─type|string|消息类型，目前仅支持"qq"、"qq_group"|false|-
└─data|string|消息元数据，与type对应，qq：QQ号，qq_group：QQ群号|true|-

**Request-example:**
```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/msg/send/CH32p41OXu --data '{
  "content": "这是一条消息",
  "meta": {
    "type": "qq",
    "data": "1277489864"
  }
}'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息"
}
```

## 系统类
### 获取机器人公开列表
**URL:** http://{{server}}/sys/qqbotlist

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取机器人公开列表

**Request-example:**
```
curl -X GET -i http://{{server}}/sys/qqbotlist
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|array|结果数据，一般反馈给前端进行处理|-
└─number|int64|QQ号|-
└─name|string|QQ昵称|-
└─state|int32|在线状态|-
└─remarks|string|备注|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "number": 1277489864,
      "name": "会飞的任性",
      "state": 1,
      "remarks": "我是备注"
    }
  ]
}
```

### 获取所有公告
**URL:** http://{{server}}/sys/note

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取所有公告

**Request-example:**
```
curl -X GET -i http://{{server}}/sys/note
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|array|结果数据，一般反馈给前端进行处理|-
└─id|int64|公告ID|-
└─main|string|公告内容|-
└─color|string|公告在前端显示的颜色|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 582,
      "main": "这是公告内容",
      "color": "#ff0000"
    }
  ]
}
```

### 生成Geetest极验验证码
**URL:** http://{{server}}/sys/geetest

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 生成Geetest极验验证码

**Request-example:**
```
curl -X GET -i http://{{server}}/sys/geetest
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|string|结果数据，一般反馈给前端进行处理|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": "vgi4e6"
}
```

### 获取QQ登录URL
**URL:** http://{{server}}/sys/qqUrl

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取QQ登录URL

**Request-example:**
```
curl -X GET -i http://{{server}}/sys/qqUrl
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|string|结果数据，一般反馈给前端进行处理|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": "k0hav0"
}
```

## 用户相关
### 用户管理
**URL:** http://{{server}}/user/login

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 用户管理

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
uid|int64|用户ID|false|-
name|string|用户名|false|-
password|string|用户密码|false|-
admin|int32|是否为管理默认0否1是|false|-
config|object|用户配置|false|-
└─qqBot|int64|绑定的机器人QQ号|false|-
cipher|string|用户密钥|false|-
dayMaxSendCount|int64|每日最大发送次数|false|-

**Request-example:**
```
curl -X POST -i http://{{server}}/user/login
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-
└─uid|int64|用户ID|-
└─name|string|用户名|-
└─admin|int32|是否为管理默认0否1是|-
└─config|object|用户配置|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot|int64|绑定的机器人QQ号|-
└─cipher|string|用户密钥|-
└─dayMaxSendCount|int64|每日最大发送次数|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 11,
    "name": "这是用户名",
    "admin": 0,
    "config": {
      "qqBot": 2816669521
    },
    "cipher": "CH32p41OXu",
    "dayMaxSendCount": 200
  }
}
```

### 注册
**URL:** http://{{server}}/user/register

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 当开启极验验证码时需附带geetest_challenge，geetest_validate，geetest_seccode参数

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
name|string|    用户名|true|-
password|string|密码|true|-

**Request-example:**
```
curl -X POST -i http://{{server}}/user/register --data 'name=blair.huel&password=euyfmp'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-
└─uid|int64|用户ID|-
└─name|string|用户名|-
└─admin|int32|是否为管理默认0否1是|-
└─config|object|用户配置|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot|int64|绑定的机器人QQ号|-
└─cipher|string|用户密钥|-
└─dayMaxSendCount|int64|每日最大发送次数|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 377,
    "name": "这是用户名",
    "admin": 0,
    "config": {
      "qqBot": 2816669521
    },
    "cipher": "CH32p41OXu",
    "dayMaxSendCount": 200
  }
}
```

### 重置个人密钥
**URL:** http://{{server}}/user/refreshCipher

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 重置个人密钥

**Request-example:**
```
curl -X GET -i http://{{server}}/user/refreshCipher
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|string|结果数据，一般反馈给前端进行处理|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": "rvlrat"
}
```

### 获取个人资料
**URL:** http://{{server}}/user/profile

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取个人资料

**Request-example:**
```
curl -X GET -i http://{{server}}/user/profile
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-
└─uid|int64|用户ID|-
└─name|string|用户名|-
└─admin|int32|是否为管理默认0否1是|-
└─config|object|用户配置|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot|int64|绑定的机器人QQ号|-
└─cipher|string|用户密钥|-
└─dayMaxSendCount|int64|每日最大发送次数|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 784,
    "name": "这是用户名",
    "admin": 0,
    "config": {
      "qqBot": 2816669521
    },
    "cipher": "CH32p41OXu",
    "dayMaxSendCount": 200
  }
}
```

### 换绑QQ机器人
**URL:** http://{{server}}/user/qq_bot

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 换绑QQ机器人

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
number|int64|机器人号码|true|-

**Request-example:**
```
curl -X POST -i http://{{server}}/user/qq_bot --data 'number=224'
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-
└─uid|int64|用户ID|-
└─name|string|用户名|-
└─admin|int32|是否为管理默认0否1是|-
└─config|object|用户配置|-
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot|int64|绑定的机器人QQ号|-
└─cipher|string|用户密钥|-
└─dayMaxSendCount|int64|每日最大发送次数|-

**Response-example:**
```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 816,
    "name": "这是用户名",
    "admin": 0,
    "config": {
      "qqBot": 2816669521
    },
    "cipher": "CH32p41OXu",
    "dayMaxSendCount": 200
  }
}
```

### 获取当日用户使用次数
**URL:** http://{{server}}/user/ToDayUseCount

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取当日用户使用次数

**Request-example:**
```
curl -X GET -i http://{{server}}/user/ToDayUseCount
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|int32|结果数据，一般反馈给前端进行处理|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": 855
}
```

### 添加QQ群白名单

**URL:** http://{{server}}/user/qqGroupWhitelist

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 添加QQ群白名单

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
number|int64|群号码|true|-

**Request-example:**

```
curl -X POST -i http://{{server}}/user/qqGroupWhitelist --data 'number=936'
```

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-
└─id|int64|id(添加时不传)|-
└─groupName|string|群名称|-
└─number|int64|群号码|-
└─userId|int64|绑定站内用户ID|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "id": 546,
    "groupName": "blair.huel",
    "number": 807374699,
    "userId": 75
  }
}
```

### 获取QQ群白名单列表

**URL:** http://{{server}}/user/qqGroupWhitelist

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取QQ群白名单列表

**Request-example:**

```
curl -X GET -i http://{{server}}/user/qqGroupWhitelist
```

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|array|结果数据，一般反馈给前端进行处理|-
└─id|int64|id(添加时不传)|-
└─groupName|string|群名称|-
└─number|int64|群号码|-
└─userId|int64|绑定站内用户ID|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 305,
      "groupName": "blair.huel",
      "number": 807374699,
      "userId": 158
    }
  ]
}
```

### 删除QQ群白名单

**URL:** http://{{server}}/user/qqGroupWhitelist/{id}

**Type:** DELETE

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 删除QQ群白名单

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int64|白名单id|true|-

**Request-example:**

```
curl -X DELETE -i http://{{server}}/user/qqGroupWhitelist/856
```

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|boolean|结果数据，一般反馈给前端进行处理|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": true
}
```

### 添加消息回调

**URL:** http://{{server}}/user/messageCallback

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json; charset=utf-8

**Description:** 添加消息回调

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int64|No comments found.|false|-
uid|int64|消息回调所属用户ID|false|-
appType|string|应用类型|false|-
keyword|string|关键词|false|-
callbackURL|string|回调地址|false|-
sender|string|发件人|false|-
group|string|所在群组|false|-
reply|boolean|是否回应|false|-
response|string|回应语|false|-

**Request-example:**

```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/user/messageCallback --data '{
  "id": 914,
  "uid": 881,
  "appType": "qq",
  "keyword": "keyword",
  "callbackURL": "https://www.baidu.com/callback",
  "sender": "1277489864",
  "group": "1277489864",
  "reply": true,
  "response": "回调成功了"
}'
```

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|object|结果数据，一般反馈给前端进行处理|-
└─id|int64|No comments found.|-
└─uid|int64|消息回调所属用户ID|-
└─appType|string|应用类型|-
└─keyword|string|关键词|-
└─callbackURL|string|回调地址|-
└─sender|string|发件人|-
└─group|string|所在群组|-
└─reply|boolean|是否回应|-
└─response|string|回应语|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "id": 567,
    "uid": 659,
    "appType": "qq",
    "keyword": "keyword",
    "callbackURL": "https://www.baidu.com/callback",
    "sender": "1277489864",
    "group": "1277489864",
    "reply": true,
    "response": "回调成功了"
  }
}
```

### 获取消息回调列表

**URL:** http://{{server}}/user/messageCallback

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取消息回调列表

**Request-example:**

```
curl -X GET -i http://{{server}}/user/messageCallback
```

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|array|结果数据，一般反馈给前端进行处理|-
└─id|int64|No comments found.|-
└─uid|int64|消息回调所属用户ID|-
└─appType|string|应用类型|-
└─keyword|string|关键词|-
└─callbackURL|string|回调地址|-
└─sender|string|发件人|-
└─group|string|所在群组|-
└─reply|boolean|是否回应|-
└─response|string|回应语|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 875,
      "uid": 97,
      "appType": "qq",
      "keyword": "keyword",
      "callbackURL": "https://www.baidu.com/callback",
      "sender": "1277489864",
      "group": "1277489864",
      "reply": true,
      "response": "回调成功了"
    }
  ]
}
```

### 删除消息回调

**URL:** http://{{server}}/user/messageCallback/{id}

**Type:** DELETE

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 删除消息回调

**Path-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
id|int64|消息回调id|true|-

**Request-example:**

```
curl -X DELETE -i http://{{server}}/user/messageCallback/636
```

**Response-fields:**

Field | Type|Description|Since
---|---|---|---
flag|boolean|结果标志，true为成功，false为失败|-
msg|string|结果消息，一般反馈给用户|-
data|boolean|结果数据，一般反馈给前端进行处理|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": true
}
```


