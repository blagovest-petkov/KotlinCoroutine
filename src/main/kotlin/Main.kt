import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    runOneMillionCoroutines()
    asyncCoroutineExample()
}

fun coroutineExample() {
    println("Run coroutineExample")
    println("Start")

    // Start a coroutine
    GlobalScope.launch {
        delay(1000)
        println("Hello")
    }

    Thread.sleep(2000) // Try to run without this line
    println("Stop")
}

fun runOneMillionCoroutines() {
    println("Run runOneMillionCoroutines")
    val c = AtomicLong()

    for (i in 1..1_000_000L) {
        GlobalScope.launch {
            //delay(1_000) // if this row is uncomment the count will be 0, because the the next line won't be passed till then
            c.addAndGet(i)
        }
    }
    println("Corountine finish! Count: " + c.get())
}

fun runOneMillionThreads() {
    println("Run runOneMillionThreads")
    val tCounter = AtomicLong()
    for (i in 1..1_000_000L) {
        thread(start = true) {
            tCounter.addAndGet(i)
        }
    }
    println("Thread finish! Count: " + tCounter.get())
}

fun asyncCoroutineExample() {
    println("Run asyncCoroutineExample")
    val deferred = (1..1_000_000).map { n ->
        GlobalScope.async {
            workload(n)
        }
    }

    runBlocking {
        var sum = 0L
        for (it in deferred) {
            sum += it.await() // wait for coroutine to finish and get their result
        }
        println("Sum: $sum")
    }
}

suspend fun workload(n: Int): Int {
    delay(1000) // If coroutine run async it will take over eleven days (run for few seconds)
    return n
}