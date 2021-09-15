# 任性推服务端文档

**简介**:任性推服务端文档

**联系人**:会飞的任性

**Version**:1.0

**接口路径**:/v3/api-docs?group=任性推

- [任性推服务端文档](#任性推服务端文档)
- [消息处理](#消息处理)
    - [发送消息](#发送消息)
- [用户管理](#用户管理)
    - [登录](#登录)
    - [获取个人资料](#获取个人资料)
    - [换绑QQ机器人](#换绑qq机器人)
    - [重置个人密钥](#重置个人密钥)
    - [注册](#注册)
- [系统类](#系统类)
    - [获取所有公告](#获取所有公告)
    - [获取机器人公开列表](#获取机器人公开列表)

# 消息处理

## 发送消息

**接口地址**:`/msg/send/{cipher}`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
    "content"
:
    "",
        "meta"
:
    {
        "data"
    :
        "",
            "type"
    :
        ""
    }
}
```

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|cipher|用户密钥|path|true|string||
|消息类|定义了消息的数据结构|body|true|消息类|消息类|
|&emsp;&emsp;content|消息内容||false|string||
|&emsp;&emsp;meta|消息元数据||false|消息元数据类|消息元数据类|
|&emsp;&emsp;&emsp;&emsp;data|消息元数据，当type为qq时，此处为接收人qq号||false|string||
|&emsp;&emsp;&emsp;&emsp;type|消息类型，目前仅支持"qq"||false|string||

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|object||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    {
    }
,
    "flag"
:
    false,
        "msg"
:
    ""
}
```

# 用户管理

## 登录

**接口地址**:`/user/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|用户名|query|true|string||
|password|密码|query|true|string||

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|object||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    {
    }
,
    "flag"
:
    false,
        "msg"
:
    ""
}
```

## 获取个人资料

**接口地址**:`/user/profile`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类«User»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|User|User|
|&emsp;&emsp;admin|是否为管理默认0否|integer(int64)||
|&emsp;&emsp;cipher|用户密钥|string||
|&emsp;&emsp;config|用户配置|string||
|&emsp;&emsp;name|用户名|string||
|&emsp;&emsp;uid|用户ID|integer(int64)||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    {
        "admin"
    :
        0,
            "cipher"
    :
        "",
            "config"
    :
        "",
            "name"
    :
        "",
            "uid"
    :
        0
    }
,
    "flag"
:
    false,
        "msg"
:
    ""
}
```

## 换绑QQ机器人

**接口地址**:`/user/qq_bot`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|number|机器人号码|query|true|integer(int64)||

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类«User»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|User|User|
|&emsp;&emsp;admin|是否为管理默认0否|integer(int64)||
|&emsp;&emsp;cipher|用户密钥|string||
|&emsp;&emsp;config|用户配置|string||
|&emsp;&emsp;name|用户名|string||
|&emsp;&emsp;uid|用户ID|integer(int64)||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    {
        "admin"
    :
        0,
            "cipher"
    :
        "",
            "config"
    :
        "",
            "name"
    :
        "",
            "uid"
    :
        0
    }
,
    "flag"
:
    false,
        "msg"
:
    ""
}
```

## 重置个人密钥

**接口地址**:`/user/refreshCipher`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类«string»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|string||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    "",
        "flag"
:
    false,
        "msg"
:
    ""
}
```

## 注册

**接口地址**:`/user/register`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|用户名|query|true|string||
|password|密码|query|true|string||

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类«User»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|User|User|
|&emsp;&emsp;admin|是否为管理默认0否|integer(int64)||
|&emsp;&emsp;cipher|用户密钥|string||
|&emsp;&emsp;config|用户配置|string||
|&emsp;&emsp;name|用户名|string||
|&emsp;&emsp;uid|用户ID|integer(int64)||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    {
        "admin"
    :
        0,
            "cipher"
    :
        "",
            "config"
    :
        "",
            "name"
    :
        "",
            "uid"
    :
        0
    }
,
    "flag"
:
    false,
        "msg"
:
    ""
}
```

# 系统类

## 获取所有公告

**接口地址**:`/sys/note`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类«List«Note»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|array|Note|
|&emsp;&emsp;color|公告在前端显示的颜色|string||
|&emsp;&emsp;id|公告ID|integer(int64)||
|&emsp;&emsp;main|公告内容|string||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    [
        {
            "color": "",
            "id": 0,
            "main": ""
        }
    ],
        "flag"
:
    false,
        "msg"
:
    ""
}
```

## 获取机器人公开列表

**接口地址**:`/sys/qqbotlist`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|结果类«List«QqInfo»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||

**响应参数**:

| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data|结果数据，一般反馈给前端进行处理|array|QqInfo|
|&emsp;&emsp;name|QQ昵称|string||
|&emsp;&emsp;number|QQ号|integer(int64)||
|&emsp;&emsp;remarks|备注|string||
|&emsp;&emsp;state|在线状态|integer(int64)||
|flag|结果标志，true为成功|boolean||
|msg|结果消息，一般反馈给用户|string||

**响应示例**:

```javascript
{
    "data"
:
    [
        {
            "name": "",
            "number": 0,
            "remarks": "",
            "state": 0
        }
    ],
        "flag"
:
    false,
        "msg"
:
    ""
}
```