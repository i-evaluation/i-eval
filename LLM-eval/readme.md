# LLM Industrial Support Evaluation
English · [中文](readme_zh.md)

Evaluate large models, mainly involving short and long text generation, method invocation (method name, parameters, invocation order), performance data, etc

## LLM Evaluation
- Basic evaluation of LLMs (v1/chat/completion interface, including text and vision models), scoring each test case with a base score of 10, automatic scoring, and writing the results to an Excel file.
### Evaluation Metrics
- Basic Metrics (Automatically Completed)
    - First token time
    - Total time
    - Tokens per second
    - Generated token count
- Other Metrics
    - Response accuracy
    - Hallucination occurrences
### Detailed Evaluation Rules
- Automatic Strategies
    - First token time
        - Deduct 1 point when the first token time exceeds 1s
    - Tokens per second (token/s)
        - Deduct 1 point when tokens per second is less than 10
    - Duration (from start of response to finish, judged in combination with number of generated tokens)
        - Deduct 1 point when number of generated tokens is less than 11 and duration exceeds 2s
        - Deduct 1 point when number of generated tokens is less than 101 and duration exceeds 3.5s
        - Deduct 1 point when number of generated tokens is less than 1001 and duration exceeds 8s
        - Deduct 1 point when number of generated tokens is less than 5001 and duration exceeds 20s
        - Deduct 1 point when number of generated tokens is less than 10001 and duration exceeds 45s
        - Deduct 1 point when number of generated tokens is less than 50001 and duration exceeds 60s
        - Deduct 1 point when number of generated tokens is less than 100001 and duration exceeds 90s
        - Deduct 2 points when duration exceeds 120s
    - Unknown functions (not in function registration list)
        - Deduct 1 point when present
    - Function parameter format error (JSON format error)
        - Deduct 2 points when present
    - When the model returns a 400 or 500 error, deduct 5 points
- Test Case Expectations (Condition Settings)
    - Generated function count
        - Deduct 5 points when actual function count differs from expected
    - Generated function sequence
        - Deduct 5 points when actual function sequence does not match expectation
    - Function parameter count
        - Deduct 5 points when function parameter count differs from expectation
    - Generated token count
        - Deduct 5 points when actual generated token count is less than expected
    - Text format (JSON)
        - Deduct 5 points when the actual generated text is not standard JSON (format error)
- Manual review
  - Illusion occurs: 5 points deducted

### Other Reference Data
- Context length
- Context entries count
### Dataset
#### Generate Plain Text
- Single Request
    - Text under 10 tokens
    - 100-token text
    - 1k token text
    - 10k token text
- Concurrency
    - 4>8>16>32>64 concurrent requests, reusing the above dataset
#### Generate JSON
- Single Request
    - Regular JSON
    - Multi-layer nested JSON
- Concurrency
    - 4>8>16>32>64 concurrent requests, reusing the above dataset
#### Function Calling
- Scope:
    - Regular CRUD
    - Long function list (16>32>64>128, first construct 50 cases with 16 methods, after passing then construct 50 cases with 32 methods, and so on)
    - Single function with many parameters (4>8>16>32, first construct 50 cases with 4 and 8 parameters, after passing then construct 50 cases with 16 and 32 parameters)
    - Single function with few parameters but large body (2 parameters, parameter length 1k>2k, construct 50 cases with parameter length 1k and 2k)
    - Tool return long data (tool execution result length 1k>2k, construct 50 cases with return length 1k and 2k)
- Examination
    - FC accuracy
    - Token performance
    - Accuracy of natural language text generation in results
- Concurrency
    - 4>8>16>32>64 concurrent requests
    - Regular CRUD (reusing the above CRUD dataset)
    - Single function with few parameters but large body, tool return long data (reusing the above CRUD dataset)
### Test Types
- Single-turn Dialogue
- Continuous Dialogue
    - Mixed dialogue: short text, long text, FC
    - Reply accuracy without exceeding context
    - When hallucinations occur
    - Behavior when exceeding context (error, freeze)
- Concurrent Testing
### Result Evaluation
#### Base Score and Deductions
- Test case base score: Base score for each individual test case is 10
- Test suite base score: Base score calculated as average test case score multiplied by 10, with maximum of 100
- Tiered deductions:
    - When test cases scoring below 10 exist: Deduction = 10*(number of cases scoring below 10 - number of cases scoring below 6)/total number of cases
    - When test cases scoring below 6 exist: Deduction = 20*(number of cases scoring below 6 - number of cases scoring below 3)/total number of cases
    - When test cases scoring below 3 exist: Deduction = 30*number of cases scoring below 3/total number of cases
#### Test Suite Rating
- Test suite score equals test suite base score minus deductions
- Ratings are divided into 6 levels from highest to lowest: SS(>95), S(>90), A(>80), B(>70), C(>60), D(<60)

# Getting Started
## LLM Eval
### Directory Description
- file/suite: Test case sets (test suite files)
- file/tools: Tool sets (openapi, jsonrpc tool schema files)
- records: JSON files generated when executing test cases
- mock_tools: Server implementations of tool schema files
- markdown: Result of test suite execution, markdown format

