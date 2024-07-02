# 任性推-Docs

| Version              | Update Time         | Status | Author   | Description          |
|----------------------|---------------------|--------|----------|----------------------|
| v2024-07-02 13:46:10 | 2024-07-02 13:46:10 | auto   | @renxing | Created by smart-doc |



## 系统类
### 获取机器人公开列表
**URL:** http://{{server}}/sys/qqbotlist

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取机器人公开列表

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/sys/qqbotlist'
```
**Response-fields:**

| Field         | Type    | Description                     | Since |
|---------------|---------|---------------------------------|-------|
| flag          | boolean | 结果标志，true为成功，false为失败           | -     |
| msg           | string  | 结果消息，一般反馈给用户                    | -     |
| data          | array   | 结果数据，一般反馈给前端进行处理                | -     |
| └─number      | int64   | QQ号                             | -     |
| └─qrCodeLogin | boolean | 是否二维码登陆,目前仅支持macOS和AndroidPAD协议 | -     |
| └─name        | string  | QQ昵称                            | -     |
| └─state       | int32   | 在线状态                            | -     |
| └─remarks     | string  | 备注                              | -     |

**Response-example:**

```json
{"flag":true,"msg":"返回的消息","data":[{"number":1277489864,"qrCodeLogin":2,true,"name":"会飞的任性","state":1,"remarks":"我是备注"}]}
```

### 获取所有公告
**URL:** http://{{server}}/sys/note

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取所有公告

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/sys/note'
```
**Response-fields:**

| Field   | Type    | Description           | Since |
|---------|---------|-----------------------|-------|
| flag    | boolean | 结果标志，true为成功，false为失败 | -     |
| msg     | string  | 结果消息，一般反馈给用户          | -     |
| data    | array   | 结果数据，一般反馈给前端进行处理      | -     |
| └─id    | int64   | 公告ID                  | -     |
| └─main  | string  | 公告内容                  | -     |
| └─color | string  | 公告在前端显示的颜色            | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取QQ登录URL

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/sys/qqUrl'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | string  | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": ""
}
```

## 用户相关
### 用户webauthn令牌注册请求
**URL:** http://{{server}}/user/webauthnRegReq

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 用户webauthn令牌注册请求

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/webauthnRegReq'
```
**Response-fields:**

| Field           | Type    | Description           | Since |
|-----------------|---------|-----------------------|-------|
| flag            | boolean | 结果标志，true为成功，false为失败 | -     |
| msg             | string  | 结果消息，一般反馈给用户          | -     |
| data            | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─array         | boolean | No comments found.    | -     |
| └─containerNode | boolean | No comments found.    | -     |
| └─valueNode     | boolean | No comments found.    | -     |
| └─missingNode   | boolean | No comments found.    | -     |
| └─object        | boolean | No comments found.    | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "array": true,
    "containerNode": true,
    "valueNode": true,
    "missingNode": true,
    "object": true
  }
}
```

### 用户webauthn令牌注册响应
**URL:** http://{{server}}/user/webauthnRegResp

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json

**Description:** 用户webauthn令牌注册响应

**Body-parameters:**

| Parameter               | Type   | Required | Description        | Since |
|-------------------------|--------|----------|--------------------|-------|
| publicKeyCredentialJson | string | false    | No comments found. | -     |

**Request-example:**

```bash
curl -X POST -H 'Content-Type: application/json' -i 'http://{{server}}/user/webauthnRegResp'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | object  | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息"
}
```

### 用户webauthn令牌登录请求
**URL:** http://{{server}}/user/webauthnLoginReq

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 用户webauthn令牌登录请求

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/webauthnLoginReq'
```
**Response-fields:**

