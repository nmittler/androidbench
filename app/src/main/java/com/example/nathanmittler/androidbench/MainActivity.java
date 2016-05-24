package com.example.nathanmittler.androidbench;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.HdrHistogram.Histogram;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {
    private static final int WARMUP_ITERATIONS = 1000;
    private static final int ITERATIONS = 5000;

    private Button startButton;
    private Button stopButton;
    private TextView textView;

    private final Workflow workflow = new Workflow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        textView = (TextView) findViewById(R.id.textView);
        stopButton.setEnabled(false);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textView.setText("");
                changeButtonMode(false);
                workflow.start(
                        new CreateSchemaBenchmark(SchemaType.HANDWRITTEN, false, WARMUP_ITERATIONS, ITERATIONS),
                        new CreateSchemaBenchmark(SchemaType.GENERIC, false, WARMUP_ITERATIONS, ITERATIONS),
                        new CreateSchemaBenchmark(SchemaType.GENERIC, true, WARMUP_ITERATIONS, ITERATIONS),
                        new WriteToBenchmark(SchemaType.HANDWRITTEN, WARMUP_ITERATIONS, ITERATIONS),
                        new WriteToBenchmark(SchemaType.GENERIC, WARMUP_ITERATIONS, ITERATIONS),
                        new MergeFromBenchmark(SchemaType.HANDWRITTEN, WARMUP_ITERATIONS, ITERATIONS),
                        new MergeFromBenchmark(SchemaType.GENERIC, WARMUP_ITERATIONS, ITERATIONS),
                        new AnnotationBenchmark(WARMUP_ITERATIONS, ITERATIONS));
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                workflow.cancel();
            }
        });
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
        private Benchmark[] benchmarks;
        private Future<?> future;
        private int index;
        private Benchmark benchmark;
        private long startTime;

        void start(Benchmark... benchmarks) {
            this.benchmarks = benchmarks;
            index = 0;
            changeButtonMode(false);
            startNextBenchmark();
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
            startNextBenchmark();
        }

        private void startNextBenchmark() {
            benchmark = nextBenchmark();
            if (benchmark == null) {
                onDone();
            } else {
                benchmark.setResultListener(this);
                startTime = System.nanoTime();
                future = executor.submit(benchmark);
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
            values.append(benchmark.getName()).append('\n')
                    .append("  50%ile (us):   ").append(latency50).append('\n')
                    .append("  90%ile (us):   ").append(latency90).append('\n')
                    .append("  95%ile (us):   ").append(latency95).append('\n')
                    .append("  99%ile (us):   ").append(latency99).append('\n')
                    .append("  99.9%ile (us): ").append(latency999).append('\n')
                    .append("  Maximum (us):  ").append(latencyMax).append('\n')
                    .append("  QPS:           ").append(queriesPerSecond).append('\n');
            System.out.println(values);
        }

        private Benchmark nextBenchmark() {
            if (index == benchmarks.length) {
                return null;
            }
            return benchmarks[index++];
        }
    }
}