### Preparation
1. Create file and records subdirectories under the current directory
2. Under the file folder, create a testPlan.xlsx file with the following format

| No. | Category | Concurrency | Prompt | Tools | LLM | Test Suite | Description | Summary | Execution Time | Result |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| 1 | Continuous Dialogue | 1 | Prompt1 | [{"schema": "file/tools/datetime_weather.yml","apiKey": ""}, {"schema": "file/tools/shopping.json","apiKey": ""}] | {"type":"openai", "chatConfig": {"model":"deepseek-chat", "endpoint":"https://api.deepseek.com", "apiKey":"you-apiKey", "apiSecret":""}, "chatOptions":{"temperature":0.1, "maxTokens":10240, "seed":7,"stream":false}} | file/suite/continuousChat1.xlsx  | FC: normal crud  | NA | NA | NA |
| 2 | Single-turn Dialogue | 1 | Prompt2 | [{"schema": "file/tools/datetime_weather.yml","apiKey": ""}, {"schema": "file/tools/shopping.json","apiKey": ""}] | {"type":"openai", "chatConfig": {"model":"deepseek-chat", "endpoint":"https://api.deepseek.com", "apiKey":"you-apiKey", "apiSecret":""}, "chatOptions":{"temperature":0.1, "maxTokens":10240, "seed":7,"stream":false}} | file/suite/singleTimeChat1.xlsx  | FC: long function list |   NA | NA | NA |
| 3 | Continuous Concurrency | 4 | Prompt3 | [{"schema": "file/tools/datetime_weather.yml","apiKey": ""}, {"schemaDir": "file/tools/equipment","apiKey": ""}] | {"type":"openai", "chatConfig": {"model":"deepseek-chat", "endpoint":"https://api.deepseek.com", "apiKey":"you-apiKey", "apiSecret":""}, "chatOptions":{"temperature":0.1, "maxTokens":10240, "seed":7,"stream":false}} | file/suite/concurrencyChat1.xlsx | FC: response large body | NA | NA | NA |
  - The first row (header) must be copied according to the above rules
  - Description column refers to the purpose of the test set
  - "Tools" is optional, supporting openapi and jsonrpc formats; "Prompt", "LLM", and "Test Suite" are required fields
    - Tools can be specified as JSON or YAML files, such as {"schema": "file/tools/datetime_weather.yml","apiKey": ""}, or as directories (scans all JSON or YML files in the directory, non-recursive), such as {"schemaDir": "file/tools/equipment","apiKey": ""}
  - "Summary", "Execution Time", and "Result" are generated results after execution, no need to fill in
3. Create suite and tools subdirectories under the file folder
  - Copy the tools specified in testPlan.xlsx to the tools subdirectory
  - Copy the test suites specified in testPlan.xlsx to the suite subdirectory, test suite format as follows
    | No. | Instruction  | Image | Expectation | Score | Duration(Total) | First Token(Average) | Tokens Per Second(Average) | Generated Tokens(Total) | Context Length | Context Entries | Details |
    |:---|:---|:---|:----|:--:|:---|:----|:----:|:----|:----|:------|:---|
    | 1  | What day is it today and how is the weather |   | {"completionTokens":"20", "fcSequence":"date-time_GET,weather_GET"} | NA | NA    | NA  |  NA  | NA | NA | NA    | NA |
    | 2  | Help me order a Starbucks iced americano  |    | {"completionTokens":"10", "fcCount":1}  | NA | NA    | NA  |  NA  | NA  | NA  | NA    | NA |
    | 3  | Help me turn on the air conditioner and set it to cooling mode  |    | {"completionTokens":"10","fcCount":2,"fcInfo":{"apiair-conditionerupdate_POST":4,"apiair-conditionerset_POST":1}}   | NA | NA | NA |  NA  | NA | NA | NA | NA |
    - The first row (header) must be copied according to the above rules
    - "Expectation" is optional, fcSequence refers to the calling method sequence, fcCount refers to the expected number of methods called in this test case, fcInfo refers to the expected methods called in this test case and the expected number of parameters passed to the corresponding methods; "Image" is only required for vision models, it is an HTTP protocol URL or local image (png, jpg or jpeg), multiple images separated by English commas
    - "Score", "Duration(Total)", "First Token(Average)", "Tokens Per Second(Average)", "Generated Tokens(Total)", "Context Length", "Context Entries", and "Details" are generated results after execution, no need to fill in
### Start Running
1. Run Main.main()
- Intermediate Outputs
    - Under the suite directory (file/suite), generate ${suite}.json file, which is the JSON data at the end of each suite (corresponding to each row in testPlan.xlsx, available for frontend display)
    - Under the records folder, generate ${sessionId}.json file, which is the full context at the end of each suite test (during concurrent testing, each thread generates one file)
    - During concurrent testing, under the file/suite directory, generate ${suite}${timestamp}.xlsx file, which is the execution data at the end of each suite

2. When hallucinations are found during manual review
  - In the score column, subtract 5 points from the original score, and add the reason for the deduction in the details column: Having hallucinations, deduct 5 points
  - Run MainReviser. main() to update the test summary (summary column of testPlan) and markdown report