| Field           | Type    | Description           | Since |
|-----------------|---------|-----------------------|-------|
| flag            | boolean | 结果标志，true为成功，false为失败 | -     |
| msg             | string  | 结果消息，一般反馈给用户          | -     |
| data            | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─array         | boolean | No comments found.    | -     |
| └─containerNode | boolean | No comments found.    | -     |
| └─valueNode     | boolean | No comments found.    | -     |
| └─missingNode   | boolean | No comments found.    | -     |
| └─object        | boolean | No comments found.    | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "array": true,
    "containerNode": true,
    "valueNode": true,
    "missingNode": true,
    "object": true
  }
}
```

### 用户webauthn令牌登录响应
**URL:** http://{{server}}/user/webauthnLoginResp

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json

**Description:** 用户webauthn令牌登录响应

**Body-parameters:**

| Parameter               | Type   | Required | Description  | Since |
|-------------------------|--------|----------|--------------|-------|
| publicKeyCredentialJson | string | false    | 前端传回的json字符串 | -     |

**Request-example:**

```bash
curl -X POST -H 'Content-Type: application/json' -i 'http://{{server}}/user/webauthnLoginResp'
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

### 用户登录
**URL:** http://{{server}}/user/login

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 用户登录

**Body-parameters:**

| Parameter | Type   | Required | Description        | Since |
|-----------|--------|----------|--------------------|-------|
| name      | string | false    | No comments found. | -     |
| password  | string | false    | No comments found. | -     |

**Request-example:**

```bash
curl -X POST -i 'http://{{server}}/user/login' --data 'password=""&name=""'
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

**Content-Type:** application/json

**Description:** telegram登录

**Body-parameters:**

| Parameter  | Type   | Required | Description        | Since |
|------------|--------|----------|--------------------|-------|
| auth_date  | int64  | true     | No comments found. | -     |
| first_name | string | false    | No comments found. | -     |
| id         | int64  | true     | No comments found. | -     |
| last_name  | string | false    | No comments found. | -     |
| photo_url  | string | false    | No comments found. | -     |
| username   | string | false    | No comments found. | -     |
| hash       | string | true     | No comments found. | -     |

**Request-example:**

```bash
curl -X POST -H 'Content-Type: application/json' -i 'http://{{server}}/user/telegramLogin' --data '{
  "auth_date": 0,
  "first_name": "",
  "id": 0,
  "last_name": "",
  "photo_url": "",
  "username": "",
  "hash": ""
}'
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** telegram二维码登录

**Body-parameters:**

| Parameter | Type   | Required | Description        | Since |
|-----------|--------|----------|--------------------|-------|
| code      | string | true     | No comments found. | -     |

**Request-example:**

```bash
curl -X POST -i 'http://{{server}}/user/telegramQRCodeLogin' --data 'code='
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 当开启极验验证码时需附带lot_number、captcha_output、pass_token、gen_time参数

**Body-parameters:**

| Parameter      | Type   | Required | Description                                                        | Since |
|----------------|--------|----------|--------------------------------------------------------------------|-------|
| name           | string | true     | 用户名<br/>Validation[Length(min=3, max=20, message=用户名长度需大于3,小于20) ] | -     |
| password       | string | true     | 密码                                                                 | -     |
| lot_number     | string | false    | No comments found.                                                 | -     |
| captcha_output | string | false    | No comments found.                                                 | -     |
| pass_token     | string | false    | No comments found.                                                 | -     |
| gen_time       | string | false    | No comments found.                                                 | -     |

**Request-example:**

```bash
curl -X POST -i 'http://{{server}}/user/register' --data 'name=&password=&lot_number=&captcha_output=&pass_token=&gen_time='
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 重置个人密钥

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/refreshCipher'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | string  | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": ""
}
```

### 获取个人资料
**URL:** http://{{server}}/user/profile

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取个人资料

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/profile'
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 换绑QQ机器人

**Body-parameters:**

| Parameter | Type  | Required | Description                                               | Since |
|-----------|-------|----------|-----------------------------------------------------------|-------|
| number    | int64 | true     | 机器人号码<br/>Validation[Length(min=6, message=机器人号码长度需大于6) ] | -     |

**Request-example:**

```bash
curl -X POST -i 'http://{{server}}/user/qq_bot' --data 'number=0'
```
**Response-fields:**

| Field                                 | Type    | Description           | Since |
|---------------------------------------|---------|-----------------------|-------|
| flag                                  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg                                   | string  | 结果消息，一般反馈给用户          | -     |
| data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─uid                                 | int64   | 用户ID                  | -     |
| └─name                                | string  | 用户名                   | -     |
| └─admin                               | int32   | 是否为管理默认0否1是           | -     |
| └─config                              | object  | 用户配置                  | -     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     |
| └─cipher                              | string  | 用户密钥                  | -     |
| └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取当日用户使用次数

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/ToDayUseCount'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | int32   | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": 0
}
```

