# i-evaluation (i-eval)
English · [中文](readme_zh.md)

Usability evaluation standards, test sets, evaluation results, and rankings for industrial Agents (including large models, agents, and knowledge bases)

## Large Models
[LLM-eval](LLM-eval/readme.md)：Standards, test sets, evaluation results, and rankings of industrial support for large models
  - Semantic Understanding: Comprehension of industrial terminology
  - Stable Tool Invocation
    - Basic CRUD operations
    - High-volume data calls
    - High-volume data returns
    - Multiple functions with fine-grained payloads
    - Common instrument calls
  - Performance and Cost
    - SaaS: token count, time
    - Private: token count and time with uniform configuration, concurrency
  - Industrial Usability Standards for Vision-Language Models

## Agent
[agent-eval](agent-eval/readme.md)：Standards, test sets, evaluation results, and rankings of intelligent agents for industrial support
  - Non-blocking Conversations
  - Tool Protocol Support
    - HTTP
    - OpenTool
    - MCP
  - Tool Strategy Support
    - Toolsets
    - Conflict Resolution Strategies
  - MultiAgent
  - Auto MultiAgent
  - API Invocation
  - SDK
  - Import/Export

## Knowledge Base
[knowledge-base-eval](knowledge-base-eval/readme.md)：Knowledge base for industrial support standards, test sets, and evaluation results
  - Embedding Model
  - Usability Criteria