# i-evaluation (i-eval)
[English](readme.md) · 中文

工业的Agent(包括大模型，agent和知识库)可用性评估标准、测试集、评估结果及排行榜

## 大模型
[LLM-eval](LLM-eval/readme_zh.md)：大模型对工业支持的标准、测试集、评估结果及排行榜
  - 语义理解：工业术语理解
  - 稳定的工具调用
    - 基础CRUD
    - 大数据量调用
    - 大数据量返回
    - 多function，小颗粒度payload
    - 常见仪器调用
  - 性能及花费
    - SaaS，token数，时间
    - 私有：通用且同一配置的token数、时间，并发度
  - VL的工业可用性标准

## Agent
[agent-eval](agent-eval/readme_zh.md)：智能体对工业支持的标准、测试集、评估结果及排行榜
  - 非阻塞会话
  - 工具协议支持
    - HTTP
    - OpenTool
    - MCP
  - 工具策略支持
    - 工具集
    - 冲突策略
  - MultiAgent
  - Auto MultiAgent
  - API调用
  - SDK
  - 导入导出

## 知识库
[knowledge-base-eval](knowledge-base-eval/readme_zh.md)：知识库对工业支持的标准、测试集、评估结果及排行榜
  - Embeding Model
  - 可用性判断标准