### 添加QQ群白名单
**URL:** http://{{server}}/user/qqGroupWhitelist

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 添加QQ群白名单

**Body-parameters:**

| Parameter | Type  | Required | Description | Since |
|-----------|-------|----------|-------------|-------|
| number    | int64 | true     | 群号码         | -     |

**Request-example:**

```bash
curl -X POST -i 'http://{{server}}/user/qqGroupWhitelist' --data 'number=0'
```
**Response-fields:**

| Field       | Type    | Description           | Since |
|-------------|---------|-----------------------|-------|
| flag        | boolean | 结果标志，true为成功，false为失败 | -     |
| msg         | string  | 结果消息，一般反馈给用户          | -     |
| data        | object  | 结果数据，一般反馈给前端进行处理      | -     |
| └─id        | int64   | id(添加时不传)             | -     |
| └─groupName | string  | 群名称                   | -     |
| └─number    | int64   | 群号码                   | -     |
| └─userId    | int64   | 绑定站内用户ID              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "id": 0,
    "groupName": "",
    "number": 807374699,
    "userId": 0
  }
}
```

### 获取QQ群白名单列表
**URL:** http://{{server}}/user/qqGroupWhitelist

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取QQ群白名单列表

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/qqGroupWhitelist'
```
**Response-fields:**

| Field       | Type    | Description           | Since |
|-------------|---------|-----------------------|-------|
| flag        | boolean | 结果标志，true为成功，false为失败 | -     |
| msg         | string  | 结果消息，一般反馈给用户          | -     |
| data        | array   | 结果数据，一般反馈给前端进行处理      | -     |
| └─id        | int64   | id(添加时不传)             | -     |
| └─groupName | string  | 群名称                   | -     |
| └─number    | int64   | 群号码                   | -     |
| └─userId    | int64   | 绑定站内用户ID              | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 0,
      "groupName": "",
      "number": 807374699,
      "userId": 0
    }
  ]
}
```

### 删除QQ群白名单
**URL:** http://{{server}}/user/qqGroupWhitelist/{id}

**Type:** DELETE

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded

**Description:** 删除QQ群白名单

**Path-parameters:**

| Parameter | Type  | Required | Description | Since |
|-----------|-------|----------|-------------|-------|
| id        | int64 | true     | 白名单id       | -     |

**Request-example:**

```bash
curl -X DELETE -i 'http://{{server}}/user/qqGroupWhitelist/{id}'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | boolean | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
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

**Content-Type:** application/json

**Description:** 添加消息回调

**Body-parameters:**

| Parameter   | Type    | Required | Description                            | Since |
|-------------|---------|----------|----------------------------------------|-------|
| id          | int64   | false    | No comments found.                     | -     |
| uid         | int64   | false    | 消息回调所属用户ID                             | -     |
| appType     | string  | false    | 应用类型:qq/qq_group/telegram              | -     |
| keyword     | string  | false    | 关键词                                    | -     |
| callbackURL | string  | false    | 回调地址                                   | -     |
| feedback    | string  | false    | 回调反馈<br/>Validation[Length(max=3000) ] | -     |
| sender      | string  | false    | 发件人                                    | -     |
| group       | string  | false    | 所在群组                                   | -     |
| message     | string  | false    | 原消息内容                                  | -     |
| reply       | boolean | false    | 是否回应                                   | -     |
| response    | string  | false    | 回应语                                    | -     |

**Request-example:**

```bash
curl -X POST -H 'Content-Type: application/json' -i 'http://{{server}}/user/messageCallback' --data '{
  "id": 0,
  "uid": 0,
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

| Field         | Type    | Description                            | Since |
|---------------|---------|----------------------------------------|-------|
| flag          | boolean | 结果标志，true为成功，false为失败                  | -     |
| msg           | string  | 结果消息，一般反馈给用户                           | -     |
| data          | object  | 结果数据，一般反馈给前端进行处理                       | -     |
| └─id          | int64   | No comments found.                     | -     |
| └─uid         | int64   | 消息回调所属用户ID                             | -     |
| └─appType     | string  | 应用类型:qq/qq_group/telegram              | -     |
| └─keyword     | string  | 关键词                                    | -     |
| └─callbackURL | string  | 回调地址                                   | -     |
| └─feedback    | string  | 回调反馈<br/>Validation[Length(max=3000) ] | -     |
| └─sender      | string  | 发件人                                    | -     |
| └─group       | string  | 所在群组                                   | -     |
| └─message     | string  | 原消息内容                                  | -     |
| └─reply       | boolean | 是否回应                                   | -     |
| └─response    | string  | 回应语                                    | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "id": 0,
    "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 获取消息回调列表

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/messageCallback'
```
**Response-fields:**

