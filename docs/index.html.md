# 任性推-Docs

 Version              | Update Time         | Status | Author   | Description          
----------------------|---------------------|--------|----------|----------------------
 v2023-03-20 11:28:13 | 2023-03-20 11:28:13 | auto   | @Renxing | Created by smart-doc 

## 管理员相关

### 请求登录qq机器人

**URL:** http://{{server}}/admin/remoteLoginQQBot

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 请求登录qq机器人

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
qq|int64|qq号|false|-

**Request-example:**
```
curl -X POST -i http://{{server}}/admin/remoteLoginQQBot
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

### 获取验证码url
**URL:** http://{{server}}/admin/getRemoteLoginQQBotUrl

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 获取验证码url

**Query-parameters:**

Parameter | Type|Description|Required|Since
---|---|---|---|---
qq|int64|在{@link #remoteLoginQQBot(Long)}中请求的qq号|false|-

**Request-example:**
```
curl -X GET -i http://{{server}}/admin/getRemoteLoginQQBotUrl?qq=662
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
  "data": "o0r4cw"
}
```

### 提交qq机器人登录验证码ticket
**URL:** http://{{server}}/admin/submitTicket

**Type:** POST

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 提交qq机器人登录验证码ticket

**Query-parameters:**

 Parameter | Type   | Description                             | Required | Since 
-----------|--------|-----------------------------------------|----------|-------
 qq        | int64  | 在{@link #remoteLoginQQBot(Long)}中请求的qq号 | false    | -     
 ticket    | string | 验证码ticket                               | false    | -     

**Request-example:**
```
curl -X POST -i http://{{server}}/admin/submitTicket
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
      "id": 755,
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
  "data": "ikb62y"
}
```

## 用户相关

### 用户webauthn令牌注册请求

**URL:** http://{{server}}/user/webauthnRegReq

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 用户webauthn令牌注册请求

**Query-parameters:**

 Parameter                                                                | Type    | Description        | Required | Since 
--------------------------------------------------------------------------|---------|--------------------|----------|-------
 id                                                                       | string  | No comments found. | false    | -     
 creationTime                                                             | int64   | No comments found. | false    | -     
 lastAccessedTime                                                         | int64   | No comments found. | false    | -     
 servletContext                                                           | object  | No comments found. | false    | -     
 └─classLoader                                                            | object  | No comments found. | false    | -     
 └─majorVersion                                                           | int32   | No comments found. | false    | -     
 └─minorVersion                                                           | int32   | No comments found. | false    | -     
 └─serverInfo                                                             | string  | No comments found. | false    | -     
 └─contextPath                                                            | string  | No comments found. | false    | -     
 └─effectiveSessionTrackingModes                                          | array   | No comments found. | false    | -     
 └─jspConfigDescriptor                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─taglibs                                  | array   | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─jspPropertyGroups                        | array   | No comments found. | false    | -     
 └─responseCharacterEncoding                                              | string  | No comments found. | false    | -     
 └─initParameterNames                                                     | object  | No comments found. | false    | -     
 └─filterRegistrations                                                    | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─servletContextName                                                     | string  | No comments found. | false    | -     
 └─servletRegistrations                                                   | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─defaultSessionTrackingModes                                            | array   | No comments found. | false    | -     
 └─virtualServerName                                                      | string  | No comments found. | false    | -     
 └─effectiveMajorVersion                                                  | int32   | No comments found. | false    | -     
 └─sessionCookieConfig                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─path                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─attributes                               | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object | object  | any object.        | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─comment                                  | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─domain                                   | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─secure                                   | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─httpOnly                                 | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─maxAge                                   | int32   | No comments found. | false    | -     
 └─effectiveMinorVersion                                                  | int32   | No comments found. | false    | -     
 └─requestCharacterEncoding                                               | string  | No comments found. | false    | -     
 └─sessionTimeout                                                         | int32   | No comments found. | false    | -     
 └─attributeNames                                                         | object  | No comments found. | false    | -     
 new                                                                      | boolean | No comments found. | false    | -     
 maxInactiveInterval                                                      | int32   | No comments found. | false    | -     
 attributeNames                                                           | object  | No comments found. | false    | -     

**Request-example:**

```
curl -X GET -i http://{{server}}/user/webauthnRegReq?servletContext.sessionCookieConfig.domain=feest.net&servletContext.servletContextName=georgiana.carroll&servletContext.sessionCookieConfig.comment=gxzbim&creationTime=1679282895481&servletContext.sessionCookieConfig.name=georgiana.carroll&maxInactiveInterval=935&id=110&servletContext.effectiveMajorVersion=156&servletContext.effectiveMinorVersion=876&servletContext.minorVersion=634&servletContext.virtualServerName=georgiana.carroll&servletContext.sessionCookieConfig.secure=true&servletContext.requestCharacterEncoding=nopdt4&servletContext.majorVersion=998&servletContext.sessionCookieConfig.path=qm0yh3&servletContext.responseCharacterEncoding=nkcx6r&servletContext.sessionTimeout=309&new=true&servletContext.contextPath=r1z1gp&lastAccessedTime=1679282895481&servletContext.sessionCookieConfig.httpOnly=true&servletContext.serverInfo=ijtpdj&servletContext.sessionCookieConfig.maxAge=8
```

**Response-fields:**

 Field           | Type    | Description           | Since 
-----------------|---------|-----------------------|-------
 flag            | boolean | 结果标志，true为成功，false为失败 | -     
 msg             | string  | 结果消息，一般反馈给用户          | -     
 data            | object  | 结果数据，一般反馈给前端进行处理      | -     
 └─array         | boolean | No comments found.    | -     
 └─object        | boolean | No comments found.    | -     
 └─valueNode     | boolean | No comments found.    | -     
 └─containerNode | boolean | No comments found.    | -     
 └─missingNode   | boolean | No comments found.    | -     

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "array": true,
    "object": true,
    "valueNode": true,
    "containerNode": true,
    "missingNode": true
  }
}
```

