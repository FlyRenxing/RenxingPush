# 任性推-Docs
Version |  Update Time  | Status | Author |  Description
---|---|---|---|---
v2022-09-20 17:33:54|2022-09-20 17:33:54|auto|@Renxing|Created by smart-doc



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
└─type|string|消息类型，目前仅支持"qq"、"qq_group、telegram"|false|-
└─data|string|消息元数据，与type对应。<br>qq-QQ号，qq_group-QQ群号，telegram-telegramID|true|-
└─qqBot|int64|指定QQ机器人号码|false|-

**Request-example:**
```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/msg/send/CH32p41OXu --data '{
  "content": "这是一条消息",
  "meta": {
    "type": "qq",
    "data": "1277489864",
    "qqBot": 1277489864
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
      "id": 578,
      "main": "这是公告内容",
      "color": "#ff0000"
    }
  ]
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
  "data": "bwgl37"
}
```

## 用户相关
### 用户登录
**URL:** http://{{server}}/user/login

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 用户登录

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
name|string|No comments found.|false|-
password|string|No comments found.|false|-

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
    "uid": 518,
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

### telegram登录
**URL:** http://{{server}}/user/telegramLogin

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json; charset=utf-8

**Description:** telegram登录

**Body-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
auth_date|int64|No comments found.|true|-
first_name|string|No comments found.|false|-
id|int64|No comments found.|true|-
last_name|string|No comments found.|false|-
photo_url|string|No comments found.|false|-
username|string|No comments found.|false|-
hash|string|No comments found.|true|-

**Request-example:**

```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/user/telegramLogin --data '{
  "auth_date": 617,
  "first_name": "sammy.gleichner",
  "id": 212,
  "last_name": "sammy.gleichner",
  "photo_url": "www.harland-robel.org",
  "username": "sammy.gleichner",
  "hash": "r52i81"
}'
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
    "uid": 958,
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

### telegram二维码登录
**URL:** http://{{server}}/user/telegramQRCodeLogin

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** telegram二维码登录

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
code|string|No comments found.|true|-

**Request-example:**
```
curl -X POST -i http://{{server}}/user/telegramQRCodeLogin --data 'code=26145'
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
    "uid": 177,
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

**Description:** 当开启极验验证码时需附带lot_number、captcha_output、pass_token、gen_time参数

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
name|string|    用户名|true|-
password|string|密码|true|-
lot_number|string|No comments found.|false|-
captcha_output|string|No comments found.|false|-
pass_token|string|No comments found.|false|-
gen_time|string|No comments found.|false|-

**Request-example:**
```
curl -X POST -i http://{{server}}/user/register --data 'name=sammy.gleichner&password=zo86jy&lot_number=5or4rx&captcha_output=k062ln&pass_token=c2nlnh&gen_time=2022-09-20 17:33:55'
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
    "uid": 422,
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
  "data": "hg1df3"
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
    "uid": 120,
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
curl -X POST -i http://{{server}}/user/qq_bot --data 'number=251'
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
    "uid": 331,
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
  "data": 518
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
curl -X POST -i http://{{server}}/user/qqGroupWhitelist --data 'number=274'
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
    "id": 299,
    "groupName": "sammy.gleichner",
    "number": 807374699,
    "userId": 726
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
      "id": 843,
      "groupName": "sammy.gleichner",
      "number": 807374699,
      "userId": 850
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
curl -X DELETE -i http://{{server}}/user/qqGroupWhitelist/510
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
appType|string|应用类型:qq/qq_group/telegram|false|-
keyword|string|关键词|false|-
callbackURL|string|回调地址|false|-
feedback|string|回调反馈|false|-
sender|string|发件人|false|-
group|string|所在群组|false|-
message|string|原消息内容|false|-
reply|boolean|是否回应|false|-
response|string|回应语|false|-

**Request-example:**

```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/user/messageCallback --data '{
  "id": 356,
  "uid": 717,
  "appType": "qq",
  "keyword": "keyword",
  "callbackURL": "https://www.baidu.com/callback",
  "feedback": "feedbackOK",
  "sender": "1277489864",
  "group": "1277489864",
  "message": "我是一条消息",
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
└─appType|string|应用类型:qq/qq_group/telegram|-
└─keyword|string|关键词|-
└─callbackURL|string|回调地址|-
└─feedback|string|回调反馈|-
└─sender|string|发件人|-
└─group|string|所在群组|-
└─message|string|原消息内容|-
└─reply|boolean|是否回应|-
└─response|string|回应语|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "id": 183,
    "uid": 681,
    "appType": "qq",
    "keyword": "keyword",
    "callbackURL": "https://www.baidu.com/callback",
    "feedback": "feedbackOK",
    "sender": "1277489864",
    "group": "1277489864",
    "message": "我是一条消息",
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
└─appType|string|应用类型:qq/qq_group/telegram|-
└─keyword|string|关键词|-
└─callbackURL|string|回调地址|-
└─feedback|string|回调反馈|-
└─sender|string|发件人|-
└─group|string|所在群组|-
└─message|string|原消息内容|-
└─reply|boolean|是否回应|-
└─response|string|回应语|-

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 589,
      "uid": 665,
      "appType": "qq",
      "keyword": "keyword",
      "callbackURL": "https://www.baidu.com/callback",
      "feedback": "feedbackOK",
      "sender": "1277489864",
      "group": "1277489864",
      "message": "我是一条消息",
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
curl -X DELETE -i http://{{server}}/user/messageCallback/942
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

### 退出账号
**URL:** http://{{server}}/user/logout

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 退出账号

**Request-example:**
```
curl -X GET -i http://{{server}}/user/logout
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


