import com.zhuo.delaymq.consumer.Consumer;
import com.zhuo.delaymq.core.DelayQueue;
import com.zhuo.delaymq.core.Job;
import com.zhuo.delaymq.producer.Producer;
import org.junit.jupiter.api.Test;

public class TestQueue {
    private DelayQueue delayQueue = new DelayQueue("DelayQueue", 3);

    @Test
    public void product(){
        Producer producer = new Producer(delayQueue);
        for(int i = 0; i < 20; ++i){
            producer.product(System.currentTimeMillis()+10*i, 10000, "t"+(i%5), "message"+i);
        }
    }

    @Test
    public void consumer(){
        Consumer consumer = new Consumer(delayQueue);
        Job job = consumer.consume("t1");
        System.out.println(job);
        consumer.finishJob(job.getId());
    }

}
