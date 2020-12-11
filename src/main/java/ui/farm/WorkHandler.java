package main.java.ui.farm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import main.java.entities.Player;
import main.java.entities.Wallet;
import main.java.entities.items.Plant;
import main.java.entities.items.workers.Worker;
import main.java.ui.worker_panel.WorkerPanelModel;
import main.java.util.events.Event;
import main.java.util.Scheduler;

public class WorkHandler<T extends Worker> {
    private final PriorityQueue<Plot> heap;

    private final List<T> workers = new ArrayList<>();

    public WorkHandler(Comparator<Plot> comparator) {
        heap = new PriorityQueue<>(comparator);
    }

    private void work(Worker worker) {
        final Plot currentPlot = heap.poll();
        worker.work(currentPlot);
        heap.add(currentPlot);
    }

    public void addPlot(Plot plot) {
        heap.add(plot);
    }

    public void removePlot(Plot plot) {
        heap.remove(plot);
    }

    public void refreshPlot(Plot plot) {
        removePlot(plot);
        addPlot(plot);
    }

    public void addWorker(T worker) {
        worker.setEvent(new Event(() -> work(worker), worker.getWorkTime()));
        workers.add(worker);
    }

    public void removeAllWorkers() {
        workers.clear();
    }

    public void stopAll() {
        for (final Worker worker : workers) {
            Scheduler.disableEvent(worker.getEvent());
        }
    }

    public void startAll() {
        for (final Worker worker : workers) {
            Scheduler.enableEvent(worker.getEvent());
        }
    }

    public void payWorkers(WorkerPanelModel model) {
        final Wallet wallet = Player.getInstance().getWallet();
        final Iterator<T> workerIterator = workers.iterator();
        while (workerIterator.hasNext()) {
            final T worker = workerIterator.next();

            final boolean success = wallet.burnMoney(worker.getWorth());
            if (!success) {
                worker.unsetEvent();
                model.removeWorker(worker);
                workerIterator.remove();
            }
        }
    }

    public static class GrowthComparator implements Comparator<Plot> {
        @Override
        public int compare(Plot plot1, Plot plot2) {
            final Plant plant1 = plot1.getPlant();
            final Plant plant2 = plot2.getPlant();
            if (plant1 == null) {
                return 1;
            }

            if (plant2 == null) {
                return -1;
            }

            return plot2.getPlant().getCurrentGrowth().ordinal()
                        - plot1.getPlant().getCurrentGrowth().ordinal();
        }
    }

    public static class WaterComparator implements Comparator<Plot> {
        @Override
        public int compare(Plot plot1, Plot plot2) {
            if (!(plot1.isEmpty() && plot2.isEmpty())) {
                if (plot1.isEmpty()) {
                    return 1;
                }

                if (plot2.isEmpty()) {
                    return -1;
                }
            }

            return plot1.getWaterAmount() - plot2.getWaterAmount();
        }
    }
}