### 用户webauthn令牌注册响应

**URL:** http://{{server}}/user/webauthnRegResp

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json; charset=utf-8

**Description:** 用户webauthn令牌注册响应

**Body-parameters:**

 Parameter                                                                | Type    | Description        | Required | Since 
--------------------------------------------------------------------------|---------|--------------------|----------|-------
 publicKeyCredentialJson                                                  | string  | No comments found. | false    | -     
 id                                                                       | string  | No comments found. | false    | -     
 creationTime                                                             | int64   | No comments found. | false    | -     
 lastAccessedTime                                                         | int64   | No comments found. | false    | -     
 servletContext                                                           | object  | No comments found. | false    | -     
 └─classLoader                                                            | object  | No comments found. | false    | -     
 └─majorVersion                                                           | int32   | No comments found. | false    | -     
 └─minorVersion                                                           | int32   | No comments found. | false    | -     
 └─serverInfo                                                             | string  | No comments found. | false    | -     
 └─contextPath                                                            | string  | No comments found. | false    | -     
 └─effectiveSessionTrackingModes                                          | array   | No comments found. | false    | -     
 └─jspConfigDescriptor                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─taglibs                                  | array   | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─jspPropertyGroups                        | array   | No comments found. | false    | -     
 └─responseCharacterEncoding                                              | string  | No comments found. | false    | -     
 └─initParameterNames                                                     | object  | No comments found. | false    | -     
 └─filterRegistrations                                                    | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─servletContextName                                                     | string  | No comments found. | false    | -     
 └─servletRegistrations                                                   | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─defaultSessionTrackingModes                                            | array   | No comments found. | false    | -     
 └─virtualServerName                                                      | string  | No comments found. | false    | -     
 └─effectiveMajorVersion                                                  | int32   | No comments found. | false    | -     
 └─sessionCookieConfig                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─path                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─attributes                               | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object | object  | any object.        | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─comment                                  | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─domain                                   | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─secure                                   | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─httpOnly                                 | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─maxAge                                   | int32   | No comments found. | false    | -     
 └─effectiveMinorVersion                                                  | int32   | No comments found. | false    | -     
 └─requestCharacterEncoding                                               | string  | No comments found. | false    | -     
 └─sessionTimeout                                                         | int32   | No comments found. | false    | -     
 └─attributeNames                                                         | object  | No comments found. | false    | -     
 new                                                                      | boolean | No comments found. | false    | -     
 maxInactiveInterval                                                      | int32   | No comments found. | false    | -     
 attributeNames                                                           | object  | No comments found. | false    | -     

**Request-example:**

```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/user/webauthnRegResp --data 'ri21t2'
```

**Response-fields:**

 Field | Type    | Description           | Since 
