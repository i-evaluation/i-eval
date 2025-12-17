# 大模型工业支持评估
[English](readme.md) · 中文

对大模型进行评估，主要涉及短、长文本生成，方法调用(方法名，参数，调用顺序)，性能数据等
## 支持大模型评估
  - 对LLM(v1/chat/completion接口，包括文本和vision模型)的基础评估，对每个用例以10分为基础分进行评估，自动打分，将结果写到excel文件
### 评估指标
  - 基础指标(自动完成)
    - 首token时间
    - 总时间
    - token每秒数
    - 生成token数
  - 其他指标
    - 回复准确度
    - 幻觉出现次数
### 评估具体细则
  - 自动策略
    - 首token时间
      - 首token时间大于1s时，减1分
    - 每秒token数(token/s)
      - 每秒token数小于10时，减1分
    - 耗时(开始返回到结束，结合生成的token数判定)
      - 生成的token数小于11，且耗时大于2s，减1分
      - 生成的token数小于101，且耗时大于3.5s，减1分
      - 生成的token数小于1001，且耗时大于8s，减1分
      - 生成的token数小于5001，且耗时大于20s，减1分
      - 生成的token数小于10001，且耗时大于45s，减1分
      - 生成的token数小于50001，且耗时大于60s，减1分
      - 生成的token数小于100001，且耗时大于90s，减1分
      - 耗时大于120s，减2分
    - 未知函数(不在函数注册列表中)
      - 存在时减1分
    - 函数参数格式出错(json格式错误)
      - 存在时减2分
  - 用例期望(条件设置)
    - 生成函数数量
      - 实际函数数量与预期不等时，减5分
    - 生成函数顺序
      - 实际函数顺序与期望不一致时，减5分
    - 函数参数数量
      - 函数参数数量与期望不一致时，减5分
    - 生成token数量
      - 实际生成token数量小于预期时，减5分
    - 文本格式(json)
      - 实际生成文本不是标准json(格式错误)时，减5分

### 其他参考数据
  - 上下文长度
  - 上下文条数
### 数据集
#### 生成普通文本
  - 单个请求
    - 10token以下的文本
    - 100token的文本
    - 1k token的文本
    - 10k token的文本
  - 并发
    - 4>8>16>32>64个并发，复用上面的数据集即可
#### 生成json
  - 单个请求
    - 普通json
    - 多层嵌套json
  - 并发
    - 4>8>16>32>64个并发，复用上面的数据集即可
#### 方法调用
  - 范围：
    - 普通crud
    - 长function list（16>32>64>128，先构造50种16个方法的，通过之后再构造50种32个方法的，依次类推）
    - 单一function多参数（4>8>16>32，先构造50种4、8个参数的，通过之后再构造50种16、32个参数的）
    - 单一function少参数大body（2个参数，参数长度1k>2k，构造50种参数长度1k和2k的）
    - tool return 长数据(工具执行结果长度1k>2k，构造50种返回长度1k和2k的)
  - 考察
    - fc的准确性
    - token性能
    - 结果返回生成自然语言文本的准确性
  - 并发
    - 4>8>16>32>64个并发
    - 普通crud（复用上面crud的数据集即可）
    - 单一function少参数大body、tool return 长数据（复用上面crud的数据集即可）
### 测试类型
  - 单次对话
  - 连续对话
    - 混合对话：短文本、长文本、fc
    - 没超出上下文的回复准确性
    - 何时出现幻觉
    - 超出上下文的行为(报错，卡死)
  - 并发测试
### 结果评估
#### 基准分以及扣分
  - 测试用例基准分：单个用例的基准分为10
  - 测试集基准分：以测试用例的平均分乘以10作为基准分，最高为100
  - 分段扣分：
    - 存在低于10分的用例：扣分数=10*(低于10分的用例数-低于6分的用例数)/总用例数
    - 存在低于6分的用例：扣分数=20*(低于6分的用例数-低于3分的用例数)/总用例数
    - 存在低于3分的用例：扣分数=30*低于3分的用例数/总用例数
#### 测试集评价
  - 测试集得分即为测试集基准法减去扣分数
  - 等级分为6个，由高到低依次为SS(>95), S(>90), A(>80), B(>70), C(>60), D(<60)

# 开始使用
## LLM eval
### 文件夹说明
  - file/suite：用例集(测试集文件)
  - file/tools：工具集(openapi,jsonrpc工具schema文件)
  - records: 执行用例时产生的json文件
  - mock_tools: 工具schema文件的server实现
### 准备工作
1. 当前目录下创建file,records子目录
2. file文件夹下，创建testPlan.xlsx文件，格式如下

