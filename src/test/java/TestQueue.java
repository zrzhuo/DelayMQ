import com.zhuo.delaymq.consumer.Consumer;
import com.zhuo.delaymq.core.DelayMQ;
import com.zhuo.delaymq.core.Job;
import com.zhuo.delaymq.producer.Producer;
import org.junit.jupiter.api.Test;

public class TestQueue {
    private DelayMQ delayMQ = new DelayMQ("DelayMQ", 3);

    @Test
    public void product(){
        Producer producer = new Producer(delayMQ);
        for(int i = 0; i < 20; ++i){
            producer.product(System.currentTimeMillis()+10*i, 10000, "t"+(i%5), "message"+i);
        }
    }

    @Test
    public void consumer(){
        Consumer consumer = new Consumer(delayMQ);
        Job job = consumer.consume("t0");
        System.out.println(job);
        consumer.finishJob(job.getId());
    }

}
