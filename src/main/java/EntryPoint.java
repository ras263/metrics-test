import com.codahale.metrics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Created by Lakhno Anton
 * at 22:11 18.10.17.
 *
 * @author Lakhno Anton
 * @version 0.1.0
 * @since 0.1.0
 */
public class EntryPoint {

	private static final MetricRegistry metrics = new MetricRegistry();
	public static final Meter somethingDoing = metrics.meter(name(SomethingDoer.class.getSimpleName(), "something.doing"));
	public static final Timer timer = metrics.timer(name(SomethingDoer.class.getSimpleName(), "something.doing.timer"));
	public static final Counter counter = metrics.counter(name(SomethingDoer.class.getSimpleName(), "something.doing.counter"));

	public static final Logger LOG = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {

		configureMetrics();

		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/resources.properties")) {
			properties.load(fis);
			verySimpleTest(new Integer(properties.getProperty("something.doer.count")));
		} catch (IOException ioex) {
			LOG.error("Can't load properties. Very simple test aborted.");
		}
	}

	private static void configureMetrics() {
		metrics.register(
				MetricRegistry.name(SomethingDoer.class, "somethingGauge", "level"),
				(Gauge<Integer>) () -> (new Random()).nextInt(1000)
		);
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.build();
		reporter.start(1, TimeUnit.SECONDS);
		final JmxReporter jmxReporter = JmxReporter.forRegistry(metrics).build();
		jmxReporter.start();
	}


	private static void verySimpleTest(int n) {
		try {
			SomethingDoer doer = new SomethingDoer(n);
			doer.doingSomething();
		} catch (InterruptedException e) {
			LOG.error("Error occurred from very simple test.", e);
		}
	}


}
