本项目基于 LangChain4j + SpringBoot 的多工具 AI Agent 系统，集成流式输出、对话记忆、Redis 缓存、Sa-Token 权限控制，覆盖完整链路.
各层职责说明:
用户层:Vue3 极简对话界面
接入层（Controller）: 认证：Sa-Token 拦截器校验 Token，路由：普通请求走 /agent/chat（同步），长任务走 /agent/chat/stream（SSE 流式）
Agent 层: System Prompt,ChatMemory,ReAct 循环
工具层（5 个工具）:按业务类型划分,查详细信息,查汇总信息,查环比/同比,生成图形,安全检查(产品数据暴跌等)
Service 层:实现业务流程
数据访问层:
存储层:
<img width="1868" height="881" alt="image" src="https://github.com/user-attachments/assets/a29bf031-de31-40f0-aadd-cbcbd8ecae73" />
通过对话生成图形,让数据不再生硬1,让趋势不再难理解
<img width="1878" height="907" alt="image" src="https://github.com/user-attachments/assets/072d8a9e-028b-46b0-a2d0-52fcae990777" />
通过日志,你可以看到系统调用哪些工具,消耗多少token,并根据token和大模型版本计算成本,实现调用链路和成本的可视化,使大模型的ReAct不再是黑盒,保护成本保护安全.
<img width="1814" height="779" alt="image" src="https://github.com/user-attachments/assets/f3cca858-510a-4923-b3c1-491ffe8fc525" />
