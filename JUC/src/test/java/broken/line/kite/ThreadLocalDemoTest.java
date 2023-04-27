package broken.line.kite;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
    void test_jdk_inheritable_thread_local() {
        final String parentValue = "parent";
        ThreadLocal<String> parentThreadLocal = ThreadLocal.withInitial(() -> parentValue);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 20; i++) {

            log.info("parent thread {} value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            executorService.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("current thread {}  value is {}", Thread.currentThread().getName(), parentThreadLocal.get());
            });
        }

    }


}
