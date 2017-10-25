import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Bean that doing something.
 *
 *
 * Created by Lakhno Anton
 * at 22:20 18.10.17.
 *
 * @author Lakhno Anton
 * @version 0.1.0
 * @since 0.1.0
 */
public class SomethingDoer {

	private static final Logger LOG = LoggerFactory.getLogger(SomethingDoer.class);

	private final int n;

	public SomethingDoer(int n) {
		this.n = n;
	}

	public int getN() {
		return n;
	}

	void doingSomething() throws InterruptedException {
		int r;
		for (int i = 0; i < n; i++) {
			r = returnSomeIntAndSleep();
		}
	}

	private int returnSomeIntAndSleep() throws InterruptedException {
		int someInt = 0;
		EntryPoint.somethingDoing.mark();
		EntryPoint.counter.inc();
		try (Timer.Context timerContext = EntryPoint.timer.time()){
			someInt = (new Random()).nextInt(10);
			int sleepTimeout = (new Random()).nextInt(100);
			LOG.info("Generate number {}. Sleeping for {} ms and then return number.", someInt, sleepTimeout);
			Thread.sleep(sleepTimeout);
		}
		return someInt;
	}

}
