# 🧪 Exercise 1: BPMN Testing (Java)

Learn to test BPMN processes with Camunda 8 using Java.

> 💡 **Kotlin version available**: `exercises/exercise-1-testing-kotlin`

## 🎯 Your Mission

Implement 6 test methods in `BikeSubscriptionProcessTest.java` using the completed `NewsletterSubscriptionProcessTest.java` as reference.

## 🚀 Getting Started

### 1. Start Infrastructure
```bash
cd ../../stack
docker-compose up -d
```

### 2. Run Tests
```bash
cd exercises/exercise-1-testing-java
../../gradlew test
```

You'll see 6 tests failing - your job is to make them pass! ✅

## 📝 Test Scenarios to Implement

Each test focuses on a specific process path:

1. **✅ Happy Path** - Customer gets their bike subscription
2. **❌ Not Available** - Bike unavailable, process ends gracefully
3. **🚫 Cancelation** - User cancels during waiting period
4. **⏰ Timer - Payment Reminder** - Timeout triggers reminder
5. **💳 Reminder Then Payment** - Customer pays after reminder
6. **🔄 Complete Integration** - Full end-to-end flow

## 🔧 Testing Tools at Your Disposal

```java
// Start a process
long instanceKey = processPort.startSubscription(subscriptionId, bikeId);

// Advance time for timers
processTestContext.increaseTime(Duration.ofHours(72));

// Send messages
processPort.sendPaymentReceived(subscriptionId);
processPort.sendRequestCanceled(subscriptionId);
processPort.sendBikeReceived(subscriptionId);

// Assert process state
assertThatProcessInstance(byKey(instanceKey)).isActive();
assertThatProcessInstance(instance).isCompleted();
assertThatProcessInstance(instance).hasCompletedElement(elementId, count);

// Verify workers (Mockito)
verify(useCase).method(subscriptionId);
verify(useCase, times(2)).method(any());
```

## 💡 Hints

- Check `NewsletterSubscriptionProcessTest.java` for working examples
- Use `@CamundaSpringProcessTest` annotation - it sets up everything
- Process variables are your friends for assertions
- Timer jobs need explicit triggering in tests with `increaseTime()`

## ✅ Success Criteria

All 6 tests pass and you understand:
- How to test different process paths
- Working with timers in tests
- Message correlation patterns
- Process variable assertions

---

**Stuck?** Compare your code with the newsletter test implementation! 🔍

Still stuck? A reference solution is available in [`solutions/exercise-1-testing-java`](../../solutions/exercise-1-testing-java).
