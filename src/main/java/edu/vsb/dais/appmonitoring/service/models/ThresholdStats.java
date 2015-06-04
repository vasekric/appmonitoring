package edu.vsb.dais.appmonitoring.service.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vasekric on 3. 5. 2015.
 */
public class ThresholdStats {
    List<Stat> statistics = new ArrayList<>();
    private final int thresholdWarning;
    private final int thresholdError;

    public ThresholdStats(int thresholdWarning, int thresholdError) {
        this.thresholdWarning = thresholdWarning;
        this.thresholdError = thresholdError;
    }

    public static class Stat {
        private final LocalDate date;
        private final int total;
        private final int warns;
        private final int errors;

        public Stat(int errors, int warns, int total, LocalDate date) {
            this.errors = errors;
            this.warns = warns;
            this.total = total;
            this.date = date;
        }

        public LocalDate getDate() {
            return date;
        }

        public int getTotal() {
            return total;
        }

        public int getWarns() {
            return warns;
        }

        public int getErrors() {
            return errors;
        }

        @Override
        public String toString() {
            return "Stat{" +
                    "date=" + date +
                    ", total=" + total +
                    ", warns=" + warns +
                    ", errors=" + errors +
                    '}';
        }
    }

    public int getThresholdWarning() {
        return thresholdWarning;
    }

    public int getThresholdError() {
        return thresholdError;
    }

    public synchronized void addStatistic(Stat stat) {
        statistics.add(stat);
    }

    public List<Stat> getStatistics() {
        return statistics;
    }

    @Override
    public String toString() {
        return "ThresholdStats{" +
                "statistics=" + statistics +
                ", thresholdWarning=" + thresholdWarning +
                ", thresholdError=" + thresholdError +
                '}';
    }
}
