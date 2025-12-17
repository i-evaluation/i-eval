# Evaluation of AI Agent Support for Industrial Applications
English · [中文](readme_zh.md)

## Agent Functions/Features Support
  - Non-blocking Sessions
  - Tool Protocol Support
    - HTTP
    - OpenTool
    - MCP
  - Tool Strategy Support
    - Tool Set
    - Conflict Strategy
  - MultiAgent
  - Auto MultiAgent
  - API Calls
  - SDK
  - Import/Export

### Agent Standard Details

Agent Basic Scoring Standards (10 points base score for each sub-item)

| Category | Indicator Name | Indicator Description | Scoring Dimensions | Score (0--10) | Weight | Scoring Notes |
|:---|:----:|:----|:-----| :--- |:-----| :--- |
| Architecture Capability | Non-blocking Session | Whether it supports asynchronous, parallel context without blocking the main thread | Concurrent Context Management - Asynchronous I/O Scheduling - Blocking Rate (Average Response Delay) | NA | 0.1 | <a href="#sub-table-1-1">Sub-table-1-1</a> |
| Architecture Capability | Agent Execution Mode | Supports parallel/sequential/rejection | Parallel Scheduling - Task Lock Control - Conflict Fallback Strategy | NA | 0.05 | <a href="#sub-table-1-2">Sub-table-1-2</a> |
| Protocol Compatibility | Tool Protocol Support | Whether it is compatible with industrial-grade protocol frameworks (HTTP/OpenTool/MCP) | Protocol Coverage - Exception Tolerance - Compatible Expansion Capability | NA | 0.05 | <a href="#sub-table-1-3">Sub-table-1-3</a> |
| Protocol Compatibility | HTTP | RESTful standards, streaming transmission, error code standardization | Interface Completeness - Error Handling - Streaming Response Support | NA | 0.05 | <a href="#sub-table-1-4">Sub-table-1-4</a> |
| Protocol Compatibility | OpenTool | Supports plug-in invocation, task serialization | Tool Registration Mechanism - Plugin Extensibility - Invocation Security | NA | 0.05 | <a href="#sub-table-1-5">Sub-table-1-5</a> |
| Protocol Compatibility | MCP | Supports connections between external tools and data sources | Number of Channels Supported - State Synchronization Accuracy - Message Traceability | NA | 0.05 | <a href="#sub-table-1-6">Sub-table-1-6</a> |
| Tool Strategy Capability | Tool Set (Method Sequence) | Supports multi-method chain/task chain (Method Chaining) | Dynamic Assembly - Exception Rollback - Dependency Management | NA | 0.1 | <a href="#sub-table-1-7">Sub-table-1-7</a> |
| Tool Strategy Capability | Conflict Strategy | Supports parallel/sequential/rejection strategies (task conflict handling) | Parallel Scheduling - Task Lock Control - Conflict Fallback Strategy | NA | 0.05 | <a href="#sub-table-1-8">Sub-table-1-8</a> |
| Collaborative Intelligence | MultiAgent | Supports multi-Agent collaboration, context sharing | Context Synchronization - Task Allocation - Result Aggregation | NA | 0.1 | <a href="#sub-table-1-9">Sub-table-1-9</a> |
| Collaborative Intelligence | Auto MultiAgent | Automatic task decomposition and sub-Agent generation | Autonomous Planning - Dynamic Sub-Agent Generation - Self-callback | NA | 0.1 | <a href="#sub-table-1-10">Sub-table-1-10</a> |
| Integration and Interface | API Calls | API coverage, stability | Stability - Monitorability - Error Recovery | NA | 0.05 | <a href="#sub-table-1-11">Sub-table-1-11</a> |
| Integration and Interface | SDK | Whether it provides multi-language SDK and complete documentation | Language Coverage - Documentation Completeness - Usability | NA | 0.05 | <a href="#sub-table-1-12">Sub-table-1-12</a> |
| Integration and Interface | Import/Export | Supports import/export of task configuration/model state | Format Compatibility - Data Consistency - Version Rollback | NA | 0.05 | <a href="#sub-table-1-13">Sub-table-1-13 |

#### Sub-table-1-1

| Score | Indicator |
| -- | ----------------------------- |
| 10 | Fully asynchronous, average response < 500ms, >99% requests successful |
| 9 | Occasional blocking, average response 500ms--1s, 95--98% requests successful |
| 6 | Semi-asynchronous, average response 1--2s, 80--94% requests successful |
| 2 | Basically synchronous, average response 2--5s, 60--79% requests successful |
| 0 | Completely synchronous blocking, average response > 5s, <60% requests successful |

#### Sub-table-1-2

| Score | Indicator |
| -- | -------------------------------------- |
| 10 | Fully supports parallel and sequential execution, with rejection strategy (such as queue, rate limiting) and fine-grained lock control (such as resource lock) |
| 9 | Supports parallel and sequential execution, with basic rejection strategy (such as direct failure) |
| 7 | Only supports single execution mode (such as only parallel or only sequential) |
| 5 | No concurrency control, resource conflicts cause task failure or data inconsistency | |
| 0 | No clear execution mode, chaotic task scheduling, deadlocks or resource competition issues |

#### Sub-table-1-3

| Score | Number of Supported Protocols |
| -- | ------------------------------ |
| 10 | HTTP + OpenTool + MCP fully supported |
| 9 | HTTP + OpenTool + MCP supported, but documentation or edge case support incomplete |
| 8 | HTTP + OpenTool supported |
| 5 | Only HTTP supported |
| 0 | No protocols supported |

