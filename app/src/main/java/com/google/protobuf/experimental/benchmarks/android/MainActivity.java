package com.google.protobuf.experimental.benchmarks.android;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.experimental.nathanmittler.androidbench.R;
import com.google.protobuf.experimental.example.PojoMessage;

import org.HdrHistogram.Histogram;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import static com.google.protobuf.experimental.benchmarks.android.BenchmarkRunner.forBenchmark;


public class MainActivity extends AppCompatActivity {
    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());

    static File ASM_SCHEMA_DIR;
    private Button startButton;
    private Button stopButton;
    private TextView textView;

    private final Workflow workflow = new Workflow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ASM_SCHEMA_DIR = getDir("ASM_SCHEMAS", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        textView = (TextView) findViewById(R.id.textView);
        stopButton.setEnabled(false);
        printPojo();
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textView.setText("");
                changeButtonMode(false);
                workflow.start(
                        /*forBenchmark(new CreateSchemaBenchmark(SchemaType.HANDWRITTEN, false)),
                        forBenchmark(new CreateSchemaBenchmark(SchemaType.GENERIC, false)),
                        forBenchmark(new CreateSchemaBenchmark(SchemaType.GENERIC, true)),*/
                        forBenchmark(new WriteToBenchmark(SchemaType.HANDWRITTEN)),
                        forBenchmark(new WriteToBenchmark(SchemaType.GENERIC)),
                        forBenchmark(new WriteToBenchmark(SchemaType.ASM)));/*,
                        forBenchmark(new MergeFromBenchmark(SchemaType.HANDWRITTEN)),
                        forBenchmark(new MergeFromBenchmark(SchemaType.GENERIC)),
                        forBenchmark(new MergeFromBenchmark(SchemaType.ASM)));*/
                //forBenchmark(new AnnotationBenchmark()));
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                workflow.cancel();
            }
        });
    }

    private void printPojo() {
        Class<?> clazz = PojoMessage.class;
        for(Field f : clazz.getDeclaredFields()) {
            int mod = f.getModifiers();
            String scope = Modifier.isPublic(mod) ? "public" : Modifier.isPrivate(mod) ? "private" : "package-private";
            logger.warning("PojoMessage field: " + f.getName() + " (" + scope + ")");
        }
        for(Method m : clazz.getDeclaredMethods()) {
            int mod = m.getModifiers();
            String scope = Modifier.isPublic(mod) ? "public" : Modifier.isPrivate(mod) ? "private" : "package-private";
            logger.warning("PojoMessage method: " + m.getName() + " (" + scope + ")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeButtonMode(final boolean start) {
        if (isOnUiThread()) {
            startButton.setEnabled(start);
            stopButton.setEnabled(!start);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startButton.setEnabled(start);
                    stopButton.setEnabled(!start);
                }
            });
        }
    }

    private void println(final String text) {
        if (isOnUiThread()) {
            textView.append(text + "\n");
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.append(text + "\n");
                }
            });
        }
    }

    private boolean isOnUiThread() {
        return Looper.getMainLooper().equals(Looper.myLooper());
    }

    private final class Workflow implements BenchmarkResultListener {
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private BenchmarkRunner[] runners;
        private Future<?> future;
        private int index;
        private BenchmarkRunner runner;
        private long startTime;

        void start(BenchmarkRunner... runners) {
            this.runners = runners;
            index = 0;
            changeButtonMode(false);
            startNextRunner();
        }

        void cancel() {
            future.cancel(true);
        }

        @Override
        public void onBenchmarkCancelled() {
            println("Cancelled by user");
            onDone();
        }

        @Override
        public void onBenchmarkCompleted(Histogram histogram) {
            printStats(histogram);
            startNextRunner();
        }

        private void startNextRunner() {
            runner = nextRunner();
            if (runner == null) {
                onDone();
            } else {
                runner.setResultListener(this);
                startTime = System.nanoTime();
                future = executor.submit(runner);
            }
        }

        private void onDone() {
            changeButtonMode(true);
        }

        private void printStats(Histogram histogram) {
            long latency50 = histogram.getValueAtPercentile(50);
            long latency90 = histogram.getValueAtPercentile(90);
            long latency95 = histogram.getValueAtPercentile(95);
            long latency99 = histogram.getValueAtPercentile(99);
            long latency999 = histogram.getValueAtPercentile(99.9);
            long latencyMax = histogram.getValueAtPercentile(100);
            long elapsedTime = System.nanoTime() - startTime;
            long queriesPerSecond = histogram.getTotalCount() * 1000000000L / elapsedTime;

            StringBuilder values = new StringBuilder();
            values.append(runner.getBenchmark().getName()).append('\n')
                    .append("  50%ile (us):   ").append(latency50).append('\n')
                    .append("  90%ile (us):   ").append(latency90).append('\n')
                    .append("  95%ile (us):   ").append(latency95).append('\n')
                    .append("  99%ile (us):   ").append(latency99).append('\n')
                    .append("  99.9%ile (us): ").append(latency999).append('\n')
                    .append("  Maximum (us):  ").append(latencyMax).append('\n')
                    .append("  QPS:           ").append(queriesPerSecond).append('\n');
            System.out.println(values);
        }

        private BenchmarkRunner nextRunner() {
            if (index == runners.length) {
                return null;
            }
            return runners[index++];
        }
    }
}