| 序号 |  类别  | 并发数 | 提示词  | 工具                                                                                                                | 大模型                                                                                                                                                                                                                     | 用例集                              | 总结 | 执行时间 | 结果 |
|:---|:----:|:----|:-----|:------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------| :---: | :--- | :--- |
| 1  | 连续对话 | 1   | 提示词1 | [{"schema": "file/tools/datetime_weather.yml","apiKey": ""}, {"schema": "file/tools/shopping.json","apiKey": ""}] | {"type":"openai", "chatConfig": {"model":"deepseek-chat", "endpoint":"https://api.deepseek.com", "apiKey":"you-apiKey", "apiSecret":""}, "chatOptions":{"temperature":0.1, "maxTokens":10240, "seed":7,"stream":false}} | file/suite/continuousChat1.xlsx  | NA | NA | NA |
| 2  | 单次对话 | 1   | 提示词2 | [{"schema": "file/tools/datetime_weather.yml","apiKey": ""}, {"schema": "file/tools/shopping.json","apiKey": ""}] | {"type":"openai", "chatConfig": {"model":"deepseek-chat", "endpoint":"https://api.deepseek.com", "apiKey":"you-apiKey", "apiSecret":""}, "chatOptions":{"temperature":0.1, "maxTokens":10240, "seed":7,"stream":false}} | file/suite/singleTimeChat1.xlsx  | NA | NA | NA |
| 3  | 连续并发 | 4   | 提示词3 | [{"schema": "file/tools/datetime_weather.yml","apiKey": ""}, {"schemaDir": "file/tools/equipment","apiKey": ""}]  | {"type":"openai", "chatConfig": {"model":"deepseek-chat", "endpoint":"https://api.deepseek.com", "apiKey":"you-apiKey", "apiSecret":""}, "chatOptions":{"temperature":0.1, "maxTokens":10240, "seed":7,"stream":false}} | file/suite/concurrencyChat1.xlsx | NA | NA | NA |
  - 第一行(header)必须按上述规则复制
  - 其中"工具"是可选的，支持openapi,jsonrpc格式；"提示词", "大模型", "用例集"为必填项
    - 工具可以指定为json或yaml文件，如{"schema": "file/tools/datetime_weather.yml","apiKey": ""} ，也可以指定为目录(扫描目录中的所有json或yml文件，不支持递归)，如{"schemaDir": "file/tools/equipment"}
  - "总结", "执行时间", "结果"为执行后生成的结果，无须填写
3. file文件夹下创建suite和tools子目录
  - 将testPlan.xlsx中指定的工具复制到tools子目录下
  - 将testPlan.xlsx中指定的用例集复制到suite子目录下，用例集格式如下

    | 序号 |      指令      | 图片 | 期望                                                                  | 得分 | 时长(总) | 首token(平均) | token每秒(平均) | 生成token数(总) | 上下文长度 | 上下文条数 | 详情 |
    |:---|:------------|:---|:--------------------------------------------------------------------|:--:|:------|:-----------|:-----------:|:------------|:------|:------|:---|
    | 1  | 今天是什么日子，天气如何 |    | {"completionTokens":"20", "fcSequence":"date-time_GET,weather_GET"} | NA | NA    | NA         |     NA      | NA          | NA    | NA    | NA |
    | 2  | 帮我点一杯星巴克冰美式  |    | {"completionTokens":"10", "fcCount":1}                              | NA | NA    | NA         |     NA      | NA          | NA    | NA    | NA |
    | 3  | 帮我打开空调，设置为制冷模式  |    | {"completionTokens":"10","fcCount":2,"fcInfo":{"apiair-conditionerupdate_POST":4,"apiair-conditionerset_POST":1}}   | NA | NA    | NA         |     NA      | NA          | NA    | NA    | NA |
    - 第一行(header)必须按上述规则复制
    - “期望"是可选的，fcSequence是指调用的方法顺序，fcCount指该条用例预期调用的方法数，fcInfo指该条用例预期调用的方法及对应方法预期传递的参数个数；"图片"是vision模型才需输入，为http协议url或本地图片(png,jpg或jpeg)，多个图片以英文逗号分隔
    - "得分", "时长(总)", "首token(平均)", "token每秒(平均)", "生成token数(总)", "上下文长度", "上下文条数", "详情"为执行后生成的结果，无须填写
### 开始运行
1. 运行Main.main()即可
  - 中间产出
    - 套件目录下(file/suite)，生成${suite}.json文件，为每个套件结束时的json数据(对应testPlan.xlsx中的每一行,可供前端显示)
    - records文件夹下，生成${sessionId}.json文件，为每个套件测试结束时的全量上下文(并发测试时，每个线程均生成一个文件)
    - 并发测试时，file/suite目录下，生成${suite}${timestamp}.xlsx文件，为每个套件结束时的执行数据