-------|---------|-----------------------|-------
 flag  | boolean | 结果标志，true为成功，false为失败 | -     
 msg   | string  | 结果消息，一般反馈给用户          | -     
 data  | object  | 结果数据，一般反馈给前端进行处理      | -     

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息"
}
```

### 用户webauthn令牌登录请求

**URL:** http://{{server}}/user/webauthnLoginReq

**Type:** GET

**Author:** Renxing

**Content-Type:** application/x-www-form-urlencoded;charset=utf-8

**Description:** 用户webauthn令牌登录请求

**Query-parameters:**

 Parameter                                                                | Type    | Description        | Required | Since 
--------------------------------------------------------------------------|---------|--------------------|----------|-------
 id                                                                       | string  | No comments found. | false    | -     
 creationTime                                                             | int64   | No comments found. | false    | -     
 lastAccessedTime                                                         | int64   | No comments found. | false    | -     
 servletContext                                                           | object  | No comments found. | false    | -     
 └─classLoader                                                            | object  | No comments found. | false    | -     
 └─majorVersion                                                           | int32   | No comments found. | false    | -     
 └─minorVersion                                                           | int32   | No comments found. | false    | -     
 └─serverInfo                                                             | string  | No comments found. | false    | -     
 └─contextPath                                                            | string  | No comments found. | false    | -     
 └─effectiveSessionTrackingModes                                          | array   | No comments found. | false    | -     
 └─jspConfigDescriptor                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─taglibs                                  | array   | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─jspPropertyGroups                        | array   | No comments found. | false    | -     
 └─responseCharacterEncoding                                              | string  | No comments found. | false    | -     
 └─initParameterNames                                                     | object  | No comments found. | false    | -     
 └─filterRegistrations                                                    | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─servletContextName                                                     | string  | No comments found. | false    | -     
 └─servletRegistrations                                                   | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─defaultSessionTrackingModes                                            | array   | No comments found. | false    | -     
 └─virtualServerName                                                      | string  | No comments found. | false    | -     
 └─effectiveMajorVersion                                                  | int32   | No comments found. | false    | -     
 └─sessionCookieConfig                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─path                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─attributes                               | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object | object  | any object.        | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─comment                                  | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─domain                                   | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─secure                                   | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─httpOnly                                 | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─maxAge                                   | int32   | No comments found. | false    | -     
 └─effectiveMinorVersion                                                  | int32   | No comments found. | false    | -     
 └─requestCharacterEncoding                                               | string  | No comments found. | false    | -     
 └─sessionTimeout                                                         | int32   | No comments found. | false    | -     
 └─attributeNames                                                         | object  | No comments found. | false    | -     
 new                                                                      | boolean | No comments found. | false    | -     
 maxInactiveInterval                                                      | int32   | No comments found. | false    | -     
 attributeNames                                                           | object  | No comments found. | false    | -     

**Request-example:**

```
curl -X GET -i http://{{server}}/user/webauthnLoginReq?servletContext.serverInfo=pky2qa&new=true&servletContext.effectiveMinorVersion=486&maxInactiveInterval=948&servletContext.sessionCookieConfig.httpOnly=true&id=110&servletContext.minorVersion=251&servletContext.responseCharacterEncoding=1o2glh&lastAccessedTime=1679282895481&creationTime=1679282895481&servletContext.servletContextName=georgiana.carroll&servletContext.sessionCookieConfig.secure=true&servletContext.sessionCookieConfig.path=w3luuz&servletContext.virtualServerName=georgiana.carroll&servletContext.contextPath=mfl8jm&servletContext.requestCharacterEncoding=rzirjt&servletContext.effectiveMajorVersion=71&servletContext.sessionCookieConfig.comment=xqkws9&servletContext.sessionTimeout=942&servletContext.majorVersion=461&servletContext.sessionCookieConfig.name=georgiana.carroll&servletContext.sessionCookieConfig.maxAge=8&servletContext.sessionCookieConfig.domain=feest.net
```

**Response-fields:**

 Field           | Type    | Description           | Since 
-----------------|---------|-----------------------|-------
 flag            | boolean | 结果标志，true为成功，false为失败 | -     
 msg             | string  | 结果消息，一般反馈给用户          | -     
 data            | object  | 结果数据，一般反馈给前端进行处理      | -     
 └─array         | boolean | No comments found.    | -     
 └─object        | boolean | No comments found.    | -     
 └─valueNode     | boolean | No comments found.    | -     
 └─containerNode | boolean | No comments found.    | -     
 └─missingNode   | boolean | No comments found.    | -     

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "array": true,
    "object": true,
    "valueNode": true,
    "containerNode": true,
    "missingNode": true
  }
}
```

### 用户webauthn令牌登录响应

**URL:** http://{{server}}/user/webauthnLoginResp

**Type:** POST

**Author:** Renxing

**Content-Type:** application/json; charset=utf-8

**Description:** 用户webauthn令牌登录响应

**Body-parameters:**

 Parameter                                                                | Type    | Description        | Required | Since 
