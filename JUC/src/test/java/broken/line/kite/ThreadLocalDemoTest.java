package broken.line.kite;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 参考 https://github.com/alibaba/transmittable-thread-local/issues/53
 *
 * @author: wanjia1
 * @date: 2023/4/27
 */
@Slf4j
public class ThreadLocalDemoTest {


    /**
     * 场景一
     */
    @Test
    @SneakyThrows
    void test_jdk_with_out_inheritable_thread_local() {
        final String parentValue = "parent";
        ThreadLocal<String> parentThreadLocal = ThreadLocal.withInitial(() -> parentValue);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int total = 20;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            parentThreadLocal.set(String.format("%s%s", parentValue, i));
            log.info("parent thread {} value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            executorService.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
                log.info("current thread {}  value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            });
        }
        countDownLatch.await();
    }

    /**
     * 场景一
     */
    @Test
    @SneakyThrows
    void test_jdk_with_inheritable_thread_local() {
        final String parentValue = "parent";
        ThreadLocal<String> parentThreadLocal = InheritableThreadLocal.withInitial(() -> parentValue);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int total = 20;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            parentThreadLocal.set(String.format("%s%s", parentValue, i));
            log.info("parent thread {} value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            executorService.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
                log.info("current thread {}  value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            });
        }
        countDownLatch.await();
    }

    @Test
    @SneakyThrows
    void test_jdk_with_ttl_thread_local() {
        final String parentValue = "parent";
        ThreadLocal<String> parentThreadLocal = TransmittableThreadLocal.withInitial(() -> parentValue);
        ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(5));
        int total = 20;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            String currentValue = String.format("%s%s", parentValue, i);
            parentThreadLocal.set(currentValue);
            log.info("parent thread {} value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            executorService.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
                log.info("current thread {} parent value is  {}", Thread.currentThread().getName(), parentThreadLocal.get());
                log.info("current thread {} value is {}, compare value is {}", Thread.currentThread().getName(), parentThreadLocal.get(), Objects.equals(parentThreadLocal.get(), currentValue));
            });
        }
        countDownLatch.await();
    }


}