| Field         | Type    | Description                            | Since |
|---------------|---------|----------------------------------------|-------|
| flag          | boolean | 结果标志，true为成功，false为失败                  | -     |
| msg           | string  | 结果消息，一般反馈给用户                           | -     |
| data          | array   | 结果数据，一般反馈给前端进行处理                       | -     |
| └─id          | int64   | No comments found.                     | -     |
| └─uid         | int64   | 消息回调所属用户ID                             | -     |
| └─appType     | string  | 应用类型:qq/qq_group/telegram              | -     |
| └─keyword     | string  | 关键词                                    | -     |
| └─callbackURL | string  | 回调地址                                   | -     |
| └─feedback    | string  | 回调反馈<br/>Validation[Length(max=3000) ] | -     |
| └─sender      | string  | 发件人                                    | -     |
| └─group       | string  | 所在群组                                   | -     |
| └─message     | string  | 原消息内容                                  | -     |
| └─reply       | boolean | 是否回应                                   | -     |
| └─response    | string  | 回应语                                    | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": [
    {
      "id": 0,
      "uid": 0,
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 删除消息回调

**Path-parameters:**

| Parameter | Type  | Required | Description | Since |
|-----------|-------|----------|-------------|-------|
| id        | int64 | true     | 消息回调id      | -     |

**Request-example:**

```bash
curl -X DELETE -i 'http://{{server}}/user/messageCallback/{id}'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | boolean | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
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

**Content-Type:** application/x-www-form-urlencoded

**Description:** 退出账号

**Request-example:**

```bash
curl -X GET -i 'http://{{server}}/user/logout'
```
**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | boolean | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息",
  "data": true
}
```

## 消息处理

### 推送消息

**URL:** http://{{server}}/msg/send/{cipher}

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json

**Description:** 推送消息

**Path-parameters:**

| Parameter | Type   | Required | Description | Since |
|-----------|--------|----------|-------------|-------|
| cipher    | string | true     | 个人密钥        | -     |

**Body-parameters:**

| Parameter | Type   | Required | Description                                                              | Since      |
|-----------|--------|----------|--------------------------------------------------------------------------|------------|
| content   | string | true     | 消息内容<br/>Validation[Length(max=3000, message=消息内容长度不能超过3000) ]           | -          |
| meta      | object | false    | 消息元数据                                                                    | -          |
| └─type    | string | false    | 消息类型，目前仅支持"qq"、"qq_group、telegram"<br/>Validation[Pattern(regexp=  (^qq$ | ^qq_group$ |^telegram$), message=type暂仅支持qq、qq_group、telegram) ]|-|
| └─data    | string | true     | 消息元数据，与type对应。<br/>qq-QQ号，qq_group-QQ群号，telegram-telegramID              | -          |
| └─qqBot   | int64  | false    | 指定QQ机器人号码                                                                | -          |

**Request-example:**

```bash
curl -X POST -H 'Content-Type: application/json' -i 'http://{{server}}/msg/send/{cipher}' --data '{
  "content": "这是一条消息",
  "meta": {
    "type": "qq",
    "data": "1277489864",
    "qqBot": 1277489864
  }
}'
```

**Response-fields:**

| Field | Type    | Description           | Since |
|-------|---------|-----------------------|-------|
| flag  | boolean | 结果标志，true为成功，false为失败 | -     |
| msg   | string  | 结果消息，一般反馈给用户          | -     |
| data  | object  | 结果数据，一般反馈给前端进行处理      | -     |

**Response-example:**

```json
{
  "flag": true,
  "msg": "返回的消息"
}
```