--------------------------------------------------------------------------|---------|--------------------|----------|-------
 publicKeyCredentialJson                                                  | string  | 前端传回的json字符串       | false    | -     
 id                                                                       | string  | No comments found. | false    | -     
 creationTime                                                             | int64   | No comments found. | false    | -     
 lastAccessedTime                                                         | int64   | No comments found. | false    | -     
 servletContext                                                           | object  | No comments found. | false    | -     
 └─classLoader                                                            | object  | No comments found. | false    | -     
 └─majorVersion                                                           | int32   | No comments found. | false    | -     
 └─minorVersion                                                           | int32   | No comments found. | false    | -     
 └─serverInfo                                                             | string  | No comments found. | false    | -     
 └─contextPath                                                            | string  | No comments found. | false    | -     
 └─effectiveSessionTrackingModes                                          | array   | No comments found. | false    | -     
 └─jspConfigDescriptor                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─taglibs                                  | array   | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─jspPropertyGroups                        | array   | No comments found. | false    | -     
 └─responseCharacterEncoding                                              | string  | No comments found. | false    | -     
 └─initParameterNames                                                     | object  | No comments found. | false    | -     
 └─filterRegistrations                                                    | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─servletContextName                                                     | string  | No comments found. | false    | -     
 └─servletRegistrations                                                   | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object                               | object  | any object.        | false    | -     
 └─defaultSessionTrackingModes                                            | array   | No comments found. | false    | -     
 └─virtualServerName                                                      | string  | No comments found. | false    | -     
 └─effectiveMajorVersion                                                  | int32   | No comments found. | false    | -     
 └─sessionCookieConfig                                                    | object  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─name                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─path                                     | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─attributes                               | map     | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─any object | object  | any object.        | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─comment                                  | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─domain                                   | string  | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─secure                                   | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─httpOnly                                 | boolean | No comments found. | false    | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─maxAge                                   | int32   | No comments found. | false    | -     
 └─effectiveMinorVersion                                                  | int32   | No comments found. | false    | -     
 └─requestCharacterEncoding                                               | string  | No comments found. | false    | -     
 └─sessionTimeout                                                         | int32   | No comments found. | false    | -     
 └─attributeNames                                                         | object  | No comments found. | false    | -     
 new                                                                      | boolean | No comments found. | false    | -     
 maxInactiveInterval                                                      | int32   | No comments found. | false    | -     
 attributeNames                                                           | object  | No comments found. | false    | -     

**Request-example:**

```
curl -X POST -H 'Content-Type: application/json; charset=utf-8' -i http://{{server}}/user/webauthnLoginResp --data 'bslkkx'
```

**Response-fields:**

 Field                                 | Type    | Description           | Since 
---------------------------------------|---------|-----------------------|-------
 flag                                  | boolean | 结果标志，true为成功，false为失败 | -     
 msg                                   | string  | 结果消息，一般反馈给用户          | -     
 data                                  | object  | 结果数据，一般反馈给前端进行处理      | -     
 └─uid                                 | int64   | 用户ID                  | -     
 └─name                                | string  | 用户名                   | -     
 └─admin                               | int32   | 是否为管理默认0否1是           | -     
 └─config                              | object  | 用户配置                  | -     
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─qqBot | int64   | 绑定的机器人QQ号             | -     
 └─cipher                              | string  | 用户密钥                  | -     
 └─dayMaxSendCount                     | int64   | 每日最大发送次数              | -     

**Response-example:**

```
{
  "flag": true,
  "msg": "返回的消息",
  "data": {
    "uid": 921,
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
    "uid": 992,
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
  "auth_date": 979,
  "first_name": "georgiana.carroll",
  "id": 734,
  "last_name": "georgiana.carroll",
  "photo_url": "www.erik-spinka.net",
  "username": "georgiana.carroll",
  "hash": "8b4vx7"
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
    "uid": 52,
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
curl -X POST -i http://{{server}}/user/telegramQRCodeLogin --data 'code=89603'
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
    "uid": 340,
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
curl -X POST -i http://{{server}}/user/register --data 'name=georgiana.carroll&password=ulaemi&lot_number=5h4s6a&captcha_output=ds31qh&pass_token=v661tt&gen_time=2023-03-20 11:28:15'
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
    "uid": 700,
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
  "data": "snw20g"
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
    "uid": 947,
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
curl -X POST -i http://{{server}}/user/qq_bot --data 'number=845'
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
    "uid": 551,
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
  "data": 874
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
curl -X POST -i http://{{server}}/user/qqGroupWhitelist --data 'number=511'
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
    "id": 868,
    "groupName": "georgiana.carroll",
    "number": 807374699,
    "userId": 326
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
      "id": 628,
      "groupName": "georgiana.carroll",
      "number": 807374699,
      "userId": 845
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
curl -X DELETE -i http://{{server}}/user/qqGroupWhitelist/270
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
  "id": 3,
  "uid": 983,
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
    "id": 645,
    "uid": 982,
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
      "id": 503,
      "uid": 789,
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
curl -X DELETE -i http://{{server}}/user/messageCallback/508
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


