package feature_testing.server;

import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class InterMessageQueue extends Observable implements
        BlockingQueue<InterMessage> {

    @Override
    public InterMessage remove() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterMessage poll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterMessage element() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterMessage peek() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<InterMessage> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends InterMessage> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean add(InterMessage e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean offer(InterMessage e) {
        this.setChanged();
        this.notifyObservers(e);
        return false;
    }

    @Override
    public void put(InterMessage e) throws InterruptedException {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean offer(InterMessage e, long timeout, TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InterMessage take() throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterMessage poll(long timeout, TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int remainingCapacity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int drainTo(Collection<? super InterMessage> c) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int drainTo(Collection<? super InterMessage> c, int maxElements) {
        // TODO Auto-generated method stub
        return 0;
    }
}
