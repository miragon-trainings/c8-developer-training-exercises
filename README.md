# 🎓 Camunda 8 Developer Training Exercises

Hands-on exercises for our Zeebe training program.

## 📚 Exercises

1. **Exercise 1: BPMN Testing** (Kotlin & Java)
   - Learn process testing with Camunda 8
   - Path: `exercises/exercise-1-testing-{kotlin|java}`

2. **Exercise 2: Outbox & Idempotency** (Kotlin & Java)
   - Master distributed transaction patterns
   - Path: `exercises/exercise-2-outbox-{kotlin|java}`

3. **Exercise 3: Outbound Connector** (Java)
   - Build a custom Camunda 8 outbound connector
   - Path: `exercises/exercise-3-connector-java`

4. **Exercise 4: Agentic Orchestration**
   - AI-powered decision making with process orchestration
   - Path: `exercises/exercise-4-agentic-orchestration`

## ✅ Solutions

Reference solutions live next to the exercises in `solutions/` and are built and tested with every change.

| Exercise | Solution |
|---|---|
| 1 – BPMN Testing | `solutions/exercise-1-testing-{kotlin\|java}` |
| 2 – 4 | no reference solution yet |

```bash
./mvnw -pl solutions/exercise-1-testing-java test
```

## 🚀 Quick Start

1. Start infrastructure: `cd stack && docker-compose up -d`
2. Choose your exercise and follow its README
3. Access Camunda UI at http://localhost:8080 (demo/demo)

---

**Need help?** Check individual exercise READMEs for detailed instructions.