#### Sub-table-1-4

| Score | Indicator |
| -- | ---------------------------------- |
| 10 | HTTP/2 (or higher version) + streaming transmission + semantic error codes (such as RFC 7807) + 99.9% interface stability |
| 8 | HTTP/1.1 + basic error codes (such as 2xx, 4xx, 5xx) + partial streaming capability support |
| 5 | Only basic HTTP, no streaming transmission, ambiguous error codes (only 200/500) |
| 0 | No HTTP support or unstable interfaces |

#### Sub-table-1-5

| Score | Indicator |
| -- | ---------------------------------- |
| 10 | Fully dynamic integration, able to use tool documentation for intelligent invocation planning, supports tool customization and version management mechanisms |
| 9 | Supports dynamic discovery and connection, able to automatically obtain tool documentation to guide invocation, supports tool customization |
| 7 | Supports dynamic discovery and connection, but unable to dynamically obtain tool documentation |
| 5 | Supports static configuration and basic tool invocation, but unable to dynamically obtain tool documentation |
| 0 | Does not support OpenTool protocol, cannot interact with any OpenTool services |

#### Sub-table-1-6

| Score | Indicator |
| -- | ---------------------------------- |
| 10 | Supports runtime dynamic discovery and reconnection, supports tool usage, and supports both Stdio and SSE transport methods |
| 9 | Supports runtime dynamic connection, supports tool usage, supports either Stdio or SSE |
| 7 | Only supports static configuration loading at startup (to add new tools, Agent must be restarted), supports tool invocation, supports either Stdio or SSE |
| 5 | Only supports connecting to a single pre-configured server, can perform basic tool invocation |
| 2 | Unstable connection and low invocation success rate, unable to properly handle return results, configuration information hardcoded |
| 0 | Does not support MCP protocol, cannot interact with any MCP servers |

#### Sub-table-1-7

| Score | Indicator |
| -- | --------------------------- |
| 10 | Can adjust plans anytime based on circumstances, supports judgment and repeated execution. Can automatically figure out step order, if failed mid-way can automatically recover, and will find the most efficient path by itself |
| 9 | Can strictly execute according to method sequence, will intelligently retry or have manual reset entry when failed |
| 7 | Can handle simple sequential relationships like "A done then do B", but stops directly when failed |
| 5 | Can execute several steps in order, but data transfer between steps often goes wrong, requiring manual docking |
| 0 | No method sequence function |

#### Sub-table-1-8

| Score | Indicator |
| -- | -------------------------------------- |
| 10 | Fully supports parallel and sequential execution, with rejection strategy (such as queue, rate limiting) and fine-grained lock control (such as resource lock) |
| 9 | Supports parallel and sequential execution, with basic rejection strategy (such as direct failure) |
| 7 | Only supports single execution mode (such as only parallel or only sequential) |
| 5 | No concurrency control, resource conflicts cause task failure or data inconsistency | |
| 0 | Does not support concurrent operations |

#### Sub-table-1-9

| Score | Indicator |
| -- | -------------------------------------- |
| 10 | Supports multi-Agent collaboration, ensures context consistency rate ≥ 99.9% through consensus mechanism, has automatic conflict resolution strategy |
| 8 | Supports multi-Agent asynchronous collaboration, avoids conflicts through design, but no strong consistency guarantee |
| 5 | Only supports single Agent independent execution, or no communication between multiple Agents |
| 0 | Does not support MultiAgent, callback loss > 40%, context consistency rate < 60% |

#### Sub-table-1-10

| Score | Indicator |
| -- | -------------------- |
| 10 | Automatically generates tasks + autonomous scheduling, no manual triggering required |
| 7 | Semi-automatic scheduling, requires external triggering (such as API calls) to start tasks, but can manage autonomously afterwards |
| 5 | Static task allocation, no scheduling capability |
| 0 | Does not support automatic scheduling |

#### Sub-table-1-11

| Score | Indicator |
| -- | ------------------------------ |
| 10 | Interface availability > 99.9%, has configurable rate limiting (such as token bucket) and circuit breaking (such as based on error rate) mechanisms |
| 8 | Interface availability > 99%, has basic rate limiting mechanism (such as fixed window) |
| 5 | Provides basic API, no fault tolerance mechanisms, prone to crash under high load |
| 0 | Service unstable, frequent failures |

#### Sub-table-1-12

| Score | Indicator |
| -- | ------------------------------- |
| 10 | Provides official SDK for ≥ 3 mainstream languages (such as TS/Java/Dart), documentation coverage > 95%, includes quick start, API reference |
| 8 | Provides SDK for 2 languages, complete documentation |
| 7 | Provides SDK for 1 language, complete documentation |
| 5 | Provides SDK for 1 language, but sparse, outdated or poor quality documentation |
| 0 | No SDK or no usable documentation |

#### Sub-table-1-13

| Score | Indicator |
| -- | ------------------------------- |
| 10 | Supports JSON/YAML format import/export + strict Schema validation + built-in version detection + configuration migration logic |
| 8 | Supports JSON/YAML format import/export + basic format validation |
| 7 | Only supports import from configuration file, no export function |
| 6 | Supports import/export, but lacks validation, prone to issues due to configuration errors |
| 0 | Hardcoded configuration, does not support external configuration files |